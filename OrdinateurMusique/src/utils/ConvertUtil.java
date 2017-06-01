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

package utils;

import java.util.Date;
import java.util.List;

import parser.AppleScriptEnumeration;


public class ConvertUtil {
	public static Boolean asBoolean(Object parsedObject) {
		Boolean result = null;
        if (parsedObject instanceof Boolean) {
        	result = (Boolean) parsedObject;
        } else if (parsedObject instanceof String) {
        	result = Boolean.valueOf((String) parsedObject);
        } else if (parsedObject instanceof List<?>) {
        	List<?> results = (List<?>) parsedObject;
        	if (results.size() == 1 && results.get(0) instanceof Boolean) {
        		result = (Boolean) results.get(0);
        	}
        }
		return result;
	}

	public static Integer asInteger(Object parsedObject) {
    	Integer result = null;
        if (parsedObject instanceof Integer) {
        	result = (Integer) parsedObject;
        } else if (parsedObject instanceof String) {
        	if ("".equals((String) parsedObject)) {
        	    return null;
        	} else if ("missing value".equals((String) parsedObject)) {
        		return null;
        	} else {
    		    result = Integer.valueOf((String) parsedObject);
        	}
    	} else if (parsedObject instanceof List<?>) {
    		List<?> results = (List<?>) parsedObject;
    		if (results.size() == 1 && results.get(0) instanceof Integer) {
    		    result = (Integer) results.get(0);
    		}
        }
        return result;
    }

    public static String asString(Object parsedObject) {
    	String result = null;
    	if (parsedObject instanceof String) {
    		result = (String) parsedObject;
    	} else if (parsedObject instanceof List<?>) {
    		List<?> results = (List<?>) parsedObject;
    		if (results.size() == 1 && results.get(0) instanceof AppleScriptEnumeration) {
    		    result = ((AppleScriptEnumeration) results.get(0)).getId();
    		}
    	}
    	return result;
    }

    /**
     * Returns the value as int, 0 if value was not recognized.
     * @param parsedObject
     * @return
     */
	public static int asInt(Object parsedObject) {
		Integer value = asInteger(parsedObject);
		if (value != null) {
		    return value.intValue();
		} else {
			return 0;
		}
	}

	public static double asDouble(Object parsedObject) {
		throw new RuntimeException("Not implemented yet");
	}

	public static Date asDate(Object parsedObject) {
		Date result = null;
		if (parsedObject instanceof String) {
			String value = (String) parsedObject;
			if (value.equals("missing value")) {
				return null;
			} else {
				// TODO, use formatter...
				Date test = new Date(value);
			}
    	} else if (parsedObject instanceof List<?>) {
    		List<?> results = (List<?>) parsedObject;
    		// TODO
    	}
		return result;
	}
}
