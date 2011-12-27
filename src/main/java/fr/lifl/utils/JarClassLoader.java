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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A class loader for loading class from jar files.
 * 
 * @author nono
 * @version $Id$
 */
public class JarClassLoader extends ClassLoader implements SimpleClassLoader {

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return jarfile.getName();
  }

  private ZipFile jarfile;

  /**
   * Creates a new loader.
   */
  public JarClassLoader(ClassLoader parent, ZipFile jarfile) {
    super(parent);
    this.jarfile = jarfile;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.ClassLoader#findClass(java.lang.String)
   */
  public Class findClass(String name) throws ClassNotFoundException {
    Class cls = findLoadedClass(name);
    if (cls != null)
      return cls;
    byte[] b = loadClassData(name);
    if (b == null)
      throw new ClassNotFoundException("Cannot find class " + name
          + " in archive " + jarfile.getName());
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
      ZipEntry entry = jarfile.getEntry(fileName);
      if (entry == null)
        return null;
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      new Pipe(bos, jarfile.getInputStream(entry)).pump();
      return bos.toByteArray();
    } catch (IOException e) {
      return null;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.ClassLoader#getResourceAsStream(java.lang.String)
   */
  public InputStream getResourceAsStream(String name) {
    try {
      ZipEntry entry = jarfile.getEntry(name);
      if (entry == null)
        return null;
      return jarfile.getInputStream(entry);
    } catch (IOException e) {
      return null;
    }
  }
}

/*
 * $Log: JarClassLoader.java,v $ Revision 1.1 2004/09/06 12:09:23 bailly added
 * class loading management
 * 
 */