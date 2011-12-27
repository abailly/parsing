/*
 * Created on Jun 29, 2004
 * 
 */
package fr.lifl.parsing;

/**
 * A class representing recoverable errors or other events 
 * occuring during parsing that does not impede it.
 * <p>
 * This class is mainly used to notify listeners of warnings 
 * and recoverable errors. 
 * 
 * @author nono
 * @version $Id$
 */
public class ParserEvent {

	/* sender of event */
	private Parser source;
	
	/* position of event */
	private ParserPosition position;
	
	
	/**
	 * @return
	 */
	public ParserPosition getPosition() {
		return position;
	}

	/**
	 * @return
	 */
	public Parser getSource() {
		return source;
	}

	/**
	 * @param position
	 */
	public void setPosition(ParserPosition position) {
		this.position = position;
	}

	/**
	 * @param parser
	 */
	public void setSource(Parser parser) {
		source = parser;
	}

}

/* 
 * $Log: ParserEvent.java,v $
 * Revision 1.1  2004/06/30 14:36:18  bailly
 * ajout structures de Parser g?n?rique : evenements, listeners, namespace,
 *
*/