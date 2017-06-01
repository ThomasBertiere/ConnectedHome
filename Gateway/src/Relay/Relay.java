package Relay;

import java.io.IOException;

import ArchitectureConfiguration.ArchitectureConfiguration;


//Classe à lancer sur la carte Edison pour controler le relais à partir du casque cérébral
public class Relay extends Thread {
	
	private ArchitectureConfiguration reseau  ;
	private boolean arret ;
	private upm_grove.GroveRelay relay ;

	public Relay(String serveurIp){
		this.reseau = new ArchitectureConfiguration();
		this.reseau.gatewayConfiguration("mn-cse");
		this.reseau.serverConfiguration(serveurIp);
		this.reseau.gatewayAddDeviceOnServeur("Headset","Gyroscope", "Mobile", 1,2);
		this.arret=false;
		this.relay = new upm_grove.GroveRelay(4); //D4
	}
	
	public void run(){
		while(!arret){
			try {
				int [] res = reseau.getDataContent("Headset", "Facial", "vide",1);
				System.out.println("Relais : " + res[0]);
				if(res[0]==1){
					relay.on();
				}
				else if (res[0]==0){
					relay.off();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void arreter(){
		arret=true;
	}
	
}
