package Headset ; 

import com.emotiv.Iedk.Edk;
import com.emotiv.Iedk.EdkErrorCode;
import com.emotiv.Iedk.EmotivCloudClient;
import com.sun.jna.ptr.IntByReference;

public class Connection {

	public static String profileName = null ;
	public static IntByReference engineUserID = null;
	public static IntByReference userCloudID = null;
	public static IntByReference profileID = null;

	public static void connect() {

		String userName = "thomas83" ; 
		String password = "Trinome51" ; 
		profileName = "Bastien" ; 
		engineUserID = new IntByReference(0);
		userCloudID  = new IntByReference(0);
		profileID 	 = new IntByReference(-1);


		//Initialize connection to Cerebral Headset
		if (Edk.INSTANCE.IEE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
			System.out.println("Emotiv Engine start up failed.");
			return;
		}
		System.out.println("Connection to headset established");



		// CONNECTION TO CLOUD //			
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
	}	
}
