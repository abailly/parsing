/*
 * Created on Jun 29, 2004
 * 
 */
package fr.lifl.parsing;

/**
 * A simple class for issueing warnings during parsing. 
 * <p>
 * Warnings are distinguishable form errors in that
 * they do not perturb parsing but they may produce side
 * effects at further stage in a compilation process.
 * 
 * @author nono
 * @version $Id$
 */
public class ParserWarning extends ParserEvent {

	/* the message of the warning */
	private String message;
	

	/**
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param string
	 */
	public void setMessage(String string) {
		message = string;
	}

}

/* 
 * $Log: ParserWarning.java,v $
 * Revision 1.1  2004/06/30 14:36:18  bailly
 * ajout structures de Parser g?n?rique : evenements, listeners, namespace,
 *
*/