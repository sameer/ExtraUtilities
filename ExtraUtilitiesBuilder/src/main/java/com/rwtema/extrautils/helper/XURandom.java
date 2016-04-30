// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.helper;

import java.util.Random;

public class XURandom extends Random
{
    private static final XURandom INSTANCE;
    private long[] rng;
    private int i;
    
    public static XURandom getInstance() {
        return XURandom.INSTANCE;
    }
    
    private synchronized void fillRNG(long seed) {
        this.i = 0;
        seed = ((seed ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL);
        (this.rng = new long[16])[0] = seed;
        for (int i = 1; i < this.rng.length; ++i) {
            seed ^= seed >> 12;
            seed ^= seed << 25;
            seed ^= seed >> 27;
            this.rng[i] = seed * 2685821657736338717L;
        }
    }
    
    public XURandom() {
        this.rng = new long[16];
        if (this.rng[0] == 0L) {
            this.setSeed(new Random().nextLong());
        }
    }
    
    public XURandom(final long seed) {
        super(seed);
        this.rng = new long[16];
    }
    
    @Override
    public synchronized void setSeed(final long seed) {
        super.setSeed(seed);
        this.i = 0;
        this.fillRNG(seed);
    }
    
    public synchronized void setRNGArray(final long... rngArray) {
        System.arraycopy(rngArray, this.i = 0, this.rng, 0, this.rng.length);
    }
    
    public int next(final int nbits) {
        final long x = this.nextLong() & (1L << nbits) - 1L;
        return (int)x;
    }
    
    @Override
    public synchronized long nextLong() {
        if (this.rng == null) {
            return 0L;
        }
        long a = this.rng[this.i];
        this.i = (this.i + 1 & 0xF);
        long b = this.rng[this.i];
        b ^= b << 31;
        b ^= b >> 11;
        a ^= a >> 30;
        this.rng[this.i] = (a ^ b);
        return this.rng[this.i] * 1181783497276652981L;
    }
    
    static {
        INSTANCE = new XURandom();
    }
}


