// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
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


