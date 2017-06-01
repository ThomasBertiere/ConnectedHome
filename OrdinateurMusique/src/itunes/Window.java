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
 * Any window
 * 
	contained by application.
	
	bounds (rectangle) : the boundary rectangle for the window
	closeable (boolean, r/o) : does the window have a close box?
	collapseable (boolean, r/o) : does the window have a collapse (windowshade) box?
	collapsed (boolean) : is the window collapsed?
	position (point) : the upper left position of the window
	resizable (boolean, r/o) : is the window resizable?
	visible (boolean) : is the window visible?
	zoomable (boolean, r/o) : is the window zoomable?
	zoomed (boolean) : is the window zoomed?
 */
public class Window extends Item {

}
