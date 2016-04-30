// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.gui;

import com.rwtema.extrautils.tileentity.TileEntityTrashCan;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotTrash extends Slot
{
    public SlotTrash(final IInventory par1iInventory, final int par2, final int par3, final int par4) {
        super(par1iInventory, par2, par3, par4);
    }
    
    public boolean isItemValid(final ItemStack p_75214_1_) {
        return !this.getHasStack();
    }
    
    public void putStack(final ItemStack par1ItemStack) {
        if (par1ItemStack != null) {
            for (int i = 0; i < 64; ++i) {
                if (this.inventory.getStackInSlot(i) == null) {
                    TileEntityTrashCan.instantAdd = true;
                    this.inventory.setInventorySlotContents(i, par1ItemStack);
                    TileEntityTrashCan.instantAdd = false;
                    return;
                }
            }
        }
        this.inventory.setInventorySlotContents(this.getSlotIndex(), par1ItemStack);
        this.onSlotChanged();
    }
}


