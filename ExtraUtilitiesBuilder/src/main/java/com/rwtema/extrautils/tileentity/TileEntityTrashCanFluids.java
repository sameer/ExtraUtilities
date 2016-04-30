// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity;

import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTrashCanFluids extends TileEntity implements IFluidHandler
{
    public int fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
        return (resource != null) ? resource.amount : 0;
    }
    
    public FluidStack drain(final ForgeDirection from, final FluidStack resource, final boolean doDrain) {
        return null;
    }
    
    public FluidStack drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
        return null;
    }
    
    public boolean canFill(final ForgeDirection from, final Fluid fluid) {
        return true;
    }
    
    public boolean canDrain(final ForgeDirection from, final Fluid fluid) {
        return false;
    }
    
    public FluidTankInfo[] getTankInfo(final ForgeDirection from) {
        return new FluidTankInfo[] { new FluidTankInfo((FluidStack)null, 16777215) };
    }
    
    public boolean canUpdate() {
        return false;
    }
}


