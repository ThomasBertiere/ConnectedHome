import com.emotiv.Iedk.Edk;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public class Application {
	
	public static void main(String[] args) {
		
		//Return a handle to memory, can hold a EmoEngine Event. 
		Pointer eEvent = Edk.INSTANCE.IEE_EmoEngineEventCreate();
		//Return a handle to memory, can store a EmoState
		Pointer eState = Edk.INSTANCE.IEE_EmoStateCreate();
		
		
		//----TO DO---- SOMETHING WITH USER (MAY BE CLOUD ??)
		//Int by reference ; not value ; example in C : int * userId = NULL ; 
		IntByReference userID = null;
		//===> userID=0 ; 
		userID = new IntByReference(0);
		

		
		
		//connect the engine (or software) depends on the option 
		// 1 => connect engine
		// 2 => connect software (TO DO option for the software) 
		Connection.connect(1);
		Handler.run(eEvent, eState);
			
		
	}

}
