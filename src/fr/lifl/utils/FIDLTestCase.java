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
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on 25 avr. 2005
 *
 */
package fr.lifl.utils;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * @author nono
 * @version $Id$
 */
public abstract class FIDLTestCase extends TestCase {

    /**
     * @param arg0
     */
    public FIDLTestCase(String arg0) {
        super(arg0);
    }

    public void assertEquals(int[] exp, int[] real) {
        if (exp == null)
            if (real == null)
                return;
            else
                throw new AssertionFailedError("Expected null array but found "
                        + printArray(real));
        if (real == null)
            if (exp == null)
                return;
            else
                throw new AssertionFailedError("Expected " + printArray(exp)
                        + " but found null array");
        if (exp.length != real.length)
            throw new AssertionFailedError("Incompatible length: Expected "
                    + exp.length + " but found " + real.length);
        int l = exp.length;
        for (int i = 0; i < l; i++)
            if (exp[i] != real[i])
                throw new AssertionFailedError("Expected " + printArray(exp)
                        + " but found " + printArray(real));
    
    }

    public void assertEquals(double[] exp, double[] real) {
        if (exp == null)
            if (real == null)
                return;
            else
                throw new AssertionFailedError("Expected null array but found "
                        + printArray(real));
        if (real == null)
            if (exp == null)
                return;
            else
                throw new AssertionFailedError("Expected " + printArray(exp)
                        + " but found null array");
        if (exp.length != real.length)
            throw new AssertionFailedError("Incompatible length: Expected "
                    + exp.length + " but found " + real.length);
        int l = exp.length;
        for (int i = 0; i < l; i++)
            if (exp[i] != real[i])
                throw new AssertionFailedError("Expected " + printArray(exp)
                        + " but found " + printArray(real));
    
    }

    /**
     * @param real
     * @return
     */
    public String printArray(int[] real) {
        StringBuffer sb = new StringBuffer();
        sb.append('[');
        int l = real.length;
        for (int i = 0; i < l; i++) {
            sb.append(real[i]);
            if (i < l - 1)
                sb.append(',');
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * @param real
     * @return
     */
    public String printArray(double[] real) {
        StringBuffer sb = new StringBuffer();
        sb.append('[');
        int l = real.length;
        for (int i = 0; i < l; i++) {
            sb.append(real[i]);
            if (i < l - 1)
                sb.append(',');
        }
        sb.append(']');
        return sb.toString();
    }
    
    
}
