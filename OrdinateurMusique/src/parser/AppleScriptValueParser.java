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

package parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper class to parse AppleScript results
 * 
 * For an exact definition of list and record see
 * http://developer.apple.com/documentation/applescript/conceptual/applescriptlangguide/reference/ASLR_classes.html#//apple_ref/doc/uid/TP40000983-CH1g-BBCDBHIE
 * 
 * List Example:
 * {"hello", variable, 5.55, date "Monday, April 1, 2002", true, {3, "another", "list"}}
 * 
 * TODO: support / recognize record results, although iTunes (8.0) never returns 
 * records it is handy for other applications that can return records.
 * 
 * Record Example:
 * {name:"Stephen", height:74.5, weight:175}
 * 
 */
public class AppleScriptValueParser {
	
	private String input;
	private int pos;

	public Object parse(String input) {
		input = input.replace("\n", "");
		this.input = input;
		pos = 0;
		
		return parseValue(false);
	}

	private Object parseValue(boolean insideList) {
		skipWhitespace();
		if (pos >= input.length()) {
			return "";
		}
		switch (peek()) {
			case '"' : return parseString(); 
			case '{' : return parseList(); 
			default  : return parseAsString(insideList);
		}
	}
	
	private String parseString() {
		assert peek() == '"';
		StringBuilder result = new StringBuilder();
		while (++pos < input.length()) {
			switch (peek()) {
			case '"': 
				pos++; 
				return result.toString(); 
			case '\\': 
				switch (peekNext()) {
				case '"': result.append('\"'); pos++; break;
				case '\\': result.append('\\'); pos++; break;
				default: result.append(peek()); 
				}
				break;
			default:
				result.append(peek());	
			}
		}
		return result.toString();
	}

	@SuppressWarnings("unchecked")
	private List parseList() {
		assert peek() == '{';
		pos++;
		skipWhitespace();
		List result = new ArrayList();
		while (pos < input.length()) {
			switch (peek()) {
			case '}': 
				pos++; 
				return result; 
			case ',': 
				pos++;
				result.add(parseValue(true));
				skipWhitespace();
				break;
			default:
				skipWhitespace();
				result.add(parseValue(true));	
			}
		}
		return result;
		
	}

	/**
	 * Parses as String, it could be a number, boolean, date, enumeration, etc..
	 */
	private Object parseAsString(boolean insideList) {
		int start = pos;
		
		// Read as String
		while (pos < input.length() && !(peek() == ',') && !(peek() == '}')) {
			pos++;
		}
		String value = input.substring(start, pos);
		
		if (value.startsWith("date ")) {
			//System.out.println("### date value 1: " + input.substring(start, input.length()));
			// date is formatted like: date "woensdag, 21 mei 2008 16:28:42" for Duth Locale
			// date is formatted like: Tuesday, November 18, 2008 6:02:45 AM for English locale
			// however we stopped after the first ','
			
			// Second:
			pos++;
			while (pos < input.length() && !(peek() == ',') && !(peek() == '}')) {
				pos++;
			}
			
			// Third (English locale):
			pos++;
			while (pos < input.length() && !(peek() == ',') && !(peek() == '}')) {
				pos++;
			}
			
			value = input.substring(start, pos);
			value = value.replace("date ", "");
			value = value.replace("\"", "");
			// TODO: the following DateFormat only works when Locale is Dutch
			//SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss"); // woensdag, 21 mei 2008 16:28:42
			SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM dd, yyyy hh:mm:ss aa"); // Monday, April 28, 2008 6:02:45 AM
			Date date = null;
			try {
				System.out.println("### date value 2: " + value);
				date = dateFormat.parse(value);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return date;
		}
		
		if (insideList) {
			// Try to convert to Integer or Double
			try {
				if (value.contains(".")) {
				    return Double.parseDouble(value);
				} else {
					return Integer.parseInt(value);
				}
			} catch (NumberFormatException e) {
				// Continue, try other types
			}
			
			// Try to convert to Boolean
			if (value.equals("true")) {
				return new Boolean(true);
			} else if (value.equals("false")) {
				return new Boolean(false);
			}

			// Assume it is an Enumeration situation, leave it up to the
			// calling side to convert it into an enum
			return new AppleScriptEnumeration(value);
		} else {
			// It is impossible to type the value, example problem:
			/*

			 tell application "iTunes"
			    get {name, special kind} of playlist 5
		     end tell

		     result: {"Partyshuffle", Party Shuffle}
		     
		     BUT:
		     get name of playlist 5
		     result: Partyshuffle
		     
		     get special kind of playlist 5
			 result: Party Shuffle
			 
			 So you could say that if there is a single result we cannot parse the result 
			 into a typed Enumeration, all we can do is return it as a String.
			 A workaround is to force the result into a list, then we can see
			 if it is a String or an Enumeration:

		     get {name} of playlist 5
		     result: {"Partyshuffle}
		     
		     get {special kind} of playlist 5
			 result: {Party Shuffle}

			*/
		}
		
		return value;
	}


	private char peek() {
		return input.charAt(pos);
	}
	
	private char peekNext() {
		if (pos + 1 < input.length()) {
			return input.charAt(pos + 1);
		}
		return 0;
	}
	
	private void skipWhitespace() {
		while (pos < input.length() && Character.isWhitespace(peek())) pos++;
	}
}