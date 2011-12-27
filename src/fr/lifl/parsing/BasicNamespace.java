/*
 * Created on 25 f?vr. 2004
 * 
 * $Log: BasicNamespace.java,v $
 * Revision 1.2  2004/06/30 14:36:18  bailly
 * ajout structures de Parser g?n?rique : evenements, listeners, namespace,
 *
 * Revision 1.1  2004/02/27 16:40:27  bailly
 * initial check-in
 *
 */
package fr.lifl.parsing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A basic implementation of nested namespaces.
 * <p>
 * This implementation of @see{Namespace} has the following features :
 * <ul>
 * <li>nested namespaces, allowing creation of namespaces within a parent namespace</li>
 * <li>lexical resolution of names : resolve method tries to find a name in
 * enclosing namespaces if not found in current namespace </li>
 * <li>provision for a toplevel namespaces object without parents</li>
 * <li>hook for name validation through @see{SymbolValidator} interface </li>
 * <li>provision for fully qualified name output with ad-hoc separator </li>
 * </ul>
 * 
 * @author bailly
 * @version $Id$
 */
public class BasicNamespace implements Namespace {

	/* path separator string */
	private String pathSeparator = "::";

	/* symbol table used */
	private HashMap symbols;

	/* link rto parent namespace */
	private BasicNamespace parent;

	/* name for this namespace */
	private String name;

	/* set of observers for this namespace */
	private Set observers = new HashSet();
	
	/* validator for this namespace - defaults to always true */
	private SymbolValidator validator = new SymbolValidator() {
		public boolean validate(String name) {
			return true;
		}
	};

	/**
	 * Construct a namespace with given parent
	 * <p>
	 * This method builds a namespace object within enclosing 
	 * <code>parent</code> namespace. This namespace object 
	 * is bound in the parent namespace to <code>name</code> using
	 * @see{rebind(String,Object)} which guarantees safe binding.  
	 * <br/>
	 * If the <code>parent</code> namespace is null, this namespace is 
	 * considered a toplevel namespace and stored as such in a collection 
	 * of toplevel namespaces. In fact, BasicNamespace maintains
	 * an internal root namespace from which all other namespaces 
	 * come from.
	 * 
	 * @param name the name this namespace is known as.
	 * @param parent the enclosing - not null - namespace.
	 */
	public BasicNamespace(String name, BasicNamespace parent)
		throws SymbolException {
		this.name = name;
		this.parent = parent;
		this.symbols = new HashMap();
		/* toplevel case */
		if (this.parent != null) {
			parent.rebind(name, this);
			this.pathSeparator = parent.getPathSeparator();
		}else /* set default separator */
			this.pathSeparator = ".";		
	}

	public BasicNamespace(String name, BasicNamespace parent,String pathSeparator)
		throws SymbolException {
		this.name = name;
		this.parent = parent;
		this.pathSeparator = pathSeparator;
		this.symbols = new HashMap();
		if(parent != null)
			parent.rebind(name, this);
	}

	/**
	 * Construct a root namespace without parent and with 
	 * default pathseparator (".")
	 * 
	 * @param string
	 */
	public BasicNamespace() throws SymbolException {
		this("",null,".");
	}

	/* (non-Javadoc)
	 * @see fr.lifl.parsing.Namespace#bind(java.lang.String, java.lang.Object)
	 */
	public void bind(String name, Object definition)
		throws InvalidSymbolException, DuplicateSymbolException {
		Object old = symbols.get(name);
		if (old != definition) /* not forwarding */
			throw new DuplicateSymbolException(
				"Symbol " + name + " already bound");
		/* validate */
		if (!validator.validate(name))
			throw new InvalidSymbolException(
				"Symbol " + name + " failed to validate");
		symbols.put(name, definition);
		notifyBind(name,definition);
	}

	/* (non-Javadoc)
	 * @see fr.lifl.parsing.Namespace#rebind(java.lang.String, java.lang.Object)
	 */
	public Object rebind(String name, Object definition)
		throws InvalidSymbolException {
		Object old = symbols.get(name);
		/* validate */
		if (!validator.validate(name))
			throw new InvalidSymbolException(
				"Symbol " + name + " failed to validate");
		symbols.put(name, definition);
		notifyBind(name,definition);
		notifyUnbind(name,old);
		return old;
	}

	/* (non-Javadoc)
	 * @see fr.lifl.parsing.Namespace#forward(java.lang.String, java.lang.Object)
	 */
	public void forward(String name, Object definition)
		throws DuplicateSymbolException, InvalidSymbolException {
		if (symbols.containsKey(name)) /* not forwarding */
			throw new DuplicateSymbolException(
				"Symbol " + name + " already bound");
		/* validate */
		if (!validator.validate(name))
			throw new InvalidSymbolException(
				"Symbol " + name + " failed to validate");
		symbols.put(name, definition);
		notifyBind(name,definition);
	}

	/* (non-Javadoc)
	 * @see fr.lifl.parsing.Namespace#resolve(java.lang.String)
	 */
	public Object resolve(String name) throws SymbolException {
		Object old = symbols.get(name);
		if (old != null)
			return old;
		/* not found */
		try {
			return parent.resolve(name);
		} catch (NullPointerException nex) { /* parent does not exists */
		}
		/* give up */
		throw new UnresolvedSymbolException(
			"Symbol " + name + " not found in this namespace");
	}

	/* (non-Javadoc)
	 * @see fr.lifl.parsing.Namespace#unbind(java.lang.String)
	 */
	public Object unbind(String name) throws UnresolvedSymbolException {
		Object old = symbols.get(name);
		if (old != null)
			return old;
		else
			throw new UnresolvedSymbolException(
				"Symbol " + name + " not found in this namespace");
	}

	/**
	 * Resolves the given name considering it as an absolute
	 * or relative path from this namespace.
	 * <p>
	 * This method tries to separate <code>name</code> into its components,
	 * using @see{getPathSeparator()} as parsing separator. The givens 
	 * names are then resolved : 
	 * <ol>
	 * <li>if <code>name</code> starts with the path separator for this namespace, 
	 * then it is considered an <b>absolute</b> path and resolved against 
	 * toplevel namespaces </li>
	 * <li> else it is considered a relative path from <b>this</b> namespace and resolve down
	 * the nested namespaces</li>
	 * </ol>
	 *  
	 * @param name the symbol to resolve
	 * @return the bound object if found
	 */
	public Object resolvePath(String name) throws SymbolException {
		String sep = getPathSeparator();
		if (name.startsWith(sep)) {
			Namespace sc= this;
			while(sc.getEnclosing() != null) sc = sc.getEnclosing();
			return sc.resolve(name.substring(sep.length()));
		}
		/* relative case */
		/* split name */
		int idx = name.indexOf(sep);
		String part = name;
		if (idx != -1)
			part = name.substring(0, idx);
		/* lookup in symbols */
		Object def = symbols.get(part);
		if (def == null)
			/* ask parent */
			if (parent != null)
				return ((BasicNamespace) parent).resolvePath(name);
			else {
				throw new UnresolvedSymbolException(
					"Symbol " + name + " cannot be resolved");
			}
		else {
			/* nested path */
			if (idx != -1) {
				return ((BasicNamespace) def).resolvePath(
					name.substring(idx + 2));
			}
			/* found leaf */
			return def;
		}

	}

	/**
	 * Returns the separator string for paths in this namespaces
	 * <p>
	 *
	 * @return a String for path separation
	 */
	public  String getPathSeparator() {
		return pathSeparator;
	}

	/**
	 * Defines a string to be the path separator used in 
	 * resolving names for this namespace hierarchy.
	 * 
	 * @param sep the new separator string : must contains at
	 * least one character
	 */
	public void setPathSeparator(String sep) {
		if (sep.length() < 1)
			throw new IllegalArgumentException("Cannot set separator to empty string");
		pathSeparator = sep;
	}
	
	/**
	 * Adds the given observer to the set of registered observers
	 * for this namespace
	 * 
	 * @param observer a non-null NamespaceObserver object
	 */
	public void addNamespaceObserver(NamespaceObserver observer) {
		this.observers.add(observer);
	}
	
	/**
	 * Remove the given observer from the set of registered observers
	 * in this namespace
 	* 
	 * @param observer a non-null NamespaceObserver object
 	*/
	public void removeNamespaceObserver(NamespaceObserver observer) {
		this.observers.remove(observer);
	}
	
	/*
	 * Notifies registered observers + parent namespace of a bind event
	 */
	private void notifyBind(String name,Object o) {
		Iterator it= observers.iterator();
		while(it.hasNext()) 
			((NamespaceObserver)it.next()).bindEvent(name,o);
		/* notify parent */
		if(parent != null)
			parent.notifyBind(this.name + pathSeparator + name,o);
	}
	
	/*
	 * Notifies registered observers + parent namespace of a unbind event
	 */
	private void notifyUnbind(String name,Object o) {
		Iterator it= observers.iterator();
		while(it.hasNext()) 
			((NamespaceObserver)it.next()).unbindEvent(name,o);
		/* notify parent */
		if(parent != null)
			parent.notifyUnbind(this.name + pathSeparator + name,o);
	}
	
	
	/* (non-Javadoc)
	 * @see fr.lifl.parsing.Namespace#getParent()
	 */
	public Namespace getEnclosing() {
		return parent;
	}

  /**
   * @return Returns the name.
   */
  public String getName() {
    return name;
  }
  /* (non-Javadoc)
   * @see fr.lifl.parsing.Namespace#getAllBindings()
   */
  public Map getAllBindings() {
    return new HashMap(symbols);
  }
}

