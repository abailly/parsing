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
 * Created on Nov 9, 2005
 * --------------------------------------------------------------------------*/

package fr.lifl.xmlutils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.ResourceBundle;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A class for resolving special URI corresponding to variable instances.
 * <p>
 * This class allows resolving of XML entities whose <em>system URI</em>
 * starts with the scheme <code>file:</code> or <code>property:</code>.
 * These URIs are resolved against the current classpath. In the first case, the
 * content is provided asis, while in the second case the corresponding property
 * file is transformed on the fly in an XML fragment.
 * </p>
 * <p>
 * An URI as <code>classpath://path/to/resource</code> is resolved against the
 * classloader's classpath as <code>path/to/resource</code> (not that there is
 * no leading '/').
 * </p>
 * <p>
 * An URI as <code>property://path/to/property</code> is resolved against the
 * classloader's classpath as <code>path/to/property</code>then loaded using
 * {@link java.util.ResourceBundle} class, which means the real name of the
 * loaded property depends on the current locale settings.
 * </p>
 * <p>
 * All other URIs are left to the system's resolver.
 * </p>
 * @author nono
 * @version $Id: VariablesResolver.java 166 2005-11-09 16:28:50 +0100 (Wed, 09
 *          Nov 2005) /C=FR/ST=Nord/L=Lille/O=Norsys SA/OU=UE/CN=Arnaud
 *          Bailly/emailAddress=abailly@norsys.fr $
 */
class VariablesResolver implements EntityResolver {

	private ClassLoader loader;

	/**
	 * Creates with default class loader.
	 * 
	 */
	public VariablesResolver() {
		this.loader = Thread.currentThread().getContextClassLoader();
	}

	/**
	 * Creates with a specific loader.
	 * 
	 * @param loader
	 *            may not be null
	 */
	public VariablesResolver(ClassLoader loader) {
		if (loader == null)
			throw new IllegalArgumentException(
					"Loader parameter may not be null");
		this.loader = loader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String,
	 *      java.lang.String)
	 */
	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {
		/* resolve entity according to scheme */
		if (systemId.startsWith("classpath:")) {
			/* if relative, locate stream in class loader */
			InputStream is = loader.getResourceAsStream(systemId.substring(12));
			return new InputSource(is);
		} else if (systemId.startsWith("property:")) {
			ResourceBundle rb = ResourceBundle.getBundle(systemId.substring(11));
			return makeXMLFromProperties(rb);
		} else
			return null;
	}

	private InputSource makeXMLFromProperties(ResourceBundle rb) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(bos));
		for (Enumeration e = rb.getKeys(); e.hasMoreElements();) {
			StringBuffer sb = new StringBuffer();
			String k = (String) e.nextElement();
			// get value from bundle
			String val = rb.getString(k);
			// output var tag
			sb.append("<var name=\"").append(k).append("\" value=\"").append(
					val).append("\" />");
			pw.println(sb.toString());
		}
		pw.flush();
		pw.close();
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		return new InputSource(bis);
	}

}
