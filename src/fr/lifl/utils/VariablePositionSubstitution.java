/*______________________________________________________________________________
 *
 * Copyright 2004 Arnaud Bailly - NORSYS/LIFL
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * (1) Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 * (2) Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 * (3) The name of the author may not be used to endorse or promote
 *     products derived from this software without specific prior
 *     written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *______________________________________________________________________________
 *
 * Created on Sep 24, 2004
 *
 */
package fr.lifl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This utility class is used to replace variables patterns of the form ${xxx}
 * ou $1, $2 ... occuring in strings.
 * 
 * @author nono
 * @version $Id: VariablePositionSubstitution.java,v 1.1 2005/04/25 11:17:21 bailly Exp $
 */
public class VariablePositionSubstitution implements VariableSubstitution {

  private static final Logger log = Logger.getLogger("fr.lifl.sitegen");

  /* a pattern for variable reference matching */
  static final private Pattern pv = Pattern.compile("(\\$([0-9][0-9]?))");

  /* list of variables */
  private List variablesList;
  

/**
   * @param l
   */
  public VariablePositionSubstitution(List l) {
    this.variablesList = l;
  }

  public VariablePositionSubstitution() {
  }
  
  /*
 *  (non-Javadoc)
 * @see fr.lifl.utils.VariableSubstitution#substitute(java.lang.String)
 */
  public String substitute(String src) {
    if(variablesList == null || variablesList.isEmpty())
      return new String(src);
    StringBuffer sb = new StringBuffer();
    Matcher m = pv.matcher(src);
    while (m.find()) {
      try {
        int vnum = Integer.parseInt(m.group(2));
        String vv = (String) variablesList.get(vnum);
        if (vv == null)
          vv = "";
        /* replace in string */
        m.appendReplacement(sb, vv);
      } catch (NumberFormatException nex) {
      } catch (NoSuchElementException e) {
      }
    }
    m.appendTail(sb);
    return sb.toString();
  }

  /**
   * Construct a string by trying each patterns in map and replacing variable
   * positions. This method expects a map from Pattern to strings. It tries to
   * match each Pattern against the given <code>name</code>. If a match is
   * found, the string associated with this Pattern is returned after having its
   * positional parameters replaced by matching parts in <code>name</code>
   * 
   * @param fmap
   *          a Map from Pattern to String
   * @param name
   *          a String to match
   * @return a String resulting from a successful match or name if no matching
   *         is found
   */
  public static String mapFile(Map fmap, String name) {
    /* get patterns */
    Iterator it = fmap.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry e = (Map.Entry) it.next();
      Pattern from = (Pattern) e.getKey();
      String to = (String) e.getValue();
      Matcher m = from.matcher(name);
      if (m.find()) {
        List l = new ArrayList();
        for (int i = 0; i < m.groupCount() + 1; i++)
          l.add(m.group(i));
        /* substitute in name */
        return new VariablePositionSubstitution(l).substitute(to);
      }
    }
    return name;
  }


  /**
   * Return multiple names from matching path agains map. This method works the
   * same way as {@link mapFile(Map,String)}but may return multiple matches.
   * 
   * @param pmap
   *          a Map from PAttern to String
   * @param path
   *          a String to match agains map of patterns
   * @return a List of matched files. This list contains at least the string
   *         <code>path</code> as its last element.
   */
  public static List mapMultiple(Map pmap, String path) {
    VariablePositionSubstitution vp = new VariablePositionSubstitution();
    List ret = new ArrayList();
    /* get patterns */
    Iterator it = pmap.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry e = (Map.Entry) it.next();
      Pattern from = (Pattern) e.getKey();
      String to = (String) e.getValue();
      Matcher m = from.matcher(path);
      if (m.find()) {
        List l = new ArrayList();
        for (int i = 0; i < m.groupCount() + 1; i++)
          l.add(m.group(i));
        /* substitute in name */
        vp.setVariablesList(l);
        ret.add(vp.substitute(to));
      }
    }
    ret.add(path);
    return ret;
  }

  /**
   * @return Returns the variablesList.
   */
  public List getVariablesList() {
    return variablesList;
  }
  /**
   * @param variablesList The variablesList to set.
   */
  public void setVariablesList(List variablesList) {
    this.variablesList = variablesList;
  }
}

/*
 * $Log: VariablePositionSubstitution.java,v $
 * Revision 1.1  2005/04/25 11:17:21  bailly
 * run commit
 *
 */