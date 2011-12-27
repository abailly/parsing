/*
 * Created on Jun 29, 2004
 * 
 */
package fr.lifl.parsing;

/**
 * A simple class representing recoverable errors notified to
 * ParserListeners
 * <p>
 * A recoverable error does not forbid parsing of some later part
 * of a stream.
 * 
 * @author nono
 * @version $Id$
 */
public class ParserError extends ParserEvent {

	/*
	 * the message of the error
	 */
	private String message;

	/* the position at which parsing is resumed - anything
	 * between position and end can be assumed being left away
	 */
	private ParserPosition recoveryPosition;

	/**
	 * @return
	 */
	public ParserPosition getRecoveryPosition() {
		return recoveryPosition;
	}

	/**
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param position
	 */
	public void setRecoveryPosition(ParserPosition position) {
		recoveryPosition = position;
	}

	/**
	 * @param string
	 */
	public void setMessage(String string) {
		message = string;
	}

	/* (non-Javadoc)
	 * @see fr.lifl.parsing.ParserEvent#toString()
	 */
	public String toString() {
		return super.toString() + "error :" +message+", skip to "+recoveryPosition;
	}

}

/* 
 * $Log: ParserError.java,v $
 * Revision 1.1  2004/06/30 14:36:18  bailly
 * ajout structures de Parser g?n?rique : evenements, listeners, namespace,
 *
*/