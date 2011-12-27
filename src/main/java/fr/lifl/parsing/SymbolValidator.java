/*
 * Created on 25 févr. 2004
 * 
 * $Log: SymbolValidator.java,v $
 * Revision 1.1  2004/02/27 16:40:27  bailly
 * initial check-in
 *
 */
package fr.lifl.parsing;

/**
 * An interface used by namespaces to validate symbols
 * against some user defined rule
 * <p>
 * This interface's only method @see{validate(String)} is
 * called by @see{BasicNamespace} objects when trying to
 * bind a symbol in this namespace. This interface is provided 
 * so that users can provide their own symbol rules.
 * 
 * @author bailly
 * @version $Id$
 */
public interface SymbolValidator {


	/**
	 * Validate symbol <code>name</code> against this 
	 * validator rules 
	 * <p>
	 * This method returns <code>true</code> if name is OK w.r.t. 
	 * the internal rules of this validator, false otherwise. It 
	 * is the responsibility of the caller to use this information 
	 * wisely.
	 * 
	 * @param name the symbol verified
	 * @return true if symbol is syntactically or semantically valid.
	 */
	public boolean validate(String name);
	
}
