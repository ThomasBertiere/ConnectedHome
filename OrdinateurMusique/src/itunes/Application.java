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

import java.util.List;

import utils.CommandUtil;
import utils.ConvertUtil;

/**
 * The application program (iTunes GUI)
 * 
 * From the iTunes script library 8.0.1:
 * 
        # contains browser windows, encoders, EQ presets, EQ windows, playlist windows, sources, visuals, windows.

		current encoder (encoder) : the currently selected encoder (MP3, AIFF, WAV, etc.)
		current EQ preset (EQ preset) : the currently selected equalizer preset
		current playlist (playlist, r/o) : the playlist containing the currently targeted track
		current stream title (text, r/o) : the name of the current song in the playing stream (provided by streaming server)
		current stream URL (text, r/o) : the URL of the playing stream or streaming web site (provided by streaming server)
		current track (track, r/o) : the current targeted track
		current visual (visual) : the currently selected visual plug-in
		EQ enabled (boolean) : is the equalizer enabled?
		fixed indexing (boolean) : true if all AppleScript track indices should be independent of the play order of the owning playlist.
		# frontmost (boolean) : is iTunes the frontmost application?
		# full screen (boolean) : are visuals displayed using the entire screen?
		# name (text, r/o) : the name of the application
		# mute (boolean) : has the sound output been muted?
		# `player position (integer) : the player�s position within the currently playing track in seconds.
		# player state (stopped/playing/paused/fast forwarding/rewinding, r/o) : is iTunes stopped, paused, or playing?
		selection (specifier, r/o) : the selection visible to the user
		# sound volume (integer) : the sound output volume (0 = minimum, 100 = maximum)
		# version (version, r/o) : the version of iTunes
		visuals enabled (boolean) : are visuals currently being displayed?
		visual size (small/medium/large) : the size of the displayed visual
 */
public class Application {
	private List<BrowserWindow> browserWindows;
	private List<Encoder> encoders;
	private List<EQPreset> eqPresets;
	private List<EQWindow> eqWindows;
	private List<PlaylistWindow> playListWindows;
	private List<Source> sources;
	private List<Visual> visuals;
	private List<Window> windows;
	
	public List<BrowserWindow> getBrowserWindows() {
		return browserWindows;
	}

	public List<Encoder> getEncoders() {
		return encoders;
	}

	public List<EQPreset> getEqPresets() {
		return eqPresets;
	}

	public List<EQWindow> getEqWindows() {
		return eqWindows;
	}

	public List<PlaylistWindow> getPlayListWindows() {
		return playListWindows;
	}

	public List<Source> getSources() {
		return sources;
	}

	public List<Visual> getVisuals() {
		return visuals;
	}

	public List<Window> getWindows() {
		return windows;
	}
	

	/**
	 * Returns the playlist containing the currently targeted track
	 * @return The name of the playlist containing the currently targeted track
	 */
	public String getCurrentPlaylist() {
		return ConvertUtil.asString(CommandUtil.executeCommand("get current playlist"));
	}

	/**
	 * Returns the the current targeted track
	 * @return The name of the current targeted track
	 */
	public String getCurrentTrack() {
		return ConvertUtil.asString(CommandUtil.executeCommand("get current track"));
	}

	/**
	 * Convenience method, not directly part of the api.
	 * Should perhaps become part of the Track class.
	 * @return The name of the current track.
	 */
	public String getCurrentTrackName() {
		return ConvertUtil.asString(CommandUtil.executeCommand("get name of current track"));
	}

	/**
	 * Convenience method, not directly part of the api.
	 * Should perhaps become part of the Track class.
	 * @return The rating of the current track.
	 */
	public int getCurrentTrackRating() {
		return ConvertUtil.asInt(CommandUtil.executeCommand("get rating of current track"));
	}

	/**
	 * Convenience method, not directly part of the api.
	 * Should perhaps become part of the Track class.
	 */
	public void setCurrentTrackRating(int rating) {
		CommandUtil.executeCommand("set rating of current track to " + rating);
	}
	
	String command = "tell application \"itunes\"\n name of current track\n end tell";

	/**
	 * Set iTunes as the frontmost application
	 */
	public void setFrontMost(boolean value) {
		if (value == true) {
			CommandUtil.executeCommand("set frontmost to " + value);
		} else {
			// iTunes does not handle 'false' or 0
            throw new RuntimeException("setFrontMost(false) does not work");
		}
	}

	/**
	 * Is iTunes the front most application?
	 * @return true, if it is the frontmost application, otherwise false
	 */
	public boolean isFrontMost() {
		Boolean result = ConvertUtil.asBoolean(CommandUtil.executeCommand("get frontmost"));
		if (result != null) {
			return result.booleanValue();
		}
		// Fallback mechanism
		return false;
	}

	/**
	 * Set visuals to display using the entire screen
	 * TODO: DOESN'T SEEM TO DO ANYTHING
	 */
	public void setFullScreen(boolean value) {
        CommandUtil.executeCommand("set full screen to " + value);
	}
	
	/**
	 * Are visuals displayed using the entire screen?
	 * TODO: DOESN'T SEEM TO DO ANYTHING, always returns true...
	 */
	public boolean isFullScreen() {
		Boolean result = ConvertUtil.asBoolean(CommandUtil.executeCommand("get full screen"));
		if (result != null) {
			return result.booleanValue();
		}
		// Fallback mechanism
		return false;
	}
	
	/**
	 * Returns the name of the application
	 * @return The name of the application: "iTunes"
	 */
	public String getName() {
		return ConvertUtil.asString(CommandUtil.executeCommand("get name"));
	}
	
	/**
	 * Mute / unmute the sound output
	 */
	public void mute(boolean value) {
        CommandUtil.executeCommand("set mute to " + value);
	}
	
	/**
	 * Has the sound output been muted?
	 */
	public boolean isMuted() {
		Boolean result = ConvertUtil.asBoolean(CommandUtil.executeCommand("get mute"));
		if (result != null) {
			return result.booleanValue();
		}
		// Fallback mechanism
		return false;
	}

	/**
	 * Returns the player�s position within the currently playing track in seconds.
	 * @return The player�s position within the currently playing track in seconds.
	 */
	public void setPlayerPosition(int value) {
		CommandUtil.executeCommand("set player position to " + value);
	}

	/**
	 * Returns the player�s position within the currently playing track in seconds.
	 * @return The player�s position within the currently playing track in seconds.
	 */
	public int getPlayerPosition() {
		return ConvertUtil.asInt(CommandUtil.executeCommand("get player position"));
	}

	/**
	 * Returns the iTunes state
	 * @return A String containing one of the following values:
	 *     stopped/playing/paused/fast forwarding/rewinding
	 *     
	 * TODO: use enums?
	 */
	public String getPlayerState() {
		return ConvertUtil.asString(CommandUtil.executeCommand("get player state"));
	}

	/**
	 * Returns the sound output volume
	 * @return a value [0..100], 0 = minimum, 100 = maximum, -1 if command failed
	 */
	public int getSoundVolume() {
		Integer value = ConvertUtil.asInteger(CommandUtil.executeCommand("get sound volume"));
		if (value != null) {
			return value.intValue();
		}
		return -1;
	}
	
	/**
	 * Set the sound output volume
	 * @param value, 0 = minimum, 100 = maximum
	 */
	public void setSoundVolume(int value) {
        if (0 <= value && value <= 100) {
        	CommandUtil.executeCommand("set sound volume to " + value);
        } else {
            throw new IllegalArgumentException("Illegal volume value, value must be 0 .. 100, but value is: " + value);
        }
	}

	/**
	 * Returns the version of the application
	 * @return The version of the application
	 */
	public String getVersion() {
		return ConvertUtil.asString(CommandUtil.executeCommand("get version"));
	}
}
