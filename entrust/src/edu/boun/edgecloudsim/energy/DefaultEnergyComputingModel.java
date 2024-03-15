package edu.boun.edgecloudsim.energy;

import edu.boun.edgecloudsim.core.SimSettings;

public class DefaultEnergyComputingModel extends EnergyComputingModel {

	public DefaultEnergyComputingModel(int numberOfMobileDevices, double maxActiveConsumption, double idleConsumption) {
		super(numberOfMobileDevices, maxActiveConsumption, idleConsumption);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateStaticEnergyConsumption() {
		//TODO pure edge use with scheduled possibly parallel runs 
		//cpuEnergyConsumption += getIdleConsumption() / 3600 * SimSettings.getInstance().updateInterval;
	}

	@Override
	public double getCpuEnergyConsumption() {
		return cpuEnergyConsumption;
	}

	@Override
	public double getTotalEnergyConsumption() {
		return cpuEnergyConsumption + networkEnergyConsumption;
	}

	@Override
	public double getMaxActiveConsumption() {
		return maxActiveConsumption;
	}

	@Override
	public void setMaxActiveConsumption(double maxActiveConsumption) {
		this.maxActiveConsumption = maxActiveConsumption;
	}

	@Override
	public double getIdleConsumption() {
		return idleConsumption;
	}

	@Override
	public void setIdleConsumption(double idleConsumption) {
		this.idleConsumption = idleConsumption;
	}

	@Override
	public double getBatteryCapacity() {
		return batteryCapacity;
	}

	@Override
	public void setBatteryCapacity(double batteryCapacity) {
		this.batteryCapacity = batteryCapacity;
	}

	@Override
	public double getBatteryLevelWattHour() {
		if (!isBatteryPowered())
			return -1;
		if (getBatteryCapacity() * this.initialBatteryLevel < getTotalEnergyConsumption())
			return 0;
		return (getBatteryCapacity() * this.initialBatteryLevel) - getTotalEnergyConsumption();
	}

	@Override
	public double getBatteryLevelPercentage() {
		return getBatteryLevelWattHour() * 100 / getBatteryCapacity();
	}

	@Override
	public boolean isBatteryPowered() {
		return isBatteryPowered;
	}

	@Override
	public void setBattery(boolean battery) {
		this.isBatteryPowered = battery;
	}

	@Override
	public void setIntialBatteryPercentage(double batteryLevel) {
			this.initialBatteryLevel = batteryLevel / 100.0;
	}

	@Override
	public String getConnectivityType() {
		return connectivity;
	}

	@Override
	public void setConnectivityType(String connectivity) {
		this.connectivity = connectivity;

		if ("cellular".equals(connectivity)) {
			transmissionEnergyPerBits = SimSettings.getInstance().getCellularDeviceTransmissionWattHourPerBit();
			receptionEnergyPerBits = SimSettings.getInstance().getCellularDeviceReceptionWattHourPerBit();
		} else if ("wifi".equals(connectivity)) {
			transmissionEnergyPerBits = SimSettings.getInstance().getWifiDeviceTransmissionWattHourPerBit();
			receptionEnergyPerBits = SimSettings.getInstance().getWifiDeviceReceptionWattHourPerBit();
		} 
		// TODO ETHERNET ?
		/*else {
			transmissionEnergyPerBits = SimSettings.getInstance().getEthernetWattHourPerBit() / 2;
			receptionEnergyPerBits = SimSettings.getInstance().getEthernetWattHourPerBit() / 2;
		}*/
	}

	@Override
	public void updatewirelessEnergyConsumption(double sizeInBits, int flag) {
		if (flag == RECEPTION)
			networkEnergyConsumption += sizeInBits * transmissionEnergyPerBits;
		else
			networkEnergyConsumption += sizeInBits * receptionEnergyPerBits;
	}

	@Override
	public void updateDynamicEnergyConsumption(double length, double mipsCapacity) {
		cpuEnergyConsumption += ((getMaxActiveConsumption() - getIdleConsumption()) / 3600 * length / mipsCapacity);
	}


}
