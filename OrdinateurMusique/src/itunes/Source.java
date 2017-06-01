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

import utils.CommandUtil;
import utils.ConvertUtil;

/**
 * A music source (music library, CD, device, etc.)
 * 
 * From the iTunes script library 8.0.1:
 * 
    # contains audio CD playlists, device playlists, library playlists, 
    #   playlists, radio tuner playlists, user playlists; contained by application.
   
	capacity (double integer, r/o) : the total size of the source if it has a fixed size
	free space (double integer, r/o) : the free space on the source if it has a fixed size
	kind (library/iPod/audio CD/MP3 CD/device/radio tuner/shared library/unknown, r/o)
 */
public class Source extends Item {
	
	private List<AudioCDPlaylist> audioCDPlaylists;
	private List<DevicePlaylist> devicePlayLists;
	private List<LibraryPlaylist> libraryPlaylists;
	private List<Playlist> playlists;
	private List<RadioTunerPlaylist> radioTunerPlaylists;
	private List<UserPlaylist> userPlaylists;

	public List<AudioCDPlaylist> getAudioCDPlaylists() {
		return audioCDPlaylists;
	}
	
	public List<DevicePlaylist> getDevicePlayLists() {
		return devicePlayLists;
	}
	
	public List<LibraryPlaylist> getLibraryPlaylists() {
		return libraryPlaylists;
	}
	
	public List<Playlist> getPlaylists() {
		playlists = new ArrayList<Playlist>();
		Integer nrOfPlaylists = getNumberOfPlaylists();
		if (nrOfPlaylists != null) {
			int i = 1; // TODO: document it starts at 1... not 0!
			while (i <= nrOfPlaylists) {
				playlists.add(new Playlist(i));
				i++;
			}
		}
		return playlists;
	}
	
    private Integer getNumberOfPlaylists() {
    	Object result = CommandUtil.executeCommand("get {count} of playlists");
    	return ConvertUtil.asInteger(result);
    }
	
	public List<RadioTunerPlaylist> getRadioTunerPlaylists() {
		return radioTunerPlaylists;
	}
	
	public List<UserPlaylist> getUserPlaylists() {
		return userPlaylists;
	}
	
	/**
	 * Returns the total size of the source if it has a fixed size
	 * 
	 * TODO: why defined as double and int ? what is returned? number of bytes?
	 */
	public Double getCapacity() {
		// TODO
		return null;
	}

	/**
	 * Returns the free space on the source if it has a fixed size
	 * 
	 * TODO: why defined as double and int ? what is returned? number of bytes?
	 */
	public Double getFreeSpace() {
		// TODO
		return null;
	}

	/**
	 * Returns the source kind.
	 * 
	 * @return String with one of the following values:
	 *     library/iPod/audio CD/MP3 CD/device/radio tuner/shared library/unknown
	 *     
     * TODO: return Enumeration ?!!
	 */
	public String getKind() {
		// TODO
		return null;
	}
}
