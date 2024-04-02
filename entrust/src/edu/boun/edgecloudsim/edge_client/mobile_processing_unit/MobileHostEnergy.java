package edu.boun.edgecloudsim.edge_client.mobile_processing_unit;

import edu.boun.edgecloudsim.energy.DefaultEnergyComputingModel;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

import java.util.List;

public class MobileHostEnergy extends MobileHost{

    private DefaultEnergyComputingModel energyModel;
    public MobileHostEnergy(int id, RamProvisioner ramProvisioner, BwProvisioner bwProvisioner, long storage, List<? extends Pe> peList, VmScheduler vmScheduler, DefaultEnergyComputingModel _energyModel) {
        super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
        energyModel = _energyModel;
    }

}
