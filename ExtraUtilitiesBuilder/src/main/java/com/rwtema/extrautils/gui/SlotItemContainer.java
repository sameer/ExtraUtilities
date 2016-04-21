// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.gui;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotItemContainer extends Slot
{
    static boolean checking;
    static boolean iterating;
    private static IInventory fakeInv;
    int filterIndex;
    ItemStack curStack;
    private IInventory filterInv;
    
    public SlotItemContainer(final IInventory par1iInventory, final int slot, final int x, final int y, final int filterIndex) {
        super(SlotItemContainer.fakeInv, slot, x, y);
        this.filterIndex = -1;
        this.curStack = null;
        this.filterInv = par1iInventory;
        this.filterIndex = filterIndex;
    }
    
    public ItemStack getStack() {
        final ItemStack filter = this.filterInv.getStackInSlot(this.filterIndex);
        if (filter != null && filter.getTagCompound() != null && filter.getTagCompound().hasKey("items_" + this.getSlotIndex())) {
            if (!SlotItemContainer.checking) {
                return this.curStack = ItemStack.loadItemStackFromNBT(filter.getTagCompound().getCompoundTag("items_" + this.getSlotIndex()));
            }
            return ItemStack.loadItemStackFromNBT(filter.getTagCompound().getCompoundTag("items_" + this.getSlotIndex()));
        }
        else {
            if (!SlotItemContainer.checking) {
                return this.curStack = null;
            }
            return null;
        }
    }
    
    public ItemStack decrStackSize(final int par1) {
        final ItemStack curItem = this.getStack();
        if (curItem == null) {
            return null;
        }
        if (curItem.stackSize <= par1) {
            final ItemStack itemstack = curItem;
            this.putStack(null);
            return itemstack;
        }
        final ItemStack itemstack = curItem.splitStack(par1);
        if (curItem.stackSize == 0) {
            this.putStack(null);
        }
        return itemstack;
    }
    
    public boolean getHasStack() {
        return this.getStack() != null;
    }
    
    public void putStack(final ItemStack par1ItemStack) {
        final ItemStack filter = this.filterInv.getStackInSlot(this.filterIndex);
        if (filter == null) {
            return;
        }
        NBTTagCompound tags = filter.getTagCompound();
        if (par1ItemStack != null) {
            if (tags == null) {
                tags = new NBTTagCompound();
            }
            if (tags.hasKey("items_" + this.getSlotIndex())) {
                tags.removeTag("items_" + this.getSlotIndex());
            }
            final NBTTagCompound itemTags = new NBTTagCompound();
            par1ItemStack.writeToNBT(itemTags);
            tags.setTag("items_" + this.getSlotIndex(), (NBTBase)itemTags);
            filter.setTagCompound(tags);
        }
        else if (tags != null) {
            tags.removeTag("items_" + this.getSlotIndex());
            if (tags.hasNoTags()) {
                filter.setTagCompound((NBTTagCompound)null);
            }
            else {
                filter.setTagCompound(tags);
            }
        }
        if (par1ItemStack != null) {
            this.curStack = par1ItemStack;
        }
        else {
            this.curStack = null;
        }
        if (!SlotItemContainer.iterating) {
            this.onSlotChanged();
        }
    }
    
    public void onSlotChanged() {
        SlotItemContainer.checking = true;
        final ItemStack oldItem = this.getStack();
        SlotItemContainer.checking = false;
        final boolean flag = false;
        if (!ItemStack.areItemStacksEqual(oldItem, this.curStack)) {
            SlotItemContainer.iterating = true;
            this.putStack(this.curStack);
            SlotItemContainer.iterating = false;
        }
        this.filterInv.markDirty();
    }
    
    static {
        SlotItemContainer.checking = false;
        SlotItemContainer.iterating = false;
        SlotItemContainer.fakeInv = (IInventory)new InventoryBasic("fakeInventory", true, 54);
    }
}


