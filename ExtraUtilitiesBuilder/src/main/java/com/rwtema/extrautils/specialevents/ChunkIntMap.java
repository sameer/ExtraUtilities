// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.specialevents;

import gnu.trove.map.hash.TLongIntHashMap;

public class ChunkIntMap extends TLongIntHashMap
{
    public static long getKey(final int a, final int b) {
        return a | b << 32;
    }
    
    public int put(final int a, final int b, final int value) {
        return this.put(getKey(a, b), value);
    }
    
    public int get(final int a, final int b) {
        return this.get(getKey(a, b));
    }
}

