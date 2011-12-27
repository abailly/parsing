/*
 * Created on 25 févr. 2004
 * 
 * $Log: NamespaceObserver.java,v $
 * Revision 1.1  2004/02/27 16:40:27  bailly
 * initial check-in
 *
 */
package fr.lifl.parsing;

/**
 * An interface allowing objects to be notified of events 
 * affecting a BasicNamespace.
 * <p>
 * This interface should be implemented by any object wishing to 
 * be notified of events modifying the structure of a Namespace.
 * A BasicNamespace calls observers registered using @see{BasicNamespace.addNamespaceObserver(NamespaceObserver)}
 * whenever one of binding, rebinding, unbinding occurs. 
 * Observers are also notified of events affecting nested namespaces. 
 * 
 * @author bailly
 * @version $Id$
 */
public interface NamespaceObserver {

	/**
	 * This method is called whenever an object <code>bound</code> is
	 * bound in a namespace or nested namespace. The given <code>name</code>
	 * is a path-qualified name relative to the namespace where this
	 * observer was registered.
	 *  
	 * @param name a path naming the bound object
	 * @param bound the object bound 
	 */
	public void bindEvent(String name, Object bound);
	
	
	/**
	 * This method is called whenever an object <code>bound</code> is
	 * unbound in a namespace or nested namespace. The given <code>name</code>
	 * is a path-qualified name relative to the namespace where this
	 * observer was registered.
	 * <p>
	 * This method is called <b>after</b> @see{bindEvent(String,Object)} when 
	 * this unbinding occurs thorugh @see{Namespace.rebind(String,Object)}.
	 *  
	 * @param name a path naming the unbound object
	 * @param bound the object unbound 
	 */
	public void unbindEvent(String name,Object bound);
}
