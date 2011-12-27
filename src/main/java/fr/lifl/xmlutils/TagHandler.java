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
 * Created on 6 oct. 2005
 * Author: Arnaud Bailly
 */
package fr.lifl.xmlutils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * An interface for handling tags in XML files. Inmplementations of this
 * interface implements a <em>Command</em> pattern for handling XML tags.
 * 
 * @author nono
 * @version $Id: TagHandler.java 231 2006-02-07 09:10:03Z /C=FR/ST=Nord/L=Lille/O=Norsys SA/OU=UE/CN=Arnaud Bailly/emailAddress=abailly@norsys.fr $
 */
public interface TagHandler {
    
    /**
     * Handles the start of a tag.
     * 
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException;

    /**
     * Handles the closing of an tag.
     * 
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    void endElement(String uri, String localName, String qName) throws SAXException;
}
