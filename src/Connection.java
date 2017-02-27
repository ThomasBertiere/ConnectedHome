import com.emotiv.Iedk.Edk;
import com.emotiv.Iedk.EdkErrorCode;
import com.emotiv.Iedk.EmotivCloudClient;

public class Connection {
	
	public static void connect(int option) {
		//port number 
		short composerPort = 1726;
		//option => connection to EmoEngine  => headset
		//		 => connection to remote Engine instance => 2 softwares from Xavier 
		
		String userName = "thomas83" ; 
		String password = "Trinome51" ; 

		
		
		
		//Launch engine considering option 
		switch(option) {
		case 1 : {
			//Initialize connection to EmoEngine (Cerebral Headset) 
			if (Edk.INSTANCE.IEE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
				System.out.println("Emotiv Engine start up failed.");
				return;
			}
			System.out.println("connection established");
			
			
			// CONNECTION TO CLOUD //
			//
			//
			
			//Initialize the connection with the cloud 
			if (EmotivCloudClient.INSTANCE.EC_Connect() != EdkErrorCode.EDK_OK.ToInt()) {
				System.out.println("Cannot connect to Emotiv Cloud");
				return;
			}
			//log on the cloud client 
			if (EmotivCloudClient.INSTANCE.EC_Login(userName, password) != EdkErrorCode.EDK_OK.ToInt()) {
				System.out.println("Your login attempt has failed. The username or password may be incorrect");
				return;
			}

			System.out.println("Logged in as " + userName);
			//CONNECTION TO CLOUD ESTABLISHED 
			
			
			
			
			break ; 
		}
		case 2 : {
			System.out.println("Target IP of EmoComposer: [127.0.0.1] ");
			//Initialize connection to a remote EmoEngine instance
			//Emotiv Control Panel => port : 3008
			//Emocomposer => port : 1726 
			System.out.println(Edk.INSTANCE.IEE_EngineRemoteConnect("127.0.0.1", composerPort,"Emotiv Systems-5"));
			if (Edk.INSTANCE.IEE_EngineRemoteConnect("127.0.0.1", composerPort,"Emotiv Systems-5")
					!= EdkErrorCode.EDK_OK.ToInt()) {
				System.out.println("Cannot connect to EmoComposer on [127.0.0.1]");
				return;
			}
			System.out.println("Connected to EmoComposer on [127.0.0.1]");
			break ; 
		}
		default: 			
			System.out.println("Invalid option...");
			return;
		}
		
	}

}
