import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ArchitectureConfiguration.ArchitectureConfiguration;

public class ClientMusique extends Thread {

	private ArchitectureConfiguration reseau  ;
	private boolean arret ;
	private Socket s  ;
	private ObjectOutput out ; 

	public ClientMusique(String ip){
		try {
			this.reseau = new ArchitectureConfiguration();
			this.reseau.gatewayConfiguration("mn-cse");
			this.reseau.serverConfiguration(ip);
			this.reseau.gatewayAddDeviceOnGateWay("ForceSensor","ForceSensor", "Salon", 1,1);
			this.arret=false;
			s = new Socket("10.32.0.124", 1235) ;
			out = new ObjectOutputStream(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMess(Message m) {
		try {
			out.writeObject((Message)m);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public void run(){
		while(!arret){
			boolean isStart = false ;
			boolean isPlay = false ; 
			while(!arret){
				try {
					int [] res = reseau.getDataContent("ForceSensor", "Force", "J",0);
					int done=0;
					if(res[0]>200  && done==0){
						if(!isStart) {
							this.sendMess((Message)new MessageMusic(0)); 
							isStart=true ; 
						}
						if(!isPlay) {
							this.sendMess((Message)new MessageMusic(1)); 
							isPlay=true ; 
						}
						done=1 ; 
					}
					else if(res[0]<20){
						done=0;
						if(isStart && isPlay) {
							this.sendMess((Message)new MessageMusic(2)); 
							isPlay=false ; 
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void arreter(){
		arret=true;
	}

}
