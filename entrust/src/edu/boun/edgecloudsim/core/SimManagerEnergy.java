package edu.boun.edgecloudsim.core;

import edu.boun.edgecloudsim.core.SimSettings.NETWORK_DELAY_TYPES;
import edu.boun.edgecloudsim.edge_client.mobile_processing_unit.MobileHostEnergy;
import edu.boun.edgecloudsim.edge_server.EdgeHostEnergy;
import edu.boun.edgecloudsim.energy.DefaultEnergyComputingModel;
import edu.boun.edgecloudsim.utils.SimLogger;
import edu.boun.edgecloudsim.utils.TaskProperty;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEvent;

import java.util.List;
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
            switch (ev.getTag()) {

                case GET_LOAD_LOG:
                    double momentOfInterest = CloudSim.clock();
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
    					super.getMobileDeviceManager().submitTask(edgeTask);
    					int datacencerId = getMobileServerManager().getDatacenter().getId();
    					Host host = ((MobileHostEnergy)getMobileServerManager().getDatacenter().getHostList().get(edgeTask.getMobileDeviceId()));    					    					
    					if (host instanceof MobileHostEnergy)    						
    						if(datacencerId == SimSettings.CLOUD_DATACENTER_ID)//TODO Type GSM
    							((MobileHostEnergy)host).getEnergyModel().setConnectivityType(NETWORK_DELAY_TYPES.WAN_DELAY); //FIXME unmanaged
    						else
    							((MobileHostEnergy)host).getEnergyModel().setConnectivityType(NETWORK_DELAY_TYPES.WLAN_DELAY);
    					
    						((MobileHostEnergy)host).getEnergyModel().updatewirelessEnergyConsumption(1,1);//TODO variare termini di ricezione trasmissione o i bit inviati    						

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


}
