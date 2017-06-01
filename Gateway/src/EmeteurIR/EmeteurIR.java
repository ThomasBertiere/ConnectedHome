package EmeteurIR;

import java.io.IOException;
import ArchitectureConfiguration.ArchitectureConfiguration;

//Classe à lancer sur la carte Edison pour controler l'emetteur infrarouge à partir du casque cérébral.

//En realite cette classe permet de récuperer des donnees liés au casque connecte
//sur le serveur OM2M et de communiquer avec une carte arduino qui elle, controle 
//l'emetteur IR.
public class EmeteurIR extends Thread {
	
	private ArchitectureConfiguration reseau  ;
	private boolean arret ;
	private upm_grove.GroveLed pin ; 

	public EmeteurIR(String ipServeur){
		this.reseau = new ArchitectureConfiguration();
		this.reseau.gatewayConfiguration("mn-cse");
		this.reseau.serverConfiguration(ipServeur);
		this.reseau.gatewayAddDeviceOnServeur("Headset","Gyroscope", "Mobile", 1,2);
		this.arret=false;
		this.pin = new upm_grove.GroveLed(3); //pin D3
	}
	
	public void run(){
		String ancienDataID = "";
		String dataID = "";
		while(!arret){
			try {
				int [] res = reseau.getDataContent("Headset", "Gyroscope", "vide",0);
				dataID = reseau.getDataID("Headset",0);
				if(!dataID.equals(ancienDataID)){
					ancienDataID=dataID;
					System.out.println("EmetteurIR : " + res[0]);
					if (res[0]==3){ //vol+
			            pin.write(1);
						Thread.sleep(10);
						pin.write(0);
					}
					else if (res[0]==4){ //vol-
						pin.write(1);
						Thread.sleep(20);
						pin.write(0);
					}
					else if (res[0]==1){ //prog+
						pin.write(1);
						Thread.sleep(30);
						pin.write(0);
					}
					else if (res[0]==2){ //prog-
						pin.write(1);
						Thread.sleep(40);
						pin.write(0);
					}
				}
				//on attend toujours 10ms 
				Thread.sleep(10);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void arreter(){
		arret=true;
	}
	
}
