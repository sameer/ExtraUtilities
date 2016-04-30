// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraftforge.oredict.OreDictionary;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFilingCabinet extends TileEntity implements IInventory
{
    public List<ItemStack> itemSlots;
    public List<ItemStack> inputSlots;
    private boolean needsUpdate;
    
    public TileEntityFilingCabinet() {
        this.itemSlots = new ArrayList<ItemStack>();
        this.inputSlots = new ArrayList<ItemStack>();
        this.needsUpdate = false;
    }
    
    public static boolean areCloseEnoughForBasic(final ItemStack a, final ItemStack b) {
        if (a == null || b == null) {
            return false;
        }
        final int[] da = OreDictionary.getOreIDs(a);
        final int[] db = OreDictionary.getOreIDs(b);
        return (da.length > 0 || db.length > 0) ? arrayContain(da, db) : (a.getItem() == b.getItem());
    }
    
    public static boolean arrayContain(final int[] a, final int[] b) {
        if (a.length == 0 || b.length == 0) {
            return false;
        }
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < b.length; ++j) {
                if (a[i] == a[j]) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public int getMaxSlots() {
        if (this.getBlockMetadata() < 6) {
            return 1728;
        }
        return 1728;
    }
    
    public void updateEntity() {
        this.handleInput();
    }
    
    public void handleInput() {
        if (this.needsUpdate) {
            for (int i = 0; i < this.itemSlots.size(); ++i) {
                if (this.itemSlots.get(i) == null) {
                    this.itemSlots.remove(i);
                    --i;
                }
            }
            while (this.inputSlots.size() > 0) {
                boolean added = false;
                for (final ItemStack itemSlot : this.itemSlots) {
                    if (XUHelper.canItemsStack(itemSlot, this.inputSlots.get(0), false, true)) {
                        final ItemStack itemStack = itemSlot;
                        itemStack.stackSize += this.inputSlots.get(0).stackSize;
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    this.itemSlots.add(this.inputSlots.get(0));
                }
                this.inputSlots.remove(0);
            }
        }
    }
    
    public void markDirty() {
        this.needsUpdate = true;
        super.markDirty();
    }
    
    public int getSizeInventory() {
        return this.itemSlots.size() + this.inputSlots.size() + 1;
    }
    
    public ItemStack getStackInSlot(final int i) {
        if (i < this.itemSlots.size()) {
            return this.itemSlots.get(i);
        }
        if (i - this.itemSlots.size() < this.inputSlots.size()) {
            return this.inputSlots.get(i - this.itemSlots.size());
        }
        return null;
    }
    
    public ItemStack decrStackSize(final int par1, int par2) {
        if (par1 >= this.itemSlots.size() || this.itemSlots.get(par1) == null) {
            return null;
        }
        if (par2 > this.itemSlots.get(par1).getMaxStackSize()) {
            par2 = this.itemSlots.get(par1).getMaxStackSize();
        }
        if (this.itemSlots.get(par1).stackSize <= par2) {
            final ItemStack itemstack = this.itemSlots.get(par1);
            this.itemSlots.set(par1, null);
            this.markDirty();
            return itemstack;
        }
        final ItemStack itemstack = this.itemSlots.get(par1).splitStack(par2);
        if (this.itemSlots.get(par1).stackSize == 0) {
            this.itemSlots.set(par1, null);
        }
        this.markDirty();
        return itemstack;
    }
    
    public ItemStack getStackInSlotOnClosing(final int i) {
        if (i < this.itemSlots.size()) {
            return this.itemSlots.get(i);
        }
        return null;
    }
    
    public void setInventorySlotContents(final int i, final ItemStack itemstack) {
        if (i < this.itemSlots.size()) {
            this.itemSlots.set(i, itemstack);
        }
        else if (i - this.itemSlots.size() < this.inputSlots.size()) {
            this.inputSlots.set(i - this.itemSlots.size(), itemstack);
        }
        else if (i == this.itemSlots.size() + this.inputSlots.size() && itemstack != null) {
            this.inputSlots.add(itemstack);
        }
        this.needsUpdate = true;
    }
    
    public String getInventoryName() {
        return "extrautils:filing.cabinet";
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public int getInventoryStackLimit() {
        if (this.getBlockMetadata() >= 6) {
            return 1;
        }
        int n = 0;
        for (int j = 0; j < this.itemSlots.size() && n <= this.getMaxSlots(); ++j) {
            if (this.itemSlots.get(j) != null) {
                n += this.itemSlots.get(j).stackSize;
            }
        }
        for (final ItemStack inputSlot : this.inputSlots) {
            if (inputSlot != null) {
                n += inputSlot.stackSize;
            }
        }
        return Math.max(1, this.getMaxSlots() - n);
    }
    
    public boolean isUseableByPlayer(final EntityPlayer entityplayer) {
        return !this.isInvalid();
    }
    
    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        if (itemstack == null) {
            return false;
        }
        if (i != this.itemSlots.size() + this.inputSlots.size()) {
            return false;
        }
        final boolean basic = this.getBlockMetadata() < 6;
        if (!basic && itemstack.getMaxStackSize() != 1) {
            return false;
        }
        int n = 0;
        for (int j = 0; j < this.itemSlots.size() && n < this.getMaxSlots(); ++j) {
            if (this.itemSlots.get(j) != null) {
                if (basic && !areCloseEnoughForBasic(this.itemSlots.get(j), itemstack)) {
                    return false;
                }
                n += this.itemSlots.get(j).stackSize;
            }
        }
        for (int j = 0; j < this.inputSlots.size() && n < this.getMaxSlots(); ++j) {
            if (this.inputSlots.get(j) != null) {
                n += this.inputSlots.get(j).stackSize;
            }
        }
        return n < this.getMaxSlots();
    }
    
    public void readInvFromTags(final NBTTagCompound tags) {
        int n = 0;
        if (tags.hasKey("item_no")) {
            n = tags.getInteger("item_no");
        }
        this.itemSlots.clear();
        this.inputSlots.clear();
        for (int i = 0; i < n; ++i) {
            final ItemStack item = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("item_" + i));
            if (item != null) {
                item.stackSize = tags.getCompoundTag("item_" + i).getInteger("Size");
                if (item.stackSize > 0) {
                    this.itemSlots.add(item);
                }
            }
        }
    }
    
    public void writeInvToTags(final NBTTagCompound tags) {
        this.handleInput();
        if (this.itemSlots.size() > 0) {
            tags.setInteger("item_no", this.itemSlots.size());
            for (int i = 0; i < this.itemSlots.size(); ++i) {
                final NBTTagCompound t = new NBTTagCompound();
                this.itemSlots.get(i).writeToNBT(t);
                t.setInteger("Size", this.itemSlots.get(i).stackSize);
                tags.setTag("item_" + i, (NBTBase)t);
            }
        }
    }
    
    public void readFromNBT(final NBTTagCompound tags) {
        super.readFromNBT(tags);
        this.readInvFromTags(tags);
    }
    
    public void writeToNBT(final NBTTagCompound tags) {
        super.writeToNBT(tags);
        this.writeInvToTags(tags);
    }
    
    public void openInventory() {
    }
    
    public void closeInventory() {
    }
}


