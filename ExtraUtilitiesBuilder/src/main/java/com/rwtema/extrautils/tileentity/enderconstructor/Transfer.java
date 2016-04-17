// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.enderconstructor;

public enum Transfer
{
    PERFORM(false), 
    SIMULATE(true);
    
    boolean simulate;
    
    private Transfer(final boolean simulate) {
        this.simulate = simulate;
    }
}
