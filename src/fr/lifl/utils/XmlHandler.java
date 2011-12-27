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
 * Created on 21 déc. 2004
 * 
 */
package fr.lifl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import fr.lifl.parsing.ParserException;

/**
 * A base class for SAX handling of xml files. Provides basic utilities for
 * files and ids manipulation.
 * 
 * @author nono
 * @version $Id: XmlHandler.java,v 1.1 2005/04/25 11:17:21 bailly Exp $
 */
public class XmlHandler extends DefaultHandler {

  private Map variables = new HashMap();

  private Stack obstack = new Stack();

  private Object current = null;

  private StringBuffer buffer;

  private Map idmap = new HashMap();

  private int indent;

  private boolean debug;

  /**
   * @return Returns the debug.
   */
  public boolean isDebug() {
    return debug;
  }

  /**
   * @param debug
   *          The debug to set.
   */
  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  /**
   * @return Returns the buffer.
   */
  public StringBuffer getBuffer() {
    return buffer;
  }

  /**
   * @param buffer
   *          The buffer to set.
   */
  public void setBuffer(StringBuffer buffer) {
    this.buffer = buffer;
  }

  /**
   * @return Returns the variables.
   */
  public Map getVariables() {
    return variables;
  }

  /**
   * @param variables
   *          The variables to set.
   */
  public void setVariables(Map variables) {
    this.variables = variables;
  }

  /**
   * @return Returns the current.
   */
  public Object getCurrent() {
    return current;
  }

  /**
   * @return Returns the indent.
   */
  public int getIndent() {
    return indent;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#characters(char[], int, int)
   */
  public void characters(char[] ch, int start, int length) throws SAXException {
    if (buffer != null)
      buffer.append(ch, start, length);
  }

  /**
   * Start this SAX parser on given file.
   * 
   * @param conf
   *          File to analyze
   * @exception ParserException
   *              general exception launched if something wrong happens.
   *              Contains nested exceptions.
   */
  public void analyze(File conf) throws ParserException {
    try {
      XMLReader reader = SAXParserFactory.newInstance().newSAXParser()
          .getXMLReader();
      reader.setFeature("http://xml.org/sax/features/namespaces", true);
      reader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
      reader.setContentHandler(this);
      reader.parse(new InputSource(new FileInputStream(conf)));
    } catch (Exception e) {
      throw new ParserException("Error in handling XMI file " + conf, e);
    }
  }

  /**
   * Start this SAX parser on given input stream.
   * 
   * @param conf
   *          File to analyze
   * @exception ParserException
   *              general exception launched if something wrong happens.
   *              Contains nested exceptions.
   */
  public void analyze(InputStream is) throws ParserException {
    XMLReader reader = null;
    try {
      reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
      reader.setFeature("http://xml.org/sax/features/namespaces", true);
      reader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
      reader.setContentHandler(this);
      reader.parse(new InputSource(is));
    } catch (Exception e) {
      throw new ParserException("Error in handling XMI stream", e);
    }
  }

  /**
   * Pop an object from the stack.
   * 
   * @return top of the object stack.
   */
  protected Object pop() {
    Object ret = current;
    if (debug) {
      indent--;
      for (int i = 0; i < indent; i++)
        System.err.print(" ");
      System.err.println("Popping " + ret);
    }
    current = obstack.pop();
    return ret;
  }

  /**
   * Returns the current ids map.
   * 
   * @return Returns the idmap.
   */
  public Map getIdmap() {
    return idmap;
  }

  /**
   * @param idmap
   *          The idmap to set.
   */
  public void setIdmap(Map idmap) {
    this.idmap = idmap;
  }

  /**
   * Push an object on the stack and make it current.
   * 
   * @param o
   *          Object to push on stack.
   */
  protected void push(Object o) {
    if (o == null)
      throw new RuntimeException("pushing null object on stack");
    if (debug) {
      for (int i = 0; i < indent; i++)

        System.err.print(" ");
      System.err.println("Pushing " + o);
      indent++;
    }
    obstack.push(current);
    current = o;
  }

  /**
   * Produce a new string by substituting variables names
   * 
   * @param src
   * @return
   */
  protected String getValue(String src) {
    Iterator it = variables.entrySet().iterator();
    StringBuffer sb = new StringBuffer();
    boolean match = false;
    /* a pattern for variable reference matching */
    Pattern pv = Pattern.compile("(\\$\\{([^${}]+)\\})");
    Matcher m = pv.matcher(src);
    while (m.find()) {
      String vn = m.group(2);
      String vv = (String) variables.get(vn);
      if (vv == null)
        vv = "";
      /* replace in string */
      m.appendReplacement(sb, vv);
      match = true;
    }
    m.appendTail(sb);
    return sb.toString();
  }

  /**
   * Set or reset the value of a variable in this configuration setting
   * 
   * @param name
   * @param val
   */
  public void setVariable(String name, String val) {
    variables.put(name, val);
  }

}

/*
 * $Log: XmlHandler.java,v $
 * Revision 1.1  2005/04/25 11:17:21  bailly
 * run commit
 *
 */