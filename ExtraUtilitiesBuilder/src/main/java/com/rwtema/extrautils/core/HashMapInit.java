// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.core;

import java.util.HashMap;

public class HashMapInit<K, V> extends HashMap<K, V>
{
    Class<? extends V> clazz;
    
    public HashMapInit(final Class<? extends V> clazz) {
        this.clazz = clazz;
    }
    
    public V getOrInit(final K key) {
        V v = super.get(key);
        if (v == null) {
            try {
                v = (V)this.clazz.newInstance();
            }
            catch (InstantiationException ignore) {}
            catch (IllegalAccessException ex) {}
        }
        return v;
    }
}
