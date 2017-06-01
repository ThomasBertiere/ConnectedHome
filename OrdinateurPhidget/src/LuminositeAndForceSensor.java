import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;
import com.phidgets.event.InputChangeEvent;
import com.phidgets.event.InputChangeListener;
import com.phidgets.event.OutputChangeEvent;
import com.phidgets.event.OutputChangeListener;
import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;

/*
 * Class describing the behaviour of the sensors linked to the Phidget Interface kit
 */
public class LuminositeAndForceSensor extends Thread {
	
	private boolean arret;
	private InterfaceKitPhidget ik;

	private Socket sock;
	private ObjectOutput out; 

	public LuminositeAndForceSensor(){
		try {
			sock = new Socket("10.32.3.24", 1234) ;
			out = new ObjectOutputStream(sock.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.initPhidget();
	}
	
	/*
	 * Send a message through the OutputStream of the socket
	 * The message sent is either from the Pressure Sensor or the Light Sensor 
	 */
	public void sendMess(Message m) {
		try {
			out.writeObject((Message)m);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	/*
	 * Init all the Phidget component with their Event Listener
	 */
	private void initPhidget(){
		try {
			ik = new InterfaceKitPhidget();
			ik.addAttachListener(new AttachListener() {
				//attachement of an InterfaceKit
				public void attached(AttachEvent ae) {
				}
			});
			ik.addDetachListener(new DetachListener() {
				//detachement of an InterfaceKit
				public void detached(DetachEvent ae) {
				}
			});
			ik.addErrorListener(new ErrorListener() {
				public void error(ErrorEvent ee) {
				}
			});
			ik.addInputChangeListener(new InputChangeListener() {
				public void inputChanged(InputChangeEvent oe) {
				}
			});
			ik.addOutputChangeListener(new OutputChangeListener() {
				public void outputChanged(OutputChangeEvent oe) {
				}
			});
			ik.addSensorChangeListener( new SensorChangeListener() {
				public void sensorChanged(SensorChangeEvent se) {}
			});
			ik.openAny();
			ik.waitForAttachment();
		} catch (PhidgetException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		int[] luminosite = new int[1] ;
		int[] force = new int[1] ;
		int ancienLum = 0;
		int ancienForce = 0;
		while(!arret){
			try {
				Thread.sleep(500);
				luminosite[0]=ik.getSensorValue(1);
				//The if condition prevents from posting too much data to the OM2M server
				if (ancienLum-luminosite[0]>=5 || ancienLum-luminosite[0]<=-5 ){
					ancienLum=luminosite[0];
					this.sendMess((Message)new MessageLuminositySensor(luminosite[0])); 
				}
				force[0]=ik.getSensorValue(4);
				//The if condition prevents from posting too much data to the OM2M server
				if (ancienForce-force[0]>=5 || ancienForce-force[0]<=-5 ){
					ancienForce=force[0];
					this.sendMess((Message)new MessageForceSensor(force[0])); 
				}
			} catch (InterruptedException | PhidgetException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		LuminositeAndForceSensor l = new LuminositeAndForceSensor();
		l.start();
	}
	
}
