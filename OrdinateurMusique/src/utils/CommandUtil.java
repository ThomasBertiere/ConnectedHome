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

import java.util.logging.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import parser.AppleScriptValueParser;

public class CommandUtil {
	private static Logger logger = Logger.getLogger(CommandUtil.class.toString());
	private static ScriptEngine se = null;
	
	public static void setScriptEngine(ScriptEngine value) {
        se = value;
	}
	
	public static ScriptEngine getScriptEngine() {
		return se;
	}

	public static Object executeCommandTo(String command) {
		Object result = null;
		try {
			logger.fine("executeCommandTo: " + command);
			result = se.eval("tell application \"iTunes\" to " + command);
		} catch (ScriptException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static Object executeCommand(String command) {
		Object parsedResult = null;
    	try {
    		logger.fine("executeCommand: " + command);
    		Object result = se.eval("tell application \"iTunes\"\n" + command + "\nend tell");
    		parsedResult = new AppleScriptValueParser().parse(((StringBuilder) result).toString());
    		//logger.info("parsedResult: " + parsedResult);
    	} catch (ScriptException ex) {
    		ex.printStackTrace();
    	}
    	return parsedResult;
	}

}
