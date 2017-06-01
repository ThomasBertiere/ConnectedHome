/*
 * Copyright 2008,2009 Ronald Martijn Morrien
 * 
 * This file is part of java-itunes-api.
 *
 * java-itunes-api is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * java-itunes-api is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with java-itunes-api. If not, see <http://www.gnu.org/licenses/>.
 */

package demo;

import itunes.ITunesSuite;

/**
 * Demo class to show some java-itunes-api capabilities
 */
public class Demo {

	public static void main(String[] args) {
		ITunesSuite iTunes = new ITunesSuite();
		System.out.println("Starting iTunes");
	    iTunes.start();
	    iTunes.play();
	    for(int i =0 ; i<10 ; i++) {
	    	try {
	    		System.out.println("Track n°:"+(i+1));
				Thread.sleep(5000);
				iTunes.nextTrack();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    }
	}
}
