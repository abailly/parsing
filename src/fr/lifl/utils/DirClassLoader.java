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
 * Created on Sep 2, 2004
 * 
 */
package fr.lifl.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * A class loader for loading class from a directory.
 * 
 * @author nono
 * @version $Id$
 */
public class DirClassLoader extends ClassLoader implements SimpleClassLoader {

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return dir.getName();
  }
  
  private File dir;

  /**
   * Creates a new loader.
   */
  public DirClassLoader(ClassLoader parent, File dir) {
    super(parent);
    if(!dir.isDirectory() || !dir.canRead())
      throw new IllegalArgumentException("Cannot access directory "+dir);
    this.dir = dir;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.ClassLoader#findClass(java.lang.String)
   */
  public Class findClass(String name) throws ClassNotFoundException {
    Class cls = findLoadedClass(name);
    if(cls != null)
      return cls;
    byte[] b = loadClassData(name);
    if (b == null)
      throw new ClassNotFoundException("Cannot find class " + name
          + " in directory " + dir.getName());
    return defineClass(name, b, 0, b.length);
  }

  /**
   * Loads a class from a class file within a jar.
   * 
   * @param className
   *          absolute name of the class (java notation)
   * @return the loaded class or null
   */
  public byte[] loadClassData(String className) {
    String fileName = className.replace('.', File.separatorChar) + ".class";
    try {
      File file  = new File(dir,fileName);
      if(!file.exists())
        return null;
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      new Pipe(bos, new FileInputStream(file)).pump();
      return bos.toByteArray();
    } catch (IOException e) {
      return null;
    }
  }

}

/*
 * $Log: DirClassLoader.java,v $
 * Revision 1.2  2004/09/07 10:02:57  bailly
 * cleared imports
 *
 * Revision 1.1  2004/09/06 12:09:23  bailly
 * added class loading management
 *
 */