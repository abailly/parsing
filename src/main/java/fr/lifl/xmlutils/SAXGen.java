/*
 * Created on Jun 15, 2004
 * 
 * $Log: SAXGen.java,v $
 * Revision 1.1  2005/04/25 11:17:21  bailly
 * run commit
 *
 * Revision 1.1  2004/06/23 13:44:33  bailly
 * entree projet de constructeur de site
 *
 */
package fr.lifl.xmlutils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException ;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class uses SAX events to write XML files
 * <p>
 * SAXGen can handle fragment files, that is aggregation of several well-formed
 * XML files into one. The first startdocument event only output headers,
 * following startDocument events are ignored and corresponding endDocument
 * events are handled as fragments.
 * 
 * @author nono
 * @version $Id: SAXGen.java,v 1.1 2005/04/25 11:17:21 bailly Exp $
 */
public class SAXGen extends DefaultHandler {

  private static final char[] amp = new char[] { '&', 'a', 'm', 'p', ';' };

  private static final char[] lt = new char[] { '&', 'l', 't', ';' };

  private boolean fragment;

  private boolean started;

  private String encoding;

  private String uri = "";

  private String prefix = "";

  private boolean startns;

  private PrintWriter ixos;

  private OutputStream output;

  private boolean indent;

  private int curIndent;

  private static final String nl = System.getProperty("line.separator"); 

  public SAXGen(File f) throws IOException {
    this(new FileOutputStream(f),"UTF-8");
  }
    
  public SAXGen(OutputStream os) throws IOException {
    this(os, "UTF-8");
  }

  public SAXGen(OutputStream os, String encoding) throws IOException {
    this.output = os;
    this.ixos = new PrintWriter(new OutputStreamWriter(os,
        Charset.forName(encoding)));
    this.encoding = encoding;
  }

  public SAXGen() {
    this.ixos = null;
    this.output = null;
    this.encoding = "UTF-8";
  }

  /**
   * @return Returns the encoding.
   */
  public String getEncoding() {
    return encoding;
  }

  /**
   * @param encoding
   *          The encoding to set.
   */
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#endDocument()
   */
  public void endDocument() throws SAXException {
    if (fragment)
      fragment = false;
    else {
      ixos.flush();
      ixos.close();
      ixos = null;
      started = false;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String uri, String localName, String qName)
      throws SAXException {
    StringBuffer out = new StringBuffer();
    if (indent) {
      curIndent--;
      for (int i = 0; i < curIndent; i++)
        out.append(' ');
    }
    out.append("</").append(qName).append('>');
    this.ixos.print(out.toString());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#startDocument()
   */
  public void startDocument() throws SAXException {
    if (!started) {
      if (ixos == null)
        openStream();
      this.ixos.print("<?xml version=\"1.0\" encoding=\"" + encoding
          + "\" ?>");
      started = true;
      if(indent)
        curIndent = 0;
    } else {
      fragment = true;
    }
  }

  /**
   * @throws SAXException
   *  
   */
  private void openStream() throws SAXException {
    if (output == null || encoding == null)
      throw new SAXException("Not configured");
      /* open streams */
      ixos = new PrintWriter(new OutputStreamWriter(output,
          Charset.forName(encoding)));
    
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String uri, String localName, String qName,
      Attributes attributes) throws SAXException {
    StringBuffer out = new StringBuffer(nl);
    if (indent) {
      for (int i = 0; i < curIndent; i++)
        out.append(' ');
      curIndent++;
    }
    out.append('<');
    if (attributes != null) {
      out.append(qName).append(' ');
        int l = attributes.getLength();
      for (int i = 0; i < l; i++) {
        out.append(attributes.getQName(i)).append("=\"").append(
            attributes.getValue(i)).append("\" ");
      }
    }else 
      out.append(qName);
    /* output prefix declaration as attribute */
    if (startns) {
      out.append(" xmlns:").append(prefix).append("=\"").append(uri).append(
          "\" ");
      startns = false;
    }
    out.append(">");
    this.ixos.print(out.toString());
  }

  /**
   * Same as Start element but immediately closess it
   */
  public void startEndElement(String uri, String localName, String qName,
      Attributes attributes) throws SAXException {
    StringBuffer out = new StringBuffer(nl);
    if (indent) {
      for (int i = 0; i < curIndent; i++)
        out.append(' ');
      curIndent++;
    }
    out.append('<');
    if (attributes != null) {
      out.append(qName).append(' ');
        int l = attributes.getLength();
      for (int i = 0; i < l; i++) {
        out.append(attributes.getQName(i)).append("=\"").append(
            attributes.getValue(i)).append("\" ");
      }
    }else 
      out.append(qName);
    /* output prefix declaration as attribute */
    if (startns) {
      out.append(" xmlns:").append(prefix).append("=\"").append(uri).append(
          "\" ");
      startns = false;
    }
    out.append(" />");
    this.ixos.print(out.toString());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#characters(char[], int, int)
   */
  public void characters(char[] ch, int start, int length) throws SAXException {
    char[] xmlc = xmlize(ch, start, length);
    ixos.write(xmlc);
  }

  /*
   * add namespace declaration to following start elemen
   * 
   * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String,
   *      java.lang.String)
   */
  public void startPrefixMapping(String prefix, String uri) throws SAXException {
    this.prefix = prefix;
    this.uri = uri;
    this.startns = true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
   */
  public void endPrefixMapping(String prefix) throws SAXException {
    this.prefix = "";
    this.uri = "";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
   */
  public void ignorableWhitespace(char[] ch, int start, int length)
      throws SAXException {
    characters(ch, start, length);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String,
   *      java.lang.String)
   */
  public void processingInstruction(String target, String data)
      throws SAXException {
    this.ixos.println("<?" + target + " " + data + " ?>");
  }

  /**
   * Put string after xmlization into output.
   * 
   * @param v
   */
  public void putString(String v) throws SAXException {
    if (v == null)
      return;
    char[] data = v.toCharArray();
    characters(data, 0, data.length);
  }

  /**
   * Converts illegal xml characters into standard xml entities.
   * 
   * @param v
   * @return
   */
  private char[] xmlize(String v) {
    char[] data = v.toCharArray();
    char[] tmp;
    for (int i = 0; i < data.length; i++) {
      switch (data[i]) {
      case '&':
        tmp = new char[data.length + 4];
        System.arraycopy(data, 0, tmp, 0, i);
        if (i + 1 < data.length)
          System.arraycopy(data, i + 1, tmp, i + 5, data.length - i - 1);
        System.arraycopy(amp, 0, tmp, i, 5);
        data = tmp;
        i += 5;
        break;
      case '<':
        tmp = new char[data.length + 3];
        System.arraycopy(data, 0, tmp, 0, i);
        if (i + 1 < data.length)
          System.arraycopy(data, i + 1, tmp, i + 4, data.length - i - 1);
        System.arraycopy(lt, 0, tmp, i, 4);
        data = tmp;
        i += 4;
        break;
      default:
      }
    }
    return data;
  }

  /**
   * Converts illegal xml characters into standard xml entities.
   * 
   * @param v
   * @return
   */
  private char[] xmlize(char[] init, int start, int length) {
    char[] data = new char[length];
    System.arraycopy(init, start, data, 0, length);
    char[] tmp;
    for (int i = 0; i < data.length; i++) {
      switch (data[i]) {
      case '&':
        tmp = new char[data.length + 4];
        System.arraycopy(data, 0, tmp, 0, i);
        if (i + 1 < data.length)
          System.arraycopy(data, i + 1, tmp, i + 5, data.length - i - 1);
        System.arraycopy(amp, 0, tmp, i, 5);
        data = tmp;
        i += 5;
        break;
      case '<':
        tmp = new char[data.length + 3];
        System.arraycopy(data, 0, tmp, 0, i);
        if (i + 1 < data.length)
          System.arraycopy(data, i + 1, tmp, i + 4, data.length - i - 1);
        System.arraycopy(lt, 0, tmp, i, 4);
        data = tmp;
        i += 4;
        break;
      default:
      }
    }
    return data;
  }

  /**
   * output the string enclosed within CDATA tags
   * 
   * @param v
   * @throws SAXException
   */
  public void putStringRaw(String v) throws SAXException {
    if (v == null)
      return;
    ixos.print("<![CDATA[");
    ixos.print(v);
    ixos.print("]]>");
  }

  /**
   * Direct output of string as data to file. CAUTION: this method does no
   * translation of characters.
   * 
   * 
   * @param v
   */
  public void putString0(String v) {
    if (v == null)
      return;
    ixos.print(v);
  }

  /**
   * @param b
   */
  public void setIndent(boolean b) {
    this.indent = b;
  }

  /**
   * 
   */
  public void startComment() {
    ixos.println(nl +"<!--");
  }
  
  public void endComment() {
    ixos.println(nl+"-->");
  }
  
}
