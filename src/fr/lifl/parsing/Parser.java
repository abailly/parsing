/*
 * Created on Jun 29, 2004
 * 
 */
package fr.lifl.parsing;

import java.io.Reader;

/**
 * A generic interface implemented by  parser objects
 * <p>
 * This generic interface allows fragmented parsing of character
 * streams between cooperating parsers
 * 
 * @author nono
 * @version $Id$
 */
public interface Parser {

    /**
     * Sets the configuration object to be used by this parser
     * 
     * @param config
     */
    void setParserConfiguration(ParserConfiguration config);
    
	/**
	 * Sets the reader object providing the stream of characters
	 * to parse. The reader is not closed by this method
	 * 
	 * @param reader a valid Reader object
	 */
	void setReader(Reader reader);
	
	/**
 	* Adds a listener for parse events to this Parser.
	 * Parsing events are generated by the Parser to notify
	 * listeners of warnings and recoverable errors. Unrecoverable
	 * errors are notified through @see{ParserException}. 
	 *  
 	* @param listener the listener to add to this parser
 	*/
	void addParserListener(ParserListener listener);
	
	/**
	 * Gives information to this parser that parsing starts
	 * at given position in the enclosing context. If this method
	 * is not called prior to a call to @see{start()} method, 
	 * start position is assumed to be line 1, column 1.
	 * 
	 * @param pos the start position - may not be null
	 */
	void setStartPosition(ParserPosition pos);
	
	/**
	 * Gives the Parser information of the enclosing Namespace 
	 * this parsing is part of.
	 * 
	 */
	void setStartScope(Namespace scope);
	
	/**
	 * Asks this Parser to start parsing. This method is normally blocking
	 * and Parser should return when finished. This method must be called
	 * after a call to @see{setReader(java.io.Reader)} or else
	 * it will throw immediatly a ParserException.
	 * <p>
	 * Recoverable parse events are notified through registered ParserListener
	 * interface, while non recoverable errors throw a PArserException.
	 * 
	 * @throws ParserException
	 */
	void start() throws ParserException;
	
}


/* 
 * $Log: Parser.java,v $
 * Revision 1.3  2005/04/25 11:17:21  bailly
 * run commit
 *
 * Revision 1.2  2004/07/23 07:10:21  bailly
 * added ParserConfiguration and events for start and end of parsing
 * modified Parser API accordingly
 *
 * Revision 1.1  2004/06/30 14:36:18  bailly
 * ajout structures de Parser g?n?rique : evenements, listeners, namespace,
 *
*/