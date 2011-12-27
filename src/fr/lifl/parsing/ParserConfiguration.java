/*______________________________________________________________________________
*
* Copyright 2003 Arnaud Bailly - NORSYS/LIFL
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
* Created on Jul 20, 2004
* 
*/
package fr.lifl.parsing;

/**
 * This is a simple interface that can be used to pass implementation 
 * speicific informations to a Parser in an abstract way.
 * Parser configuration is done via instances of this interface that
 * acts like maps from string to objects. Each property should be assigned
 * a unique name within a specific namespace URI (like XML namespace) and they 
 * can then be retrieved using the {@see getProperty(String,String)} method.<br />
 * Storing is done in an implementaiton specific way.
 * 
 * @author nono
 * @version $Id$
 */
public interface ParserConfiguration {

  /**
   * Gets a property with given name within given namespace URI.
   * 
   * @param ns the namespace URI for this property
   * @param name the name of this property
   * @return an implementation specific object or null
   */
  public Object getProperty(String ns, String name);
  
}

/* 
 * $Log: ParserConfiguration.java,v $
 * Revision 1.2  2005/04/25 11:17:21  bailly
 * run commit
 *
 * Revision 1.1  2004/07/23 07:10:21  bailly
 * added ParserConfiguration and events for start and end of parsing
 * modified Parser API accordingly
 *
*/