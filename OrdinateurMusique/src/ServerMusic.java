import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import itunes.ITunesSuite;


public class ServerMusic implements Runnable {
	private ServerSocket ss;
	private Message messageRecu;
	private ITunesSuite iTunes ;

	public ServerMusic(){
		try {
			ss=new ServerSocket(1235);
			iTunes=new ITunesSuite() ; 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		Message message=null;
		Socket s=null;
		try {
			s=this.ss.accept();
			ObjectInputStream is = new ObjectInputStream(s.getInputStream());
			while (!Thread.currentThread().isInterrupted()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
				//Put the socket in reception mode
				try{
					message = (Message) is.readObject() ;
				}
				catch (IOException | ClassNotFoundException e1) {
					return;
				}
				this.messageRecu=message;
				System.out.println("ServerMusic : " + this.messageRecu.toString());
				if (this.messageRecu instanceof MessageMusic) {
					MessageMusic mm=(MessageMusic)this.messageRecu;
					if(mm.getValue()==0) {
						iTunes.start();
					} else if(mm.getValue()==1) {
						iTunes.play();
					} else if (mm.getValue()==2) {
						iTunes.pause();
					}
				}
				
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}

	}
	
	public static void main (String [] args){
		ServerMusic s = new ServerMusic();
		s.run();
		while(true){}
	}

	
}
