// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.nodebuffer;

import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import com.rwtema.extrautils.tileentity.transfernodes.InvHelper;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.IInventory;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.ItemStack;

public class ItemBuffer implements INodeBuffer
{
    public INode node;
    public ItemStack item;
    
    public ItemBuffer() {
        this.item = null;
    }
    
    @Override
    public boolean transfer(final TileEntity tile, final ForgeDirection side, final IPipe insertingPipe, final int x, final int y, final int z, final ForgeDirection travelDir) {
        if (this.item != null && tile instanceof IInventory && side != ForgeDirection.UNKNOWN) {
            final boolean nonSided = !(tile instanceof ISidedInventory);
            final IInventory inv = TNHelper.getInventory(tile);
            int empty = -1;
            int filter = -1;
            final int maxStack = Math.min(this.item.getMaxStackSize(), inv.getInventoryStackLimit());
            if (insertingPipe != null) {
                filter = insertingPipe.limitTransfer(tile, side, this);
            }
            if (filter < 0) {
                filter = maxStack;
            }
            else if (filter == 0) {
                return true;
            }
            boolean flag = true;
            for (final int i : InvHelper.getSlots(inv, side.ordinal())) {
                if (inv.getStackInSlot(i) == null) {
                    if (empty == -1 && inv.isItemValidForSlot(i, this.item) && (nonSided || ((ISidedInventory)inv).canInsertItem(i, this.item, side.ordinal()))) {
                        empty = i;
                    }
                }
                else if (InvHelper.canStack(this.item, inv.getStackInSlot(i)) && inv.isItemValidForSlot(i, this.item) && (nonSided || ((ISidedInventory)inv).canInsertItem(i, this.item, side.ordinal()))) {
                    final ItemStack dest = inv.getStackInSlot(i);
                    if (maxStack - dest.stackSize > 0 && filter > 0) {
                        final int l = Math.min(Math.min(this.item.stackSize, maxStack - dest.stackSize), filter);
                        if (l > 0) {
                            final ItemStack itemStack = dest;
                            itemStack.stackSize += l;
                            final ItemStack item = this.item;
                            item.stackSize -= l;
                            filter -= l;
                            flag = true;
                            if (this.item.stackSize <= 0) {
                                this.item = null;
                                break;
                            }
                            if (filter <= 0) {
                                break;
                            }
                        }
                    }
                }
            }
            if (filter > 0 && this.item != null && empty != -1 && inv.isItemValidForSlot(empty, this.item) && (nonSided || ((ISidedInventory)inv).canInsertItem(empty, this.item, side.ordinal()))) {
                if (filter < this.item.stackSize) {
                    inv.setInventorySlotContents(empty, this.item.splitStack(filter));
                }
                else {
                    inv.setInventorySlotContents(empty, this.item);
                    this.item = null;
                }
                flag = true;
            }
            if (flag) {
                inv.markDirty();
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getBuffer() {
        return this.item;
    }
    
    @Override
    public String getBufferType() {
        return "items";
    }
    
    @Override
    public void setBuffer(final Object buffer) {
        if (buffer == null || buffer instanceof ItemStack) {
            this.item = (ItemStack)buffer;
        }
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound tags) {
        if (tags.hasKey("bufferItem")) {
            this.item = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("bufferItem"));
        }
        else {
            this.item = null;
        }
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound tags) {
        if (this.item != null) {
            final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            this.item.writeToNBT(nbttagcompound1);
            tags.setTag("bufferItem", (NBTBase)nbttagcompound1);
        }
    }
    
    @Override
    public boolean isEmpty() {
        if (this.item == null) {
            return true;
        }
        if (this.item.stackSize == 0) {
            this.item = null;
            return true;
        }
        return false;
    }
    
    @Override
    public void setNode(final INode node) {
        this.node = node;
    }
    
    @Override
    public INode getNode() {
        return this.node;
    }
    
    @Override
    public boolean transferTo(final INodeBuffer receptor, final int no) {
        if (this.item == null || this.item.stackSize == 0 || !this.getBufferType().equals(receptor.getBufferType())) {
            return false;
        }
        final ItemStack buffer = (ItemStack)receptor.getBuffer();
        ItemStack newbuffer;
        if (buffer == null) {
            newbuffer = this.item.copy();
            newbuffer.stackSize = 0;
        }
        else {
            newbuffer = buffer.copy();
        }
        if (receptor.getNode() instanceof IInventory && ((IInventory)receptor.getNode()).isItemValidForSlot(0, this.item)) {
            return false;
        }
        if (XUHelper.canItemsStack(this.item, newbuffer)) {
            int m = newbuffer.getMaxStackSize() - newbuffer.stackSize;
            if (no < m) {
                m = no;
            }
            if (this.item.stackSize < m) {
                m = this.item.stackSize;
            }
            if (m <= 0) {
                return false;
            }
            final ItemStack itemStack = newbuffer;
            itemStack.stackSize += m;
            receptor.setBuffer(newbuffer);
            receptor.markDirty();
            final ItemStack item = this.item;
            item.stackSize -= m;
            if (this.item.stackSize == 0) {
                this.item = null;
            }
        }
        return true;
    }
    
    @Override
    public Object recieve(final Object a) {
        if (!(a instanceof ItemStack)) {
            return a;
        }
        final ItemStack i = (ItemStack)a;
        if (this.item == null) {
            this.item = i;
            return null;
        }
        if (XUHelper.canItemsStack(i, this.item)) {
            final int m = this.item.getMaxStackSize() - this.item.stackSize;
        }
        return i;
    }
    
    @Override
    public void markDirty() {
        this.node.bufferChanged();
    }
    
    @Override
    public boolean shouldSearch() {
        return !this.isEmpty();
    }
}
