package edu.boun.edgecloudsim.energy;

import edu.boun.edgecloudsim.network.NetworkLink;

public abstract class EnergyNetworkModel {

	/**
	 * The network link to monitor.
	 */
	protected NetworkLink link;

	/**
	 * The amount of energy consumed by each bit of transmitted data.
	 */
	protected double energyPerBit;

	/*
	 * An attribute that implements the Null Object Design Pattern to avoid {@link NullPointerException} when using the
	 * NULL object instead of attributing null to EnergyModelNetworkLink variables.
	 */
	//public static final EnergyModelNetworkLink NULL = EnergyModelNetworkLinkNull.getInstance();

	public EnergyNetworkModel(final double energyPerBit, NetworkLink link) {
		this.energyPerBit = energyPerBit;
		this.link = link;
	}

	/**
	 * Gets the corresponding network link.
	 * @return The network link
	 */
	public abstract NetworkLink getLink();

	/**
	 * Gets the consumed energy at this instant.
	 * @return The current energy consumption
	 */
	public abstract double getCurrentEnergyConsumption() ;
	/**
	 * Gets the total consumed energy since the beginning of the simulation.
	 * @return The total consumed energy
	 */
	public abstract double getTotalEnergyConsumption();

	/**
	 * Gets The amount of energy consumed by each bit of transmitted data.
	 * @return The energy consumed per each bit of transferred data
	 */
	public abstract double getEnergyPerBit();

	
}
