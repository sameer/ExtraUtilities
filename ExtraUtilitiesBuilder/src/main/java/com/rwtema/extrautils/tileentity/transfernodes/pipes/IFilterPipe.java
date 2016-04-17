// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.world.IBlockAccess;

public interface IFilterPipe
{
    IInventory getFilterInventory(final IBlockAccess p0, final int p1, final int p2, final int p3);
}
