// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import net.minecraft.world.IBlockAccess;

public interface IMultiBoxBlock
{
    void prepareForRender(final String p0);
    
    BoxModel getWorldModel(final IBlockAccess p0, final int p1, final int p2, final int p3);
    
    BoxModel getInventoryModel(final int p0);
}
