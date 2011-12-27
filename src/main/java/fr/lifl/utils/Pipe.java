package fr.lifl.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author bailly
 * @version $Id$
 */
public class Pipe {

	InputStream is;
	OutputStream os;
	
	/**
	 * Constructor for Pipe.
	 */
	public Pipe(OutputStream os, InputStream ris) {
		this.os  = os;
		this.is = ris;

	}

	public void pump() throws IOException {
		byte[] buf = new byte[1024];
		int count = 0;
		while ((count = is.read(buf, 0, 1024)) > -1)
			os.write(buf, 0, count);
	} 

}
