/*
 * Created on Jun 30, 2004
 * 
 */
package fr.lifl.parsing;

import java.io.File;

/**
 * An event notifying that parsing of a new file has  begun
 * 
 * @author nono
 * @version $Id$
 */
public class ParserFileEvent extends ParserEvent {

  	/* the File object being parsed */
  private File file;
  
  /* flag to denote if we are starting or ending processing of a file */
  private boolean starting;
  
    /**
     * @return Returns the file.
     */
    public File getFile() {
      return file;
    }
    /**
     * @param file The file to set.
     */
    public void setFile(File file) {
      this.file = file;
    }
  /**
   * @return Returns the starting.
   */
  public boolean isStarting() {
    return starting;
  }
  /**
   * @param starting The starting to set.
   */
  public void setStarting(boolean starting) {
    this.starting = starting;
  }
}

/* 
 * $Log: ParserFileEvent.java,v $
 * Revision 1.1  2004/06/30 14:36:18  bailly
 * ajout structures de Parser g?n?rique : evenements, listeners, namespace,
 *
*/