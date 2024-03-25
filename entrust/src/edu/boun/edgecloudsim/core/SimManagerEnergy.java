package edu.boun.edgecloudsim.core;

import edu.boun.edgecloudsim.energy.DefaultEnergyComputingModel;

public class SimManagerEnergy  extends SimManager{

    private DefaultEnergyComputingModel defaultEnergyComputingModel;
    private ScenarioFactoryEnergy scenarioFactoryEnergy;


    public SimManagerEnergy(ScenarioFactoryEnergy _scenarioFactory, int _numOfMobileDevice, String _simScenario, String _orchestratorPolicy) throws Exception {
        super(_scenarioFactory, _numOfMobileDevice, _simScenario, _orchestratorPolicy);
        scenarioFactoryEnergy = _scenarioFactory;
        defaultEnergyComputingModel=scenarioFactoryEnergy.getDefaultEnergyComputerModel();
        defaultEnergyComputingModel.initialize();
    }


}
