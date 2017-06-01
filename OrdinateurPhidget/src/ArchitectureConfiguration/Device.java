package ArchitectureConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * This class describes the behaviour of a connected object in the network
 */
public class Device {
	
	//Name of the connected device
	private String name ; 
	//Type of the connected device
	private String type ; 
	//Localisation of the connected device
	private String location ; 
	//Name of parent gateway (if it exists)
	private String gateway;
	//IP address of OM2M server
	private String addrIp;
	/*
	 * 0 if the device is on the server
	 * 1 if the device is on the gateway
	 */
	private int emplacement ;
	//Number of values contained in the connected object data
	private int nbValue; 
	//Number of data containers
	private int nbDataContainer ;
	
	/*
	 * Device constructor with a gateway name meaning it has a parent gateway (emplacement=1)
	 */
	public Device(String addrIp,String name, String type, String location, String gateway,int nbValue, String support, int nbDataContainer) {
		this.name=name;
		this.addrIp=addrIp;
		this.type=type;
		this.location=location;
		this.gateway=gateway;
		this.nbValue=nbValue; 
		this.emplacement=1;
		this.nbDataContainer = nbDataContainer ;
		/*
		 * If on the server, we add all device elements on the network 
		 * If on the gateway, we only create the device for network vision purpose
		 */
		if (support.equals("serveur")){
			addApplication();
			addDescriptorContainer();
			addDescriptorContentInstance();
			int value[] = new int[nbValue];
			for (int i = 0 ; i<nbValue ; i++){
				value[i]=0;
			}
			for (int i = 0 ; i<nbDataContainer ; i++){
				addDataContainer(i);
				addDataContentInstance("",i,value,"");
			}					
		}
	}
	
	/*
	 * Device constructor without gateway name meaning it is directly connected to the OM2M server (emplacement=0)
	 */
	public Device(String addrIp,String name, String type, String location,int nbValue, String support, int nbDataContainer) {
		this.name=name;
		this.addrIp=addrIp;
		this.type=type;
		this.location=location;
		this.gateway="none";
		this.nbValue=nbValue; 
		this.nbDataContainer = nbDataContainer ;
		this.emplacement=0;
		/*
		 * If on the server, we add all device elements on the network 
		 * If on the gateway, we only create the device for network vision purpose
		 */
		if (support.equals("serveur")){
			addApplication();
			addDescriptorContainer();
			addDescriptorContentInstance();
			int value[] = new int[nbValue];
			for (int i = 0 ; i<nbValue ; i++){
				value[i]=0;
			}
			for (int i = 0 ; i<nbDataContainer ; i++){
				addDataContainer(i);
				addDataContentInstance("",i,value,"");
			}	
		}
	}

	public String getAddrIp(){
		return this.addrIp;
	}

	public String getName() {
		return name;
	}
	
	public int getNbValue() {
		return nbValue;
	}
	
	//Add the connected device on the network in ascending order of device fields
	public void addApplication() {
		try {
			URL url = null;
			if (this.emplacement==0){
				url = new URL("http://" + addrIp + ":8080/~/in-cse");
			}
			else if (this.emplacement==1){
				url = new URL("http://" + addrIp + ":8080/~/mn-cse");
			}
			String body = "<m2m:ae xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" rn=\""
					+ this.name
					+ "\"><api>api</api><lbl>Type/"+type+" Location/"+location+"</lbl><rr>false</rr></m2m:ae>";

			sendPostRequest(url, 2, body);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Add a descriptor container of the connected object on the network
	public void addDescriptorContainer() {
		try {
			URL url = null;
			if (this.emplacement==0){
				url = new URL("http://" + addrIp + ":8080/~/in-cse/in-name/"+name);
			}
			else if (this.emplacement==1){
				url = new URL("http://" + addrIp + ":8080/~/mn-cse/"+gateway+"/"+name);
			}
			String body = "<m2m:cnt xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" rn=\"DESCRIPTOR\"></m2m:cnt>";
			sendPostRequest(url, 3, body);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Add a descriptor in the descriptor container in the network
	public void addDescriptorContentInstance() {
		try {
			URL url = null;
			if (this.emplacement==0){
				url = new URL("http://" + addrIp + ":8080/~/in-cse/in-name/"+name+"/DESCRIPTOR");
			}
			else if (this.emplacement==1){
				url = new URL("http://" + addrIp + ":8080/~/mn-cse/"+gateway+"/"+name+"/DESCRIPTOR");
			}
			String body = "<m2m:cin xmlns:m2m=\"http://www.onem2m.org/xml/protocols\">"
					+ "<cnf>application/xml</cnf>"
					+ "<con>"
					+ "&lt;obj&gt;"
					+ "&lt;str name=&quot;type&quot; val=&quot;"
					+ type
					+ "&quot;/&gt;"
					+ "&lt;str name=&quot;location&quot; val=&quot;"
					+ location
					+ "&quot;/&gt;"
					+ "&lt;str name=&quot;appId&quot; val=&quot;"
					+ name
					+ "&quot;/&gt;"
					+ "&lt;op name=&quot;getValue&quot; href=&quot;/mn-cse/"+gateway+"/"
					+ name
					+ "/DATA/la&quot;"
					+ "in=&quot;obix:Nil&quot; out=&quot;obix:Nil&quot; is=&quot;retrieve&quot;/&gt;"
					+ "&lt;/obj&gt;" + "</con>" + "</m2m:cin>";
			sendPostRequest(url, 4, body);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Add a data container of the connected object on the network
	public void addDataContainer(int numContainer) {
		if (0<= numContainer && numContainer<=nbDataContainer){
			try {
				URL url = null;
				if (this.emplacement==0){
					url = new URL("http://" + addrIp + ":8080/~/in-cse/in-name/"+name);
				}
				else if (this.emplacement==1){
					url = new URL("http://" + addrIp + ":8080/~/mn-cse/"+gateway+"/"+name);
				}
				String body = "<m2m:cnt xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" rn=\"DATA"+numContainer+"\"></m2m:cnt>";
				sendPostRequest(url, 3, body);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//Add a data in the descriptor container of the connected device on the network
	public void addDataContentInstance(String datatype, int numContainer, int[] value, String unit) {
		if (0<= numContainer && numContainer<=nbDataContainer){
			try {
				URL url = null;
				if (this.emplacement==0){
					url = new URL("http://" + addrIp + ":8080/~/in-cse/in-name/"+name+"/DATA"+numContainer);
				}
				else if (this.emplacement==1){
					url = new URL("http://" + addrIp + ":8080/~/mn-cse/"+gateway+"/"+name+"/DATA"+numContainer);
				}
				String body = "<m2m:cin xmlns:m2m=\"http://www.onem2m.org/xml/protocols\">"
						+ "<cnf>message</cnf>"
						+ "<con>"
						+ "&lt;obj&gt;"
						+ "&lt;str name=&quot;appId&quot; val=&quot;"+name+"&quot;/&gt;";
				int i;
				for (i=0;i<nbValue;i++){
					body=body+ "&lt;str name=&quot;category&quot; val=&quot;"+datatype+" &quot;/&gt;"
							+ "&lt;int name=&quot;data&quot; val=&quot;"+value[i]+"&quot;/&gt;"
							+ "&lt;int name=&quot;unit&quot; val=&quot;"+unit+"&quot;/&gt;";
				}
				body=body+ "&lt;/obj&gt;" + "</con>" + "</m2m:cin>";
				sendPostRequest(url, 4, body);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//Return the last value of the connected device
	public int[] getDataContentInstance(String datatype,int numContainer, String unit) {
		int[] res = new int[nbValue];
		if (0<= numContainer && numContainer<=nbDataContainer){
			URL url = null;
			String output="" ; 
			try {
				if (this.emplacement==0){
					url = new URL("http://" + addrIp + ":8080/~/in-cse/in-name/"+name+ "/DATA"+numContainer+"/la");
				}
				else if (this.emplacement==1){
					url = new URL("http://" + addrIp + ":8080/~/mn-cse/"+gateway+"/"+name+ "/DATA"+numContainer+"/la");
				}
				HttpURLConnection conn =  (HttpURLConnection) url.openConnection() ;
				conn.setRequestMethod("GET");
				conn.setRequestProperty("X-M2M-Origin","admin:admin");   
				conn.setRequestProperty("Accept","application/xml");	 
				BufferedReader buff = new BufferedReader(new InputStreamReader(conn.getInputStream())) ; 
				boolean trouve=false;
				int increm = 3;
				int sousIncrem = 0;
				int finRes = 0 ;
				while((output = buff.readLine()) != null && trouve!=true) {
					char[] tabChar = output.toCharArray();	
					if(tabChar[increm]=='<' && tabChar[increm+1]=='c'&& tabChar[increm+2]=='o' && tabChar[increm+3]=='n'&& tabChar[increm+4]=='>'){
						increm=56+name.length()+8;	
						for(int n = 0; n<nbValue;n++){
							sousIncrem=increm+44+datatype.length()+50;
							finRes=0;
							while(tabChar[sousIncrem+finRes]!='&'){
								finRes++;
							}
							res[n]=Integer.parseInt(String.copyValueOf(tabChar,sousIncrem,finRes));
							increm=sousIncrem+finRes+57;
						}
						break;
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} 
		}
		return res;
	}
	
	//Return the last value's ID of the connected device
	public String getDataContentID(int numContainer) {
		URL url = null;
		String output="" ; 
		String res="" ; 
		try {
			if (this.emplacement==0){
				url = new URL("http://" + addrIp + ":8080/~/in-cse/in-name/"+name+ "/DATA"+numContainer+"/la");
			}
			else if (this.emplacement==1){
				url = new URL("http://" + addrIp + ":8080/~/mn-cse/"+gateway+"/"+name+ "/DATA"+numContainer+"/la");
			}
			HttpURLConnection conn =  (HttpURLConnection) url.openConnection() ;
			conn.setRequestMethod("GET");
			conn.setRequestProperty("X-M2M-Origin","admin:admin");   
			conn.setRequestProperty("Accept","application/xml");	 
			BufferedReader buff = new BufferedReader(new InputStreamReader(conn.getInputStream())) ; 
			boolean trouve=false;
			int increm = 0;
			int sousIncrem = 0; 
			int finRes = 0 ;
			int cpt = 0 ;
			while((output = buff.readLine()) != null && trouve!=true) {
				char[] tabChar = output.toCharArray();	
				if(tabChar[increm]=='<' && tabChar[increm+1]=='m'&& tabChar[increm+2]=='2' && tabChar[increm+3]=='m'&& tabChar[increm+4]==':'){
					increm=5 ;
					while(cpt!= 3){
						if (tabChar[increm]=='"'){
							cpt++;
						}
						increm++;
					}
					sousIncrem=increm;
					while(tabChar[sousIncrem]!='"'){
						sousIncrem++;
						finRes++;
					}		
					res=String.copyValueOf(tabChar,increm,finRes);
					trouve=true;
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} 	
		return res;
	}	
	
	//Allow request sending to the server
	public void sendPostRequest(URL url, int type, String body) {
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("X-M2M-Origin", "admin:admin");
			conn.setRequestProperty("Content-Type", "application/xml;ty="
					+ type);
			conn.setDoOutput(true);
			byte[] outputInBytes = body.getBytes("UTF-8");
			OutputStream os = conn.getOutputStream();
			os.write(outputInBytes);
			os.close();
			conn.getResponseMessage();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String output;
			while ((output = br.readLine()) != null) {
			}
			conn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
