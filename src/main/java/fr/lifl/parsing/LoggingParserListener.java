/**
 * 
 */
package fr.lifl.parsing;

import org.apache.commons.logging.Log;


/**
 * An implementation of {@link fr.lifl.parsing.ParserListener} that
 * logs events to a commons logging {@link org.apache.commons.logging.Log} 
 * instance.
 * 
 * @author nono
 */
public class LoggingParserListener implements ParserListener {

	private Log log;
	private int nbWarnings_;
	private int nbErrors_;
	private String fileName_;

	/**
	 * Create an instance of listener from given log 
	 * instance.
	 * 
	 * @param log the Log instance.
	 */
	public LoggingParserListener(Log log) {
		this.log = log;
	}

	  /**
	   * * Obtain the number of warnings. * *
	   * 
	   * @return The number of warnings.
	   */
	  public int getNbWarnings() {
	    return nbWarnings_;
	  }

	  /**
	   * * Obtain the number of errors. * *
	   * 
	   * @return The number of errors.
	   */
	  public int getNbErrors() {
	    return nbErrors_;
	  }

	  /**
	   * * Set the name of the file that is read. * *
	   * 
	   * @param The
	   *          name of the file.
	   */
	  public void setFileName(String fileName) {
	    fileName_ = fileName;
	  }

	  public String getFileName() {
	    return fileName_;
	  }

	/* (non-Javadoc)
	 * @see fr.lifl.parsing.ParserListener#event(fr.lifl.parsing.ParserEvent)
	 */
	public void event(ParserEvent event) {
	    StringBuffer msg = new StringBuffer();
	    msg.append(fileName_).append(' ').append(event.getPosition())
	        .append(" : ");
	    if (event instanceof ParserWarning) {
	      msg.append(((ParserWarning) event).getMessage());
	      log.warn(msg.toString());
	    } else if (event instanceof ParserError) {
	      msg.append(((ParserError) event).getMessage()).append(", skip to ")
	          .append(((ParserError) event).getRecoveryPosition());
	      log.error(msg.toString());
	    } else if (event instanceof ParserFileEvent) {
	      ParserFileEvent pfe = (ParserFileEvent) event;
	      if (pfe.isStarting()) {
	        this.fileName_ = ((ParserFileEvent) event).getFile().getPath();
	        msg.append("start parsing file ").append(fileName_);
	      } else
	        msg.append("end parsing file ").append(fileName_);
	      log.info(msg.toString());
	    }

	}

}
