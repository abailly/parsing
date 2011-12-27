/*
 * Created on 25 févr. 2004
 * 
 * $Log: UnresolvedSymbolException.java,v $
 * Revision 1.1  2004/02/27 16:40:27  bailly
 * initial check-in
 *
 */
package fr.lifl.parsing;

/**
 * This exception is thrown when a symbol cannot be resovled
 * in a Namespace
 * 
 * @author bailly
 * @version $Id$
 */
public class UnresolvedSymbolException extends SymbolException {

	/**
	 * 
	 */
	public UnresolvedSymbolException() {
		super();
	}

	/**
	 * @param message
	 */
	public UnresolvedSymbolException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UnresolvedSymbolException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnresolvedSymbolException(String message, Throwable cause) {
		super(message, cause);
	}

}
