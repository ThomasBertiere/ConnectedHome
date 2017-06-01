import java.io.IOException;

import com.emotiv.Iedk.Edk;
import com.sun.jna.ptr.IntByReference;

public class ThreadGyro extends Thread{

	@Override
	public void run() {

		Communication comm = new Communication() ; 

		int count = 0 ; 

		while(true) {


			IntByReference pXOut = new IntByReference(0) ; 
			IntByReference pYOut = new IntByReference(0) ; 

			Edk.INSTANCE.IEE_HeadsetGetGyroDelta(0, pXOut, pYOut) ;

			if(pXOut.getValue()>=200) {
				System.out.println("----------------------------");
				System.out.println("DROITE");
				System.out.println("----------------------------");
				int[] tab = {1} ; 
				try {
					comm.getReseau().addDataContent("Headset", "Gyroscope", tab, "vide", 0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count=0 ; 
			} else if(pXOut.getValue()<=-200) {
				System.out.println("----------------------------");
				System.out.println("GAUCHE");
				System.out.println("----------------------------");
				int[] tab = {2} ; 
				try {
					comm.getReseau().addDataContent("Headset", "Gyroscope", tab, "vide", 0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count=0 ; 
			}
			else if(pYOut.getValue()>=200) {
				System.out.println("----------------------------");
				System.out.println("HAUT");
				System.out.println("----------------------------");
				int[] tab = {3} ; 
				try {
					comm.getReseau().addDataContent("Headset", "Gyroscope", tab, "vide", 0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count=0 ; 
			}
			else if(pYOut.getValue()<=-200) {
				System.out.println("----------------------------");
				System.out.println("BAS");
				System.out.println("----------------------------");
				int[] tab = {4} ; 
				try {
					comm.getReseau().addDataContent("Headset", "Gyroscope", tab, "vide", 0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count=0 ; 
			}
			else {
				count++ ; 
				if(count==500000) {
					System.out.println("Reinitialize gyroscope");
					Edk.INSTANCE.IEE_HeadsetGyroRezero(0) ; 
					count=0 ; 
				}
			}
		}
	}
}
