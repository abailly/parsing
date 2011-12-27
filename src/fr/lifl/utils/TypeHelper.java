package fr.lifl.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * A Helper class for converting string parameters to primitive types
 * 
 * @author Arnaud Bailly
 */
public class TypeHelper {

  /** hash table to store conversion constructors */
  private static java.util.Map constructormap = new java.util.HashMap();

  /** hash table to store conversion methods */
  private static java.util.Map conversionmap = new java.util.HashMap();

  /** hashtable for type names to class mapping */
  private static java.util.Map classmap = new java.util.HashMap();

  /**
   * static initilizer put constructors for base types
   */
  static {
    Class cls = Integer.class;
    try {
      Class[] ctorparam = new Class[] { java.lang.String.class };
      // type int
      cls = Integer.class;
      Constructor ctor = cls.getConstructor(ctorparam);
      constructormap.put(int.class, ctor);
      classmap.put("int", int.class);
      // type long
      cls = Long.class;
      ctor = cls.getConstructor(ctorparam);
      constructormap.put(long.class, ctor);
      classmap.put("long", long.class);
      // type float
      cls = Float.class;
      ctor = cls.getConstructor(ctorparam);
      constructormap.put(float.class, ctor);
      classmap.put("float", float.class);
      // type boolean
      cls = Boolean.class;
      ctor = cls.getConstructor(ctorparam);
      constructormap.put(boolean.class, ctor);
      classmap.put("boolean", boolean.class);
      // type double
      cls = Double.class;
      ctor = cls.getConstructor(ctorparam);
      constructormap.put(double.class, ctor);
      classmap.put("double", double.class);
      // type short
      cls = Short.class;
      ctor = cls.getConstructor(ctorparam);
      constructormap.put(short.class, ctor);
      classmap.put("short", short.class);
      // type byte
      cls = Byte.class;
      ctor = cls.getConstructor(ctorparam);
      constructormap.put(byte.class, ctor);
      classmap.put("byte", byte.class);
    } catch (Exception ex) {
      System.err.println("Unable to get constructor for class " + cls.getName()
          + " : " + ex.getMessage());
    }
  }

  /**
   * A method to register a factory for a type
   * 
   * @param cls
   *          a class object for which we gives a factory
   * @param method
   *          a method object to invoke for constructing objects. This method
   *          must be static, takes one String parameter and returns objects of
   *          class cls
   * @exception IllegalArgumentException
   *              if method or cls are invalid (null, not static...)
   */
  public static void registerFactory(Class cls, Method method) {
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
   *          a Class object
   * @param str
   *          a String to parse into an object of given class
   * @return an instance of the class <code>cls</code> or null if it was not
   *         possible to instantiate an object from a string
   */
  public static Object convert(Class cls, String str) {
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
      // try to find a static method taking one string parameter and returning
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

  private static Object invokeCtor(Constructor ctor, String str) {
    try {
      return ctor.newInstance(new Object[] { str });
    } catch (Throwable t) {
      System.err.println("Error in constructor invocation with argument " + str
          + " : " + t.getMessage());
      return null;
    }
  }

  private static Object invokeMethod(Method meth, String str) {
    try {
      // assume method is static
      return meth.invoke(null, new Object[] { str });
    } catch (Throwable t) {
      System.err.println("Error in method invocation with argument " + str
          + " : " + t.getMessage());
      return null;
    }
  }

  /**
   * Try to set a property in current with given name to object o.
   * 
   * @param current
   * @param localName
   * @param o
   */
  public static void set(Object current, String localName, Object o) {
    Class cls = current.getClass();
    BeanInfo info;
    try {
      info = Introspector.getBeanInfo(cls);
    } catch (IntrospectionException e) {
      return;
    }
    PropertyDescriptor[] props = info.getPropertyDescriptors();
    for (int i = 0; i < props.length; i++) {
      if (props[i].getName().equals(localName)) {
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

  // retrieve Class object for arrays
  // translate classxxx[] -> [Lclassxxx;
  // from externale to internal signature
  private static Class getArrayClass(String name) throws ClassNotFoundException {
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
   *          a Class object
   */
  public static Class getComponentType(Class cls) throws ClassNotFoundException {
    if (!cls.isArray())
      return cls;
    // size of array
    String cname = cls.getName().substring(1);

    switch (cname.charAt(0)) {
    case '[': //multidim array
      return Class.forName(cname);
    case 'L':
      return Class.forName(cname.substring(1, cname.length() - 1).replace('/',
          '.'));
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
   * types
   * 
   * @return a Class object
   * @exception ClassNotFoundException
   *              if name is not defined
   */
  public static Class getClass(String name) throws ClassNotFoundException {
      if (name.indexOf('[') >= 0)
        return getArrayClass(name);
      // primitive type ?
      Class cls = (Class) classmap.get(name);
      if (cls == null)
        cls = Class.forName(name);
      return cls;
  }
}