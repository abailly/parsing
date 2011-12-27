/*
 * Created on 20 f?vr. 2004
 * 
 * $Log: SwingLogHandler.java,v $
 * Revision 1.2  2004/09/06 12:09:23  bailly
 * added class loading management
 *
 * Revision 1.1  2004/06/23 13:42:40  bailly
 * transfert depuis FIDL des utilitaires
 *
 * Revision 1.1  2004/02/20 16:18:22  nono
 * Added log handler for displaying log messages in JTextPane
 * Tweaked grammars to handle Jaskell
 * TODO: semantic for messages
 *
 */
package fr.lifl.utils;


import java.awt.Color;
import java.util.HashMap;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;


/**
 * A sipmple log handler to display messages in a window
 * 
 * @author bailly
 * @version $Id$
 */
public class SwingLogHandler extends Handler {

	/* map from log levels to styles */
	private HashMap styles;

	/* panel where data is displayed in */
	private JTextPane text;

	private static final String eol = System.getProperty("line.separator");


	/**
	 * Constructs a log handler with given text panel as sink
	 * for log messages
	 * 
	 * @param pane a JTextPane 
	 */
	public SwingLogHandler(JTextPane pane) {
		this.text = pane;		//Initialize some styles.
		this.styles = new HashMap();
		Style def =
			StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);
		/* add styles for different log levels */
		Style regular = text.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "SansSerif");
		StyleConstants.setFontSize(def, 10);

		Style ital = text.addStyle("italic", regular);
		StyleConstants.setItalic(ital, true);

		Style bold = text.addStyle("bold", regular);
		StyleConstants.setBold(bold, true);
	
		Style ul = 	text.addStyle("underline",regular);
		StyleConstants.setUnderline(ul,true);
		
		/* style for severe messages  - bold ,red*/
		Style sty = text.addStyle("severe", bold);
		StyleConstants.setForeground(sty, Color.red);
		styles.put(Level.SEVERE,sty);
		
		/* style for warning messages  - bold, orange */
		sty = text.addStyle("warning", bold);
		StyleConstants.setForeground(sty, Color.orange);
		styles.put(Level.WARNING,sty);
		
		/* style for info messages  - regular, black */
		sty = text.addStyle("info", regular);
		styles.put(Level.INFO,sty);
		
		/* style for config messages  - italic, black */
		sty = text.addStyle("config", ital);
		styles.put(Level.CONFIG,sty);

		/* style for fine messages - green */
		sty = text.addStyle("fine", regular);
		StyleConstants.setForeground(sty, Color.green);
		styles.put(Level.FINE,sty);

		/* style for finer messages - blue */
		sty = text.addStyle("finer", regular);
		StyleConstants.setForeground(sty, Color.blue);
		styles.put(Level.FINER,sty);

		/* style for finest messages - lightgray, italic */
		sty = text.addStyle("finest", ital);
		StyleConstants.setForeground(sty, Color.gray);
		styles.put(Level.FINEST,sty);

	}
				
	
	/* (non-Javadoc)
	 * @see java.util.logging.Handler#close()
	 */
	public void close() throws SecurityException {
		this.text = null;
	}

	/* (non-Javadoc)
	 * @see java.util.logging.Handler#flush()
	 */
	public void flush() {
	}

	/* (non-Javadoc)
	 * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
	 */
	public void publish(LogRecord record) {
		if(text == null)
			return;
		if(!isLoggable(record))
			return;
		Formatter frm  = getFormatter();
		if(frm == null)
			setFormatter(new VerySimpleFormatter());
		/* format message */
		String msg = getFormatter().format(record);		
		Level lvl = record.getLevel();
		/* display message with style appropriate for record */
		Document doc =text.getDocument();
		try {
			doc.insertString(doc.getLength(), // at end
					 msg, // message 
					 (Style)styles.get(lvl));
			text.setCaretPosition(doc.getLength());
		} catch (BadLocationException e) {
			getErrorManager().error("Error in displaying message "+msg,e,0);
		} 
	}

}
