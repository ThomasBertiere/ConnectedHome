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

package itunes;

/**
 * An item
 */
public class Item {
	private int id = -1;
	private int index = -1;
	
	public Item() {}

	public Item(int index) {
		this.index = index;
	}
	
    // container (specifier, r/o) : the container of the item
	// What is a container and a specifier:
	// http://developer.apple.com/documentation/AppleScript/Conceptual/AppleScriptLangGuide/conceptual/ASLR_fundamentals.html#//apple_ref/doc/uid/TP40000983-CH218-SW7
	// TODO: how to represent this in java? A reference to another item?
    
	/**
	 * Returns the id of the item
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id, only to be used when data is constructed from AppleScript results.
	 * It is not possible to update the id of an Item
	 * @param id the id of this Item
	 */
	protected void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Returns the index of the item in the internal application order
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Returns the name of the item
	 * @return the name of the item
	 */
	public String getName() {
		// TODO
		return null;
	}
	
	/**
	 * Returns the id of the item as a hexadecimal string. 
	 * This id does not change over time.
	 * @return the id of the item as a hexadecimal string.
	 */
	public String getPersistendID() {
		// TODO
		return null;
	}
}
