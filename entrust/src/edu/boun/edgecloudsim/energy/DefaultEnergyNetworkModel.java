package edu.boun.edgecloudsim.energy;

import edu.boun.edgecloudsim.network.NetworkLink;

public class DefaultEnergyNetworkModel extends EnergyNetworkModel {

	public DefaultEnergyNetworkModel(double energyPerBit, NetworkLink link) {
		super(energyPerBit, link);
	}

	@Override
	public NetworkLink getLink() {
		return link;
	}
	
	@Override
	public double getCurrentEnergyConsumption() {
		return getEnergyPerBit() * (getLink().getUsedBandwidth());
	}

	@Override
	public double getTotalEnergyConsumption() {
		return getEnergyPerBit() * (getLink().getTotalTransferredData());
	}

	@Override
	public double getEnergyPerBit() {
		return energyPerBit;
	}

	


}
