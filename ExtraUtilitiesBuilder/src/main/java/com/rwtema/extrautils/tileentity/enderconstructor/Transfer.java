// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
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


