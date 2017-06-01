import java.io.IOException;

import ArchitectureConfiguration.ArchitectureConfiguration;

/*
 * Class describing the behaviour of the component storing values in the OM2M server
 */
public class Passerelle {
	
	private ArchitectureConfiguration reseau;

	public Passerelle(String ipServeur){
		this.reseau = new ArchitectureConfiguration();
		this.reseau.gatewayConfiguration("mn-cse");
		this.reseau.serverConfiguration(ipServeur);
		this.reseau.gatewayAddDeviceOnGateWay("LuminositySensor","CapteurLuminosity", "Kitchen", 1,1);
		this.reseau.gatewayAddDeviceOnGateWay("ForceSensor","ForceSensor", "Salon", 1,1);
	}
	
	/*
	 * Send the value contained inside the message to the server via the 0M2M Network
	 * depending on if the message is coming from the luminosity sensor or the force sensor
	 */
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
