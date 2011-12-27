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
 * Created on 27 mai 2005
 *
 */
package fr.lifl.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * An abstract implementation of Map.
 * <p>
 * This implementation of Map is meant to use only the {@see #get(Object)} 
 * method which is declared abstract. This is like a mathematical map, that is a total
 * function. This explains why the {@see containsKey(Object)} method always returns
 * true.
 * 
 * @author nono
 * @version $Id$
 */
public abstract class ReadOnlyMap implements Map {

    /* (non-Javadoc)
     * @see java.util.Map#size()
     */
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see java.util.Map#clear()
     */
    public void clear() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object key) {
        return true;
    }

    /* (non-Javadoc)
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(Object value) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see java.util.Map#values()
     */
    public Collection values() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map t) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.util.Map#entrySet()
     */
    public Set entrySet() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.util.Map#keySet()
     */
    public Set keySet() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.util.Map#remove(java.lang.Object)
     */
    public Object remove(Object key) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Object put(Object key, Object value) {
        // TODO Auto-generated method stub
        return null;
    }

}
