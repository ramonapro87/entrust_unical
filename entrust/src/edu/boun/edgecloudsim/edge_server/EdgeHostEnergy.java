package edu.boun.edgecloudsim.edge_server;

import edu.boun.edgecloudsim.core.SimSettings;
import edu.boun.edgecloudsim.energy.DefaultEnergyComputingModel;
import edu.boun.edgecloudsim.utils.DeadHost;
import edu.boun.edgecloudsim.utils.SimUtils;

import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

import java.util.List;

public class EdgeHostEnergy extends EdgeHost {


    private DefaultEnergyComputingModel energyModel;
    private boolean isDead;
    private Double batteryLevel;
    protected double deathTime;
    private double batteryCapacity;
    private double energyAllVM = 0;

    private DeadHost deadlisthost;


    public EdgeHostEnergy(int id, RamProvisioner ramProvisioner, BwProvisioner bwProvisioner, long storage, List<? extends Pe> peList, VmScheduler vmScheduler, DefaultEnergyComputingModel em, Double bc) {
        super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
        energyModel = em;
        batteryLevel = SimUtils.getRandomDoubleNumber(SimSettings.getInstance().getMIN_BATT_PERC(), 100.0);
        batteryCapacity = bc;
        isDead = false;
        deadlisthost = DeadHost.getInstance();
    }

    public boolean isDead() {
        return isDead;
    }

    public DefaultEnergyComputingModel getEnergyModel() {
        return energyModel;
    }

    public double getBatteryCapacity() {
        return batteryCapacity;
    }

    public Double getBatteryLevel() {
        return batteryLevel;
    }

    protected void setDeath(Boolean dead, double time) {
        isDead = dead;
        deathTime = time;
        deadlisthost.addEdgeHost(this.getId());
    }

    public Double upDateBatteryLevel() {
        // scalare dal livello della batteria il consumo energetico
        double percentageConsumed = Math.round(energyAllVM / batteryCapacity);


        batteryLevel = batteryLevel - percentageConsumed;
        energyModel.setBatteryCapacity(batteryLevel);


        return batteryLevel;

    }

    public void setBatteryLevel(Double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public double energyConsumption(double timePassed) {

        for (Vm vm : getVmList()) {

            double energyCurrentVm = 0;

            double mipsTotali = vm.getTotalUtilizationOfCpuMips(timePassed);
            // todo (CLAUDIO, ROBERTO) , ho notato che per alcuni istanti di tempo il valore di mipsTotali è 0
            // e quindi non viene aggiornato il consumo energetico (il metodo chiamato getTotalUtilizationOfCpuMips è della lib cloudsim)
            // da capire se per il calcolo del consumo energetico si deve considerare anche altro.

            if (mipsTotali > 0)
                energyModel.updateDynamicEnergyConsumption(vm.getSize(), mipsTotali);
            energyAllVM = energyModel.getTotalEnergyConsumption();
            //aggiorna il livello batteria
            this.updateBatteryLevel();
        }
        return energyAllVM;
    }


    public void updateStatus() {
        if (isDead()) return;

        getEnergyModel().updateStaticEnergyConsumption();
        if (getEnergyModel().isBatteryPowered() && getEnergyModel().getBatteryLevelWattHour() <= 0) {
            setDeath(true, CloudSim.clock());
        }
    }

    public Double updateBatteryLevel() {
    	
//    	System.err.println("edge["+this.getId()+"] _battery"+batteryLevel);

        Double percentageConsumed = energyAllVM > 0
                ? energyAllVM / batteryCapacity
                : 0.0;
        batteryLevel = batteryLevel >= percentageConsumed
                ? batteryLevel - percentageConsumed
                : 0.0;
        //System.out.println("livello batteria HOST"+ batteryLevel +  "...." +this.getId());
        if (batteryLevel.equals(0.0)) {
        	System.err.println("edge["+this.getId()+"] _battery"+batteryLevel+" energy consumed: "+percentageConsumed);
            setDeath(true, CloudSim.clock());
        }
        
//    	System.err.println("edge["+this.getId()+"] _battery"+batteryLevel+" energy consumed: "+percentageConsumed);

        energyModel.setBatteryCapacity(batteryLevel);
        return batteryLevel;
    }


}

