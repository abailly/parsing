/*---------------------------------------------------------------------------
 * �2006 NORSYS
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
 * Created on Jan 9, 2006
 * --------------------------------------------------------------------------*/

package fr.lifl.xmlutils;

import org.apache.commons.logging.Log;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A class that handles error events from a SAX PArser.
 * <p>
 * This class is used for error reporting when an {@link fr.norsys.mapper.xml.XMLHandler}
 * is created in validating mode. It uses this instance's logger to
 * report warnings and halts on all errors and fatalerrors. 
 * </p>
 * @author nono
 * @version $Id: ConfiguratorErrorHandler.java 231 2006-02-07 09:10:03Z /C=FR/ST=Nord/L=Lille/O=Norsys SA/OU=UE/CN=Arnaud Bailly/emailAddress=abailly@norsys.fr $
 */
public class ConfiguratorErrorHandler implements ErrorHandler {

	private Log log;

	public ConfiguratorErrorHandler(Log log) {
		this.log = log;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
	 */
	public void warning(SAXParseException arg0) throws SAXException {
		log.warn(arg0.getLocalizedMessage(),arg0);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
	 */
	public void error(SAXParseException arg0) throws SAXException {
		log.error(arg0.getLocalizedMessage(),arg0);
		arg0.fillInStackTrace();
		throw arg0;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	public void fatalError(SAXParseException arg0) throws SAXException {
		log.fatal(arg0.getLocalizedMessage(),arg0);
		arg0.fillInStackTrace();
		throw arg0;
	}

}
