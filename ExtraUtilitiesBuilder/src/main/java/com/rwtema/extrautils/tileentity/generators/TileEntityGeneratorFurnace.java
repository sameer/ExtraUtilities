// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.generators;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.inventory.ISidedInventory;

public class TileEntityGeneratorFurnace extends TileEntityGenerator implements ISidedInventory
{
    InventoryGeneric inv;
    int[] slots;
    
    public TileEntityGeneratorFurnace() {
        this.inv = new InventoryGeneric("generatorFurnace", false, 1);
        this.slots = null;
    }
    
    @Override
    public int transferLimit() {
        return 400;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
    }
    
    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return this.getFuelBurn(itemstack) != 0.0 && this.getInventory().isItemValidForSlot(i, itemstack);
    }
    
    @Override
    public int getMaxCoolDown() {
        return 0;
    }
    
    @Override
    public boolean shouldProcess() {
        return this.coolDown == 0.0 || this.coolDown < this.getMaxCoolDown();
    }
    
    @Override
    public boolean processInput() {
        return this.burnItem();
    }
    
    public double getGenLevelForStack(final ItemStack itemStack) {
        return (this.getFuelBurn(itemStack) != 0.0) ? this.genLevel() : 0.0;
    }
    
    public void adjustGenLevel(final ItemStack item) {
    }
    
    public boolean burnItem() {
        final ItemStack itemStack = this.inv.getStackInSlot(0);
        final double c = this.getFuelBurn(itemStack);
        if (c <= 0.0) {
            return false;
        }
        if (!itemStack.getItem().hasContainerItem(itemStack)) {
            this.adjustGenLevel(itemStack);
            this.addCoolDown(c, false);
            this.inv.decrStackSize(0, 1);
            this.markDirty();
            return true;
        }
        if (itemStack.stackSize == 1) {
            this.addCoolDown(c, false);
            this.adjustGenLevel(itemStack);
            this.inv.setInventorySlotContents(0, itemStack.getItem().getContainerItem(itemStack));
            this.markDirty();
            return true;
        }
        return false;
    }
    
    @Override
    public double genLevel() {
        return 40.0;
    }
    
    @Override
    public InventoryGeneric getInventory() {
        return this.inv;
    }
    
    @Override
    public boolean canExtractItem(final int i, final ItemStack itemstack, final int j) {
        return this.getFuelBurn(itemstack) == 0.0 || (itemstack != null && itemstack.getItem().hasContainerItem(itemstack) && itemstack.stackSize > 1);
    }
    
    public double getFuelBurn(final ItemStack item) {
        return TileEntityGenerator.getFurnaceBurnTime(item) * 12.5 / 40.0;
    }
    
    public int getSizeInventory() {
        return this.getInventory().getSizeInventory();
    }
    
    public ItemStack getStackInSlot(final int i) {
        return this.getInventory().getStackInSlot(i);
    }
    
    public ItemStack decrStackSize(final int i, final int j) {
        return this.getInventory().decrStackSize(i, j);
    }
    
    public ItemStack getStackInSlotOnClosing(final int i) {
        return this.getInventory().getStackInSlotOnClosing(i);
    }
    
    public void setInventorySlotContents(final int i, final ItemStack itemstack) {
        this.getInventory().setInventorySlotContents(i, itemstack);
    }
    
    public String getInventoryName() {
        return this.getInventory().getInventoryName();
    }
    
    public boolean hasCustomInventoryName() {
        return this.getInventory().hasCustomInventoryName();
    }
    
    public int getInventoryStackLimit() {
        return this.getInventory().getInventoryStackLimit();
    }
    
    public boolean isUseableByPlayer(final EntityPlayer entityplayer) {
        return this.getInventory().isUseableByPlayer(entityplayer);
    }
    
    public void openInventory() {
    }
    
    public void closeInventory() {
    }
    
    public int[] getAccessibleSlotsFromSide(final int var1) {
        if (this.slots == null) {
            final int t = this.getSizeInventory();
            this.slots = new int[t];
            for (int i = 0; i < t; ++i) {
                this.slots[i] = i;
            }
        }
        return this.slots;
    }
    
    public boolean canInsertItem(final int i, final ItemStack itemstack, final int j) {
        return true;
    }
}

