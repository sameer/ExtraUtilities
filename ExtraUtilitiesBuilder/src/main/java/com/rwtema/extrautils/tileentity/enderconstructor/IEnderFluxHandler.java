// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.enderconstructor;

public interface IEnderFluxHandler
{
    boolean isActive();
    
    int recieveEnergy(final int p0, final Transfer p1);
    
    float getAmountRequested();
}


