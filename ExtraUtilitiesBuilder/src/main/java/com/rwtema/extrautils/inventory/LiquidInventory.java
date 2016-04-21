// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraft.inventory.ISidedInventory;

public class LiquidInventory implements ISidedInventory
{
    IFluidHandler tank;
    ForgeDirection side;
    ItemStack[] genericItems;
    
    public LiquidInventory(final IFluidHandler tank, final ForgeDirection side) {
        this.genericItems = new ItemStack[] { FluidContainerRegistry.EMPTY_BUCKET, FluidContainerRegistry.EMPTY_BOTTLE };
        this.tank = tank;
        this.side = side;
    }
    
    public int[] getAccessibleSlotsFromSide(final int var1) {
        if (this.tank.getTankInfo(this.side) == null) {
            return new int[0];
        }
        final int[] t = new int[this.tank.getTankInfo(this.side).length];
        for (int i = 0; i < t.length; ++i) {
            t[i] = i;
        }
        return t;
    }
    
    public boolean canInsertItem(final int var1, final ItemStack var2, final int var3) {
        final FluidStack f = FluidContainerRegistry.getFluidForFilledItem(var2);
        return f != null && f.getFluid() != null && this.tank.canFill(this.side, f.getFluid()) && this.tank.fill(this.side, f, false) == f.amount;
    }
    
    public boolean canExtractItem(final int var1, final ItemStack var2, final int var3) {
        final FluidStack f = FluidContainerRegistry.getFluidForFilledItem(var2);
        return f != null && f.getFluid() != null && this.tank.canDrain(this.side, f.getFluid()) && f.isFluidStackIdentical(this.tank.drain(this.side, f, false));
    }
    
    public int getSizeInventory() {
        return this.tank.getTankInfo(this.side).length;
    }
    
    public ItemStack getStackInSlot(final int var1) {
        final FluidStack f = this.tank.getTankInfo(this.side)[var1].fluid;
        for (final ItemStack item : this.genericItems) {
            final ItemStack i = FluidContainerRegistry.fillFluidContainer(f, item);
            if (i != null) {
                return i;
            }
        }
        return null;
    }
    
    public ItemStack decrStackSize(final int var1, final int var2) {
        final FluidStack f = this.tank.getTankInfo(this.side)[var1].fluid;
        for (final ItemStack item : this.genericItems) {
            final ItemStack i = FluidContainerRegistry.fillFluidContainer(f, item);
            if (i != null) {
                final FluidStack t = FluidContainerRegistry.getFluidForFilledItem(i);
                if (t != null && t.isFluidEqual(this.tank.drain(this.side, t, false))) {
                    this.tank.drain(this.side, t, true);
                    return i;
                }
            }
        }
        return null;
    }
    
    public ItemStack getStackInSlotOnClosing(final int var1) {
        return this.getStackInSlot(var1);
    }
    
    public void setInventorySlotContents(final int var1, final ItemStack var2) {
        final FluidStack f = FluidContainerRegistry.getFluidForFilledItem(var2);
        if (f == null || f.getFluid() == null) {
            return;
        }
        if (!this.tank.canFill(this.side, f.getFluid())) {
            return;
        }
        if (this.tank.fill(this.side, f, false) == f.amount) {
            this.tank.fill(this.side, f, true);
        }
    }
    
    public String getInventoryName() {
        return "fakeTank";
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public int getInventoryStackLimit() {
        return this.tank.getTankInfo(this.side).length;
    }
    
    public void markDirty() {
        if (this.tank instanceof TileEntity) {
            ((TileEntity)this.tank).markDirty();
        }
    }
    
    public boolean isUseableByPlayer(final EntityPlayer var1) {
        return false;
    }
    
    public void openInventory() {
    }
    
    public void closeInventory() {
    }
    
    public boolean isItemValidForSlot(final int var1, final ItemStack var2) {
        return this.canInsertItem(var1, var2, 0);
    }
}

