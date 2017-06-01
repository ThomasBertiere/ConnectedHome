import java.io.IOException;

import ArchitectureConfiguration.ArchitectureConfiguration;

public class ForceLuminositePasserelle {
	
	private ArchitectureConfiguration reseau  ;

	public ForceLuminositePasserelle(String ipServeur){
		this.reseau = new ArchitectureConfiguration();
		this.reseau.gatewayConfiguration("mn-cse");
		this.reseau.serverConfiguration(ipServeur);
		this.reseau.gatewayAddDeviceOnGateWay("LuminositySensor","CapteurLuminosity", "Kitchen", 1,1);
		this.reseau.gatewayAddDeviceOnGateWay("ForceSensor","ForceSensor", "Salon", 1,1);
	}
	
	public void pontSendToServeurOM2M(Message msg){
		int data[]=new int[1];
		MessageForceSensor messageForceSensor = new MessageForceSensor(0);
		MessageLuminositySensor messageLuminositySensor = new MessageLuminositySensor(0);
		if(msg.getClass().isInstance(messageForceSensor)){
			data[0]=((MessageForceSensor)msg).getValue();
			try {
				this.reseau.addDataContent("ForceSensor", "Force", data, "J", 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (msg.getClass().isInstance(messageLuminositySensor)){
			data[0]=((MessageLuminositySensor)msg).getValue();
			try {
				this.reseau.addDataContent("LuminositySensor", "Luminosity", data, "Lux", 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
