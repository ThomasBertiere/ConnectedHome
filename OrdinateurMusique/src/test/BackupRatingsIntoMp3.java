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

package test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import itunes.ITunesSuite;
import itunes.Playlist;
import itunes.Source;
import itunes.Track;
import itunes.Track.TrackProperty;

/**
 * Backups iTunes track data into the mp3's comment field.
 * Why? To prevent the loss of rating data. Or to sync between different 
 * iTunes libraries or different pc's.
 * 
 * Example xml in the comment field: <track><rating>80</rating><playedcount>0</playedcount><playeddate></playeddate><originalcomment></originalcomment><!-- iTunes Rating Script - www.escay.nl/itunes/ --></track>
 */
public class BackupRatingsIntoMp3 {
	public static void main(String [] args) {
		ITunesSuite iTunes = new ITunesSuite();
	    iTunes.start();

	    Source source = new Source();
	    
	    List<Playlist> playLists = source.getPlaylists();
	    System.out.println("Playlists: " + playLists);
	    for (Playlist playlist : playLists) {
	    	if (playlist.getName().equals("Music") || playlist.getName().equals("Muziek")) {
		    	System.out.println("Playlist, index: " + playlist.getIndex() + ", data: " + playlist);
		    	
	    		List<Track> tracks = playlist.getTracks(TrackProperty.name, TrackProperty.artist, TrackProperty.rating, TrackProperty.played_count, TrackProperty.played_date, TrackProperty.comment);
	    		for (Track track : tracks) {
	    			System.out.println("Track, index: " + track.getIndex() + ", data: " + track);
	    		    
	    			if (track.getComment().startsWith("<track>")) {
	    		        // Update existing track xml contents
	    				updateTrackXml(track);
	    		    } else {
	    		    	// Create new track xml contents
	    			    createTrackXml(track);
	    		    }
	    		}
	    	}
	    }
	}
	
	
	private static void createTrackXml(Track track) {
		setTrackXml(track, track.getRating(), track.getPlayedCount(), track.getPlayedDate(), track.getComment());
	}
	
	private static void setTrackXml(Track track, int rating, int playedCount, Date playedDate, String comment) {
		String xmlString = null;
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			Element trackElement = doc.createElement("track");
			
			Element ratingElement = doc.createElement("rating");
			ratingElement.appendChild(doc.createTextNode("" + rating));
			
			Element playedCountElement = doc.createElement("playedcount");
			playedCountElement.appendChild(doc.createTextNode("" + playedCount));
			
			Element playedDateElement = doc.createElement("playeddate");
			if (playedDate != null) {
				// Save as "Mon, Apr 01, 2002"
				SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM dd, yyyy", Locale.ENGLISH);
			    playedDateElement.appendChild(doc.createTextNode(dateFormat.format(playedDate)));
			}
			Element originalCommentElement = doc.createElement("originalcomment");
			if (comment != null) {
			    originalCommentElement.appendChild(doc.createTextNode(comment));
			}
			Comment commentNode = doc.createComment(" iTunes Rating Script - www.escay.nl/itunes/ ");
			
			trackElement.appendChild(ratingElement);
			trackElement.appendChild(playedCountElement);
			trackElement.appendChild(playedDateElement);
			trackElement.appendChild(originalCommentElement);
			trackElement.appendChild(commentNode);

			doc.appendChild(trackElement);
			
			TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    DOMSource source = new DOMSource(doc);  
            StringWriter sw = new StringWriter();
            StreamResult rsult = new StreamResult(sw);
            transformer.transform(source, rsult);
            xmlString = sw.toString();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		// Remove <?xml version="1.0" encoding="UTF-8" standalone="no"?>
		if (xmlString.startsWith("<?xml")) {
			int endTag = xmlString.indexOf('>');
			xmlString = xmlString.substring(endTag + 1);
		}
		
		// Only update the track if the comments have changed
		if (!track.getComment().equals(xmlString)) {
		    track.setComment(xmlString);
		}
	}

	/**
	 * Compares track data with the comment field xml. Decides what the new values are to
	 * be stored in the comment field. Eventually the comment field is updated to reflect
	 * any changes. It is possible that track fields like the rating are updated if the
	 * comment xml contains higher / better values.  
	 * @param track, the track to be updated.
	 */
	private static void updateTrackXml(Track track) {
		int rating = 0;
		int playedCount = 0;
		Date playedDate = null;
		String originalComment = null;
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			String xmlString = track.getComment();
			InputStream is = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
			Document doc = docBuilder.parse(is);
			
			// Look at the child nodes of <track>
			NodeList nodes = doc.getDocumentElement().getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (element.getNodeName().equals("rating")) {
                    	// Expecting 1 child, which is the text value
                      	Node valueNode = element.getFirstChild();
                    	if (valueNode != null && valueNode.getNodeType() == Node.TEXT_NODE) {
                    		//System.out.println("Rating from xml is: " + valueNode.getNodeValue());
                    		Integer xmlValue = new Integer(valueNode.getNodeValue());
                    		// Save the highest value
                    		rating = track.getRating();
                    		if (rating < xmlValue.intValue()) {
                    			track.setRating(xmlValue.intValue());
                    			rating = xmlValue.intValue();
                    		}
                    	}
                    } else if (element.getNodeName().equals("playedcount")) {
                    	// Expecting 1 child, which is the text value
                    	Node valueNode = element.getFirstChild();
                    	if (valueNode != null && valueNode.getNodeType() == Node.TEXT_NODE) {
                    		//System.out.println("PlayedCount from xml is: " + valueNode.getNodeValue());
                    		Integer xmlValue = new Integer(valueNode.getNodeValue());
                    		// Save the highest value
                    		// TODO: why is this out of sync, should we maybe increase the value, or let
                    		// the user decide what to do....
                    		playedCount = track.getPlayedCount();
                    		if (playedCount < xmlValue.intValue()) {
                    			track.setPlayedCount(xmlValue.intValue());
                    			playedCount = xmlValue.intValue();
                    		}
                    	}
                    } else if (element.getNodeName().equals("playeddate")) {
                    	// Expecting 1 child, which is the text value
                    	Node valueNode = element.getFirstChild();
                    	if (valueNode != null && valueNode.getNodeType() == Node.TEXT_NODE) {
                    		//System.out.println("PlayedDate from xml is: " + valueNode.getNodeValue());
                            // TODO: until I have no decent date value transformations
                    		// I only use the track.getPlayedDate value
                    		playedDate = track.getPlayedDate();
                    	}
                    } else if (element.getNodeName().equals("originalcomment")) {
                    	// Expecting 1 child, which is the text value
                    	Node valueNode = element.getFirstChild();
                    	if (valueNode != null && valueNode.getNodeType() == Node.TEXT_NODE) {
                    		//System.out.println("Original comment from xml is: " + valueNode.getNodeValue());
                    		originalComment = valueNode.getNodeValue();
                    	}
                    } else {
                    	// TODO: throw exception... for now just return
                    	System.out.println("Unsupported node name: " + element.getNodeName());
                    	return;
                    }
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Now update the track comment field with the found values
		setTrackXml(track, rating, playedCount, playedDate, originalComment);
	}
}
