package edu.boun.edgecloudsim.core;

import edu.boun.edgecloudsim.applications.sample_app6.SampleMobileDeviceManager;
import edu.boun.edgecloudsim.core.SimSettings.NETWORK_DELAY_TYPES;
import edu.boun.edgecloudsim.edge_client.Task;
import edu.boun.edgecloudsim.edge_client.mobile_processing_unit.MobileHostEnergy;
import edu.boun.edgecloudsim.edge_server.EdgeHostEnergy;
import edu.boun.edgecloudsim.edge_server.EdgeServerManager;
import edu.boun.edgecloudsim.energy.DefaultEnergyComputingModel;
import edu.boun.edgecloudsim.utils.Location;
import edu.boun.edgecloudsim.utils.SimLogger;
import edu.boun.edgecloudsim.utils.SimUtils;
import edu.boun.edgecloudsim.utils.TaskProperty;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEvent;

import java.util.concurrent.atomic.AtomicReference;

public class SimManagerEnergy extends SimManager {

    private static final int CREATE_TASK = 0;
    private static final int CHECK_ALL_VM = 1;
    private static final int GET_LOAD_LOG = 2;
    private static final int PRINT_PROGRESS = 3;
    private static final int STOP_SIMULATION = 4;
        
    

    private DefaultEnergyComputingModel defaultEnergyComputingModel;
    private ScenarioFactoryEnergy scenarioFactoryEnergy;
    public boolean detailHostEenergy = false;


    public SimManagerEnergy(ScenarioFactoryEnergy _scenarioFactory, int _numOfMobileDevice, String _simScenario, String _orchestratorPolicy) throws Exception {
        super(_scenarioFactory, _numOfMobileDevice, _simScenario, _orchestratorPolicy);
        scenarioFactoryEnergy = _scenarioFactory;
        defaultEnergyComputingModel = scenarioFactoryEnergy.getDefaultEnergyComputerModel();
        defaultEnergyComputingModel.initialize();
    }

    @Override
    public void startSimulation() throws Exception {
        //Starts the simulation
        SimLogger.print(super.getName() + " [energy] is starting...");
        //Start Edge Datacenters & Generate VMs
        getEdgeServerManager().startDatacentersEnegy();
        super.startSimulation();
    }

    /**
     * ridefiniamo questo metodo per aggiungere
     * il log del consumo energetico, al momento sembra che non ci sia un metodo per ottenere il consumo energetico
     * di un server, quindi per ora lo metto qui
     */
    @Override
    public void processEvent(SimEvent ev) {
        synchronized (this) {
        	double momentOfInterest = CloudSim.clock();
            switch (ev.getTag()) {

                case GET_LOAD_LOG:
                    
                    AtomicReference<Double> energyMobileConsumed = new AtomicReference<>((double) 0);
                    AtomicReference<Double> energyEdgeConsumed = new AtomicReference<>((double) 0);
                    AtomicReference<Double> energyCloudConsumed = new AtomicReference<>((double) 0);


                    getCloudServerManager().getDatacenter().getHostList().forEach(host -> {
                    	
                        if (host instanceof MobileHostEnergy) {
                            ((MobileHostEnergy) host).updateStatus();
                            System.out.println("MobileHostEnergy: " + ((MobileHostEnergy) host).getBatteryLevel());
                        }
                    });

                    SimLogger.getInstance().addVmUtilizationLog(momentOfInterest,
                            getEdgeServerManager().getAvgUtilization(),
                            getCloudServerManager().getAvgUtilization(),
                            getMobileServerManager().getAvgUtilization(),
                            getEdgeServerManager().getEnergyConsumption(momentOfInterest),
                            energyCloudConsumed.get(),
                            getMobileServerManager().getEnergyConsumed(momentOfInterest));

                    schedule(getId(), SimSettings.getInstance().getVmLoadLogInterval(), GET_LOAD_LOG);
                    break;
    			case CREATE_TASK:
    				try {
    					TaskProperty edgeTask = (TaskProperty) ev.getData();
    					Task task = ((SampleMobileDeviceManager)super.getMobileDeviceManager()).submitTaskEnergy(edgeTask);
    					calculateNetConsume(task,SimUtils.TRANSMISSION);
						
    				} catch (Exception e) {
    					e.printStackTrace();
    					System.exit(1);
    				}
    				break;
                    
                default:
                    super.processEvent(ev);
                    break;
            }
        }
    }
    
    
    
    public void calculateNetConsume(Task task, int flag) {
    	long size;
    	if (flag== SimUtils.TRANSMISSION) {
    		
    		System.out.println("UPLOAD");
    		size = task.getCloudletFileSize();
    	}
    	else {
    		System.out.println("DOWNLOAD");
    		size = task.getCloudletOutputSize();
    	}
    	    	
		int nexthop = getEdgeOrchestrator().getDeviceToOffload(task);    					    					
			    					
		int mobileid = task.getMobileDeviceId();
		Location loc_mobile = getMobilityModel().getLocation(mobileid, CloudSim.clock());    					
		
		MobileHostEnergy host = ((MobileHostEnergy)getMobileServerManager().getDatacenter().getHostList().get(mobileid));    
		System.out.println("_ID: mob: "+mobileid+" HOST: "+host.getId()+" edgetask: "+task.hashCode());
//		EdgeHostEnergy host2 = ((EdgeHostEnergy)esm.getDatacenterList().get(0).getHostList().get(0));
//		System.out.println("_ID: tas: "+task.getMobileDeviceId()+" HOST: "+host.getId()+" edgetask: "+edgeTask.hashCode());
//		System.out.println("--------------"+locm.getXPos()+","+locm.getYPos());
		
		EdgeServerManager esm = getEdgeServerManager();    					

		// GSM mobile to cloud    OK
		// WLAN mobile to edge   OK
		
		// MAN edge to edge    ? TODO
		// WAN edge to cloud   ? TODO
		
		
		switch (nexthop) {
		case SimSettings.CLOUD_DATACENTER_ID: {
			host.getEnergyModel().setConnectivityType(NETWORK_DELAY_TYPES.GSM_DELAY);
//			System.out.println("-------TO---CLOUD");
			break;
		}
		case SimSettings.MOBILE_DATACENTER_ID: { //DEVICE TO DEVICE ?						
//			System.out.println("-------TO---MOBILE");
			host.getEnergyModel().setConnectivityType(NETWORK_DELAY_TYPES.GSM_DELAY);
			break;
		}
		case SimSettings.GENERIC_EDGE_DEVICE_ID: {							
//			System.out.println("-------TO---EDGE");
			host.getEnergyModel().setConnectivityType(NETWORK_DELAY_TYPES.WLAN_DELAY);
			for (Datacenter d : esm.getDatacenterList()) {
				EdgeHostEnergy edgehost = (EdgeHostEnergy)d.getHostList().get(0); // one host per datacenter
				if (edgehost.getLocation().equals(loc_mobile)) {								
//					System.out.println("--------------"+locm.getXPos()+","+locm.getYPos());
					edgehost.getEnergyModel().setConnectivityType(NETWORK_DELAY_TYPES.WLAN_DELAY);
					edgehost.getEnergyModel().updatewirelessEnergyConsumption(size,flag);
				}
			}		
			break;
		}
		default://TODO
			host.getEnergyModel().setConnectivityType(NETWORK_DELAY_TYPES.WLAN_DELAY);
			System.err.println("-------TO---????" + nexthop);
			break;
		}	

		host.getEnergyModel().updatewirelessEnergyConsumption(size,flag);
		
    	
    }
    
    
    


}
