// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import net.minecraft.util.IIcon;
import net.minecraft.item.ItemStack;

public interface IItemMultiTransparency
{
    int numIcons(final ItemStack p0);
    
    IIcon getIconForTransparentRender(final ItemStack p0, final int p1);
    
    float getIconTransparency(final ItemStack p0, final int p1);
}
