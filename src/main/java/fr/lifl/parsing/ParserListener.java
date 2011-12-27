/*
 * Created on Jun 29, 2004
 * 
 */
package fr.lifl.parsing;


/**
 * A callback interface to receive notification of events 
 * occuring during parsing.
 * 
 * @author nono
 * @version $Id$
 */
public interface ParserListener {

	/**
	 * Main notification method for listeners
	 * 
	 * @param event  gives information on the event (basically position
	 * and emitter)
	 */
	void event(ParserEvent event);

}

/* 
 * $Log: ParserListener.java,v $
 * Revision 1.1  2004/06/30 14:36:18  bailly
 * ajout structures de Parser g?n?rique : evenements, listeners, namespace,
 *
*/