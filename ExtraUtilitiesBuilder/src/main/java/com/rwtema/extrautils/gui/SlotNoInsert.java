// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.gui;

import net.minecraft.item.ItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotNoInsert extends Slot
{
    public SlotNoInsert(final IInventory par1iInventory, final int par2, final int par3, final int par4) {
        super(par1iInventory, par2, par3, par4);
    }
    
    public boolean isItemValid(final ItemStack par1ItemStack) {
        return false;
    }
}

