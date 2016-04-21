// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.chests;

import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.nbt.NBTTagCompound;
import com.rwtema.extrautils.dynamicgui.DynamicContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.InventoryBasic;
import com.rwtema.extrautils.dynamicgui.IDynamicContainerProvider;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class TileMiniChest extends TileEntity implements IInventory, IDynamicContainerProvider
{
    public final InventoryBasic inv;
    
    public TileMiniChest() {
        this.inv = new InventoryBasic("tile.extrautils:chestMini.name", false, 1);
    }
    
    public boolean canUpdate() {
        return false;
    }
    
    public int getSizeInventory() {
        return this.inv.getSizeInventory();
    }
    
    public ItemStack getStackInSlot(final int p_70301_1_) {
        return this.inv.getStackInSlot(p_70301_1_);
    }
    
    public ItemStack decrStackSize(final int p_70298_1_, final int p_70298_2_) {
        return this.inv.decrStackSize(p_70298_1_, p_70298_2_);
    }
    
    public ItemStack getStackInSlotOnClosing(final int p_70304_1_) {
        return this.inv.getStackInSlotOnClosing(p_70304_1_);
    }
    
    public void setInventorySlotContents(final int p_70299_1_, final ItemStack p_70299_2_) {
        this.inv.setInventorySlotContents(p_70299_1_, p_70299_2_);
    }
    
    public String getInventoryName() {
        return this.inv.getInventoryName();
    }
    
    public boolean hasCustomInventoryName() {
        return this.inv.hasCustomInventoryName();
    }
    
    public int getInventoryStackLimit() {
        return this.inv.getInventoryStackLimit();
    }
    
    public void markDirty() {
        this.inv.markDirty();
    }
    
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 64.0;
    }
    
    public void openInventory() {
        this.inv.openInventory();
    }
    
    public void closeInventory() {
        this.inv.closeInventory();
    }
    
    public boolean isItemValidForSlot(final int p_94041_1_, final ItemStack p_94041_2_) {
        return this.inv.isItemValidForSlot(p_94041_1_, p_94041_2_);
    }
    
    public void func_145976_a(final String displayName) {
        this.inv.func_110133_a(displayName);
    }
    
    public DynamicContainer getContainer(final EntityPlayer player) {
        return new DynamicContainerMiniChest((IInventory)player.inventory, this);
    }
    
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);
        XUHelper.writeInventoryBasicToNBT(tag, this.inv);
    }
    
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);
        XUHelper.readInventoryBasicFromNBT(tag, this.inv);
    }
}


