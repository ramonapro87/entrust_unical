/**
 *     PureEdgeSim:  A Simulation Framework for Performance Evaluation of Cloud, Edge and Mist Computing Environments 
 *
 *     This file is part of PureEdgeSim Project.
 *
 *     PureEdgeSim is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     PureEdgeSim is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with PureEdgeSim. If not, see <http://www.gnu.org/licenses/>.
 *     
 *     @author Charafeddine Mechalikh
 **/
package edu.boun.edgecloudsim.network;

import org.cloudbus.cloudsim.Host;

import edu.boun.edgecloudsim.core.SimManager;
import edu.boun.edgecloudsim.core.SimSettings;

//import com.mechalikh.pureedgesim.datacentersmanager.Host;
//import com.mechalikh.pureedgesim.energy.EnergyModelNetworkLink;
//import com.mechalikh.pureedgesim.network.NetworkLink.NetworkLinkTypes;
//import com.mechalikh.pureedgesim.scenariomanager.SimulationParameters; 
//import com.mechalikh.pureedgesim.simulationmanager.SimManager;

import edu.boun.edgecloudsim.energy.DefaultEnergyNetworkModel;

/**
 * A WAN network link.
 */
public class NetworkLinkWanUp extends NetworkLink {

	public NetworkLinkWanUp(Host src, Host dst, SimManager simulationManager, NetworkLinkTypes type) {
		super(src, dst, simulationManager, type);
		setBandwidth(SimSettings.getInstance().getWanBandwidth());
		setDelay(SimSettings.getInstance().getWanPropagationDelay());
		setEnergyModel(new DefaultEnergyNetworkModel(SimSettings.getInstance().getWanWattHourPerBit(), this));
	}

}
