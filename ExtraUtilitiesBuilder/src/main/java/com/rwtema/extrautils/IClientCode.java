// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IClientCode
{
    @SideOnly(Side.CLIENT)
    void exectuteClientCode();
}

