/*
 * Created on Jun 30, 2004
 * 
 */
package fr.lifl.parsing;

/**
 * An event  that is sent when a token is successfully parsed 
 * by the Parser object.
 * 
 * @author nono
 * @version $Id$
 */
public class ParserTokenEvent extends ParserEvent {

  	/* the token */
  private String token;
  
  
    /**
     * @return Returns the token.
     */
    public String getToken() {
      return token;
    }
    
    /**
     * @param token The token to set.
     */
    public void setToken(String token) {
      this.token = token;
    }
}

/* 
 * $Log: ParserTokenEvent.java,v $
 * Revision 1.1  2004/06/30 14:36:18  bailly
 * ajout structures de Parser g?n?rique : evenements, listeners, namespace,
 *
*/