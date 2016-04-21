// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.generators;

import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityGeneratorMagma extends TileEntityGenerator implements IFluidHandler
{
    public FluidTank[] tanks;
    
    public TileEntityGeneratorMagma() {
        this.tanks = new FluidTank[] { new FluidTankRestricted(4000, new String[] { "lava" }) };
    }
    
    @Override
    public int transferLimit() {
        return 160;
    }
    
    @Override
    public FluidTank[] getTanks() {
        return this.tanks;
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
        for (int i = 0; i < this.getTanks().length; ++i) {
            final int c = this.getFuelBurn(this.getTanks()[i].getFluid());
            if (c > 0 && this.getTanks()[i].getFluidAmount() >= this.fluidAmmount() && this.addCoolDown(c, true)) {
                this.addCoolDown(c, false);
                this.getTanks()[i].drain(this.fluidAmmount(), true);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public double genLevel() {
        return 40.0;
    }
    
    public int fluidAmmount() {
        return 100;
    }
    
    public int getFuelBurn(final FluidStack fluid) {
        return this.fluidAmmount();
    }
    
    @Override
    public int fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
        return super.fill(from, resource, doFill);
    }
    
    @Override
    public FluidStack drain(final ForgeDirection from, final FluidStack resource, final boolean doDrain) {
        return super.drain(from, resource, doDrain);
    }
    
    @Override
    public FluidStack drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
        return super.drain(from, maxDrain, doDrain);
    }
    
    @Override
    public boolean canFill(final ForgeDirection from, final Fluid fluid) {
        return super.canFill(from, fluid);
    }
    
    @Override
    public boolean canDrain(final ForgeDirection from, final Fluid fluid) {
        return super.canDrain(from, fluid);
    }
    
    @Override
    public FluidTankInfo[] getTankInfo(final ForgeDirection from) {
        return super.getTankInfo(from);
    }
}

