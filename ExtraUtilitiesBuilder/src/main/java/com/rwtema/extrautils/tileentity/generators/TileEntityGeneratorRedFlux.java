// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.generators;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityGeneratorRedFlux extends TileEntityGeneratorFurnace implements IFluidHandler
{
    public FluidTank[] tanks;
    public int curLevel;
    
    public TileEntityGeneratorRedFlux() {
        this.tanks = new FluidTank[] { new FluidTankRestricted(4000, new String[] { "redstone", "lava" }) };
        this.curLevel = 0;
    }
    
    public FluidTank[] getTanks() {
        return this.tanks;
    }
    
    @Override
    public int getMaxCoolDown() {
        return 0;
    }
    
    @Override
    public boolean processInput() {
        final double c = this.getFuelBurn(this.getTanks()[0].getFluid());
        if (c > 0.0 && this.getTanks()[0].getFluidAmount() >= this.fluidAmmount() && this.addCoolDown(c, true)) {
            if ("lava".equals(FluidRegistry.getFluidName(this.getTanks()[0].getFluid()))) {
                final double d = this.getFuelBurn(this.inv.getStackInSlot(0));
                if (d <= 0.0) {
                    return false;
                }
                this.inv.decrStackSize(0, 1);
                this.curLevel = 80;
            }
            else {
                this.curLevel = 80;
            }
            this.addCoolDown(c, false);
            this.getTanks()[0].drain(this.fluidAmmount(), true);
            this.markDirty();
            return true;
        }
        return false;
    }
    
    @Override
    public double genLevel() {
        return this.curLevel;
    }
    
    public int fluidAmmount() {
        return 125;
    }
    
    public double getFuelBurn(final FluidStack fluid) {
        return this.fluidAmmount() * 2.5;
    }
    
    @Override
    public double getFuelBurn(final ItemStack item) {
        return (item != null && item.getItem() == Items.redstone) ? 1.0 : 0.0;
    }
    
    public int fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
        return super.fill(from, resource, doFill);
    }
    
    public FluidStack drain(final ForgeDirection from, final FluidStack resource, final boolean doDrain) {
        return super.drain(from, resource, doDrain);
    }
    
    public FluidStack drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
        return super.drain(from, maxDrain, doDrain);
    }
    
    public boolean canFill(final ForgeDirection from, final Fluid fluid) {
        return super.canFill(from, fluid);
    }
    
    public boolean canDrain(final ForgeDirection from, final Fluid fluid) {
        return super.canDrain(from, fluid);
    }
    
    public FluidTankInfo[] getTankInfo(final ForgeDirection from) {
        return super.getTankInfo(from);
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.curLevel = nbt.getInteger("curLevel");
    }
    
    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("curLevel", this.curLevel);
    }
}

