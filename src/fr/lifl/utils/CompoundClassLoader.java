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
* Created on Sep 3, 2004
* 
*/
package fr.lifl.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author nono
 * @version $Id$
 */
public class CompoundClassLoader extends ClassLoader implements SimpleClassLoader {


  /* one loader for each jar file - to solve interarchive dependencies */
  private Map /* < String, ClassLoader > */loaders = new HashMap();

  /**
   * 
   */
  public CompoundClassLoader() {
    super();
  }

  /**
   * @param parent
   */
  public CompoundClassLoader(ClassLoader parent) {
    super(parent);
  }

  public Class findClass(String name) throws ClassNotFoundException {
    Iterator it = loaders.values().iterator();
    while (it.hasNext()) {
      SimpleClassLoader cl = (SimpleClassLoader) it.next();
      try {
        Class cls = cl.findClass(name);
        return cls;
      } catch (ClassNotFoundException e) {
      }
    }
    throw new ClassNotFoundException(name);
  }

  
  /**
   * @return Returns the loaders.
   */
  public Map getLoaders() {
    return loaders;
  }
  
  public void addLoader(String name, SimpleClassLoader loader) {
    loaders.put(name,loader);
  }

  /**
   * @param key
   */
  public void removeLoader(String key) {
    loaders.remove(key);
  }
}

/* 
 * $Log: CompoundClassLoader.java,v $
 * Revision 1.1  2004/09/06 12:09:23  bailly
 * added class loading management
 *
*/