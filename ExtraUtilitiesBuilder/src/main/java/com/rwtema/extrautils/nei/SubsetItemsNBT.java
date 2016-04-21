// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.nei;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import codechicken.nei.api.ItemFilter;

public class SubsetItemsNBT implements ItemFilter
{
    public Item item;
    
    public SubsetItemsNBT(final Item item) {
        this.item = item;
    }
    
    public boolean matches(final ItemStack item) {
        return item.hasTagCompound() && this.item.equals(item.getItem());
    }
}

