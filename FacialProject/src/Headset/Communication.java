package Headset;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import ArchitectureConfiguration.ArchitectureConfiguration;

public class Communication implements Observer {

	private ArchitectureConfiguration reseau;
	
	private int data_256 = 1 ; 


	public Communication() {
		this.reseau = new ArchitectureConfiguration();
		this.reseau.gatewayConfiguration("mn-cse");
		this.reseau.serverConfiguration("10.32.0.124");
		this.reseau.gatewayAddDeviceOnServeur("Headset", "Gyroscope", "Mobile", 1,1);

		//Initialize to neutral
		int[] tab = { 0 };
		try {
			reseau.addDataContent("Headset", "Facial", tab, "vide",1);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}

	@Override
	//When a new expression is detected, it is sent to the server
	public void update(Observable o, Object arg) {
		int facialExpression = (int) arg ; 
		if(o instanceof DetectAction){
			if (facialExpression == 256) {//Serrer les dents
				data_256 = (data_256+1)%2 ; 
				int[] tab = {data_256};
				try {
					reseau.addDataContent("Headset","Facial",tab, "vide",1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}	
		}
	}
}
