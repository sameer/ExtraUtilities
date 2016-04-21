// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IBlockTooltip
{
    void addInformation(final ItemStack p0, final EntityPlayer p1, final List p2, final boolean p3);
}

