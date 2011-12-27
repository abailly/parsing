/*
 * Created on 25 f?vr. 2004
 * 
 * $Log: Namespace.java,v $
 * Revision 1.3  2004/09/07 10:02:57  bailly
 * cleared imports
 *
 * Revision 1.2  2004/06/30 14:36:18  bailly
 * ajout structures de Parser g?n?rique : evenements, listeners, namespace,
 *
 * Revision 1.1  2004/02/27 16:40:27  bailly
 * initial check-in
 *
 */
package fr.lifl.parsing;

import java.util.Map;

/**
 * An abstract interface for binding and resolving symbols 
 * during parsing. 
 * <p>
 * This interface is meant to represent a hierarchy of symbol 
 * tables on which several common operations are defined. 
 * Basically, a Namespace is simply a map from names - ie. String - to objects 
 * defined during parsing process. 
 * 
 * @author bailly
 * @version $Id$
 */
public interface Namespace {

	/**
	 * Binds a name to an object in this namespace
	 * <p>
	 * This method binds the string <code>name</code> to 
	 * the object <code>definition</code> in this namespace object so
	 * that later invocations of @see{resolve(String)} on it with <code>name</code>
	 * as parameter returns object <code>definition</code>. This method 
	 * returns nothing but throws a @see{SymbolException} if 
	 * <code>name</code> is already bound to something in this
	 * Namespace.
	 * 
	 * @param name the symbol to bind
	 * @param definition the object bound to the symbol
	 * @throws SymbolException  if <code>name</code> is already
	 * used in this Namespace.
	 */
	public void bind(String name, Object definition)
		throws SymbolException;

	/**
	 * Binds or rebinds a name to an object in this Namespace
	 * <p>
	 * This method, like @see{bind(String,Object)} binds <code>name</code> 
	 * to <code>definition</code> in this namespace, with the additional 
	 * property that, if <code>name</code> is already bound, no exception
	 * is thrown and this method returns the old binding. The object <code>definition</code>
	 * is always bound after an invocation of rebind.
	 * 
	 * @param name the symbol to bind
	 * @param definition the object bound 
	 * @return previous binding for <code>name</code> or null
	 * @exception SymbolException if something goes wrong in rebinding
	 */
	public Object rebind(String name, Object definition) throws SymbolException;

	/**
	 * Forwarding declaration for a name
	 * <p>
	 * This method requests a forward declaration for symbol <code>name</code>.
	 * A partial <code>definition</code> object is bound to the symbol and 
	 * and later invocations of @see{bind(String,Object)} will fail with 
	 * an exception, unless given definition parameter is the same object
	 * (in the sense of operator <code>==</code>) as previously passed
	 * to <code>forward</code>. 
	 * <br/>
	 * This method is intended for parsing process where partial 
	 * declarations of names are allowed or when declaration order is
	 * not relevant but it is needed for compilation.
	 *   
	 * @param name the symbol being reserved
	 * @param definition the forward definition to bind
	 * @throws SymbolException if <code>name</code> is already
	 * used in this Namespace.
	 */
	public void forward(String name, Object definition)
		throws SymbolException;

	/**
	 * Tries to resolve a symbol against this namespace
	 * <p>
	 * This method tries to resolve the symbol <code>name</code> in
	 * this namespace. The resolving process is implementation dependant 
	 * but basic contract is that if any of @see{bind(String,Object)}, @see{rebind(String,Object)},
	 * of @see{forward(String,Object)} has been called before with symbol 
	 * <code>name</code>, this method should succeed returning the bound 
	 * definition (but see @see{unbind(String)}).
	 * <br/>
	 * This method returns the bound object if found or throws an exception
	 * @see{SymbolException}. 
	 *    
	 * @param name the symbol being resolved
	 * @return the bound object if found
	 * @throws SymbolException if <code>name</code>is not bound 
	 * in this namespace
	 */
	public Object resolve(String name) throws SymbolException;

	/**
	 * Tries to remove a binding in this namespace
	 * <p>
	 * This method tries to remove the mapping from <code>name</code>
	 * to an object in this namespace. If <code>name</code> is bound 
	 * the removed definition is returned after being unbound and later
	 * invocations of @see{bind(String,Object}) with this symbol will succeed. 
	 * <br/>
	 * If the symbol is not bound, an exception @see{SymbolException} 
	 * is thrown.
	 * 
	 * @param name the symbol to unbind
	 * @return the bound object if it exists
	 * @throws SymbolException if <code>name</code>is not bound 
	 * in this namespace
	 */
	public Object unbind(String name) throws SymbolException;

	/**
	 * Returns the enclosing Namespace this namespace is defined in.
	 * <p>
	 * This method allows upward traversal of namespaces. A toplevel 
	 * namespace or a non nested namespace has no parent hence this method may return null.
	 * @return
	 */
	public Namespace getEnclosing();
	
	/**
	 * Returns the name this Namespace is known as.
	 * @return this namespace's name
	 */
	public String getName();
	
	/**
	 * Returns a Map object from String to Object This
	 * is a static view of all the bindings in this Namespace.
	 * 
	 * @return a Map from strings to Object
	 */
	public Map getAllBindings();
	
}
