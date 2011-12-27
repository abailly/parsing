package fr.lifl.utils;

import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.AbstractMapDecorator;

/**
 * Decorates a map to forbid duplicate key insertions via put method.
 * <p>
 * This decorator disallows duplicate insertion of key sin the map until removal
 * has been done. That is, if method {@link java.util.Map#put(Object, Object)} is called
 * with twice in a row with the same key (as stated by
 * {@link java.lang.Object#equals(java.lang.Object)} method, an
 * {@link java.lang.IllegalStateException} is thrown. Between successive
 * invocations of put with the same key, method
 * {@link java.util.Map#remove(java.lang.Object)} must be called.
 * </p>
 * <p>
 * Example code with failure :
 * <code>
 * Map str = new StrictMap(new HashMap());
 * str.put("foo","foo");
 * str.put("bar","bar");
 * // will throw exception
 * str.put("foo","bar");
 * </code> 
 * The following code is OK:
 * <code>
 * Map str = new StrictMap(new HashMap());
 * str.put("foo","foo");
 * str.remove("foo");
 * // OK 
 * str.put("foo","bar");
 * </code> 
 * <p>
 * 
 * @author nono
 * @version $Id: StrictMap.java 231 2006-02-07 09:10:03Z /C=FR/ST=Nord/L=Lille/O=Norsys SA/OU=UE/CN=Arnaud Bailly/emailAddress=abailly@norsys.fr $
 */
public class StrictMap extends AbstractMapDecorator {

	public StrictMap(Map arg0) {
		super(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.collections.map.AbstractMapDecorator#put(java.lang.Object,
	 *      java.lang.Object)
	 */
	public Object put(Object arg0, Object arg1) {
		if (getMap().containsKey(arg0))
			throw new IllegalStateException("Duplicate key insertion: " + arg0);
		return super.put(arg0,arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.collections.map.AbstractMapDecorator#putAll(java.util.Map)
	 */
	public void putAll(Map arg0) {
		Set keys = getMap().keySet();
		keys.retainAll(arg0.keySet());
		if (!keys.isEmpty())
			throw new IllegalStateException("Duplicate key insertion: " + keys);
		super.putAll(arg0);
	}

}
