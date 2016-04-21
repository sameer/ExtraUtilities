// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.generators;

import net.minecraft.item.ItemStack;

public class TileEntityGeneratorFurnaceSurvival extends TileEntityGeneratorFurnace
{
    @Override
    public int transferLimit() {
        return 160;
    }
    
    @Override
    public double genLevel() {
        return 5.0;
    }
    
    @Override
    public double getFuelBurn(final ItemStack item) {
        return 10 * TileEntityGenerator.getFurnaceBurnTime(item);
    }
}

