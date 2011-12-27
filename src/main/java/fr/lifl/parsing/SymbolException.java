/*
 * Created on 25 f?vr. 2004
 * 
 * $Log: SymbolException.java,v $
 * Revision 1.2  2004/06/30 14:36:18  bailly
 * ajout structures de Parser g?n?rique : evenements, listeners, namespace,
 *
 * Revision 1.1  2004/02/27 16:40:27  bailly
 * initial check-in
 *
 */
package fr.lifl.parsing;

/**
 * An exception thrown by Namespaces when something goes
 * wrong during binding of symbols.
 *  
 * @author bailly
 * @version $Id$
 */
public class SymbolException extends Exception {

	/**
	 * 
	 */
	public SymbolException() {
		super();
	}

	/**
	 * @param message
	 */
	public SymbolException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public SymbolException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SymbolException(String message, Throwable cause) {
		super(message, cause);
	}

}
