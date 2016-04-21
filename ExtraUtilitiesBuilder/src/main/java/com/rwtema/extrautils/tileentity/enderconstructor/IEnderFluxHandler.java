// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.enderconstructor;

public interface IEnderFluxHandler
{
    boolean isActive();
    
    int recieveEnergy(final int p0, final Transfer p1);
    
    float getAmountRequested();
}

