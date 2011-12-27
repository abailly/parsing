package fr.lifl.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

/**
 * @author bailly
 * @version $Id$
 */
public class FlatPreferencesFactory implements PreferencesFactory {
	
	// the property object used to store preferences
	private static  Properties properties;
	
	// the property file name
	private static String storeName;
	
	// the user property file
	private static File userStore;

	// the system property file
	private static File systemStore;

	// the global hashmap to store roots
	private static Map roots = new HashMap();
		
	// creates static objects
	static {
		// retrieve user preferences file
		String homename = System.getProperty("user.home",".");
		File home = new File(homename);
		// check that we can write to home directory
		if(!home.canWrite() || !home.canRead()) {
			System.err.println("Cannot read/write preference file in "+homename);
		}
		userStore = new File(home,".user.prefs");		
		if(!userStore.exists())
			try {
				userStore.createNewFile();
			}catch(IOException ex) {
				System.err.println("Cannot create user preference file in"+homename);
				userStore= null;
			}
		// store system prefs in java.home directory
		homename = System.getProperty("java.home",".");
		home = new File(homename);
		// check we can at least read
		if(!home.canRead()) 
			System.err.println("Cannot read system preferences file in"+homename);
		systemStore = new File(home,".system.prefs");
	}
				
	/**
	 * @see java.util.prefs.PreferencesFactory#systemRoot()
	 */
	public Preferences systemRoot() {
		Preferences prefs = (Preferences)roots.get(systemStore);
		if(prefs == null) {
			prefs = FlatFilePreferences.createRoot(systemStore);
			roots.put(systemStore,prefs);
		}
		return prefs;
	}

	/**
	 * @see java.util.prefs.PreferencesFactory#userRoot()
	 */
	public Preferences userRoot() {
		Preferences prefs = (Preferences)roots.get(userStore);
		if(prefs == null) {
			prefs = FlatFilePreferences.createRoot(userStore);
			roots.put(userStore,prefs);
		}
		return prefs;
	}

}
