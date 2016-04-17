// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.specialevents;

import com.google.common.base.Function;
import gnu.trove.map.hash.TLongObjectHashMap;

public class ChunkObjectMap<V>
{
    TLongObjectHashMap<V> map;
    Function<Void, V> init;
    
    public ChunkObjectMap() {
        this.map = (TLongObjectHashMap<V>)new TLongObjectHashMap();
        this.init = null;
    }
    
    public static long getKey(final int a, final int b) {
        return a | b << 32;
    }
    
    public V putChunk(final int x, final int z, final V value) {
        return (V)this.map.put(getKey(x, z), (Object)value);
    }
    
    public V getChunk(final int x, final int z) {
        if (this.init == null) {
            return (V)this.map.get(getKey(x, z));
        }
        final long key = getKey(x, z);
        V v = (V)this.map.get(key);
        if (v == null) {
            v = (V)this.init.apply((Object)null);
            this.map.put(key, (Object)v);
        }
        return v;
    }
}
