package edu.boun.edgecloudsim.edge_client.mobile_processing_unit;

import edu.boun.edgecloudsim.energy.DefaultEnergyComputingModel;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

import java.util.List;

public class MobileHostEnergy extends MobileHost{

    private DefaultEnergyComputingModel energyModel;
    private Double batteryLevel;
    private boolean isDead;
    public MobileHostEnergy(int id, RamProvisioner ramProvisioner, BwProvisioner bwProvisioner, long storage, List<? extends Pe> peList, VmScheduler vmScheduler, DefaultEnergyComputingModel _energyModel) {
        super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
        energyModel = _energyModel;
        batteryLevel = Math.round(Math.random() * 10000) / 100.0;  //todo Livello Batteria random, da valure se cambiarlo

    }
  //todo  da implementare
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
        return batteryLevel;
    }
    //todo da implementare
    public Double upDateBatteryLevel(){
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
        double energyAllVM = 0;
        for (Vm vm : getVmList()) {

            double energyCurrentVm = 0;
            // Ottieni la quantità totale di lavoro eseguito dalla CPU della VM in MIPS
            double mipsTotali = vm.getTotalUtilizationOfCpuMips(timePassed);

            // Esempio di consumo energetico per unità di lavoro (1 MIPS), magari possiamo leggerlo dalle properties
            double energiaPerMIPS = 0.001;

            // Calcola l'energia consumata dalla CPU
            double energiaConsumataCPU = mipsTotali * energiaPerMIPS;

            // Aggiungi il consumo energetico della CPU
            energyCurrentVm += energiaConsumataCPU;

            // Calcola e aggiungi il consumo energetico della RAM
            double consumoRAM = vm.getCurrentRequestedRam() * getConsumoRAMPerUnit(timePassed);
            energyCurrentVm += consumoRAM;

            // Calcola e aggiungi il consumo energetico della larghezza di banda
            double consumoBanda = vm.getCurrentRequestedBw() * getConsumoBandaPerUnit(timePassed);
            energyCurrentVm += consumoBanda;

            // todo .. altri consumi?

            energyAllVM += energyCurrentVm;
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
