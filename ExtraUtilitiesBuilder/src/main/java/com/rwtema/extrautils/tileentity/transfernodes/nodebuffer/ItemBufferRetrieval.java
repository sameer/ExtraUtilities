// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes.nodebuffer;

import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.tileentity.transfernodes.InvHelper;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.IInventory;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.StdPipes;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.tileentity.TileEntity;

public class ItemBufferRetrieval extends ItemBuffer
{
    @Override
    public boolean shouldSearch() {
        return this.item == null || this.item.stackSize < this.item.getMaxStackSize();
    }
    
    public boolean transferBack(final TileEntity tile, final ForgeDirection side, final IPipe insertingPipe, final int x, final int y, final int z) {
        return super.transfer(tile, side, StdPipes.getPipeType(0), x, y, z, side);
    }
    
    @Override
    public boolean transfer(final TileEntity tile, final ForgeDirection side, final IPipe insertingPipe, final int x, final int y, final int z, final ForgeDirection travelDir) {
        if (tile instanceof IInventory && side != ForgeDirection.UNKNOWN && this.node instanceof ISidedInventory) {
            final boolean nonSided = !(tile instanceof ISidedInventory);
            final IInventory nodeInv = (IInventory)this.node;
            final IInventory inv = TNHelper.getInventory(tile);
            boolean extracted = false;
            for (final int i : InvHelper.getSlots(inv, side.ordinal())) {
                if (inv.getStackInSlot(i) != null && ((this.item == null && nodeInv.isItemValidForSlot(0, inv.getStackInSlot(i))) || InvHelper.canStack(this.item, inv.getStackInSlot(i)))) {
                    if (extracted) {
                        return false;
                    }
                    if (this.item == null || this.item.stackSize < this.item.getMaxStackSize()) {
                        int n = nodeInv.getInventoryStackLimit();
                        if (this.node.getNode().upgradeNo(3) == 0) {
                            n = 1;
                        }
                        if (this.item != null) {
                            n = Math.min(n, this.item.getMaxStackSize() - this.item.stackSize);
                        }
                        if (n > 0 && (nonSided || ((ISidedInventory)inv).canExtractItem(i, inv.getStackInSlot(i), side.ordinal()))) {
                            final ItemStack r = inv.decrStackSize(i, n);
                            if (r != null && r.stackSize > 0) {
                                if (this.item == null) {
                                    this.item = r;
                                }
                                else {
                                    final ItemStack item = this.item;
                                    item.stackSize += r.stackSize;
                                }
                                inv.markDirty();
                                if (n == r.stackSize && inv.getStackInSlot(i) != null) {
                                    return false;
                                }
                                extracted = true;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}


