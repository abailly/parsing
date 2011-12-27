/*
 * Created on 25 févr. 2004
 * 
 * $Log: DuplicateSymbolException.java,v $
 * Revision 1.1  2004/02/27 16:40:27  bailly
 * initial check-in
 *
 */
package fr.lifl.parsing;

/**
 * This exception is thrown when trying to redefine 
 * an already bound symbol in a Namespace
 * 
 * @author bailly
 * @version $Id$
 */
public class DuplicateSymbolException extends SymbolException {
	
	
	/**
	 * 
	 */
	public DuplicateSymbolException() {
		super();
	}

	/**
	 * @param message
	 */
	public DuplicateSymbolException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DuplicateSymbolException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DuplicateSymbolException(String message, Throwable cause) {
		super(message, cause);
	}

}
