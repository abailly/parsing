/*
 * Created on Jun 29, 2004
 * 
 */
package fr.lifl.parsing;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A utility class that handles ParserListener objects attached
 * to a Parser
 * <p>
 * This class may be used by Parser instances to handle the details 
 * of handling registration/deregistration of listeners objects
 * and notification of events.
 * 
 * @author nono
 * @version $Id$
 */
public class ParserListenerDelegate {

	/* the set  of ParserListener objects */
	private Set /* < ParserListener > */ listeners =  new HashSet();
	
	/**
	 * Adds a new ParserListener to the listeners Set. If this parser
	 * is registered already, this is a no-op (provided equals and hashcode
	 * are properly handler by ParserListener instance)
	 * 
	 * @param listener
	 */
	public void addParserListener(ParserListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove a registered PArserListener. Does nothing if listener
	 * was not registered
	 * 
	 * @param listener
	 */
	public void removeParserListener(ParserListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notfies listeners of  ParserEvents
	 * <p>
	 * This method pushes the event to each registerd listener. The order
	 * of notification is the order of registration of listeners.
	 * 
	 * @param event the event to notify 
	 */
	public void notify(ParserEvent event) {
		Iterator it =  listeners.iterator();
		while(it.hasNext())	
			((ParserListener)it.next()).event(event);
	}
	
	/**
	 * Returns the set  of listeners this delegate handles
	 * 
	 * @return a Set object 
	 */
	public Set getAllListeners()  {
		return new HashSet(listeners);
	}
	
	/**
	 * Copy this delegate set of listeners to the given parser
	 * 
	 * @param parser
	 */
	public void  listenTo(Parser parser) {
	     Iterator it = listeners.iterator();
	     while(it.hasNext()) {
	       parser.addParserListener((ParserListener)it.next());
	     }

	}
}

/* 
 * $Log: ParserListenerDelegate.java,v $
 * Revision 1.1  2004/06/30 14:36:18  bailly
 * ajout structures de Parser g?n?rique : evenements, listeners, namespace,
 *
*/