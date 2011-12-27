/**
 * 
 */
package fr.lifl.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A class for temporary directory management. This class allows user to create
 * a directory, write files and directories in it, then delete everything.
 * 
 * @author nono
 * 
 */
public class TemporaryFS {

  private File root;

  /**
   * Create the temporary FS with given name.
   * 
   * @param string
   *          the root directory. Must be a relative or absolute path.
   * 
   */
  public TemporaryFS(String string) {
    this(new File(string));
  }

  /**
   * Create the temporary FS in given dir. If root does not exist, it is
   * created. If it exists but is not file, an error is thrown.
   * 
   * @param root
   *          a File object, which may or may not exists.
   */
  public TemporaryFS(File root) {
    this.root = root;
    if (!root.exists())
      root.mkdirs();
    if (root.isFile())
      throw new IllegalArgumentException(root
          + " exists and is not a directory");
  }

  /**
   * Remove this temporary directory and everythig in it.
   * 
   * 
   */
  public void clean() {
    delete(root);
  }

  private void delete(File temp2) {
    if (!temp2.isDirectory())
      temp2.delete();
    else {
      for (File f : temp2.listFiles())
        delete(f);
      temp2.delete();
    }
  }

  /**
   * Get the root directory of this temporary.
   * 
   * @return a directory.
   */
  public File root() {
    return root;
  }

  /**
   * Adds the given resource from classpath to the temporary FS. This method
   * replicates directory structure from path.
   * 
   * @param resourcePath
   *          a slash separated resource from the classpath.
   * @throws IOException
   */
  public File copy(String resourcePath) throws IOException {
    InputStream is = getClass().getClassLoader().getResourceAsStream(
        resourcePath);
    // create directory structure
    File f = root;
    int idx = resourcePath.indexOf('/');
    while (idx >= 0) {
      if (idx == 0) {
        if (resourcePath.charAt(0) == '/')
          resourcePath = resourcePath.substring(1);
      } else {
        assert idx > 0;
        String path = resourcePath.substring(0, idx);
        // create directory
        f = new File(f, path);
        if (!f.exists() && !f.mkdir())
          throw new IOException("Cannot create directory " + f);
        // recurse
        resourcePath = resourcePath.substring(idx + 1);
      }
      idx = resourcePath.indexOf('/');
    }
    // copy resource content
    assert f.exists() && f.isDirectory();
    if (resourcePath.equals(""))
      return f;
    if (is == null)
      throw new IOException("cannot find resource " + resourcePath);
    f = new File(f, resourcePath);
    FileOutputStream fos = new FileOutputStream(f);
    byte[] data = new byte[1024];
    int ln = 0;
    while ((ln = is.read(data, 0, 1024)) >= 0) {
      fos.write(data, 0, ln);
    }
    fos.flush();
    fos.close();
    return f;
  }

}
