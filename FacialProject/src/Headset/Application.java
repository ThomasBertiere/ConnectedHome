package Headset ;

import com.emotiv.Iedk.Edk;
import com.sun.jna.Pointer;

public class Application {
	
	public static void main(String[] args) {
		
		Pointer eEvent = Edk.INSTANCE.IEE_EmoEngineEventCreate() ; 
		
		Pointer eState = Edk.INSTANCE.IEE_EmoStateCreate() ; 
		
		Handler handler = new Handler() ; 
		
		Communication comm = new Communication() ; 
				
		handler.getDetect().addObserver(comm);
				
		Connection.connect();
		
		handler.go(eEvent, eState);
			
		
	}

}
