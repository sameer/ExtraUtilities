// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.generators;

import net.minecraft.item.ItemStack;

public class TileEntityGeneratorFurnaceOverClocked extends TileEntityGeneratorFurnace
{
    private final double multiplier = 10.0;
    
    @Override
    public int transferLimit() {
        return 100000;
    }
    
    @Override
    public double genLevel() {
        return super.genLevel() * 10.0;
    }
    
    @Override
    public double getFuelBurn(final ItemStack item) {
        return super.getFuelBurn(item) / 10.0 * 0.25;
    }
}

