// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.multipart;

import codechicken.multipart.TMultiPart;
import net.minecraft.item.ItemStack;
import codechicken.lib.vec.BlockCoord;
import net.minecraft.world.World;
import net.minecraft.block.Block;

public class ItemBlockMultiPartMagnumTorch extends ItemBlockMultiPart
{
    public ItemBlockMultiPartMagnumTorch(final Block par1) {
        super(par1);
        this.hasBlockMetadata = false;
    }
    
    @Override
    public TMultiPart createMultiPart(final World world, final BlockCoord pos, final ItemStack item, final int side) {
        return (TMultiPart)new MagnumTorchPart();
    }
}

