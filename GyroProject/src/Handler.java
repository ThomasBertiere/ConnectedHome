import com.emotiv.Iedk.Edk;
import com.sun.jna.Pointer;

public class Handler {

	//handle events 
	public static void run(Pointer eEvent,Pointer eState) {

		//state sent by EmoEngine
		ThreadGyro tg = new ThreadGyro() ; 
		tg.start();

		while(true) {

			//retrieves the next EmoEngine event 
			Edk.INSTANCE.IEE_EngineGetNextEvent(eEvent);
		} 
	}
}
