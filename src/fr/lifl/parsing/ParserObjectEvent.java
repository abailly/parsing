/*
 * Created on Jun 30, 2004
 * 
 */
package fr.lifl.parsing;

/**
 * An event denoting that an object has been succesfully parsed and
 * bound to namespace
 * <p>
 * This event may be used by parsers to notify generation of an AST. 
 * The event  contains the namespace where the object is bound, the name
 * it is bound to and the object itself.
 * 
 * @author nono
 * @version $Id$
 */
public class ParserObjectEvent extends ParserEvent {

  	/* name of the bound object */
  private String name;
  
  /* the object bound */
  private ParsedObject bound;
  
  /* the namespace the object is bound to */
  private Namespace namespace;
  
  
  /**
   * @return Returns the bound.
   */
  public ParsedObject getBound() {
    return bound;
  }
  /**
   * @param bound The bound to set.
   */
  public void setBound(ParsedObject bound) {
    this.bound = bound;
  }
    /**
     * @return Returns the name.
     */
    public String getName() {
      return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
      this.name = name;
    }
  /**
   * @return Returns the namespace.
   */
  public Namespace getNamespace() {
    return namespace;
  }
  /**
   * @param namespace The namespace to set.
   */
  public void setNamespace(Namespace namespace) {
    this.namespace = namespace;
  }
}

/* 
 * $Log: ParserObjectEvent.java,v $
 * Revision 1.2  2004/07/23 14:37:24  bailly
 * Modified to handle ParsedObjects
 *
 * Revision 1.1  2004/06/30 14:36:18  bailly
 * ajout structures de Parser g?n?rique : evenements, listeners, namespace,
 *
*/