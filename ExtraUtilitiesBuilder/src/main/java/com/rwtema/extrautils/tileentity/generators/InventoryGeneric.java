// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.generators;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.inventory.InventoryBasic;

public class InventoryGeneric extends InventoryBasic
{
    public InventoryGeneric(final String par1Str, final boolean par2, final int par3) {
        super(par1Str, par2, par3);
    }
    
    public void writeToNBT(final NBTTagCompound nbt) {
        final NBTTagCompound tag = new NBTTagCompound();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            final ItemStack item = this.getStackInSlot(i);
            if (item != null) {
                final NBTTagCompound itemtag = new NBTTagCompound();
                item.writeToNBT(itemtag);
                tag.setTag("item_" + i, (NBTBase)itemtag);
            }
        }
        nbt.setTag("items", (NBTBase)tag);
    }
    
    public void readFromNBT(final NBTTagCompound nbt) {
        if (!nbt.hasKey("items")) {
            for (int i = 0; i < this.getSizeInventory(); ++i) {
                this.setInventorySlotContents(i, (ItemStack)null);
            }
        }
        else {
            final NBTTagCompound tag = nbt.getCompoundTag("items");
            for (int j = 0; j < this.getSizeInventory(); ++j) {
                if (tag.hasKey("item_" + j)) {
                    this.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(tag.getCompoundTag("item_" + j)));
                }
                else {
                    this.setInventorySlotContents(j, (ItemStack)null);
                }
            }
        }
    }
}
