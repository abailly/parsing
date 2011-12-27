package fr.lifl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * A class that implements Preferences API through a flat property file
 * 
 * This class implements the java.util.prefs API using properties. A flat file 
 * containing couples of property names/values is used as the backing store.
 * 
 * @author bailly
 * @version $Id$
 */
public class FlatFilePreferences extends AbstractPreferences {

	// properties map
	private Properties properties;

	// store
	private File store;

	static FlatFilePreferences createRoot(File store) {
		return new FlatFilePreferences(store);
	}

	/**
	 * This constructor is used solely for creating root nodes
	 */
	private FlatFilePreferences(File store) {
		super(null, "");
		this.store = store;
		// populate properties
		properties = new Properties();
		try {
			properties.load(new FileInputStream(store));
		} catch (IOException ex) {
			System.err.println("Unable to load properties from " + store);
		}
	}

	/**
	 * Constructor for FlatFilePreferences.
	 * @param parent
	 * @param name
	 */
	public FlatFilePreferences(AbstractPreferences parent, String name) {
		super(parent, name);
	}

	/**
	 * @see java.util.prefs.AbstractPreferences#putSpi(String, String)
	 */
	protected void putSpi(String key, String value) {
		if (parent() == null) // root node	
			properties.put("/"+key, value);
		else
			((FlatFilePreferences) parent()).putSpi(name() + "/" + key, value);
	}

	/**
	 * @see java.util.prefs.AbstractPreferences#getSpi(String)
	 */
	protected String getSpi(String key) {
		if (parent() == null) // root node	
			return (String) properties.get("/"+key);
		else
			return ((FlatFilePreferences) parent()).getSpi( name() + "/" +key);
	}

	/**
	 * @see java.util.prefs.AbstractPreferences#removeSpi(String)
	 */
	protected void removeSpi(String key) {
		if (parent() == null) // root node	
			properties.remove("/"+key);
		else
			 ((FlatFilePreferences) parent()).removeSpi( name() + "/" +key);
	}

	/**
	 * @see java.util.prefs.AbstractPreferences#removeNodeSpi()
	 */
	protected void removeNodeSpi() throws BackingStoreException {
		if (parent() == null) // root node	
			throw new BackingStoreException("Cannot remove root node");
		else
			 ((FlatFilePreferences) parent()).removeSpi(name());
	}

	/**
	 * @see java.util.prefs.AbstractPreferences#keysSpi()
	 */
	protected String[] keysSpi() throws BackingStoreException {
		return enumKeys("");
	}

	private String[] enumKeys(String prefix) {
		String[] ret = new String[0];
		java.util.List l = new ArrayList();
		if (parent() != null)
			return ((FlatFilePreferences) parent()).enumKeys(
				"/" + name() + prefix);
		// return array of properties with given prefix
		for (Enumeration e = properties.propertyNames();e.hasMoreElements();) {
			String k = (String) e.nextElement();
			if (k.indexOf(prefix) == 0) {
				String rest = k.substring(prefix.length()+1);
				if(rest.indexOf("/") != -1) 
					continue;
				if(properties.get(k) instanceof Preferences)
					continue;
				l.add(k);
			}
		}
		return (String[]) l.toArray(ret);
	}

	/**
	 * @see java.util.prefs.AbstractPreferences#childrenNamesSpi()
	 */
	protected String[] childrenNamesSpi() throws BackingStoreException {
		String[] keys = enumKeys("");
		List l = new ArrayList();
		for (int i = 0; i < keys.length; i++) {
			Object o = properties.get(keys[i]);
			if (o instanceof Preferences)
				l.add(keys[i]);
		}
		return (String[]) l.toArray(keys);
	}

	/**
	 * @see java.util.prefs.AbstractPreferences#childSpi(String)
	 */
	protected AbstractPreferences childSpi(String name) {
		return child(this, name);
	}

	private FlatFilePreferences child(
		FlatFilePreferences parent,
		String name) {
		if (parent() == null) { // root node 
			String path = parent.absolutePath() + "/" + name;
			Object o = properties.get(path);
			if (!(o instanceof AbstractPreferences)) {
				o = new FlatFilePreferences(parent, name);
				properties.put(path, o);
			}
			return (FlatFilePreferences) o;
		} else
			return ((FlatFilePreferences) parent()).child(parent, name);

	}
	/**
	 * @see java.util.prefs.AbstractPreferences#syncSpi()
	 */
	protected void syncSpi() throws BackingStoreException {
		if (parent() == null) {
			writeProperties();
		} else
			 ((FlatFilePreferences) parent()).syncSpi();
	}

	/**
	 * @see java.util.prefs.AbstractPreferences#flushSpi()
	 */
	protected void flushSpi() throws BackingStoreException {
		if (parent() == null)
			writeProperties();
		else
			 ((FlatFilePreferences) parent()).flushSpi();
	}

	// write the properties file to file
	private void writeProperties() throws BackingStoreException {
		try {
			// open stream
			PrintWriter out = new PrintWriter(new FileOutputStream(store));
			// write all properties, skipping childs
			Iterator it = properties.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String k = (String) entry.getKey();
				Object v = entry.getValue();
				if (!(v instanceof Preferences) && (v != null)) {
					out.print(k);
					out.print("=");
					out.println(v);
				}
			}
			out.close();
		} catch (IOException ex) {
			throw new BackingStoreException(ex);
		}
	}

}
