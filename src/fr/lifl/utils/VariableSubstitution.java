/*______________________________________________________________________________
*
* Copyright 2005 Arnaud Bailly - NORSYS/LIFL
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
* Created on 2 janv. 2005
* 
*/
package fr.lifl.utils;

/**
 * An interface providing the ability to substitute variables in a 
 * string.
 * This interface allows substitution of strings denoting variables 
 * by their value in a given string.
 * 
 * @author nono
 * @version $Id: VariableSubstitution.java,v 1.1 2005/04/25 11:17:21 bailly Exp $
 */
public interface VariableSubstitution {

  /**
   * Process variables occurence and substitute variables.
   * 
   * @param from the String where substitution should occur.
   * @return a new String with variables occurence replaced by their value.
   */
  public String substitute(String from);
  
}

/* 
 * $Log: VariableSubstitution.java,v $
 * Revision 1.1  2005/04/25 11:17:21  bailly
 * run commit
 *
*/