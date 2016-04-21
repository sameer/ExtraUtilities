// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.nei;

import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import codechicken.nei.api.ItemFilter;

public class SubsetBlockClass implements ItemFilter
{
    public Class<? extends Block> base;
    
    public SubsetBlockClass(final Class<? extends Block> base) {
        this.base = base;
    }
    
    public boolean matches(final ItemStack item) {
        return this.base.equals(Block.getBlockFromItem(item.getItem()).getClass());
    }
}

