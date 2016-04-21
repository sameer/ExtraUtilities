// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.generators;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidTankRestricted extends FluidTank
{
    String[] name;
    
    public FluidTankRestricted(final int capacity, final String... name) {
        super(capacity);
        this.name = name;
    }
    
    public int fill(final FluidStack resource, final boolean doFill) {
        if (this.fluid == null) {
            final String str = FluidRegistry.getFluidName(resource);
            for (final String aName : this.name) {
                if (aName.equals(str)) {
                    return super.fill(resource, doFill);
                }
            }
            return 0;
        }
        return super.fill(resource, doFill);
    }
}


