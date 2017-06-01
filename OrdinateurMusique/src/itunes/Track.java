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
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import parser.AppleScriptEnumeration;
import utils.CommandUtil;
import utils.ConvertUtil;

/**
 * A playable audio source
 * 
 *  From the iTunes script library 8.0.1:
 * 
    # contains artworks; contained by playlists.

	album (text) : the album name of the track
	album artist (text) : the album artist of the track
	album rating (integer) : the rating of the album for this track (0 to 100)
	album rating kind (user/computed, r/o) : the rating kind of the album rating for this track
	artist (text) : the artist/source of the track
	bit rate (integer, r/o) : the bit rate of the track (in kbps)
	bookmark (real) : the bookmark time of the track in seconds
	bookmarkable (boolean) : is the playback position for this track remembered?
	bpm (integer) : the tempo of this track in beats per minute
	category (text) : the category of the track
	comment (text) : freeform notes about the track
	compilation (boolean) : is this track from a compilation album?
	composer (text) : the composer of the track
	database ID (integer, r/o) : the common, unique ID for this track. If two tracks in different playlists have the same database ID, they are sharing the same data.
	date added (date, r/o) : the date the track was added to the playlist
	description (text) : the description of the track
	disc count (integer) : the total number of discs in the source album
	disc number (integer) : the index of the disc containing this track on the source album
	duration (real, r/o) : the length of the track in seconds
	enabled (boolean) : is this track checked for playback?
	episode ID (text) : the episode ID of the track
	episode number (integer) : the episode number of the track
	EQ (text) : the name of the EQ preset of the track
	finish (real) : the stop time of the track in seconds
	gapless (boolean) : is this track from a gapless album?
	genre (text) : the music/audio genre (category) of the track
	grouping (text) : the grouping (piece) of the track. Generally used to denote movements within a classical work.
	kind (text, r/o) : a text description of the track
	long description (text)
	lyrics (text) : the lyrics of the track
	modification date (date, r/o) : the modification date of the content of this track
	played count (integer) : number of times this track has been played
	played date (date) : the date and time this track was last played
	podcast (boolean, r/o) : is this track a podcast episode?
	rating (integer) : the rating of this track (0 to 100)
	rating kind (user/computed, r/o) : the rating kind of this track
	sample rate (integer, r/o) : the sample rate of the track (in Hz)
	season number (integer) : the season number of the track
	shufflable (boolean) : is this track included when shuffling?
	skipped count (integer) : number of times this track has been skipped
	skipped date (date) : the date and time this track was last skipped
	show (text) : the show name of the track
	sort album (text) : override string to use for the track when sorting by album
	sort artist (text) : override string to use for the track when sorting by artist
	sort album artist (text) : override string to use for the track when sorting by album artist
	sort name (text) : override string to use for the track when sorting by name
	sort composer (text) : override string to use for the track when sorting by composer
	sort show (text) : override string to use for the track when sorting by show name
	size (integer, r/o) : the size of the track (in bytes)
	start (real) : the start time of the track in seconds
	time (text, r/o) : the length of the track in MM:SS format
	track count (integer) : the total number of tracks on the source album
	track number (integer) : the index of the track on the source album
	unplayed (boolean) : is this track unplayed?
	video kind (none/movie/music video/TV show) : kind of video track
	volume adjustment (integer) : relative volume adjustment of the track (-100% to 100%)
	year (integer) : the year the track was recorded/released
 */
public class Track extends Item {
	
	public enum RatingKind {
		computed,
		user
	}

	/**
	 * An enumeration that enables listing of Track properties to 
	 * be retrieved and cached upon creation of the Track object.
	 * 
	 * The enum values are the AppleScript constants, spaces are 
	 * replaced with underscores.
	 */
	public enum TrackProperty {
		album,
		album_artist,
		album_rating,
		album_rating_kind,
		artist,
		bit_rate,
		bookmark,
		bookmarkable,
		bpm,
		category,
		comment,
		compilation,
		composer,
		database_ID,
		date_added,
		description,
		disc_count,
		disc_number,
		duration,
		enabled,
		episode_ID,
		episode_number,
		EQ,
		finish,
		gapless,
		genre,
		grouping,
		id, // From Item
		kind,
		long_description,
		lyrics,
		modification_date,
		name, // From Item
		played_count,
		played_date,
		podcast,
		rating,
		rating_kind,
		sample_rate,
		season_number,
		show,
		shufflable,
		size,
		skipped_count,
		skipped_date,
		sort_album,
		sort_album_artist,
		sort_artist,
		sort_composer,
		sort_name,
		sort_show,
		start,
		time,
		track_count,
		track_number,
		unplayed,
		video_kind,
		volume_adjustment,
		year
	}
	
	public enum VideoKind {
		movie,
		music_video,
		none,
		TV_show
	}
	
	private static Logger logger = Logger.getLogger(Track.class.toString());
	private String album;
	private String album_artist;
	private int album_rating;
	private RatingKind album_rating_kind;
	private String artist;
	private List<ArtWork> artworks;
	private int bit_rate;
	private double bookmark;
	private boolean bookmarkable;
	private int bpm;
	private String category;
	private String comment;
	private boolean compilation;
	private String composer;
	private int database_ID;
	private Date date_added; // TODO: Check format, is it OSX locale specific? 
	private String description;
	private int disc_count;
	private int disc_number;
	private double duration;
	private boolean enabled;
	private String episode_ID;
	private int episode_number;
	private String EQ;
	private double finish;
	private boolean gapless;
	private String genre;
	private String grouping;
	private String kind;
	private String long_description;
	private String lyrics;
	private Date modification_date;
	private String name;
	private int played_count;
	private Date played_date;
	private Playlist playlist;
	private boolean podcast;
	private int rating;
	private RatingKind rating_kind;
	private int sample_rate;
	private Integer season_number;
	private String show;
	private boolean shufflable;
	private int size;
	private int skipped_count;
	private Date skipped_date;
	private String sort_album;
	private String sort_album_artist;
	private String sort_artist;
	private String sort_composer;
	private String sort_name;
	private String sort_show;
	private double start;
	private String time;
	private Integer track_count;
	private Integer track_number;
	private List<TrackProperty> trackProperties = new ArrayList<TrackProperty>();
	private boolean unplayed;
	private VideoKind video_kind;
	private int volume_adjustment;
	private Integer year;

	/**
	 * The Track constructor.
	 * 
	 * The easiest way to navigate trough songs in iTunes is via a playlist track index.
	 * Therefore Tracks are constructed on playlist information.
	 * 
	 * A Playlist can construct its Tracks and will have to define the index of the track. 
	 * To enable retrieving and persisting data it is require to let the Track know it's playlist.
	 * 
	 * @param index The index of the Track in the given Playlist
	 * @param playlist The Playlist that contains the Track at the given index.
	 */
	public Track(int index, Playlist playlist) {
		super(index);
		this.playlist = playlist;
	}

	/**
	 * @return the album
	 */
	public String getAlbum() {
		return album;
	}

	/**
	 * @return the album_artist
	 */
	public String getAlbumArtist() {
		return album_artist;
	}
	
	/**
	 * @return the album_rating
	 */
	public int getAlbumRating() {
		return album_rating;
	}
	
	/**
	 * @return the album_rating_kind
	 */
	public RatingKind getAlbumRatingKind() {
		String value = ConvertUtil.asString(getProperty(TrackProperty.album_rating_kind));
		if (RatingKind.user.name().equals(value)) {
			return RatingKind.user;
		}
		if (RatingKind.computed.name().equals(value)) {
			return RatingKind.computed;
		}
		return null;
	}

	/**
	 * Returns the artist/source of the track
	 * @return the artist/source of the track
	 */
	public String getArtist() {
		return ConvertUtil.asString(getProperty(TrackProperty.artist));
	}
	
	public List<ArtWork> getArtworks() {
		return artworks;
	}

	/**
	 * @return the bit_rate
	 */
	public int getBit_rate() {
		return bit_rate;
	}

	/**
	 * @return the bookmark
	 */
	public double getBookmark() {
		return bookmark;
	}

	/**
	 * @return the bpm
	 */
	public int getBpm() {
		return bpm;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * Get freeform notes about the track
	 * @return String containing the notes
	 */
	public String getComment() {
		return ConvertUtil.asString(getProperty(TrackProperty.comment));
	}

	/**
	 * @return the composer
	 */
	public String getComposer() {
		return ConvertUtil.asString(getProperty(TrackProperty.composer));
	}
	
	/**
	 * @return the database_ID
	 */
	public int getDatabaseID() {
		return ConvertUtil.asInt(getProperty(TrackProperty.database_ID));
	}
	
	/**
	 * @return the date_added
	 */
	public Date getDateAdded() {
		return ConvertUtil.asDate(getProperty(TrackProperty.date_added));
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return ConvertUtil.asString(getProperty(TrackProperty.description));
	}

	/**
	 * @return the disc_count
	 */
	public int getDiscCount() {
		return ConvertUtil.asInt(getProperty(TrackProperty.disc_count));
	}
	
	/**
	 * @return the disc_number
	 */
	public int getDiscNumber() {
		return ConvertUtil.asInt(getProperty(TrackProperty.disc_number));
	}

	/**
	 * @return the duration
	 */
	public double getDuration() {
		return ConvertUtil.asDouble(getProperty(TrackProperty.duration));
	}

	/**
	 * @return the episode_ID
	 */
	public String getEpisodeID() {
		return ConvertUtil.asString(getProperty(TrackProperty.episode_ID));
	}

	/**
	 * @return the episode_number
	 */
	public int getEpisodeNumber() {
		return ConvertUtil.asInt(getProperty(TrackProperty.episode_number));
	}

	/**
	 * @return the eQ
	 */
	public String getEQ() {
		return ConvertUtil.asString(getProperty(TrackProperty.EQ));
	}

	/**
	 * @return the finish
	 */
	public double getFinish() {
		return ConvertUtil.asDouble(getProperty(TrackProperty.finish));
	}

	/**
	 * @return the genre
	 */
	public String getGenre() {
		return ConvertUtil.asString(getProperty(TrackProperty.genre));
	}

	/**
	 * @return the grouping
	 */
	public String getGrouping() {
		return ConvertUtil.asString(getProperty(TrackProperty.grouping));
	}

	/**
	 * Get kind
	 * @return String the kind
	 */
	public String getKind() {
		return ConvertUtil.asString(getProperty(TrackProperty.kind));
	}

	/**
	 * @return the long_description
	 */
	public String getLongDescription() {
		return ConvertUtil.asString(getProperty(TrackProperty.long_description));
	}

	/**
	 * @return the lyrics
	 */
	public String getLyrics() {
		return ConvertUtil.asString(getProperty(TrackProperty.lyrics));
	}

	/**
	 * @return the modification_date
	 */
	public Date getModificationDate() {
		return ConvertUtil.asDate(getProperty(TrackProperty.modification_date));
	}

	@Override
	public String getName() {
		return ConvertUtil.asString(getProperty(TrackProperty.name));
	}

	/**
	 * Returns the number of times this track has been played
	 * @return the number of times this track has been played, -1 if failed
	 */
	public int getPlayedCount() {
		return ConvertUtil.asInt(getProperty(TrackProperty.played_count));
	}

	/**
	 * Returns the date and time this track was last played
	 * TODO: analyse the format, have to dive into AppleScript date
	 * Example string returned on my system: "dinsdag, 1 april 2008 15:12:23"
	 * 
	 * @return the date and time this track was last played
	 */
	public Date getPlayedDate() {
		// TODO: return Date type?!
		return ConvertUtil.asDate(getProperty(TrackProperty.played_date));
	}

	/**
	 * @return the playlist
	 */
	public Playlist getPlaylist() {
		return playlist;
	}

	private Object getProperty(TrackProperty property) {
		if (trackProperties.contains(property)) {
			switch (property) {
			case id: return getId();
			case name: return name;
			case album: return album;
			case album_artist: return album_artist;
			case album_rating: return album_rating;
			case album_rating_kind: return album_rating_kind;
			case artist: return artist;
			case bit_rate: return bit_rate;
			case bookmark: return bookmark;
			case bookmarkable: return bookmarkable;
			case bpm: return bpm;
			case category: return category;
			case comment: return comment;
			case compilation: return compilation;
			case composer: return composer;
			case database_ID: return database_ID;
			case date_added: return date_added;
			case description: return description;
			case disc_count: return disc_count;
			case disc_number: return disc_number;
			case duration: return duration;
			case enabled: return enabled;
			case episode_ID: return episode_ID;
			case episode_number: return episode_number;
			case EQ: return EQ;
			case finish: return finish;
			case gapless: return gapless;
			case genre: return genre;
			case grouping: return grouping;
			case kind: return kind;
			case long_description: return long_description;
			case lyrics: return lyrics;
			case modification_date: return modification_date;
			case played_count: return played_count;
			case played_date: return played_date;
			case podcast: return podcast;
			case rating: return rating;
			case rating_kind: return rating_kind;
			case sample_rate: return sample_rate;
			case season_number: return season_number;
			case shufflable: return shufflable;
			case skipped_count: return skipped_count;
			case skipped_date: return skipped_date;
			case show: return show;
			case sort_album: return sort_album;
			case sort_artist: return sort_artist;
			case sort_album_artist: return sort_album_artist;
			case sort_name: return sort_name;
			case sort_composer: return sort_composer;
			case sort_show: return sort_show;
			case size: return size;
			case start: return start;
			case time: return time;
			case track_count: return track_count;
			case track_number: return track_number;
			case unplayed: return unplayed;
			case video_kind: return video_kind;
			case volume_adjustment: return volume_adjustment;
			case year: return year;
		    }
		}
		
		String propertyForQuery = property.name().replace('_', ' ');
		Object result = CommandUtil.executeCommand("get " + propertyForQuery + " of track " + getIndex() + " of playlist " + playlist.getIndex());
		trackProperties.add(property);
		return result;
	}

	/**
	 * Returns the rating of this track (0 to 100)
	 * @return the rating of this track (0 to 100), -1 if failed
	 */
	public int getRating() {
		return ConvertUtil.asInt(getProperty(TrackProperty.rating));
	}

	/**
	 * Returns the rating kind of this track
	 * @return RatingKind user/computed
	 */
	public RatingKind getRatingKind() {
		String value = ConvertUtil.asString(getProperty(TrackProperty.rating_kind));
		if (RatingKind.user.name().equals(value)) {
			return RatingKind.user;
		}
		if (RatingKind.computed.name().equals(value)) {
			return RatingKind.computed;
		}
		return null;
	}

	/**
	 * @return the sample_rate
	 */
	public int getSampleRate() {
		return ConvertUtil.asInt(getProperty(TrackProperty.sample_rate));
	}

	/**
	 * @return the season_number
	 */
	public Integer getSeasonNumber() {
		return ConvertUtil.asInteger(getProperty(TrackProperty.season_number));
	}

	/**
	 * @return the show
	 */
	public String getShow() {
		return ConvertUtil.asString(getProperty(TrackProperty.show));
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return ConvertUtil.asInt(getProperty(TrackProperty.size));
	}

	/**
	 * @return the skipped_count
	 */
	public int getSkippedCount() {
		return ConvertUtil.asInt(getProperty(TrackProperty.skipped_count));
	}

	/**
	 * @return the skipped_date
	 */
	public Date getSkippedDate() {
		return ConvertUtil.asDate(getProperty(TrackProperty.skipped_date));
	}

	/**
	 * @return the sort_album
	 */
	public String getSortAlbum() {
		return ConvertUtil.asString(getProperty(TrackProperty.sort_album));
	}

	/**
	 * @return the sort_album_artist
	 */
	public String getSortAlbumArtist() {
		return ConvertUtil.asString(getProperty(TrackProperty.sort_album_artist));
	}

	/**
	 * @return the sort_artist
	 */
	public String getSortArtist() {
		return ConvertUtil.asString(getProperty(TrackProperty.sort_artist));
	}

	/**
	 * @return the sort_composer
	 */
	public String getSortComposer() {
		return ConvertUtil.asString(getProperty(TrackProperty.sort_composer));
	}

	/**
	 * @return the sort_name
	 */
	public String getSortName() {
		return ConvertUtil.asString(getProperty(TrackProperty.sort_name));
	}

	/**
	 * @return the sort_show
	 */
	public String getSortShow() {
		return ConvertUtil.asString(getProperty(TrackProperty.sort_show));
	}

	/**
	 * @return the start
	 */
	public double getStart() {
		return ConvertUtil.asDouble(getProperty(TrackProperty.start));
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return ConvertUtil.asString(getProperty(TrackProperty.time));
	}

	/**
	 * @return the track_count
	 */
	public Integer getTrackCount() {
		return ConvertUtil.asInteger(getProperty(TrackProperty.track_count));
	}

	/**
	 * @return the track_number
	 */
	public Integer getTrackNumber() {
		return ConvertUtil.asInteger(getProperty(TrackProperty.track_number));
	}

	/**
	 * @return the trackProperties
	 */
	public List<TrackProperty> getTrackProperties() {
		return trackProperties;
	}

	/**
	 * @return the video_kind
	 */
	public VideoKind getVideoKind() {
		return video_kind;
	}

	/**
	 * @return the volume_adjustment
	 */
	public int getVolumeAdjustment() {
		return ConvertUtil.asInt(getProperty(TrackProperty.volume_adjustment));
	}

	/**
	 * @return the year
	 */
	public Integer getYear() {
		return ConvertUtil.asInteger(getProperty(TrackProperty.year));
	}

	/**
	 * @return the bookmarkable
	 */
	public boolean isBookmarkable() {
		return ConvertUtil.asBoolean(getProperty(TrackProperty.bookmarkable));
	}

	/**
	 * @return the compilation
	 */
	public boolean isCompilation() {
		return ConvertUtil.asBoolean(getProperty(TrackProperty.compilation));
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return ConvertUtil.asBoolean(getProperty(TrackProperty.enabled));
	}

	/**
	 * @return the gapless
	 */
	public boolean isGapless() {
		return ConvertUtil.asBoolean(getProperty(TrackProperty.gapless));
	}

	/**
	 * @return the podcast
	 */
	public boolean isPodcast() {
		return ConvertUtil.asBoolean(getProperty(TrackProperty.podcast));
	}

	/**
	 * @return the shufflable
	 */
	public boolean isShufflable() {
		return ConvertUtil.asBoolean(getProperty(TrackProperty.shufflable));
	}

	/**
	 * @return the unplayed
	 */
	public boolean isUnplayed() {
		return ConvertUtil.asBoolean(getProperty(TrackProperty.unplayed));
	}

	/**
	 * Set the artist/source of the track
	 */
	public void setArtist(String artist) {
		CommandUtil.executeCommand("set artist of track " + getIndex() + " of playlist " + playlist.getIndex() + " to \"" + artist + "\"");
		if (!trackProperties.contains(TrackProperty.artist)) {
		    trackProperties.add(TrackProperty.artist);
		}
		this.artist = artist; 
	}

	/**
	 * Set freeform notes about the track
	 * @param comment the notes to set
	 */
	public void setComment(String comment) {
		CommandUtil.executeCommand("set comment of track " + getIndex() + " of playlist " + playlist.getIndex() + " to \"" + comment + "\"");
		if (!trackProperties.contains(TrackProperty.comment)) {
		    trackProperties.add(TrackProperty.comment);
		}
		this.comment = comment; 
	}

	/**
	 * Set the number of times this track has been played
	 * @param value the number of times this track has been played
	 */
	public void setPlayedCount(int value) {
		// TODO
	}

	/**
	 * Set the date and time this track was last played
	 * @param value the date and time this track was last played
	 */
	public void setPlayedDate(String value) {
		// TODO
	}

	/**
	 * Convenience method to construct Track data, without persisting it
	 */
	public void setProperty(TrackProperty property, Object value) {
		if (value instanceof AppleScriptEnumeration) {
			if (((AppleScriptEnumeration) value).getId().equals("missing value")) {
				value = null;
			}
		}
		trackProperties.add(property);
		switch (property) {
			case id: setId((Integer) value); break;
			case name: name = (String) value; break;
			case album: album = (String) value; break;
			case album_artist: album_artist = (String) value; break;
			case album_rating: album_rating = (Integer) value; break;
			case album_rating_kind: album_rating_kind = (RatingKind) value; break;
			case artist: artist = (String) value; break;
			case bit_rate: bit_rate = (Integer) value; break;
			case bookmark: bookmark = (Double) value; break;
			case bookmarkable: bookmarkable = (Boolean) value; break;
			case bpm: bpm = (Integer) value; break;
			case category: category = (String) value; break;
			case comment: comment = (String) value; break;
			case compilation: compilation = (Boolean) value; break;
			case composer: composer = (String) value; break;
			case database_ID: database_ID = (Integer) value; break;
			case date_added: date_added = (Date) value; break;
			case description: description = (String) value; break;
			case disc_count: disc_count = (Integer) value; break;
			case disc_number: disc_number = (Integer) value; break;
			case duration: duration = (Double) value; break;
			case enabled: enabled = (Boolean) value; break;
			case episode_ID: episode_ID = (String) value; break;
			case episode_number: episode_number = (Integer) value; break;
			case EQ: EQ = (String) value; break;
			case finish: finish = (Double) value; break;
			case gapless: gapless = (Boolean) value; break;
			case genre: genre = (String) value; break;
			case grouping: grouping = (String) value; break;
			case kind: kind = (String) value; break;
			case long_description: long_description = (String) value; break;
			case lyrics: lyrics = (String) value; break;
			case modification_date: modification_date = (Date) value; break;
			case played_count: played_count = (Integer) value; break;
			case played_date:
				System.out.println("### VALUE = " + value);
				played_date = (Date) value; break;
			case podcast: podcast = (Boolean) value; break;
			case rating: rating = (Integer) value; break;
			case rating_kind: rating_kind = (RatingKind) value; break;
			case sample_rate: sample_rate = (Integer) value; break;
			case season_number: season_number = (Integer) value; break;
			case shufflable: shufflable = (Boolean) value; break;
			case skipped_count: skipped_count = (Integer) value; break;
			case skipped_date: skipped_date = (Date) value; break;
			case show: show = (String) value; break;
			case sort_album: sort_album = (String) value; break;
			case sort_artist: sort_artist = (String) value; break;
			case sort_album_artist: sort_album_artist = (String) value; break;
			case sort_name: sort_name = (String) value; break;
			case sort_composer: sort_composer = (String) value; break;
			case sort_show: sort_show = (String) value; break;
			case size: size = (Integer) value; break;
			case start: start = (Double) value; break;
			case time: time = (String) value; break;
			case track_count: track_count = (Integer) value; break;
			case track_number: track_number = (Integer) value; break;
			case unplayed: unplayed = (Boolean) value; break;
			case video_kind: video_kind = (VideoKind) value; break;
			case volume_adjustment: volume_adjustment = (Integer) value; break;
			case year: year = (Integer) value; break;
		}
	}

	/**
	 * Set the rating of this track (0 to 100)
	 * @param value (0 to 100)
	 */
	public void setRating(int value) {
		// TODO
	}

	public String toString() {
		return getId() + " - " + getArtist() + " - " + getName();
	}
}
