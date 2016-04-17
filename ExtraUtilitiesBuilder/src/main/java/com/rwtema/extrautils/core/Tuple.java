// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.core;

import java.util.Objects;

public final class Tuple<U, V>
{
    private final U a;
    private final V b;
    
    public Tuple(final U a, final V b) {
        this.a = a;
        this.b = b;
    }
    
    public U getA() {
        return this.a;
    }
    
    public V getB() {
        return this.b;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Tuple tuple = (Tuple)o;
        return Objects.equals(this.a, tuple.a) && Objects.equals(this.b, tuple.b);
    }
    
    @Override
    public int hashCode() {
        int result = (this.a != null) ? this.a.hashCode() : 0;
        result = 31 * result + ((this.b != null) ? this.b.hashCode() : 0);
        return result;
    }
}
