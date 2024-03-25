package edu.boun.edgecloudsim.energy;

import edu.boun.edgecloudsim.core.SimSettings;

public class DefaultEnergyComputingModel extends EnergyComputingModel {

	SimSettings SS = SimSettings.getInstance();

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
	/**Consumo energetico della CPU:

	Il consumo energetico della CPU è calcolato principalmente in due modi:
	Quando la CPU è inattiva, viene consumata una quantità fissa di energia specificata dall'attributo idleConsumption.
	Durante l'uso attivo della CPU, viene consumata una quantità di energia proporzionale al tempo di utilizzo e alla capacità di elaborazione (MIPS) della CPU. Questo consumo energetico viene calcolato tramite il metodo updateDynamicEnergyConsumption().
	Il consumo energetico totale della CPU viene quindi aggiornato tramite il metodo updateStaticEnergyConsumption().
	Consumo energetico della rete:

	Il consumo energetico della rete dipende dal tipo di connettività associata al nodo (connectivity).
	Quando vengono trasmesse o ricevute dati attraverso la rete, viene calcolato il consumo energetico in base alla quantità di dati trasmessi (sizeInBits) e al tipo di trasmissione (flag, che può essere trasmissione o ricezione). Questo consumo energetico viene aggiunto all'attributo networkEnergyConsumption tramite il metodo updatewirelessEnergyConsumption().
	Batteria:

	Se il nodo è alimentato da una batteria (isBatteryPowered è true), viene calcolato il livello di carica della batteria in base all'energia consumata dalla CPU e dalla rete. Il livello di carica della batteria è quindi restituito come percentuale della capacità totale della batteria.
	Energia totale consumata:

	L'energia totale consumata è la somma del consumo energetico della CPU e del consumo energetico della rete.


	**/


}
