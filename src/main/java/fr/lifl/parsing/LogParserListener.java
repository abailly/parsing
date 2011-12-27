
package fr.lifl.parsing;

import java.util.logging.Logger;

/**
 * A ParserListener that logs message to a specific logger (uses jdk1.4 API).
 * 
 * This implementation of {@link ParserListener} logs errors, warnings and source change from
 *  parsing to a logger instance.
 * 
 * @see ParserListener
 * @see Logger
 * @see ParserEvent
 * @author Arnaud Bailly
 * @version $Id$
 */
public class LogParserListener implements ParserListener {

  ///////////////////////////////////////////////////////////////
  // MEMBER FIELDS
  ///////////////////////////////////////////////////////////////

  private Logger logger;

  private int nbWarnings_;

  private int nbErrors_;

  private String fileName_;

  ///////////////////////////////////////////////////////////////
  // CONSTRUCTOR
  ///////////////////////////////////////////////////////////////

  /**
   * * The constructor. * *
   * 
   * @param fileName
   *          The name of the file that is being parsed.
   */
  public LogParserListener(Logger logger) {
    this.logger = logger;
  }

  ///////////////////////////////////////////////////////////////
  // PRIVATE METHODS
  ///////////////////////////////////////////////////////////////

  // ==================================================================
  //
  // Public methods.
  //
  // ==================================================================

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

  /*
   * (non-Javadoc)
   * 
   * @see fr.lifl.parsing.ParserListener#event(fr.lifl.parsing.ParserEvent)
   */
  public void event(ParserEvent event) {
    StringBuffer msg = new StringBuffer();
    msg.append(fileName_).append(' ').append(event.getPosition())
        .append(" : ");
    if (event instanceof ParserWarning) {
      msg.append(((ParserWarning) event).getMessage());
      logger.warning(msg.toString());
    } else if (event instanceof ParserError) {
      msg.append(((ParserError) event).getMessage()).append(", skip to ")
          .append(((ParserError) event).getRecoveryPosition());
      logger.severe(msg.toString());
    } else if (event instanceof ParserFileEvent) {
      ParserFileEvent pfe = (ParserFileEvent) event;
      if (pfe.isStarting()) {
        this.fileName_ = ((ParserFileEvent) event).getFile().getPath();
        msg.append("start parsing file ").append(fileName_);
      } else
        msg.append("end parsing file ").append(fileName_);
      logger.info(msg.toString());
    }
  }
}