/*______________________________________________________________________________
 *
 * Copyright 2004 Arnaud Bailly - NORSYS/LIFL
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
 * Created on Sep 24, 2004
 *
 */
package fr.lifl.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This utility class is used to replace variables patterns of the form ${xxx}
 * occuring in strings.
 * <p>
 * The basic usage pattern of instances of this class is to replace occurences
 * of the string <code>${foo}</code> with some value: this is syntactic
 * variable substitution which occurs frequently in properties handling. Use the
 * method {@link #replaceVars(Map, String)}.
 * </p>
 * 
 * @author nono
 * @version $Id: VariableSubstitutor.java 134 2005-11-03 13:03:02Z
 *          /C=FR/ST=Nord/L=Lille/O=Norsys SA/OU=UE/CN=Arnaud
 *          Bailly/emailAddress=abailly@norsys.fr $
 */
public class VariableSubstitutor {

	/* pattern for simple variables matching */
	private Pattern namedVars = Pattern.compile("(\\$\\{([^${}]+)\\})");

	/**
	 * 
	 * Produce a new string by substituting variables names. This method scans
	 * string <code>src</code> for occurences of variables references and
	 * replaces them by their values from given map of <code>variables</code>.
	 * If a variable's value is not found in the map, then it <strong>is</strong>
	 * replaced by the empty string. This behavior allows - with some care- from
	 * the client to implement default substitutions.
	 * 
	 * @param variables
	 *            a map from String to String
	 * @param src
	 *            the source string to substitute
	 * @return the new string
	 */
	public String replaceVars(Map variables, String src) {
		StringBuffer sb = new StringBuffer();
		Matcher m = namedVars.matcher(src);
		while (m.find()) {
			String vn = m.group(2);
			String vv = (String) variables.get(vn);
			if (vv == null)
				vv = "";
			/* replace in string */
			m.appendReplacement(sb, vv);
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public Pattern getNamedVars() {
		return namedVars;
	}

	public void setNamedVars(Pattern namedVars) {
		this.namedVars = namedVars;
	}

}
