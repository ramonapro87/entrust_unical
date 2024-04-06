package edu.boun.edgecloudsim.core;

import edu.boun.edgecloudsim.edge_client.mobile_processing_unit.MobileHostEnergy;
import edu.boun.edgecloudsim.energy.DefaultEnergyComputingModel;
import edu.boun.edgecloudsim.utils.SimLogger;
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


    public SimManagerEnergy(ScenarioFactoryEnergy _scenarioFactory, int _numOfMobileDevice, String _simScenario, String _orchestratorPolicy) throws Exception {
        super(_scenarioFactory, _numOfMobileDevice, _simScenario, _orchestratorPolicy);
        scenarioFactoryEnergy = _scenarioFactory;
        defaultEnergyComputingModel = scenarioFactoryEnergy.getDefaultEnergyComputerModel();
        defaultEnergyComputingModel.initialize();
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

                    getEdgeServerManager().getDatacenterList().forEach(datacenter -> {
                        datacenter.getHostList().forEach(host -> {
                            if (host instanceof MobileHostEnergy)
                                System.out.println("MobileHostEnergy: " + ((MobileHostEnergy) host).getBatteryLevel());
                        });
                    });

                    getCloudServerManager().getDatacenter().getHostList().forEach(host -> {
                        if (host instanceof MobileHostEnergy) {
                        	((MobileHostEnergy) host).updateStatus();
                            System.out.println("MobileHostEnergy: " + ((MobileHostEnergy) host).getBatteryLevel());
                        }
                    });



                    /**
                     * Calcolo dell'energia:
                     * - Per ogni host di ogni datacenter
                     *   - Per ogni VM di ogni host
                     *     - Calcolo dell'energia consumata dalla CPU
                     */
                    getMobileServerManager().getDatacenter().getHostList().forEach(host -> {
                        if (host instanceof MobileHostEnergy) {
                            // only for this we have energy data
                            double ec = ((MobileHostEnergy) host).energyConsumption(momentOfInterest);
                            ec += energyEdgeConsumed.get();
                            energyMobileConsumed.set(ec);
                        }
                    });

                    SimLogger.getInstance().addVmUtilizationLog(
                            momentOfInterest,
                            getEdgeServerManager().getAvgUtilization(),
                            getCloudServerManager().getAvgUtilization(),
                            getMobileServerManager().getAvgUtilization(),
                            energyEdgeConsumed.get(),
                            energyCloudConsumed.get(),
                            energyMobileConsumed.get()

                    );
                    schedule(getId(), SimSettings.getInstance().getVmLoadLogInterval(), GET_LOAD_LOG);
                    break;
                default:
                    super.processEvent(ev);
                    break;
            }
        }
    }


}
