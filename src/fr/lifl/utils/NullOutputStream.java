/*
 * Created on Jun 17, 2004
 * 
 * $Log: NullOutputStream.java,v $
 * Revision 1.1  2004/06/23 13:42:40  bailly
 * transfert depuis FIDL des utilitaires
 *
 */
package fr.lifl.utils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author nono
 * @version $Id$
 */
public class NullOutputStream extends OutputStream {

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	public void write(int b) throws IOException {
		/* NOP */
	}

	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	public void write(byte[] b, int off, int len) throws IOException {
		/* NOP */
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[])
	 */
	public void write(byte[] b) throws IOException {
		/* NOP */
	}

}
