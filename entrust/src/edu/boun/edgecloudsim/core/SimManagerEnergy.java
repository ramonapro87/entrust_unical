package edu.boun.edgecloudsim.core;

import edu.boun.edgecloudsim.edge_client.mobile_processing_unit.MobileHostEnergy;
import edu.boun.edgecloudsim.edge_server.EdgeHostEnergy;
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

                    getEdgeServerManager().getDatacenterList().forEach(datacenter -> {
                        datacenter.getHostList().forEach(host -> {
                            if (host instanceof EdgeHostEnergy) {
                                double ec = ((EdgeHostEnergy) host).energyConsumption(momentOfInterest);
                                if (this.detailHostEenergy)
                                    System.out.println("energia consumata EDGEhost" + ec + "---EDGE host ID[" + host.getId() + "]");
                                ec += energyEdgeConsumed.get();
                                energyEdgeConsumed.set(ec);
                            }
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
                     *   - Per ogni VM di ogni host (se non è dead, ovvero se non è scarico)
                     *     - Calcolo dell'energia consumata dalla CPU
                     */
                    getMobileServerManager().getDatacenter().getHostList().forEach(host -> {
                        if (host instanceof MobileHostEnergy) {
                            ((MobileHostEnergy) host).updateStatus();
                            if (!((MobileHostEnergy) host).isDead()) {
                                double ec = ((MobileHostEnergy) host).energyConsumption(momentOfInterest);
                                if (this.detailHostEenergy)
                                    System.out.println("energia consumata: " + ec + " - host ID[" + host.getId() + "] momentOfInterest: " + momentOfInterest);
                                ec += energyMobileConsumed.get();
                                energyMobileConsumed.set(ec);
                            } else {
                                if (detailHostEenergy)
                                    System.out.println("MobileHostEnergy: " + ((MobileHostEnergy) host).getBatteryLevel());
                            }
                        }
                    });
                    SimLogger.getInstance().addVmUtilizationLog(momentOfInterest,
                            // avg utilization
                            getEdgeServerManager().getAvgUtilization(),
                            getCloudServerManager().getAvgUtilization(),
                            getMobileServerManager().getAvgUtilization(),
                            // energy consumed
                            energyEdgeConsumed.get(),
                            energyCloudConsumed.get(),
                            energyMobileConsumed.get());
                            // todo possiamo provare a includere il calcolo dell energia direttamente nelle classi EdgeServerManager e MobileServerManager,
                            // come viene fatto per AVGUtilization, magari estendiamo le classi con *energy seguendo la stesso modello utilizzato per le altre classi

                    schedule(getId(), SimSettings.getInstance().getVmLoadLogInterval(), GET_LOAD_LOG);
                    break;
                default:
                    super.processEvent(ev);
                    break;
            }
        }
    }


}
