/*
 * Created on Jun 29, 2004
 * 
 */
package fr.lifl.parsing;

/**
 * An exception for unrecoverable parse errors
 * <p>
 * This exception extends RuntimeException so as not to break 
 * existing parser generator : semantic code can safely throw a
 * ParserException without breaking parser generation routines.
 * 
 * @author nono
 * @version $Id$
 */
public class ParserException extends RuntimeException {

	/**
	 * 
	 */
	public ParserException() {
		super();
	}

	/**
	 * @param message
	 */
	public ParserException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ParserException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}

}

/* 
 * $Log: ParserException.java,v $
 * Revision 1.1  2004/06/30 14:36:18  bailly
 * ajout structures de Parser g?n?rique : evenements, listeners, namespace,
 *
*/