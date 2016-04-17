// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.helper;

import java.util.Iterator;
import java.lang.ref.WeakReference;
import java.util.LinkedList;

public class WeakList<E> implements Iterable<E>
{
    LinkedList<WeakReference<E>> list;
    
    public WeakList() {
        this.list = new LinkedList<WeakReference<E>>();
    }
    
    public boolean add(final E a) {
        return a != null && this.list.add(new WeakReference<E>(a));
    }
    
    public void clear() {
        this.list.clear();
    }
    
    @Override
    public Iterator<E> iterator() {
        return new WeakIterator();
    }
    
    public class WeakIterator implements Iterator<E>
    {
        E next;
        Iterator<WeakReference<E>> iter;
        
        public WeakIterator() {
            this.next = null;
            this.loadNext();
        }
        
        private void loadNext() {
            this.next = null;
            while (this.iter.hasNext()) {
                this.next = this.iter.next().get();
                if (this.next != null) {
                    return;
                }
                this.iter.remove();
            }
        }
        
        @Override
        public boolean hasNext() {
            return this.next != null;
        }
        
        @Override
        public E next() {
            final E e = this.next;
            this.loadNext();
            return e;
        }
        
        @Override
        public void remove() {
            this.iter.remove();
        }
    }
}
