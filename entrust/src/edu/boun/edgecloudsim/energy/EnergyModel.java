package edu.boun.edgecloudsim.energy;



//TODO ENERGY NETWORK LINK

public abstract class EnergyModel {
	protected int numberOfMobileDevices; //*from edgecloudsim model*

	protected double maxActiveConsumption; // Consumed energy when the cpu is operating at 100% in Watt
	protected double idleConsumption; // Consumed energy when idle (in Watt)
	protected double cpuEnergyConsumption = 0;
	protected double batteryCapacity;
	protected double initialBatteryLevel = 1;
	protected String connectivity;
	protected boolean isBatteryPowered = false;
	public static final int TRANSMISSION = 0; // used to update edge devices batteries
	public static final int RECEPTION = 1;
//	/**
//	 * An attribute that implements the Null Object Design Pattern to avoid
//	 * {@link NullPointerException} when using the NULL object instead of
//	 * attributing null to EnergyModelComputingNode variables.
//	 */
//	public static final EnergyModel NULL = EnergyModel.getInstance();
	protected double networkEnergyConsumption;
	protected double transmissionEnergyPerBits;
	protected double receptionEnergyPerBits;

	public EnergyModel(int numberOfMobileDevices,double maxActiveConsumption, double idleConsumption) {

		this.numberOfMobileDevices = numberOfMobileDevices;//*from edgecloudsim model*

		this.setMaxActiveConsumption(maxActiveConsumption);
		this.setIdleConsumption(idleConsumption);
	}
	
	
	/**
	 * initializes custom energy model
	 */
	public abstract void initialize();
	
	
	
	public abstract void updateStaticEnergyConsumption();

	public abstract double getCpuEnergyConsumption();

	public abstract double getTotalEnergyConsumption();

	public abstract double getMaxActiveConsumption();

	public abstract void setMaxActiveConsumption(double maxActiveConsumption);

	public abstract double getIdleConsumption();

	public abstract void setIdleConsumption(double idleConsumption);

	public abstract double getBatteryCapacity();

	public abstract void setBatteryCapacity(double batteryCapacity);

	public abstract double getBatteryLevelWattHour();

	public abstract double getBatteryLevelPercentage();

	public abstract boolean isBatteryPowered();

	public abstract void setBattery(boolean battery);

	public abstract void setIntialBatteryPercentage(double batteryLevel);

	public abstract String getConnectivityType();

	public abstract void setConnectivityType(String connectivity);

	public abstract void updatewirelessEnergyConsumption(double sizeInBits, int flag);

	public abstract void updateDynamicEnergyConsumption(double length, double mipsCapacity);

}
