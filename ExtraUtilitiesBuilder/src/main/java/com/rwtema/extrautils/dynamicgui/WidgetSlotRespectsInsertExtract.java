// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.dynamicgui;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;

public class WidgetSlotRespectsInsertExtract extends WidgetSlot
{
    public WidgetSlotRespectsInsertExtract(final ISidedInventory inv, final int slot, final int x, final int y) {
        super((IInventory)inv, slot, x, y);
    }
    
    @Override
    public boolean canTakeStack(final EntityPlayer par1EntityPlayer) {
        return this.getStack() != null && ((ISidedInventory)this.inventory).canExtractItem(this.slotNumber, this.getStack(), 0);
    }
    
    @Override
    public boolean isItemValid(final ItemStack par1ItemStack) {
        return !this.getHasStack() && super.isItemValid(par1ItemStack) && ((ISidedInventory)this.inventory).canInsertItem(this.slotNumber, par1ItemStack, 0);
    }
}
