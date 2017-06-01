public class Communication {

	private ArchitectureConfiguration reseau;

	public Communication() {
		this.reseau = new ArchitectureConfiguration();
		this.reseau.gatewayConfiguration("mn-cse");
		this.reseau.serverConfiguration("192.168.1.108");
		this.reseau.serveurAddDeviceOnServeur("Headset", "Gyroscope", "Mobile", 1,2);

		//Initialize to neutral
		int[] tab = { 0 };
		try {
			reseau.addDataContent("Headset", "Gyroscope", tab, "vide",0);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public ArchitectureConfiguration getReseau() {
		return this.reseau ; 
	}
}
