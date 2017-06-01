package Headset ; 

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;

public class DetectAction extends Observable {

	private LinkedBlockingQueue <Integer> fifo ; 

	private HashMap <Integer, Integer> hm ; 
		
	private int detectionThreshold ; 
	
	private int capacity ; 
	
	public final static ArrayList <Integer> detectableActions = new ArrayList<Integer> (Arrays.asList(0,32,64,128,256)) ;

	public DetectAction(int capacity, int detectionThreshold) {
		this.capacity = capacity ;
		fifo = new LinkedBlockingQueue<>(capacity) ; 
		hm = new HashMap<Integer, Integer>() ; 
		this.detectionThreshold = detectionThreshold; 
		this.initHashMap() ; 
		System.out.println("Capacity : " + capacity + "Threshold : " + this.detectionThreshold + "\n" + this.HashMapToString());
	}

	public void add(int p) {
		if (hm.containsKey(p)) {
			if (fifo.remainingCapacity() != 0) {
				fifo.add(p) ; 
				hm.put(p, hm.get(p)+1) ; 
			} else {
				int stateRemoved = fifo.remove() ;
				hm.put(stateRemoved, hm.get(stateRemoved)-1) ; 
				fifo.add(p) ; 
				hm.put(p, hm.get(p)+1) ; 
			}
		}
	}

	public void  initHashMap() {
		Iterator <Integer> it = detectableActions.iterator() ;
		int curr ; 
		while (it.hasNext()) {
			curr = it.next() ; 
			this.hm.put(curr, 0) ; 
		}
	}
	
	public void clearQueue() {
		this.fifo.clear(); 
	}
	

	public String HashMapToString() {
		String result = "" ; 
		for (int key: hm.keySet()){
			String value = hm.get(key).toString();
			result += key + " ; " + value + "\n" ; 
		} 
		return result ; 
	}

	public int detectAction() {
		for (int curr: hm.keySet()){
			if (hm.get(curr) > detectionThreshold) {
				this.initHashMap() ;
				this.clearQueue();
				setChanged() ; 
				notifyObservers(curr) ;
				return curr ; 
			}
		}
		return 0;
	}
	
	

	/**
	 * GETTERS AND SETTERS
	 */

	public LinkedBlockingQueue<Integer> getFifo() {
		return fifo;
	}

	public HashMap<Integer, Integer> getEm() {
		return hm;
	}
	
	public int getCapacity() {
		return this.capacity ; 
	}

}
