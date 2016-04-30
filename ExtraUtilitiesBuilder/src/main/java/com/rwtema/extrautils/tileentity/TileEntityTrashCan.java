// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import com.rwtema.extrautils.item.ItemNodeUpgrade;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTrashCan extends TileEntity implements IInventory
{
    public static final int NUM_SLOTS = 64;
    private ItemStack[] itemSlots;
    public static boolean instantAdd;
    private boolean added;
    private boolean checkedValid;
    
    public TileEntityTrashCan() {
        this.itemSlots = new ItemStack[64];
        this.added = false;
    }
    
    public void updateEntity() {
        if (!this.checkedValid && this.worldObj != null) {
            if (ExtraUtils.trashCan == null || this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) != ExtraUtils.trashCan) {
                this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
                this.invalidate();
            }
            else {
                this.checkedValid = true;
            }
        }
        if (this.added) {
            this.added = false;
            this.processInv();
        }
    }
    
    public void readFromNBT(final NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        if (par1NBTTagCompound.hasKey("filter")) {
            this.itemSlots[0] = ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("filter"));
        }
    }
    
    public void writeToNBT(final NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        if (this.itemSlots[0] != null) {
            final NBTTagCompound itemTag = new NBTTagCompound();
            this.itemSlots[0].writeToNBT(itemTag);
            par1NBTTagCompound.setTag("filter", (NBTBase)itemTag);
        }
    }
    
    public void processInv() {
        if (this.itemSlots[0] != null) {
            if (ExtraUtils.nodeUpgrade == null || !ItemNodeUpgrade.isFilter(this.itemSlots[0])) {
                this.itemSlots[0] = null;
            }
        }
        for (int i = 1; i < 64; ++i) {
            this.itemSlots[i] = null;
        }
        this.markDirty();
    }
    
    public int getSizeInventory() {
        return 65;
    }
    
    public ItemStack getStackInSlot(final int i) {
        return (i == 64) ? null : this.itemSlots[i];
    }
    
    public ItemStack decrStackSize(final int par1, final int par2) {
        if (par1 == 64) {
            return null;
        }
        if (this.itemSlots[par1] == null) {
            return null;
        }
        if (this.itemSlots[par1].stackSize <= par2) {
            final ItemStack itemstack = this.itemSlots[par1];
            this.itemSlots[par1] = null;
            this.markDirty();
            return itemstack;
        }
        final ItemStack itemstack = this.itemSlots[par1].splitStack(par2);
        if (this.itemSlots[par1].stackSize == 0) {
            this.itemSlots[par1] = null;
        }
        this.markDirty();
        return itemstack;
    }
    
    public ItemStack getStackInSlotOnClosing(final int i) {
        return this.getStackInSlot(i);
    }
    
    public void setInventorySlotContents(final int i, final ItemStack itemstack) {
        if (i == 64) {
            return;
        }
        this.itemSlots[i] = itemstack;
        this.markDirty();
        if (TileEntityTrashCan.instantAdd) {
            this.processInv();
        }
        else {
            this.added = true;
        }
    }
    
    public String getInventoryName() {
        return "TrashCan";
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public int getInventoryStackLimit() {
        return 64;
    }
    
    public boolean isUseableByPlayer(final EntityPlayer par1EntityPlayer) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 64.0;
    }
    
    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return i <= 64 && (this.itemSlots[0] == null || ItemNodeUpgrade.matchesFilterItem(itemstack, this.itemSlots[0]));
    }
    
    public void openInventory() {
    }
    
    public void closeInventory() {
    }
    
    static {
        TileEntityTrashCan.instantAdd = false;
    }
}


