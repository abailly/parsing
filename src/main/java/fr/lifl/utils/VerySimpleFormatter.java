/*
 * Created on Jun 10, 2003 by Arnaud Bailly - bailly@lifl.fr
 * Copyright 2003 - Arnaud Bailly 
 */
package fr.lifl.utils;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 *This formatter subclass formats only outputs the message part of a LogRecord object
 *as its string representation
 *
 * @author bailly
 * @version $Id$
 */
public class VerySimpleFormatter extends SimpleFormatter {
	
	private static final String eol = System.getProperty("line.separator");

	/* (non-Javadoc)
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	public synchronized String format(LogRecord record) {
		return "[" + record.getLevel() +"] " + record.getMessage() + eol;
	}

}
