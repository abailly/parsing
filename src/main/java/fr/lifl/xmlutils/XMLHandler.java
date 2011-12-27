/*______________________________________________________________________________
 *
 * Copyright 2005 NORSYS
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
 * OF THE POSSIBILITY OF SUCH DAMAGE
 *______________________________________________________________________________
 *
 * Created on 27 sept. 2005
 * Author: Arnaud Bailly
 */
package fr.lifl.xmlutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import fr.lifl.utils.VariableSubstitutor;

/**
 * Utility class for configuring a business component mapper with an XML file.
 * <p>
 * This configurator reads in one or more XML streams, either from
 * {@link java.io.InputStream}raw data or from {@link java.io.Reader}character
 * data. It is an instance of SAX content handler. This configurator can
 * delegate handling of certain fragments of the XML file(s) read to
 * sub-systems. This is done by first registering another XMLConfigurator using
 * method {@link #addConfigurator(String,Configurator)}which associates a
 * namespace with an instance of configurator. This configurator will then
 * delegate tags within this namespace to the right configurator. The namespace
 * associated with this configurator is
 * <code>http://norsys.fr/framework-ldap/configuration.dtd</code>.
 * </p>
 * <p>
 * This implantation can handle the following tags:
 * <dl>
 * <dd>var</dd>
 * <dt>A variable declaration. Later occurence of this variable will be
 * replaced the given value</dt>
 * <dd>subconfig</dd>
 * <dt>Declares the class of a subconfigurator that can later be invoked.
 * Attribute <code>class</code> must be a valid class name which can be
 * instantiated without parameters and attribute <code>namespace</code> a
 * string denoting the namespace of this configurator.</dt>
 * </dl>
 * </p>
 * <p>
 * Sub-classes can override all content handler methods but not
 * {@link #characters(char[], int, int)} which handles variables substitution.
 * Method {@link #getBuffer()} should be used instead to retrieve text nodes'
 * content.
 * </p>
 * <p>
 * Variables can be used in the source XML file. These variables are syntactic:
 * if <code>var</code> is the name of a variable defined in this configurator,
 * then all occurences of the string <code>${var}</code> in the source XML
 * file are replaced by the value of the variable. All variables substitution
 * occurs in attributes values and in text nodes before they are passed to the
 * subconfigurators.
 * </p>
 * <p>
 * A DTD (<code>base-configuration.dtd</code>) is provided for base tags
 * handled by this configurator but as other fragments may be included, it does
 * only include the few tags directly managed by this class. The parser can be
 * set in validating mode using {@link #setValidating(boolean)} method, in which
 * case all syntax errors will give rise to a parsing exception and stops
 * processing.
 * </p>
 * <p>
 * The various parse methods may be called several times as configuration may be
 * split between various locations. Once the method
 * {@link #configure(Configure)}has been called and has returned correctly,
 * client may assume that configuration state is cleared so calls to this method
 * may be interspersed with calls to <code>parse()</code> methods.
 * </p>
 * 
 * @author nono
 * @version $Id: XMLConfigurator.java 212 2005-11-17 15:19:21Z
 *          /C=FR/ST=Nord/L=Lille/O=Norsys SA/OU=UE/CN=Arnaud
 *          Bailly/emailAddress=abailly@norsys.fr $
 * @see <a href="/doc/models/configuration.dtd">DTD for base tags </a>
 */
public class XMLHandler extends DefaultHandler {

	/*
	 * table for variables substitution
	 */
	private Map variables = new HashMap();

	/*
	 * map of subconfigurators
	 */
	private Map /* < String, XMLConfigurator > */configurators = new HashMap();

	/*
	 * Variables substitution handling
	 */
	private VariableSubstitutor varhandler = new VariableSubstitutor();

	/* current buffer for characters data */
	private StringBuffer buffer = new StringBuffer();

	/*
	 * The namespace
	 */
	private String namespace = "http://norsys.fr/framework-ldap/configuration.dtd";

	private XMLHandler parent;

	/*
	 * DTD Validation flag
	 */
	private boolean validating = false;

	/*
	 * logger
	 */
	private static final Log log = LogFactory.getLog(XMLHandler.class);

	/*
	 * stack of constructed objects
	 */
	private Stack obstack = new Stack();

	/*
	 * current object
	 */
	private Object current = null;

	/**
	 * Pop an object from the stack.
	 * 
	 * @return top of the object stack.
	 */
	protected Object pop() {
		Object ret = current;
		current = obstack.pop();
		return ret;
	}

	/**
	 * Return the current object.
	 * 
	 * @return
	 */
	protected Object peek() {
		return current;
	}

	/**
	 * Push an object on the stack and make it current.
	 * 
	 * @param o
	 *            Object to push on stack.
	 */
	protected void push(Object o) {
		if (o == null)
			throw new RuntimeException("pushing null object on stack");
		obstack.push(current);
		current = o;
	}

	/* map from tag to handlers */
	private Map handlers = new HashMap();

	/*
	 * if true, unknown tags will throw an exception
	 */
	private boolean dontIgnoreTags;

	public XMLHandler() {
		// set substitution pattern
		varhandler.setNamedVars(Pattern.compile("(\\#\\{([^#{}]+)\\})"));
	}

	/**
	 * Parse data from the given input stream. Platform encoding is assumed
	 * unless explicitly overriden in the xml file header. The stream must
	 * represent data from a valid xml file.
	 * 
	 * @param is
	 *            an InputStream. Must not be null.
	 * @throws IOException
	 * @throws IOExceptionif
	 *             something gets wrong reading the stream
	 */
	public final void parse(InputStream is) throws XMLHandlerException {
		try {
			XMLReader reader = SAXParserFactory.newInstance().newSAXParser()
					.getXMLReader();
			reader.setFeature("http://xml.org/sax/features/namespaces", true);
			reader.setFeature("http://xml.org/sax/features/namespace-prefixes",
					true);
			if (validating) {
				reader.setFeature("http://xml.org/sax/features/validation",
						true);
			} else 
				reader.setFeature("http://xml.org/sax/features/validation",
						false);
			reader.setEntityResolver(new VariablesResolver());
			reader.setContentHandler(this);
			if (log.isInfoEnabled())
				log.info("start parsing");
			reader.parse(new InputSource(is));
			if (log.isInfoEnabled())
				log.info("done parsing");
		} catch (Exception e) {
			throw new XMLHandlerException("Error while parsing XML stream", e);
		}
	}

	/**
	 * Parse data from the given file. This method calls
	 * {@link #parse(InputStream)}with a constructed FileInputStream
	 * 
	 * @param f
	 *            the File to read. May not be null.
	 * @throws XMLHandlerException
	 */
	public final void parse(File f) throws XMLHandlerException {
		try {
			if (log.isInfoEnabled())
				log.info("start parsing file " + f);
			parse(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			throw new XMLHandlerException("Cannot find file " + f, e);
		}
	}

	/**
	 * Returns the actual value of a parameter.
	 * 
	 * @param param
	 *            name of parameter
	 * @return a value or <code>null</code> if this parameter does not exists
	 *         in this configurator.
	 */
	public Object getVariable(String param) {
		return variables.get(param);
	}

	/**
	 * Sets the value of a variable in this configuration.
	 * 
	 * @param var
	 *            parameter name
	 * @param value
	 *            parameter value as a string
	 * @throws MapperConfigurationException
	 *             an exception may be thrown if the parameter does not exist in
	 *             this configuration or if the type of
	 */
	public void setVariable(String var, String value) {
		this.variables.put(var, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.norsys.mapper.core.core.config.ComponentConfigurator#addConfigurator(String,
	 *      fr.norsys.mapper.core.core.config.ComponentConfigurator)
	 */
	public void addConfigurator(String ns, XMLHandler subconf) {
		this.configurators.put(ns, subconf);
		((XMLHandler) subconf).setParent(this);
		if (log.isDebugEnabled())
			log.debug("adding xml configurator with ns " + ns);
	}

	/**
	 * Internal method to allow linking of parent and childrend
	 * 
	 * @param configurator
	 */
	private void setParent(XMLHandler configurator) {
		this.parent = configurator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		try {
			if (namespace.equals(uri)) {
				if ("var".equals(localName)) {
					String name = attributes.getValue("name");
					String val = varhandler.replaceVars(variables, attributes
							.getValue("value"));
					setVariable(name, val);
				} else if ("subconfig".equals(localName)) {
					String ns = attributes.getValue("name");
					String cln = varhandler.replaceVars(variables, attributes
							.getValue("class"));
					/* try to create object */
					Class cls = Class.forName(cln);
					Object o = cls.newInstance();
					addConfigurator(ns, (XMLHandler) o);
				} else {
					TagHandler th = (TagHandler) handlers.get(localName);
					if (th != null)
						th.startElement(uri, localName, qName, attributes);
					else if (dontIgnoreTags)
						throw new SAXException("Don't know how to handle tag "
								+ localName);
				}

			} else {
				/* clear buffer */
				this.buffer.delete(0, Integer.MAX_VALUE);
				/* try to find namespace */
				XMLHandler conf = (XMLHandler) configurators.get(uri);
				if (conf == null) {
					/* do nothing */
					return;
				}
				/* replace attributes value according to variables */
				AttributesImpl ai = new AttributesImpl(attributes);
				int ln = ai.getLength();
				for (int i = 0; i < ln; i++) {
					String v = ai.getValue(i);
					ai.setValue(i, varhandler.replaceVars(variables, v));
				}
				/* try something else */
				conf.startElement(uri, localName, qName, ai);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SAXException(e);
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
		try {
			if (!namespace.equals(uri)) {
				/* find suitable config */
				XMLHandler conf = (XMLHandler) configurators.get(uri);
				if (conf != null)
					conf.endElement(uri, localName, qName);
			}else {
				TagHandler th = (TagHandler) handlers.get(localName);
				if (th != null)
					th.endElement(uri, localName, qName);
				else if (dontIgnoreTags)
					throw new SAXException("Don't know how to handle tag "
							+ localName);
			}
			// clear buffer 
			buffer.delete(0,buffer.length());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SAXException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public final void characters(char[] ch, int start, int length)
			throws SAXException {
		/*
		 * apply variables substitution to character array
		 */
		String s = new String(ch, start, length);
		s = varhandler.replaceVars(variables, s);
		/* append to buffer */
		buffer.append(s);
	}

	/**
	 * Returns the accumulated characters as a String.
	 * 
	 * @return the accumulated content of the tags.
	 */
	public final String getBuffer() {
		if (parent != null)
			return parent.getBuffer();
		else
			return buffer.toString().trim();
	}

	/**
	 * Resets the configuration of this configurator; All accumulated
	 * configuration is cleared as if the XMLConfigurator has just been created.
	 * 
	 */
	public void resetConfig() {
		this.variables.clear();
		this.configurators.clear();
	}

	/**
	 * @return Returns the validating.
	 */
	public boolean isValidating() {
		return validating;
	}

	/**
	 * @param validating
	 *            The validating to set.
	 */
	public void setValidating(boolean validating) {
		this.validating = validating;
	}

	/**
	 * Add a new handler for given tag.
	 * 
	 * @param node
	 *            the node's name (without prefix).
	 * @param th
	 *            the TagHandler instance. May not be null.
	 */
	public void addHandler(String node, TagHandler th) {
		this.handlers.put(node, th);
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @return Returns the dontIgnoreTags.
	 */
	public boolean isDontIgnoreTags() {
		return dontIgnoreTags;
	}

	/**
	 * @param dontIgnoreTags
	 *            The dontIgnoreTags to set.
	 */
	public void setDontIgnoreTags(boolean dontIgnoreTags) {
		this.dontIgnoreTags = dontIgnoreTags;
	}
}
