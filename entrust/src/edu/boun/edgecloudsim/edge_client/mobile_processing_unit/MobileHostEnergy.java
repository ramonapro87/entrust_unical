package edu.boun.edgecloudsim.edge_client.mobile_processing_unit;

import edu.boun.edgecloudsim.energy.DefaultEnergyComputingModel;
import edu.boun.edgecloudsim.energy.DefaultEnergyNetworkModel;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

import java.util.List;

public class MobileHostEnergy extends MobileHost{

    private DefaultEnergyComputingModel energyModel;

    private Double batteryLevel;
    private boolean isDead;
    protected double deathTime;
    private double batteryCapacity;
    double energyAllVM = 0;

    public MobileHostEnergy(int id, RamProvisioner ramProvisioner, BwProvisioner bwProvisioner, long storage, List<? extends Pe> peList, VmScheduler vmScheduler, DefaultEnergyComputingModel _energyModel, Double _batteryCapacity) {
        super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
        energyModel = _energyModel;
        batteryLevel =  Math.round(Math.random() * 10000) / 100.0;
        batteryCapacity=_batteryCapacity;
    }
    
	public void updateStatus() {
		// Check if the device is dead
		if (isDead())
			return;
		// Update the static energy consumption, the dynamic one is measure separately
		// in DefaultComputingNode.startExecution() for performance and accuracy reasons
		getEnergyModel().updateStaticEnergyConsumption();//FIXME su pure edge è usata questa funzione

		if (getEnergyModel().isBatteryPowered() && getEnergyModel().getBatteryLevelWattHour() <= 0) {
			setDeath(true, CloudSim.clock());//FIXME non so se è corretto il clock
		}
	}
	protected void setDeath(Boolean dead, double time) {
		isDead = dead;
		deathTime = time;
	}
    
    
    
    

    public boolean isDead() {



        return isDead;
    }

    public DefaultEnergyComputingModel getEnergyModel() {
        return energyModel;
    }

    public void setEnergyModel(DefaultEnergyComputingModel energyModel) {
        this.energyModel = energyModel;
    }

    public Double getBatteryLevel() {

        return energyModel.getBatteryLevelWattHour();
    }
    //todo
    public Double upDateBatteryLevel(){
        // scalare dal livello della batteria il consumo energetico
        double percentageConsumed = Math.round(energyAllVM / batteryCapacity);


        batteryLevel = batteryLevel-percentageConsumed;
        energyModel.setBatteryCapacity(batteryLevel);


       return batteryLevel;

    }

    public void setBatteryLevel(Double batteryLevel) {


        this.batteryLevel = batteryLevel;

    }

    /**
     * Calcola l'energia consumata dalla macchina virtuale.
     *
     * @param timePassed tempo trascorso per il calcolo dell'energia
     * @return l'energia consumata come valore double
     *
     * <p>
     * Fattori considerati per il calcolo dell'energia:
     * <ul>
     *   <li>MIPS (Million Instructions Per Second): Rappresenta la capacità di elaborazione della VM, che influisce sul consumo energetico in base alla quantità di lavoro svolto.</li>
     *   <li>Numero di PEs (Processing Elements): Indica il numero di elementi di elaborazione della VM, che può influenzare il consumo energetico in base alla parallelizzazione delle operazioni.</li>
     *   <li>RAM: La quantità di memoria RAM allocata per la VM, che influisce sul consumo energetico in base alla gestione e all'accesso alla memoria.</li>
     *   <li>Bandwidth (Larghezza di banda): La larghezza di banda disponibile per la VM, che influisce sul consumo energetico in base al trasferimento di dati in rete o tra dispositivi.</li>
     *   <li>Dimensione della VM: La dimensione della VM, che può influenzare il consumo energetico in base alla gestione dello spazio su disco o di altri dispositivi di memorizzazione.</li>
     *   <li>Stato della VM: Indica se la VM è in fase di migrazione, che può influenzare il consumo energetico in base alle operazioni necessarie per la migrazione stessa.</li>
     *   <li>Utilizzo totale della CPU: L'utilizzo totale della CPU da parte della VM, che può influenzare il consumo energetico in base alla quantità di lavoro eseguito.</li>
     * </ul>
     * </p>
     */
    public double energyConsumption(double timePassed) {

        for (Vm vm : getVmList()) {

            double energyCurrentVm = 0;
            // Ottieni la quantità totale di lavoro eseguito dalla CPU della VM in MIPS
            double mipsTotali = vm.getTotalUtilizationOfCpuMips(timePassed);

            if(mipsTotali > 0)
           energyModel.updateDynamicEnergyConsumption(vm.getSize(),mipsTotali);

//
            energyAllVM=energyModel.getTotalEnergyConsumption();
            //aggiorna il livello batteria
            this.upDateBatteryLevel();

        }
        return energyAllVM;
    }

    //metodo per ottenere il consumo energetico della RAM per unità di tempo
    private double getConsumoRAMPerUnit(double timePassed) {
        double ram_cost = 0.00001; // todo Valore di esempio, possiamo definirlo nei file di properties o definire un algoritmo per calcolarlo
        return ram_cost * timePassed;
    }

//    metodo per ottenere il consumo energetico della larghezza di banda per unità di tempo
    private double getConsumoBandaPerUnit(double timePassed) {
        double banda_cost = 0.00001; //todo  Valore di esempio, possiamo definirlo nei file di properties o definire un algoritmo per calcolarlo
        return banda_cost * timePassed;
    }



}
