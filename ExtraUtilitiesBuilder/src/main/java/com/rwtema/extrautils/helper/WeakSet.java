// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.helper;

import java.util.Iterator;
import java.util.Collection;
import java.util.WeakHashMap;
import java.util.Set;
import java.util.AbstractSet;

public class WeakSet<E> extends AbstractSet<E> implements Set<E>
{
    private transient WeakHashMap<E, Object> map;
    private static final Object PRESENT;
    
    public WeakSet() {
        this.map = new WeakHashMap<E, Object>();
    }
    
    public WeakSet(final Collection<? extends E> c) {
        this.map = new WeakHashMap<E, Object>(Math.max((int)(c.size() / 0.75f) + 1, 16));
        this.addAll(c);
    }
    
    public WeakSet(final int initialCapacity, final float loadFactor) {
        this.map = new WeakHashMap<E, Object>(initialCapacity, loadFactor);
    }
    
    public WeakSet(final int initialCapacity) {
        this.map = new WeakHashMap<E, Object>(initialCapacity);
    }
    
    WeakSet(final int initialCapacity, final float loadFactor, final boolean dummy) {
        this.map = new WeakHashMap<E, Object>(initialCapacity, loadFactor);
    }
    
    @Override
    public Iterator<E> iterator() {
        return this.map.keySet().iterator();
    }
    
    @Override
    public int size() {
        return this.map.size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.map.containsKey(o);
    }
    
    @Override
    public boolean add(final E e) {
        return this.map.put(e, WeakSet.PRESENT) == null;
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.map.remove(o) == WeakSet.PRESENT;
    }
    
    @Override
    public void clear() {
        this.map.clear();
    }
    
    static {
        PRESENT = new Object();
    }
}

