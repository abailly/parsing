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
 * Created on 30 mai 2005
 *
 */
package fr.lifl.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * An abstract utility class for implementing read-only sets.
 * <p>
 * This class provides defautl implementations for various {@see java.util.Set}
 * methods to ease creation of read-only sets that can compute containment
 * methods on the fly.
 * 
 * @author nono
 * @version $Id$
 */
public abstract class ReadOnlySet implements Set {

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#size()
     */
    public int size() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#clear()
     */
    public void clear() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#isEmpty()
     */
    public boolean isEmpty() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#toArray()
     */
    public Object[] toArray() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#add(java.lang.Object)
     */
    public boolean add(Object o) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#contains(java.lang.Object)
     */
    public abstract boolean contains(Object o);

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#remove(java.lang.Object)
     */
    public boolean remove(Object o) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#addAll(java.util.Collection)
     */
    public boolean addAll(Collection c) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection c) {
        for (Iterator i = c.iterator(); i.hasNext();)
            if (!contains(i.next()))
                return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection c) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection c) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#iterator()
     */
    public Iterator iterator() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#toArray(java.lang.Object[])
     */
    public Object[] toArray(Object[] a) {
        return null;
    }

}
