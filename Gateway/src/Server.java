import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server implements Runnable {
	private ServerSocket ss;
	private Message messageRecu;
	private ForceLuminositePasserelle p ;  

	public Server(String ipServeur){
		try {
			ss=new ServerSocket(1234);
			p = new ForceLuminositePasserelle(ipServeur) ; 
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
				p.pontSendToServeurOM2M(message);

				if (this.messageRecu instanceof MessageLuminositySensor) {
					MessageLuminositySensor mls=(MessageLuminositySensor)this.messageRecu;
					System.out.println("Luminosity : "+mls.getValue());
				}
				else {
					MessageForceSensor mfs=(MessageForceSensor)this.messageRecu;
					System.out.println("Force : "+mfs.getValue());
				}
				
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		finally {
		}

	}

	
}
