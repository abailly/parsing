/*
 * Created on Jun 29, 2004
 * 
 */
package fr.lifl.parsing;

/**
 * A simple class to handle (line,column) position informations within a Parser.
 * <p>
 * 
 * @author nono
 * @version $Id$
 */
public class ParserPosition {

  private int line = 1;

  private int column = 1;

  /**
   * Creates a ParserPosition object with given position
   * 
   * @param line
   *          a position - must be superior to 0
   * @param column
   *          must be superior to 0
   */
  public ParserPosition(int line, int column) {
    this.line = line;
    this.column = column;
  }

  /**
   * @return
   */
  public int getColumn() {
    return column;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return "(" + line + ',' + column + ')';
  }

  /**
   * @return
   */
  public int getLine() {
    return line;
  }

  /**
   * @param i
   */
  public void setColumn(int i) {
    column = i;
  }

  /**
   * @param i
   */
  public void setLine(int i) {
    line = i;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    ParserPosition other = (ParserPosition) obj;
    if (other == null)
      return false;
    return other.line == line && other.column == column;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return (line << 7) ^ column;
  }

}

/*
 * $Log: ParserPosition.java,v $
 * Revision 1.1  2004/06/30 14:36:18  bailly
 * ajout structures de Parser g?n?rique : evenements, listeners, namespace,
 *
 */