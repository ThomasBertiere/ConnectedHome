package Casque;

import ArchitectureConfiguration.ArchitectureConfiguration;

public class Casque extends Thread {
	
	private ArchitectureConfiguration reseau  ;

	public Casque(String ipServeur){
		this.reseau = new ArchitectureConfiguration();
		this.reseau.gatewayConfiguration("mn-cse");
		this.reseau.serverConfiguration(ipServeur);
		this.reseau.serveurAddDeviceOnServeur("Headset","Gyroscope", "Mobile", 1,2);
	}
	
	
}
