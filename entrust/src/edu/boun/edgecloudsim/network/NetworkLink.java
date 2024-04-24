package edu.boun.edgecloudsim.network;


import org.cloudbus.cloudsim.Host;

import edu.boun.edgecloudsim.core.SimManager;
import edu.boun.edgecloudsim.energy.EnergyNetworkModel;

//import com.mechalikh.pureedgesim.datacentersmanager.ComputingNode;
//import com.mechalikh.pureedgesim.energy.EnergyModelNetworkLink;
//import com.mechalikh.pureedgesim.network.TransferProgress;
//import com.mechalikh.pureedgesim.simulationmanager.SimulationManager;

public class NetworkLink {//extends SimEntity {

	//TODO Link between two compute nodes in the infrastructure graph
	public static final int UPDATE_PROGRESS = 1;
	
	private int network_update_interval = 1; //SimulationParameters.networkUpdateInterval
	
	protected double delay = 0;
	protected double bandwidth = 0;
//	protected List<TransferProgress> transferProgressList = new ArrayList<>();
	protected Host src;
	protected Host dst;
	protected SimManager simulationManager;
	protected double usedBandwidth = 0;
	protected double totalTrasferredData = 0;
	protected EnergyNetworkModel energyModel;
	protected boolean scheduled = false;

	public enum NetworkLinkTypes {
		WAN, MAN, LAN, IGNORE
	}
	
	protected NetworkLinkTypes type;

	
	public NetworkLink(String name) {
		//super(name);
		// TODO Auto-generated constructor stub
	}
	
	
	public NetworkLink(Host src, Host dst, SimManager simulationManager,
			NetworkLinkTypes type) {
//		super(src.getId()+"+"+dst.getId());
//		super(simulationManager.getSimulation());
		this.simulationManager = simulationManager;
		this.src = src;
		this.dst = dst;
		this.setType(type);
	}
	
	protected void updateTransfersProgress() {
		
		
//		usedBandwidth = 0;
//		double allocatedBandwidth = getBandwidth(transferProgressList.size());
//		for (int i = 0; i < transferProgressList.size(); i++) {
//			// Allocate bandwidth
//			usedBandwidth += transferProgressList.get(i).getRemainingFileSize();
//
//			transferProgressList.get(i).setCurrentBandwidth(allocatedBandwidth);
//			updateTransfer(transferProgressList.get(i));
//		}
	}
	
	
	public double getUsedBandwidth() {
		// Return bandwidth usage in bits per second
		return Math.min(bandwidth, usedBandwidth);
	}
	
	public double getTotalTransferredData() {
		return totalTrasferredData;
	}
	
	

		
	// renamed from latency
	public double getDelay() {
		return delay;
	}

	public NetworkLink setDelay(double delay) {
		this.delay = delay;
		return this;
	}

	public NetworkLink setBandwidth(double bandwidth) {
		this.bandwidth = bandwidth;
		return this;
	}	
		
	public Host getSrc() {
		return src;
	}

	public void setSrc(Host src) {
		this.src = src;
	}

	public Host getDst() {
		return dst;
	}

	public void setDst(Host node) {
		this.dst = node;
	}

	public NetworkLinkTypes getType() {
		return type;
	}

	public void setType(NetworkLinkTypes type) {
		this.type = type;
	}
	public EnergyNetworkModel getEnergyModel() {
		return energyModel;
	}
	
	protected void setEnergyModel(EnergyNetworkModel energyModel) {
		this.energyModel = energyModel;
	}
	
	
	


	
	
	
	
	
//
//	@Override
//	public void processEvent(SimEvent ev) {
//		if (ev.getTag() == UPDATE_PROGRESS) {
//			// Update the progress of the current transfers and their allocated bandwidth
//			updateTransfersProgress();
//			if (this.transferProgressList.size() != 0)
//				schedule(this.getId(),network_update_interval , UPDATE_PROGRESS);
//			else
//				scheduled = false;
//		}		
//	}
//	

//	
//	protected void updateTransfer(TransferProgress transfer) {
//
//		double oldRemainingSize = transfer.getRemainingFileSize();
//
////		// Update progress (remaining file size)
////		if (SimulationParameters.realisticNetworkModel)
////			transfer.setRemainingFileSize(transfer.getRemainingFileSize()
////					- (network_update_interval * transfer.getCurrentBandwidth()));
////		else
////			transfer.setRemainingFileSize(0);
//
////		 Realistic network model
//		transfer.setRemainingFileSize(0);
//
//		
//		
//		double transferDelay = (oldRemainingSize - transfer.getRemainingFileSize()) / transfer.getCurrentBandwidth();
//
//		// Set the task network delay to decide whether it has failed due to latency or
//		// not.
//		transfer.getTask().addActualNetworkTime(transferDelay);
//
//		// Update network usage delay
//		if (type == NetworkLinkTypes.LAN)
//			transfer.setLanNetworkUsage(transfer.getLanNetworkUsage() + transferDelay);
//
//		// Update MAN network usage delay
//		else if (type == NetworkLinkTypes.MAN)
//			transfer.setManNetworkUsage(transfer.getManNetworkUsage() + transferDelay);
//
//		// Update WAN network usage delay
//		else if (type == NetworkLinkTypes.WAN)
//			transfer.setWanNetworkUsage(transfer.getWanNetworkUsage() + transferDelay);
//
//		if (transfer.getRemainingFileSize() <= 0) { // Transfer finished
//			transfer.setRemainingFileSize(0); // if < 0 set it to 0
//			transferFinished(transfer);
//		}
//	}	
//	
//	protected void transferFinished(TransferProgress transfer) {
//
//		this.transferProgressList.remove(transfer);
//
//		// Add the network link latency to the task network delay
//		transfer.getTask().addActualNetworkTime(0);
//
//		// Remove the previous hop (data has been transferred one hop)
//		transfer.getVertexList().remove(0);
//		transfer.getEdgeList().remove(0);
//
//		// Data has reached the destination
//		if (transfer.getVertexList().size() == 1) {
//			// Update logger parameters
//			
//			//simulationManager.getSimulationLogger().updateNetworkUsage(transfer); // forse non serve perché l'utilizzio della rete viene aggiornato al completamento o fallimento di una task
//			//schedule(simulationManager.getNetworkModel(), delay, NetworkModel.TRANSFER_FINISHED, transfer.task); // come gestire questo evento?
//		} else {
//			// Still did not reach destination, send it to the next hop
//			transfer.setRemainingFileSize(transfer.getFileSize());
//			transfer.getEdgeList().get(0).addTransfer(transfer);
//		}
//	}
//	
//	protected double getBandwidth(double remainingTasksCount) {
//		return (bandwidth / (remainingTasksCount > 0 ? remainingTasksCount : 1));
//	}
//	
		
	
//	public void addTransfer(TransferProgress transfer) {
//		// Used by the energy model to get the total energy consumed by this network
//		// link
//		totalTrasferredData += transfer.getFileSize();
//		transferProgressList.add(transfer);
//
//		if (!scheduled) {
//			scheduleNow(this.getId(), UPDATE_PROGRESS);
//			scheduled = true;
//		}
//	}
//	
	

//	@Override
//	public void shutdownEntity() {
//		// Do something when the simulation finishes.
//		
//	}
//
//	@Override
//	public void startEntity() { 
//		// Do nothing for now.
//	}

}
