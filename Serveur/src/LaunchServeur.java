import ForceSensor.ForceSensor;
import Casque.Casque;
import Luminosite.Luminosite;

public class LaunchServeur {

	public LaunchServeur() {}
	
	public static void main(String[] args) {
		String ip = "10.32.1.201";
		System.out.println("##### Lancement du serveur #####");
		ForceSensor ir = new ForceSensor(ip);
		System.out.println("##### Capteur de pression ajoute #####");
		Luminosite lum = new Luminosite(ip);
		System.out.println("##### Luminosite ajoute     #####");
		Casque casq = new Casque(ip);
		System.out.println("##### Casque ajoute #####");
		//lum.start();
		//System.out.println("##### Traitement de la luminosite lance #####");
		System.out.println("##### Serveur en fonctionnement #####");
	}

}
