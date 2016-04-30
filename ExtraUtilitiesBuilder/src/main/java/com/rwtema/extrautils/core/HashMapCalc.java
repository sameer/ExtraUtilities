// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.core;

import com.google.common.base.Function;
import java.util.HashMap;

public abstract class HashMapCalc<K, V> extends HashMap<K, V>
{
    Function<K, V> function;
    
    protected HashMapCalc(final Function<K, V> function) {
        this.function = function;
    }
    
    public V getCalc(final K key) {
        if (!this.containsKey(key)) {
            final V calcEntry = (V)this.function.apply(key);
            this.put(key, calcEntry);
        }
        return this.get(key);
    }
}


