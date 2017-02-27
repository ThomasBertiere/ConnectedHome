import com.emotiv.Iedk.Edk;
import com.emotiv.Iedk.EdkErrorCode;
import com.emotiv.Iedk.EmoState;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public class Handler {

	//handle events 
	public static void run(Pointer eEvent,Pointer eState) {

		//state sent by EmoEngine
		int state = 0;
		
		
		while(true) {
			//retrieves the next EmoEngine event 
			// ! NOT BLOCKING CALL  ! 
			state = Edk.INSTANCE.IEE_EngineGetNextEvent(eEvent);
			
			//NEW EVENT RECEIVED 
			if(state==EdkErrorCode.EDK_OK.ToInt()) {
				System.out.println("\n######### NEW EVENT #########");
				//get the type of the event retrieved 
				int eventType = Edk.INSTANCE.IEE_EmoEngineEventGetType(eEvent);
				//we can retrieve the user ID linked to this event => (optional for now) 
				
				//if the event has been updated 
				//eventType=64 = IEE_EmoStateUpdated
				if(eventType==Edk.IEE_Event_t.IEE_EmoStateUpdated.ToInt()){
					
					//retrieves state linked to an event and store it into the memory 
					Edk.INSTANCE.IEE_EmoEngineEventGetEmoState(eEvent, eState); 
					
					//DO WHAT YOU WANT 
					//
					//
					//================
					
					//Example: 
					//Get time from start 
					float timestamp = EmoState.INSTANCE.IS_GetTimeFromStart(eState);
					System.out.println("Time from start : "+timestamp+"sec");
		//WIRELESS  //Get wirelessSignalStatus => (2 => fair) 
					System.out.println("WirelessSignalStatus : "+
										EmoState.INSTANCE.IS_GetWirelessSignalStatus(eState));
		//BATTERY	//Get battery level => Sometimes -1 don't know why 
					IntByReference batteryLevel , maxBatteryLevel ; 
					batteryLevel=new IntByReference(0) ;  maxBatteryLevel=new IntByReference(0) ; 
					EmoState.INSTANCE.IS_GetBatteryChargeLevel(eState, batteryLevel, maxBatteryLevel);
					System.out.println("Battery level : "+batteryLevel.getValue()+" maxBattery : "
									+maxBatteryLevel.getValue());
		//BLINK		//Blink ? (cligner des yeux)
					if (EmoState.INSTANCE.IS_FacialExpressionIsBlink(eState) == 1)
						System.out.println("Blink");
					//End 
					//
					//
					//
				}
				System.out.println("############################");
			}
		}
	}
	
}
