// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;

public class SlotRestrictive extends Slot
{
    ItemStack item;
    
    public SlotRestrictive(final IInventory par1iInventory, final int par2, final int par3, final int par4, final ItemStack item) {
        super(par1iInventory, par2, par3, par4);
        this.item = null;
        this.item = item;
    }
    
    public boolean isItemValid(final ItemStack par1ItemStack) {
        return this.item != null && par1ItemStack != null && par1ItemStack.getItem() == this.item.getItem() && (par1ItemStack.getItemDamage() == this.item.getItemDamage() || this.item.getItemDamage() == 32767);
    }
}

