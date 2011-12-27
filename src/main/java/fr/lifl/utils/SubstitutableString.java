/*---------------------------------------------------------------------------
 * �2005 NORSYS
 * main author : nono
 *
 * This software is a computer program whose purpose is to provide abstraction
 * for accessing directory data sources within java applications.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 * Created on Oct 19, 2005
 * --------------------------------------------------------------------------*/
package fr.lifl.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class for replacing variables in a string.
 * <p>
 * This class represents a pattern of string with holes for variables. When
 * created, it is given a source string containing occurences of a pattern
 * representing variables.
 * 
 * @author nono
 * @version $Id: SubstitutableString.java 231 2006-02-07 09:10:03Z
 *          /C=FR/ST=Nord/L=Lille/O=Norsys SA/OU=UE/CN=Arnaud
 *          Bailly/emailAddress=abailly@norsys.fr $
 */
public class SubstitutableString {

  public static final String DEFAULT_PATTERN = "(\\$\\{([^${}]+)\\})";

  /* pattern for simple variables matching */
  private Pattern vars = Pattern.compile(DEFAULT_PATTERN);

  private List string;

  private boolean ignoreMissing;

  private boolean recurse;

  private class Fragment {
    Fragment(String string) {
      frag = string;
    }

    String instance(Map loc) {
      return frag;
    }

    String frag;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
      return frag;
    }
  }

  private class Variable extends Fragment {
    Variable(String vn) {
      super(vn);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.norsys.mapper.core.utils.VariableString.Fragment#toString()
     */
    public String toString() {
      return "${" + frag + "}";
    }

    String instance(Map loc) {
      Object o = loc.get(frag);
      if (o != null && o instanceof SubstitutableString && recurse)
        return ((SubstitutableString) o).instance(loc);
      return (String) o;
    }

  }

  /**
   * Constructs a variable string from given string and variable pattern.
   * 
   * The given <code>pattern</code> is used to extract variables names and
   * positions from <code>variableString</code> thus yielding a string with
   * holes for variables.
   * 
   * @param variableString
   *          the matrix string containing variables occurences
   * @param loc
   *          a pattern used for extracting local variables occurences
   */
  public SubstitutableString(String variableString, String loc) {
    vars = Pattern.compile(loc);
    /* first match local vars */
    List l = makeList(vars, variableString);
    /* done */
    this.string = l;
  }

  /**
   * Construct a SubstitutableString with default variable pattern.
   * 
   * @param vars
   *          the source string.
   */
  public SubstitutableString(String vars) {
    this(vars, DEFAULT_PATTERN);
  }

  /**
   * Returns a list of Fragment/Variable objects according to pattern
   * substitution.
   * 
   * @param pat
   * @param string
   * @return
   */
  private List makeList(Pattern pat, String string) {
    /* list of fragments/variable names */
    List l = new ArrayList();
    /* first match local vars */
    Matcher m = pat.matcher(string);
    int i = 0;
    while (m.find()) {
      String vn = m.group(2);
      int s = m.start(1);
      int t = m.end(1);
      l.add(new Fragment(string.substring(i, s)));
      l.add(new Variable(vn));
      i = t;
    }
    if (i < string.length())
      l.add(new Fragment(string.substring(i)));
    return l;
  }

  /**
   * Instanciate this given variable string with an environment. This method
   * constructs a new string by replacing occurences of variable names with
   * their value in the map <code>env</code>. If the value is null or a
   * variable name is missing, an exception is thrown according to the setting
   * of <code>ignoreMissing</code> property;
   * 
   * @param env
   *          a Map<String,String> object. May not be null.
   * @return a String with all occurences of variables replaced by their values
   *         from env.
   * @throws IllegalArgumentException
   *           if <code>ignoreMissing</code> is set to false (the default) and
   *           <code>env</code> contains a null for a variable name or no
   *           value at all.
   */
  public String instance(Map env) {
    StringBuffer sb = new StringBuffer();
    for (Iterator i = string.iterator(); i.hasNext();) {
      Fragment frag = (Fragment) i.next();
      String rep = frag.instance(env);
      if (rep == null) {
        if (!isIgnoreMissing())
          throw new IllegalArgumentException("Variable " + frag.frag
              + " has no value in environment");
      } else
        sb.append(rep);

    }
    return sb.toString();
  }

  /**
   * @return Returns the ignoreMissing.
   */
  public boolean isIgnoreMissing() {
    return ignoreMissing;
  }

  /**
   * @param ignoreMissing
   *          The ignoreMissing to set.
   */
  public void setIgnoreMissing(boolean ignoreMissing) {
    this.ignoreMissing = ignoreMissing;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString() {
    /* save missing */
    boolean ig = this.ignoreMissing;
    this.ignoreMissing = true;
    StringBuffer sb = new StringBuffer();
    for (Iterator i = string.iterator(); i.hasNext();) {
      sb.append(i.next());
    }
    this.ignoreMissing = ig;
    return sb.toString();
  }

  /**
   * Appends another substitutable string to this string.
   * 
   * @param string
   */
  public SubstitutableString append(SubstitutableString string) {
    this.string.addAll(string.string);
    return this;
  }

  /**
   * Return the set of variables referenced in this string.
   * 
   * @return a Set<String> instance containing all variable names referenced in
   *         this string.
   */
  public Set variables() {
    Set s = new HashSet();
    for (Iterator i = string.iterator(); i.hasNext();) {
      Object o = i.next();
      if (o instanceof Variable)
        s.add(((Variable) o).frag);
    }
    return s;
  }

  public boolean isRecurse() {
    return recurse;
  }

  public void setRecurse(boolean recurse) {
    this.recurse = recurse;
  }
}
