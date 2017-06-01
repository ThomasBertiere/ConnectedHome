import EmeteurIR.EmeteurIR;
import Relay.Relay;

public class LaunchEdison {

	public LaunchEdison() {}
	
	public static void main(String[] args) {
		String ip = "10.32.1.201";
		System.out.println("##### Lancement de la gateway #####");
		new EmeteurIR(ip);
		new Relay(ip);
		ClientMusique clientMusique = new ClientMusique(ip);
		final Server s = new Server(ip);
		//ir.start(); System.out.println("##### Emetteur IR lance #####");
		//relais.start(); System.out.println("##### Relais lance #####");
		new Thread(){
			public void run(){
				s.run();
			}
		}.start();
		try {
			//on attend 10 secondes pour que le serveur HTTP de la force ait le temps
			//de rajouter des données de force sur le serveur OM2M
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("##### Serveur HTTP et passerelle de force-luminosite lances #####");
		clientMusique.start();
		System.out.println("##### Client HTTP de musique lance #####");
		System.out.println("##### Gateway en fonctionnement #####");
	}

}
