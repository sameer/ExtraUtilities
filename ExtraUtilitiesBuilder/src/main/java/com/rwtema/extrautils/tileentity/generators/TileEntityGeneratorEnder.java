// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.generators;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityGeneratorEnder extends TileEntityGeneratorFurnace
{
    public FluidTank[] tanks;
    
    public TileEntityGeneratorEnder() {
        this.tanks = new FluidTank[] { new FluidTankRestricted(4000, new String[] { "ender" }) };
    }
    
    @Override
    public int transferLimit() {
        return 400;
    }
    
    @Override
    public double genLevel() {
        return 40.0;
    }
    
    @Override
    public int getMaxCoolDown() {
        return 0;
    }
    
    @Override
    public double getFuelBurn(final ItemStack item) {
        if (item != null) {
            if (item.getItem() == Items.ender_pearl) {
                return 750.0;
            }
            if (item.getItem() == Items.ender_eye) {
                return 3000.0;
            }
            if (item.getItem() == Item.getItemFromBlock((Block)ExtraUtils.enderLily)) {
                return 12000.0;
            }
            final FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(item);
            if (fluid != null && "ender".equals(fluid.getFluid().getName())) {
                return fluid.amount * 6;
            }
        }
        return 0.0;
    }
}


