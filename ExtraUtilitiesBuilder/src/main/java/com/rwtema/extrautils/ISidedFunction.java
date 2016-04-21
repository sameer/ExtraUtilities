// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface ISidedFunction<F, T>
{
    @SideOnly(Side.SERVER)
    T applyServer(final F p0);
    
    @SideOnly(Side.CLIENT)
    T applyClient(final F p0);
}


