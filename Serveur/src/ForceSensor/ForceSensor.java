package ForceSensor;

import ArchitectureConfiguration.ArchitectureConfiguration;

public class ForceSensor extends Thread {
	
	private ArchitectureConfiguration reseau  ;

	public ForceSensor(String ip){
		this.reseau = new ArchitectureConfiguration();
		this.reseau.gatewayConfiguration("mn-cse");
		this.reseau.serverConfiguration(ip);
		this.reseau.serveurAddDeviceOnGateWay("ForceSensor","ForceSensor", "Salon", 1,1);
	}
	
	
}
