// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;

public class BlockAngelBlock extends Block
{
    public BlockAngelBlock() {
        super(Material.rock);
        this.setBlockName("extrautils:angelBlock");
        this.setBlockTextureName("extrautils:angelBlock");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(1.0f);
        this.setStepSound(Block.soundTypeStone);
    }
}

