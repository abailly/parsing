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
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *______________________________________________________________________________
 *
 * Created on 3 oct. 2005
 * Author: Arnaud Bailly
 */
package fr.lifl.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Helper class for converting string parameters to primitive types.
 * 
 * @author Arnaud Bailly
 */
public class TypeHelper {

	/**
	 * Publicly shared instance of TypeHelper. This field may be used
	 * in a given JVM to share types conversions parameter easily.
	 */
	public static final TypeHelper instance = new TypeHelper();
	
	/** hash table to store conversion constructors */
	private Map /* < Class, Constructor > */constructormap = new java.util.HashMap();

	/** hash table to store conversion methods */
	private Map /* < Class, Method > */conversionmap = new java.util.HashMap();

	/** hashtable for type names to class mapping */
	private Map /* < String, Class > */classmap = new java.util.HashMap();

	/** map for default values for classes */
	private Map /* < Class, String > */defaultsmap = new java.util.HashMap();

	/**
	 * initilizer put constructors for base types
	 */
	public TypeHelper() {
		Class cls = Integer.class;
		try {
			Class[] ctorparam = new Class[] { java.lang.String.class };
			// type int
			cls = Integer.class;
			Constructor ctor = cls.getConstructor(ctorparam);
			constructormap.put(int.class, ctor);
			classmap.put("int", int.class);
			defaultsmap.put(int.class, new Integer(0));
			// type long
			cls = Long.class;
			ctor = cls.getConstructor(ctorparam);
			constructormap.put(long.class, ctor);
			classmap.put("long", long.class);
			defaultsmap.put(long.class, new Long(0L));
			// type float
			cls = Float.class;
			ctor = cls.getConstructor(ctorparam);
			constructormap.put(float.class, ctor);
			classmap.put("float", float.class);
			defaultsmap.put(float.class, new Float(0.0f));
			// type boolean
			cls = Boolean.class;
			ctor = cls.getConstructor(ctorparam);
			constructormap.put(boolean.class, ctor);
			classmap.put("boolean", boolean.class);
			defaultsmap.put(boolean.class, Boolean.FALSE);
			// type double
			cls = Double.class;
			ctor = cls.getConstructor(ctorparam);
			constructormap.put(double.class, ctor);
			classmap.put("double", double.class);
			defaultsmap.put(double.class, new Double(0.0));
			// type short
			cls = Short.class;
			ctor = cls.getConstructor(ctorparam);
			constructormap.put(short.class, ctor);
			classmap.put("short", short.class);
			defaultsmap.put(short.class, new Short((short) 0));
			// type byte
			cls = Byte.class;
			ctor = cls.getConstructor(ctorparam);
			constructormap.put(byte.class, ctor);
			classmap.put("byte", byte.class);
			defaultsmap.put(byte.class, new Byte((byte) 0));
		} catch (Exception ex) {
			System.err.println("Unable to get constructor for class "
					+ cls.getName() + " : " + ex.getMessage());
		}
	}

	/**
	 * returns the default value for instances of the given class. Default value
	 * is <code>null</code> for all object and array classes, and 0 for
	 * primitive types.
	 * 
	 * @param cls
	 *            the class definition.
	 * @return a strnig representation of the default value.
	 */
	public Object getDefault(Class cls) {
		return defaultsmap.get(cls);
	}

	/**
	 * A method to register a factory for a type
	 * 
	 * @param cls
	 *            a class object for which we gives a factory
	 * @param method
	 *            a method object to invoke for constructing objects. This
	 *            method must be static, takes one String parameter and returns
	 *            objects of class cls
	 * @exception IllegalArgumentException
	 *                if method or cls are invalid (null, not static...)
	 */
	public void registerFactory(Class cls, Method method) {
		Class[] clsparms = new Class[] { java.lang.String.class };
		Class retcls = method.getReturnType();
		int mod = method.getModifiers();
		Class[] parms = method.getParameterTypes();
		if (!retcls.equals(cls) || !Modifier.isStatic(mod)
				|| !Modifier.isPublic(mod) || Modifier.isAbstract(mod)
				|| !java.util.Arrays.equals(parms, clsparms))
			throw new IllegalArgumentException(
					"Invalid argument to method TypeHelper.registerFactory");
		conversionmap.put(cls, method);
	}

	/**
	 * Main method to convert from a string given a class object.
	 * 
	 * @param cls
	 *            a Class object
	 * @param str
	 *            a String to parse into an object of given class
	 * @return an instance of the class <code>cls</code> or null if it was not
	 *         possible to instantiate an object from a string
	 */
	public Object convert(Class cls, String str) {
		Class[] clsparms = new Class[] { java.lang.String.class };
		// first look into hashtables
		Constructor ctor = (Constructor) constructormap.get(cls);
		if (ctor != null)
			return invokeCtor(ctor, str);
		Method meth = (Method) conversionmap.get(cls);
		if (meth != null)
			return invokeMethod(meth, str);
		// try to find a suitable constructor
		try {
			ctor = cls.getConstructor(clsparms);
			// store in hashtable
			constructormap.put(cls, ctor);
			return invokeCtor(ctor, str);
		} catch (Exception ex) {
		}
		// try to find a suitable method
		try {
			Method[] methods = cls.getMethods();
			// try to find a static method taking one string parameter and
			// returning
			// an object of class cls
			for (int i = 0; i < methods.length; i++) {
				Class retcls = methods[i].getReturnType();
				int mod = methods[i].getModifiers();
				Class[] parms = methods[i].getParameterTypes();
				if (!retcls.equals(cls) || !Modifier.isStatic(mod)
						|| !java.util.Arrays.equals(parms, clsparms))
					continue;
				// found a method - hope it is OK !!!
				conversionmap.put(cls, methods[i]);
				return invokeMethod(methods[i], str);
			}
		} catch (Throwable t) {
		}
		return null;
	}

	private Object invokeCtor(Constructor ctor, String str) {
		try {
			return ctor.newInstance(new Object[] { str });
		} catch (Throwable t) {
			System.err.println("Error in constructor invocation with argument "
					+ str + " : " + t.getMessage());
			return null;
		}
	}

	private Object invokeMethod(Method meth, String str) {
		try {
			// assume method is static
			return meth.invoke(null, new Object[] { str });
		} catch (Throwable t) {
			System.err.println("Error in method invocation with argument "
					+ str + " : " + t.getMessage());
			return null;
		}
	}

	/**
	 * Try to set a property in current with given name to object o.
	 * 
	 * This method recognizes encodings for properties which are collections:
	 * arrays, Lists and Maps. Basically, if <code>fname</code> is of the form
	 * <code>xxx[zzz]</code> then a property named <code>xxx</code> is
	 * extracted from the object and <code>zzz</code> is handled according to
	 * the following conditions:
	 * <ul>
	 * <li>if zzz may be transformed into a positive integer, then xxx must be
	 * either an array type or a List instance. In this case, the value at index
	 * zzz is extracted</li>
	 * <li>if xxx is a Map, then the following happens:
	 * <ul>
	 * <li>if there is at least one element in the map, then the class of the
	 * first key returned by getKeys() is used as the base class of the Map and
	 * zzz is considered as a string representation of this class.</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * If the property name is null or empty, then the object current itself is
	 * returned.
	 * 
	 * @param current
	 * @param fname
	 * @param o
	 */
	public void set(Object current, String fname, Object o) {
		if ("".equals(fname) || fname == null)
			return;
		Class cls = current.getClass();
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(cls);
		} catch (IntrospectionException e) {
			return;
		}
		/* special handling for indexed properties */
		String index;
		/* RE to extract base name and index */
		String are = "(.*)\\[(.*)\\]";
		Matcher m = Pattern.compile(are).matcher(fname);
		if (m.matches()) {
			fname = m.group(1);
			index = m.group(2);
			setIndexed(current, fname, index, o);
		}
		PropertyDescriptor[] props = info.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			if (props[i].getName().equals(fname)) {
				/* try to write o */
				Method setter = props[i].getWriteMethod();
				try {
					setter.invoke(current, new Object[] { o });
					/* if everything goes right, return */
					return;
				} catch (Exception e1) {
				}
			}
		}
	}

	/**
	 * Try to set a property in current with given name to object o by
	 * converting a value from string.
	 * 
	 * This method recognizes encodings for properties which are collections:
	 * arrays, Lists and Maps. Basically, if <code>fname</code> is of the form
	 * <code>xxx[zzz]</code> then a property named <code>xxx</code> is
	 * extracted from the object and <code>zzz</code> is handled according to
	 * the following conditions:
	 * <ul>
	 * <li>if zzz may be transformed into a positive integer, then xxx must be
	 * either an array type or a List instance. In this case, the value at index
	 * zzz is extracted</li>
	 * <li>if xxx is a Map, then the following happens:
	 * <ul>
	 * <li>if there is at least one element in the map, then the class of the
	 * first key returned by getKeys() is used as the base class of the Map and
	 * zzz is considered as a string representation of this class.</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * If the property name is null or empty, then the object current itself is
	 * returned.
	 * 
	 * @param current
	 *            the target of setter
	 * @param fname
	 *            the property name (lower case first letter)
	 * @param val
	 *            the value. There must exist in this helper a conversion method
	 *            for the type of the property.
	 */
	public void setString(Object current, String fname, String val) {
		if ("".equals(fname) || fname == null)
			return;
		Class cls = current.getClass();
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(cls);
		} catch (IntrospectionException e) {
			return;
		}
		/* special handling for indexed properties */
		String index;
		/* RE to extract base name and index */
		String are = "(.*)\\[(.*)\\]";
		Matcher m = Pattern.compile(are).matcher(fname);
		if (m.matches()) {
			fname = m.group(1);
			index = m.group(2);
			setIndexed(current, fname, index,  val);
		}
		PropertyDescriptor[] props = info.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			if (props[i].getName().equals(fname)) {
				/* try to write o */
				Method setter = props[i].getWriteMethod();
				/* convert val */
				Object o = convert(props[i].getPropertyType(), val);
				try {
					setter.invoke(current, new Object[] { o });
					/* if everything goes right, return */
					return;
				} catch (Exception e1) {
				}
			}
		}
	}

	private void setIndexed(Object current, String fname, String index,
			 Object o) {
		/* retrieve base property */
		Object coll = get(current, fname);
		if (coll == null)
			return;
		/* handle indexing */
		Class ccls = coll.getClass();
		if (ccls.isArray()) {
			setIndexedArray(coll, index, o);
		} else if (List.class.isAssignableFrom(ccls))
			setIndexedList(coll, index, o);
		else if (Map.class.isAssignableFrom(ccls))
			setIndexedMap(coll, index, o);
	}

	/**
	 * Try to get property named <code>name</code> from <code>current </codo>.
	 * This method returns null if the property cannot be retrieved.
	 * This method recognizes encodings for properties which are collections: arrays,
	 * Lists and Maps. Basically, if <code>fname</code> is of the 
	 * form <code>xxx[zzz]</code> then a property named <code>xxx</code> is
	 * extracted from the object and <code>zzz</code> is handled according 
	 * to the following conditions:
	 * <ul>
	 * <li>if zzz may be transformed into a positive integer, then xxx must 
	 * be either an array type or a List instance. In this case, the value
	 * at index zzz is extracted </li>
	 * <li>if xxx is a Map, then the following happens:
	 * <ul>
	 * <li>if there is at least one element in the map, then the class
	 * of the first key returned by getKeys() is used as the base class
	 * of the Map and zzz is considered as a string representation 
	 * of this class. </li>
	 * </ul>
	 * </li>
	 * </ul> 
	 *  If the property name is null or empty, then the object current itself is returned.
	 * 
	 * @param current the object to get property from
	 * @param fname the name of property
	 * @return null if proprety cannot be retrieved
	 */
	public Object get(Object current, String fname) {
		if ("".equals(fname) || fname == null)
			return current;
		Class cls = current.getClass();
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(cls);
		} catch (IntrospectionException e) {
			return null;
		}
		/* special handling for indexed properties */
		String index;
		/* RE to extract base name and index */
		String are = "(.*)\\[(.*)\\]";
		Matcher m = Pattern.compile(are).matcher(fname);
		if (m.matches()) {
			fname = m.group(1);
			index = m.group(2);
			return getIndexed(current, fname, index, info);
		}
		PropertyDescriptor[] props = info.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			if (props[i].getName().equals(fname)) {
				/* try to write o */
				Method getter = props[i].getReadMethod();
				try {
					return getter.invoke(current, new Object[0]);
					/* if everything goes right, return */
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		// FAILURE
		return null;
	}

	/**
	 * This method checks whether the given field has a default value. This
	 * method extracts current value from field and compares it with default
	 * value for the type.
	 * 
	 * @param current
	 *            object to check.
	 * @param fname
	 *            field to retrieve value from
	 * @return true if field has default value for its type, false otherwise.
	 */
	public boolean hasDefaultValue(Object current, String fname) {
		Object o = get(current, fname);
		Class c = getClass(current, fname);
		if (c == null)
			return false;
		Object d = defaultsmap.get(c);
		if (o == d || (o != null && o.equals(d)))
			return true;
		return false;
	}

	/**
	 * Retrieves the type Of a field as a Class objcet.
	 * 
	 * @param current
	 *            the object to get property from
	 * @param fname
	 *            the name of property
	 * @return a Class denoting type of fname it current or null.
	 */
	public Class getClass(Object current, String fname) {
		if ("".equals(fname) || fname == null)
			return null;
		Class cls = current.getClass();
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(cls);
		} catch (IntrospectionException e) {
			return null;
		}
		PropertyDescriptor[] props = info.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			if (props[i].getName().equals(fname)) {
				return props[i].getPropertyType();
			}
		}
		// FAILURE
		return null;

	}

	/**
	 * @param current
	 * @param fname
	 * @param index
	 * @param info
	 * @return
	 */
	private Object getIndexed(Object current, String fname, String index,
			BeanInfo info) {
		/* retrieve base property */
		Object coll = get(current, fname);
		if (coll == null)
			return null;
		/* handle indexing */
		Class ccls = coll.getClass();
		if (ccls.isArray()) {
			return getIndexedArray(coll, index);
		} else if (List.class.isAssignableFrom(ccls))
			return getIndexedList(coll, index);
		else if (Map.class.isAssignableFrom(ccls))
			return getIndexedMap(coll, index);
		// FAILURE : type of collection not handled
		return null;
	}

	/**
	 * @param coll
	 * @param index
	 * @return
	 */
	private Object getIndexedMap(Object coll, String index) {
		Map map = (Map) coll;
		/* error */
		if (map.isEmpty())
			return null;
		// first key
		Object k = map.keySet().iterator().next();
		// convert index
		Object kk = convert(k.getClass(), index);
		// return value
		return map.get(kk);
	}

	/**
	 * @param coll
	 * @param index
	 * @return
	 */
	private void setIndexedMap(Object coll, String index, Object data) {
		Map map = (Map) coll;
		/* error */
		if (map.isEmpty())
			return;
		// first key
		Object k = map.keySet().iterator().next();
		// convert index
		Object kk = convert(k.getClass(), index);
		// return value
		map.put(kk, data);
	}

	/**
	 * @param coll
	 * @param index
	 * @return
	 */
	private Object getIndexedList(Object coll, String index) {
		// convert to int
		int i = ((Integer) convert(int.class, index)).intValue();
		return ((List) coll).get(i);
	}

	/**
	 * @param coll
	 * @param index
	 * @return
	 */
	private void setIndexedList(Object coll, String index, Object data) {
		// convert to int
		int i = ((Integer) convert(int.class, index)).intValue();
		((List) coll).set(i, data);
	}

	/**
	 * @param coll
	 * @param index
	 * @return
	 */
	private Object getIndexedArray(Object coll, String index) {
		// convert to int
		int i = ((Integer) convert(int.class, index)).intValue();
		// extract value
		return Array.get(coll, i);
	}

	/**
	 * Sets the value of given array <code>coll</code> at <code>index</code>
	 * to <code>data</code>. An exception may be thrown if index is not in
	 * the range of the array.
	 * 
	 * @param coll
	 * @param index
	 * @return
	 */
	private void setIndexedArray(Object coll, String index, Object data) {
		// convert to int
		int i = ((Integer) convert(int.class, index)).intValue();
		// extract value
		Array.set(coll, i, data);
	}

	// retrieve Class object for arrays
	// translate classxxx[] -> [Lclassxxx;
	// from externale to internal signature
	public static Class getArrayClass(String name)
			throws ClassNotFoundException {
		String bare = name.substring(0, name.indexOf('['));
		String newname = "";
		int dim = 0;
		// count dimensions
		for (int i = 0; i < name.length(); i++)
			if (name.charAt(i) == '[') {
				newname += '[';
				dim++;
			}
		// primitive type ?
		if (bare.equals("int"))
			newname += 'I';
		else if (bare.equals("long"))
			newname += 'J';
		else if (bare.equals("float"))
			newname += 'F';
		else if (bare.equals("boolean"))
			newname += 'Z';
		else if (bare.equals("double"))
			newname += 'D';
		else if (bare.equals("byte"))
			newname += 'B';
		else if (bare.equals("short"))
			newname += 'S';
		else if (bare.equals("char"))
			newname += 'C';
		else {
			// this a hack around a probleme in jdk1.4.1
			// there is no way to retrieve a Class instance denoting
			// an array of objects with a name
			// neither my.package.Myclass[] no [Lmy/package/Myclass; works
			Class base = Class.forName(bare);
			int[] dims = new int[dim];
			for (int i = 0; i < dims.length; i++)
				dims[i] = 0;
			Object o = java.lang.reflect.Array.newInstance(base, dims);
			return o.getClass();
		}
		// return class
		return Class.forName(newname);

	}

	/**
	 * returns a Class object representing the base component type of an array
	 * type. If cls is a multi-dimensionnal array Class, this method returns an
	 * array Class one dimension under cls. If cls is not an array Class, this
	 * method returns cls
	 * 
	 * @param cls
	 *            a Class object
	 */
	public static Class getComponentType(Class cls)
			throws ClassNotFoundException {
		if (!cls.isArray())
			return cls;
		// size of array
		String cname = cls.getName().substring(1);

		switch (cname.charAt(0)) {
		case '[': // multidim array
			return Class.forName(cname);
		case 'L':
			return Class.forName(cname.substring(1, cname.length() - 1)
					.replace('/', '.'));
		case 'I':
			return int.class;
		case 'J':
			return long.class;
		case 'Z':
			return boolean.class;
		case 'F':
			return float.class;
		case 'D':
			return double.class;
		case 'C':
			return char.class;
		case 'S':
			return short.class;
		case 'B':
			return byte.class;
		}
		// should never get there
		return null;
	}

	/**
	 * returns the Class object given a class name Mainly useful for primitive
	 * types. The class name may be given either in with package separator being
	 * slashes or dots.
	 * 
	 * @return a Class object
	 * @exception ClassNotFoundException
	 *                if name is not defined
	 */
	public Class getClass(String name) throws ClassNotFoundException {
		if (name.indexOf('[') >= 0)
			return getArrayClass(name);
		// primitive type ?
		Class cls = (Class) classmap.get(name);
		// contains / ?
		name = name.replace('/', '.');
		if (cls == null)
			cls = Class.forName(name);
		return cls;
	}

	/**
	 * This method transforms a bean instance into a map from property names to
	 * property values. This method as all methods in this class never throws an
	 * exception. If an attribute's value cannot be retrieved for any reason, it
	 * is set to null in the return map.
	 * 
	 * @param o
	 *            an Object following the javabean pattern.
	 * @return a Map < String, Object > instance
	 */
	public Map toMap(Object o) {
		Map ret = new HashMap();
		Class cls = o.getClass();
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(cls);
		} catch (IntrospectionException e) {
			return null;
		}
		PropertyDescriptor[] props = info.getPropertyDescriptors();
		Object[] a = new Object[0];
		for (int i = 0; i < props.length; i++) {
			/* try to write o */
			Method getter = props[i].getReadMethod();
			String name = props[i].getName();
			try {
				Object val = getter.invoke(o, a);
				ret.put(name, val);
				/* if everything goes right, return */
			} catch (Exception e1) {
				ret.put(name, null);
			}
		}
		return ret;
	}

	/**
	 * Dereferences a chain of dot separated object references. This methods
	 * works using recursion.
	 * 
	 * @param base
	 *            the context to start with.
	 * @param uri
	 *            a string of the form xxx.yyy[zzz]
	 * @return
	 */
	public Object getReference(Object base, String uri) {
		String fname = uri;
		String rest = fname;
		/* strip leading slashes */
		while (fname.startsWith("."))
			fname = fname.substring(1);
		rest = fname;
		/* extract first component and rest */
		int i = fname.indexOf('.');
		if (i != -1) {
			fname = fname.substring(0, i);
			rest = rest.substring(i);
		}
		/* retrieve field from base */
		Object o = get(base, fname);
		if (o == null)
			return null;
		/* recursion */
		if (rest.startsWith("."))
			return get(o, rest);
		else
			return o;
	}

	/**
	 * Dereferences a string reference to an object and set its value. This
	 * method recursively traverses an expression starting from a given base or
	 * the root object. This method returns the enclosing object of the
	 * reference set.
	 * 
	 * @param url
	 * @param base
	 * @param value
	 */
	public Object setReference(String url, Object base, Object value) {
		String fname = url;
		String rest = fname;
		/* strip leading slashes */
		while (fname.startsWith("."))
			fname = fname.substring(1);
		rest = fname;
		/* extract first component and rest */
		int i = fname.indexOf('.');
		if (i != -1) {
			fname = fname.substring(0, i);
			rest = rest.substring(i);
		}
		/* recursion */
		if (rest.startsWith(".")) {
			/* retrieve field from base */
			Object o = get(base, fname);
			if (o == null)
				return null;
			return setReference(rest, o, value);
		} else {
			set(base, rest, value);
			return base;
		}
	}

}
