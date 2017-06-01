package ArchitectureConfiguration;
import java.io.IOException;
import java.util.*;


//classe permettant de gérer le reseau d'object connecté à travers le reseau OM2M
public class ArchitectureConfiguration {

	//@IP du serveur
	private String serverIp;
	//nom de la gateway
	private String gatewayName;
	//liste d'objet connecté
	private List<Device> listeDevice;
	
	public ArchitectureConfiguration(){
		this.listeDevice = new ArrayList<Device>();
	}
	
	//configure le serveur (on précise simplement son addresse IP) 
	public void serverConfiguration(String ip){
		this.serverIp=ip;
	}

	//configure la gateway (on précise simplement son nom) 
	public void gatewayConfiguration(String name){
		//this.gatewayName=name;
		//le nom par defaut est mn-name. On le donne directement pour 
		//éviter de devoir modifier les fichier de config OM2M
		this.gatewayName="mn-name";
	}
	
	/*
	 * Fonctions d'ajout d'appareil connecte sur le reseau OM2M :
	 * 
	 * Parametre :
	 * Nom du device
	 * Type du device
	 * Localisation du device
	 * Nombre de valeur qu'un device renvoie (par exemple un gyroscope qui retourne x et y necessite 2 valeurs)
	 * Nombre de conteneur de données du device (si on veut creer plusieur groupe de donnee à partir d'un device)
	 * 
	 */
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
	
	//Ajoute une donnée (pouvant contenir plusieurs valeurs, un tableau) associe a un appareil connecté sur le reseau 
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
	
	//Recupere la derniere donnée (pouvant contenir plusieurs valeurs, un tableau) associe a un appareil connecté sur le reseau 
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
	
	//Recupere l'ID de la derniere donnée associe a un appareil connecté sur le reseau 
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
