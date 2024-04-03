package edu.boun.edgecloudsim.core;

import edu.boun.edgecloudsim.edge_client.mobile_processing_unit.MobileHostEnergy;
import edu.boun.edgecloudsim.edge_server.EdgeVmAllocationPolicy_Custom;
import edu.boun.edgecloudsim.energy.DefaultEnergyComputingModel;
import edu.boun.edgecloudsim.utils.SimLogger;
import edu.boun.edgecloudsim.utils.TaskProperty;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEvent;

import java.io.IOException;

public class SimManagerEnergy  extends SimManager{

    private static final int CREATE_TASK = 0;
    private static final int CHECK_ALL_VM = 1;
    private static final int GET_LOAD_LOG = 2;
    private static final int PRINT_PROGRESS = 3;
    private static final int STOP_SIMULATION = 4;

    private DefaultEnergyComputingModel defaultEnergyComputingModel;
    private ScenarioFactoryEnergy scenarioFactoryEnergy;


    public SimManagerEnergy(ScenarioFactoryEnergy _scenarioFactory, int _numOfMobileDevice, String _simScenario, String _orchestratorPolicy) throws Exception {
        super(_scenarioFactory, _numOfMobileDevice, _simScenario, _orchestratorPolicy);
        scenarioFactoryEnergy = _scenarioFactory;
        defaultEnergyComputingModel=scenarioFactoryEnergy.getDefaultEnergyComputerModel();
        defaultEnergyComputingModel.initialize();
    }

    /**
     * ridefiniamo questo metodo per aggiungere
     * il log del consumo energetico, al momento sembra che non ci sia un metodo per ottenere il consumo energetico
     * di un server, quindi per ora lo metto qui
     * */
    @Override
    public void processEvent(SimEvent ev) {
        synchronized(this){
            switch (ev.getTag()) {

                case GET_LOAD_LOG:

                    SimLogger.getInstance().addVmUtilizationLog(
                            CloudSim.clock(),
                            getEdgeServerManager().getAvgUtilization(),
                            getCloudServerManager().getAvgUtilization(),
                            getMobileServerManager().getAvgUtilization());


                    getEdgeServerManager().getDatacenterList().forEach(datacenter -> {
                        datacenter.getHostList().forEach(host -> {
                            try {
                                if (host instanceof MobileHostEnergy) {
                                    System.out.println("MobileHostEnergy: " + ((MobileHostEnergy) host).getBatteryLevel());
                                    System.out.println("MobileHostEnergy: " + ((MobileHostEnergy) host).getEnergyModel());
                                } else {
                                    System.out.println("Host : " + host.getId());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    });

                        getCloudServerManager().getDatacenter().getHostList().forEach(host -> {
                            try {
                                if(host instanceof MobileHostEnergy){
                                    System.out.println("MobileHostEnergy: "+ ((MobileHostEnergy) host).getBatteryLevel());
                                }
                                else {
                                    System.out.println("Host : "+ host.getId());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        getMobileServerManager().getDatacenter().getHostList().forEach(host -> {
                            try {
                                if(host instanceof MobileHostEnergy){
                                    System.out.println("MobileHostEnergy: "+ ((MobileHostEnergy) host).getBatteryLevel());
                                    // only for this we have energy data
                                }
                                else {
                                    System.out.println("Host : "+ host.getId());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

//                    System.out.println("getEdgeServerManager: "+ getEdgeServerManager().getAvgUtilization());
//                    System.out.println("getCloudServerManager: "+ getCloudServerManager().getAvgUtilization());
//                    System.out.println("getMobileServerManager: "+ getMobileServerManager().getAvgUtilization());


                    schedule(getId(), SimSettings.getInstance().getVmLoadLogInterval(), GET_LOAD_LOG);
                    break;

                default:
                    super.processEvent(ev);
                    break;
            }
        }
    }


}
