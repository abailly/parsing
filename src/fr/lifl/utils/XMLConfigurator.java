/*
 * Created on Jun 16, 2004
 * 
 * $Log: XMLConfigurator.java,v $
 * Revision 1.1  2005/04/25 11:17:21  bailly
 * run commit
 *
 * Revision 1.1  2004/06/23 13:44:33  bailly
 * entree projet de constructeur de site
 *
 */
package fr.lifl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import fr.lifl.utils.TypeHelper;
import fr.lifl.utils.VariableString;

/**
 * A base class for configuring objects trees based on XML. By default, this
 * class reads an XML file and uses the following rules to create a tree of
 * objects :
 * <ul>
 * <li>each node name represents a property of the enclosing node.</li>
 * <li>each node can have attributes type and optionally value. Attribute type
 * is used to create an instance of an object on the current stack of objects
 * with given type. If attribute value is present, the object is constructed
 * with given value as a String to a constructor or static factory methode
 * {@see TypeHelper}.</li>
 * Character nodes are ignored</li>
 * </ul>
 * Subclasses should override the methods startTag and endTag and return true to
 * handle custom tags. <br />
 * This class handles automatically referencing of objects and variables
 * substitution. It uses the namespace <code>http://www.lifl.fr/fidl"</code>
 * for tag <code>var</code>.
 * 
 * @author nono
 * @version $Id: XMLConfigurator.java,v 1.1 2005/04/25 11:17:21 bailly Exp $
 */
public class XMLConfigurator extends DefaultHandler {

    private static Logger log = Logger.getLogger(XMLConfigurator.class
            .getName());

    /* map from ids to parsed nodes */
    private Map idmap = new HashMap();

    private StringBuffer buffer;

    private Stack obstack = new Stack();

    private Object current = null;

    /* map from variables names to their substitions */
    private Map /* <string, String > */
    variables = new HashMap();

    private VariableSubstitution substitution;

    private XMLConfigurator rootConfig;

    /**
     * Configure this objects using given file.
     * 
     * @param conf
     *            a File object from which data is read.
     */
    public final void configure(File conf) throws XMLConfiguratorException {
        try {
            XMLReader reader = SAXParserFactory.newInstance().newSAXParser()
                    .getXMLReader();
            reader.setFeature("http://xml.org/sax/features/namespaces", true);
            reader.setFeature("http://xml.org/sax/features/namespace-prefixes",
                    true);
            reader.setContentHandler(this);
            reader.parse(new InputSource(new FileInputStream(conf)));
        } catch (Exception e) {
            throw new XMLConfiguratorException(
                    "Error in handling configuration file " + conf + " : "
                            + e.getMessage(), e);
        }
    }

    /**
     * Configure this object using given stream.
     * 
     * @param conf
     *            an InputStream used as source
     */
    public final void configure(InputStream conf)
            throws XMLConfiguratorException {
        try {
            XMLReader reader = SAXParserFactory.newInstance().newSAXParser()
                    .getXMLReader();
            reader.setFeature("http://xml.org/sax/features/namespaces", true);
            reader.setFeature("http://xml.org/sax/features/namespace-prefixes",
                    true);
            reader.setContentHandler(this);
            reader.parse(new InputSource(conf));
        } catch (Exception e) {
            throw new XMLConfiguratorException(
                    "Error in handling configuration strea ", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public final void endElement(String uri, String localName, String qName)
            throws SAXException {
        try {
            log.fine("end "+localName);
            if (endTag(uri, localName, qName))
                return;
            else { /* assume setter name */
                Object o = pop();
                TypeHelper.set(current, localName, o);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SAXException(e);
        }

    }

    /**
     * Called by the configurator when a tag ends. Default implementation
     * returns false.
     * 
     * @param uri
     * @param localName
     * @param name
     * @return true if subclass handles the tag
     */
    public boolean endTag(String uri, String localName, String name)
            throws XMLConfiguratorException {
        return false;
    }

    /**
     * @return
     */
    protected final Object pop() {
        Object ret = current;
        current = obstack.pop();
        return ret;
    }

    /**
     * @param o
     */
    protected final void push(Object o) {
        obstack.push(current);
        current = o;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public final void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        try {
            log.fine("start "+localName);
            if (uri.equals("http://lifl.fr/fidl") && localName.equals("var")) {
                String name = attributes.getValue("name");
                String val = VariableString.replaceVars(variables, attributes
                        .getValue("value"));
                setVariable(name, val);
            } else if (!startTag(uri, localName, qName, attributes)) {
                String type = attributes.getValue("type");
                if (type == null)
                    type = "java.lang.String";
                Class cls = TypeHelper.getClass(type);
                String value = attributes.getValue("value");
                Object o;
                /* give immediate value by converting from String or push object */
                if (value != null) {
                    value = VariableString.replaceVars(variables, value);
                    o = TypeHelper.convert(cls, value);
                    log.fine("creating object " + o);
                } else {
                    o = cls.newInstance();
                }
                push(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SAXException(e);
        }
    }

    /**
     * Called by this configurator when start of a tag is found. Default
     * implementation return false.
     * 
     * @param uri
     * @param localName
     * @param name
     * @param attributes
     * @return true if subclass handles the tag
     */
    public boolean startTag(String uri, String localName, String name,
            Attributes attributes) throws XMLConfiguratorException {
        return false;
    }

    /**
     * Set or reset the value of a variable in this configuration setting
     * 
     * @param name
     * @param val
     */
    public final void setVariable(String name, String val) {
        variables.put(name, val);
    }

    /**
     * @return Returns the variables.
     */
    public Map getVariables() {
        return variables;
    }

    /**
     * @param variables
     *            The variables to set.
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
     * @return Returns the idmap.
     */
    public Map getIdmap() {
        return idmap;
    }

    /**
     * @return Returns the buffer.
     */
    public StringBuffer getBuffer() {
        return buffer;
    }

    /**
     * @param buffer
     *            The buffer to set.
     */
    public void setBuffer(StringBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * @return Returns the log.
     */
    public static Logger getLog() {
        return log;
    }

    /**
     * @param log2
     */
    public static void setLog(Logger log2) {
log = log2;    }

}