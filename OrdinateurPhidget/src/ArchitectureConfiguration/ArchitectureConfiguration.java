package ArchitectureConfiguration;
import java.io.IOException;
import java.util.*;
/*
 * Class allowing to manage the connect object network through OM2M
 */
public class ArchitectureConfiguration {

	//Server IP address
	private String serverIp;
	//Gateway's name
	private String gatewayName;
	//Connected object list
	private List<Device> listeDevice;
	
	public ArchitectureConfiguration(){
		this.listeDevice = new ArrayList<Device>();
	}
	
	/*
	 * Configure the server by setting it's IP
	 */
	public void serverConfiguration(String ip){
		this.serverIp=ip;
	}

	/*
	 * Configure the gateway by setting it's name
	 */
	public void gatewayConfiguration(String name){
		//name by default is mn-name. In order to use the gateway without
		//editing existing config files, we set the name by default
		this.gatewayName="mn-name";
	}
	
	/*
	 * Functions allowing to add connected components in the OM2M network
	 * 
	 * Parameters
	 * Device name
	 * Device type
	 * Device localisation
	 * How many values are returned by the device (e.g. a gyroscope sending an x and y coordinates needs 2 values)
	 * How many data contents of a device (Allow multiple data group creation from a device)
	 * 
	 */
	
	/*
	 * Adding devices on the gateway from the server
	 */
	public void serveurAddDeviceOnGateWay(String name, String type, String location,int nbValue, int nbDataContainer){
		listeDevice.add(new Device(serverIp,name,type,location,gatewayName,nbValue,"serveur",nbDataContainer));
	}
	
	/*
	 * Adding device on itself from the gateway
	 */
	public void gatewayAddDeviceOnGateWay(String name, String type, String location,int nbValue, int nbDataContainer){
		listeDevice.add(new Device(serverIp,name,type,location,gatewayName,nbValue,"gateway",nbDataContainer));
	}
	
	/*
	 * Adding device on itself from the server
	 */
	public void serveurAddDeviceOnServeur(String name, String type, String location,int nbValue, int nbDataContainer){
		listeDevice.add(new Device(serverIp,name,type,location,nbValue,"serveur",nbDataContainer));
	}
	
	/*
	 * Adding device on the server from the gateway
	 */
	public void gatewayAddDeviceOnServeur(String name, String type, String location,int nbValue, int nbDataContainer){
		listeDevice.add(new Device(serverIp,name,type,location,nbValue,"gateway",nbDataContainer));
	}
	
	/*
	 * Adding values array associated to a connected device on the network
	 */
	public void addDataContent(String deviceName, String datatype, int[] value, String unit, int nunDataContainer) throws IOException{
		java.util.Iterator<Device> ite=listeDevice.iterator();	
		boolean trouve = false;
		while (ite.hasNext() && !trouve){
			Device device = ite.next();
			if (device.getName().equals(deviceName)){
				System.out.println(device.getName());
				device.addDataContentInstance(datatype,nunDataContainer,value,unit);
				trouve=true;
			}
		}
		if (!trouve){
			throw new IOException("Device not found"); 
		}
	}	
	
	/*
	 * Get last value posted (can be an array) associated to a connected objet from the network
	 */
	public int[] getDataContent(String deviceName,String datatype, String unit, int nunDataContainer) throws IOException{
		java.util.Iterator<Device> ite=listeDevice.iterator();	
		boolean trouve = false;
		int[] res = null;
		while (ite.hasNext() && !trouve){
			Device device = ite.next();
			if (device.getName().equals(deviceName)){
				res = new int[device.getNbValue()];
				res = device.getDataContentInstance(datatype, nunDataContainer, unit);
				trouve=true;
			}
		}
		if (!trouve){
			throw new IOException("Device not found");
		}
		return res;
	}	
	
	/*
	 * Get the ID of the last value posted associated to a connected objet from the network
	 */
	public String getDataID(String deviceName, int numContainer) throws IOException{
		java.util.Iterator<Device> ite=listeDevice.iterator();	
		boolean trouve = false;
		String res = "";
		while (ite.hasNext() && !trouve){
			Device device = ite.next();
			if (device.getName().equals(deviceName)){
				res = device.getDataContentID(numContainer);
				trouve=true;
			}
		}
		if (!trouve){
			throw new IOException("Device not found");
		}
		return res;
	}	
	
	/*
	 * Main function allowing to test posting and getting values from the OM2M object
	 */
	public static void main(String[] args){
		ArchitectureConfiguration archi = new ArchitectureConfiguration();
		archi.serverConfiguration("127.0.0.1");
		archi.gatewayConfiguration("mn-name");
		archi.serveurAddDeviceOnGateWay("FreqGene", "FrequenceGenerator","Salon",4,2);
		archi.serveurAddDeviceOnServeur("MeteoCapteur", "Meteo","Jardin",3,3);
		int[] tab1={1,2,3,4};
		int[] tab2={31,32,33};
		try {
			archi.addDataContent("FreqGene","Frequence",tab1,"Hz",1);
			archi.addDataContent("MeteoCapteur","Temperature",tab2,"°C",1);
			int[] tab3=archi.getDataContent("FreqGene","Frequence","Hz",1);
			int[] tab4=archi.getDataContent("MeteoCapteur","Temperature","°C",1);
			System.out.println("FreqGene");
			for (int j =0 ; j<tab3.length;j++){
				System.out.println(tab3[j]);
			}
			System.out.println("MeteoCapteur");
			for (int j =0 ; j<tab4.length;j++){
				System.out.println(tab4[j]);
			}
			System.out.println("\nFreqGene");
			System.out.println(archi.getDataID("FreqGene",1));
			System.out.println("MeteoCapteur");
			System.out.println(archi.getDataID("MeteoCapteur",1));
			int[] tab5={10,20,30,40};
			archi.addDataContent("MeteoCapteur","Temperature",tab5,"°C",2);
			System.out.println(archi.getDataID("MeteoCapteur",2));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
