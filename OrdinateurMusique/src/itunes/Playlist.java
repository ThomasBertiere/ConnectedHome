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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import itunes.Track.TrackProperty;
import utils.CommandUtil;
import utils.ConvertUtil;

/**
 * A list of songs/streams
 * 
	 # contains tracks; contained by sources.

	 #duration (integer, r/o) : the total length of all songs (in seconds)
	 #name (text) : the name of the playlist
	 #parent (playlist, r/o) : folder which contains this playlist (if any)
	 #shuffle (boolean) : play the songs in this playlist in random order?
	 #size (double integer, r/o) : the total size of all songs (in bytes)
	 #song repeat (off/one/all) : playback repeat mode
	 #special kind (none/Audiobooks/folder/Movies/Music/Party Shuffle/Podcasts/Purchased Music/TV Shows/Videos, r/o) : special playlist kind
	 #time (text, r/o) : the length of all songs in MM:SS format
	 #visible (boolean, r/o) : is this playlist visible in the Source list?
 */
public class Playlist extends Item {
	
	private static Logger logger = Logger.getLogger(Playlist.class.toString());
	private List<Track> tracks;
	
	public Playlist() {
		super();
	}

	public Playlist(int index) {
		super(index);
	}

	/**
	 * Returns all tracks in this playlist.
	 * For performance the requested properties of the Track can be specified.
	 * @param requestedProperties The track properties that will be retrieved.
	 * @return A list of Tracks in the playlist.
	 */
	public List<Track> getTracks(TrackProperty... requestedProperties) {
		tracks = new ArrayList<Track>();
		int numberOfTracks = getCount();
		if (numberOfTracks > 0) {
			List<TrackProperty> trackProperties = new ArrayList<TrackProperty>();
			
			for (TrackProperty property : requestedProperties) {
				trackProperties.add(property);
			}
			
			if (trackProperties.size() == 0) {
				// Default properties to retrieve
				trackProperties.add(TrackProperty.artist);
				trackProperties.add(TrackProperty.id);
				trackProperties.add(TrackProperty.kind);
				trackProperties.add(TrackProperty.name);
			}
			
			String propertiesQuery = "";

			for (TrackProperty property : trackProperties) {
				if (propertiesQuery.length() > 0) {
					propertiesQuery = propertiesQuery + ", ";
				}
				// Convert enum to query parameter string
				propertiesQuery += property.name().replace('_', ' ');
			}
			
			// get {id, name, artist} of every track of library playlist 1
			Object commandResult = CommandUtil.executeCommand("get {" + propertiesQuery + "} of every track of playlist " + getIndex());
			if (commandResult instanceof List<?>) {
				List<?> parseResult = (List<?>) commandResult;
				
				// Expecting a List containing a List for each TrackProperty
				assert(parseResult.size() == trackProperties.size());
				
				// Validate that each TrackProperty list has the same length as the number of tracks in the playlist
				for(int i = 0; i < trackProperties.size();i++) {
					List<?> trackPropertyList = (List<?>) parseResult.get(i);
					assert(numberOfTracks == trackPropertyList.size());
				}
				
				// Construct tracks from the parseResult
				for (int trackNumber = 0; trackNumber < numberOfTracks; trackNumber++) {
					Track track = new Track(trackNumber + 1, this);
					int i = 0;
					for (TrackProperty property : trackProperties) {
						List<?> trackPropertyList = (List<?>) parseResult.get(i);
						track.setProperty(property, trackPropertyList.get(trackNumber));
						i++;
					}
					tracks.add(track);
				}
			} else {
				// HMM.... not a normal playlist ??! or no tracks??
			}
		}
		return tracks;
	}

	/**
	 * Returns the number of items in the playlist
	 * @return int the size
	 */
	public int getCount() {
		return ConvertUtil.asInt(CommandUtil.executeCommand("count of tracks of playlist " + getIndex()));
	}
	
	/**
	 * Returns the total length of all songs (in seconds)
	 * @return int the total length of all songs (in seconds)
	 */
	public int getDuration() {
		// TODO: fails for 'Films', for now only show for none and Music...
		String specialKind = getSpecialKind(); // TODO: cache the value...
		if (specialKind.equals("none") || specialKind.equals("Music") ) {
		    Object result = CommandUtil.executeCommand("get {duration} of playlist " + getIndex());
		    return ConvertUtil.asInteger(result).intValue();
		}
		return -1;
	}
	
	/**
	 * Sets the name of the playlist
	 * @param name The name
	 */
	public void setName(String name) {
		// TODO
	}
	
	/**
	 * Returns the name of the playlist
	 */
	public String getName() {
		Object result = CommandUtil.executeCommand("get name of playlist " + getIndex());
		return ConvertUtil.asString(result);
	}
	
	/**
	 * Returns folder which contains this playlist (if any)
	 * @return
	 */
	public Playlist getParent() {
		//Object result = CommandUtil.executeCommand("get parent of playlist " + getIndex());
		//String test = ConvertUtil.asString(result);
		return null;
	}
	
	/**
	 * Enable or disable shuffle, if enabled the songs in this 
	 * playlist are played in random order.
	 */
	public void setShuffle(boolean value) {
	    // TODO
	}
	
	/**
	 * Returns true if this playlist is played in random order
	 * @return true if this playlist is played in random order
	 */
	public boolean getShuffle() {
		Object result = CommandUtil.executeCommand("get {shuffle} of playlist " + getIndex());
		return ConvertUtil.asBoolean(result).booleanValue();
	}

	/** 
	 * Returns the total size of all songs, in bytes
	 * @return the total size of all songs in bytes
	 */
	public int getSize() {
		// Object result = CommandUtil.executeCommand("get total size of playlist " + getIndex());
		// return ConvertUtil.asInteger(result).intValue();
		return -1;
	}
	
	/**
	 * Set the playback repeat mode. 
	 * @param mode off/one/all
	 */
	public void setSongRepeat(String mode) {
		// TODO
	}
	
	/**
	 * Returns the playback repeat mode
	 * @return String off/one/all
	 */
	public String getSongRepeat() {
		Object result = CommandUtil.executeCommand("get {song repeat} of playlist " + getIndex());
		return ConvertUtil.asString(result);
	}
	
	/**
	 * Returns the special playlist kind
	 * @return String none/Audiobooks/folder/Movies/Music/Party Shuffle/Podcasts/Purchased Music/TV Shows/Videos
	 */
	public String getSpecialKind() {
		Object result = CommandUtil.executeCommand("get {special kind} of playlist " + getIndex());
		return ConvertUtil.asString(result);
	}
	
	/**
	 * Returns the length of all songs in MM:SS format
	 * @return the length of all songs in MM:SS format
	 */
	public String getTime() {
		//Object result = CommandUtil.executeCommand("get length of playlist " + getIndex());
		//return ConvertUtil.asString(result);
		return null;
	}
	
	/**
	 * Returns true if this playlist is visible in the Source list
	 * @return true if this playlist is visible in the Source list
	 */
	public boolean isVisible() {
		Object result = CommandUtil.executeCommand("get visible of playlist " + getIndex());
		return ConvertUtil.asBoolean(result).booleanValue();
	}
	
	public String toString() {
		return getName();
	}
}
