/*
 * Created on 25 févr. 2004
 * 
 * $Log: InvalidSymbolException.java,v $
 * Revision 1.1  2004/02/27 16:40:27  bailly
 * initial check-in
 *
 */
package fr.lifl.parsing;

/**
 * Exception thrown by BasicNamespace when symbol fails to
 * validate against SymbolValidator.
 * 
 * @author bailly
 * @version $Id$
 */
public class InvalidSymbolException extends SymbolException {

	/**
	 * 
	 */
	public InvalidSymbolException() {
		super();
	}

	/**
	 * @param message
	 */
	public InvalidSymbolException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InvalidSymbolException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidSymbolException(String message, Throwable cause) {
		super(message, cause);
	}

}
