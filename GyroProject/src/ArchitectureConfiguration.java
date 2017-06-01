
import java.io.IOException;
import java.util.*;

public class ArchitectureConfiguration {
	
	private String serverIp;
	private String gatewayName;
	private List<Device> listeDevice;
	
	public ArchitectureConfiguration(){
		this.listeDevice = new ArrayList<Device>();
	}
	
	public void serverConfiguration(String ip){
		this.serverIp=ip;
	}

	public void gatewayConfiguration(String name){
		this.gatewayName="mn-name";
	}
	
	//on est le serveur et on ajoute des devices sur une gateway
	public void serveurAddDeviceOnGateWay(String name, String type, String location,int nbValue, int nbDataContainer){
		listeDevice.add(new Device(serverIp,name,type,location,gatewayName,nbValue,"serveur",nbDataContainer));
	}
	
	//on est la gateway et on ajoute des devices sur nous même
	public void gatewayAddDeviceOnGateWay(String name, String type, String location,int nbValue, int nbDataContainer){
		listeDevice.add(new Device(serverIp,name,type,location,gatewayName,nbValue,"gateway",nbDataContainer));
	}
	
	//on est le serveur et on ajoute des devices sur nous meme 
	public void serveurAddDeviceOnServeur(String name, String type, String location,int nbValue, int nbDataContainer){
		listeDevice.add(new Device(serverIp,name,type,location,nbValue,"serveur",nbDataContainer));
	}
	
	//on est la gateway et on ajoute des devices sur notre serveur
	public void gatewayAddDeviceOnServeur(String name, String type, String location,int nbValue, int nbDataContainer){
		listeDevice.add(new Device(serverIp,name,type,location,nbValue,"gateway",nbDataContainer));
	}
	
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
			throw new IOException("Device inexistant"); 
		}
	}	
	
	
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
			throw new IOException("Device inexistant");
		}
		return res;
	}	
	
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
			throw new IOException("Device inexistant");
		}
		return res;
	}	
}
