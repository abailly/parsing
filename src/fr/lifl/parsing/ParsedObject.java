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
* Created on Jul 23, 2004
* 
*/
package fr.lifl.parsing;

/**
 * An interface for parsed objects created by a {@link Parser} during 
 * its operations. 
 * <p>
 * This interface allows attachment of <b>tags</b> to parsed objects and thier
 * retrieval by other tools. 
 * 
 * @author nono
 * @version $Id$
 */
public interface ParsedObject {

    /**
     * Retrieves the tag identified by the  given string from this parser object.
     * 
     * @param name name of tag
     * @return an object - may be null
     */
    public Object getTag(String name);

    /**
     * Defines a tag for this parsed object. If tag already exists with same name,
     * it is implementation dependant whether this method will replace existing tag,
     * ignore the new tag or defines multiple tags with same name.
     * 
     * @param name the name of tag to set
     * @param tag the value of tag to set
     */
    public void setTag(String name,Object tag);
    
}

/* 
 * $Log: ParsedObject.java,v $
 * Revision 1.1  2004/07/23 14:37:06  bailly
 * added ParsedObject
 *
*/