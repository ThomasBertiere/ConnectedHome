package Headset ; 

import com.emotiv.Iedk.Edk;
import com.emotiv.Iedk.EdkErrorCode;
import com.emotiv.Iedk.EmoState;
import com.sun.jna.Pointer;

public class Handler {

	/*private Pointer eEvent ; 

	private Pointer eState ;*/

	private DetectAction detect ; 

	public int nEvent = 0 ;
	
	public Handler() {
		detect = new DetectAction(50,35) ;  
	}


	//handle events 
	public void go(Pointer eEvent, Pointer eState) {	

		//state sent by EmoEngine
		int state = 0;

		System.out.println("Launch handler thread...");
		while(true) {

			//retrieves the next EmoEngine event 
			state = Edk.INSTANCE.IEE_EngineGetNextEvent(eEvent);
			nEvent++ ; 

			//NEW EVENT RECEIVED 
			if(state==EdkErrorCode.EDK_OK.ToInt()) {

				//get the type of the event retrieved 
				int eventType = Edk.INSTANCE.IEE_EmoEngineEventGetType(eEvent);

				//if the event has been updated 
				if(eventType==Edk.IEE_Event_t.IEE_EmoStateUpdated.ToInt()){

					//retrieves state linked to an event and store it into the memory 
					Edk.INSTANCE.IEE_EmoEngineEventGetEmoState(eEvent, eState); 

					//Si puissance suffisante
					if (EmoState.INSTANCE.IS_FacialExpressionGetLowerFaceActionPower(eState) > 0.01) {
						//On ajoute dans la fifo des états
						detect.add(EmoState.INSTANCE.IS_FacialExpressionGetLowerFaceAction(eState));
						int stateDetected = detect.detectAction() ; 
						System.out.println(detect.HashMapToString()) ; 
						if (stateDetected != 0) {
							System.out.println("Expression detectee : " + stateDetected);
						}; 
					}

					if (EmoState.INSTANCE.IS_FacialExpressionGetUpperFaceActionPower(eState) > 0.01) {
						//On ajoute dans la fifo des états
						detect.add(EmoState.INSTANCE.IS_FacialExpressionGetUpperFaceAction(eState));
						int stateDetected = detect.detectAction() ; 
						System.out.println(detect.HashMapToString()) ; 
						if (stateDetected != 0) {
							System.out.println("Expression detectee : " + stateDetected);
						}; 
					}
				} 
			}
		} 
	}

	public DetectAction getDetect() {
		return detect;
	}


	public void setDetect(DetectAction detect) {
		this.detect = detect;
	}

	public int getnEvent() {
		return nEvent;
	}


	public void setnEvent(int nEvent) {
		this.nEvent = nEvent;
	}
}
