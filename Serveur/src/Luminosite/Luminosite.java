package Luminosite;

import java.io.IOException;

import ArchitectureConfiguration.ArchitectureConfiguration;

public class Luminosite extends Thread {
	
	private ArchitectureConfiguration reseau  ;
	private boolean arret ;

	public Luminosite(String ip){
		this.reseau = new ArchitectureConfiguration();
		this.reseau.gatewayConfiguration("mn-cse");
		this.reseau.serverConfiguration(ip);
		this.reseau.serveurAddDeviceOnGateWay("LuminositySensor","CapteurLuminosity", "Kitchen", 1,1);
		this.arret=false;
	}
	
	public void run(){
		while(!arret){
			try {
				int [] res = reseau.getDataContent("LuminositySensor", "Luminosity", "Luz",0);
				System.out.println("Luminosite : " + res[0]);
				if(res[0]>100 && res[0]<250){
					JSONParser js= new JSONParser("62 rue du midi");
					js.askWeather();
					Thread.sleep(120000);
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void arreter(){
		arret=true;
	}

}
