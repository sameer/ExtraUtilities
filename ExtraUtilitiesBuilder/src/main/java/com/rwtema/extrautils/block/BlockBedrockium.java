// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockCompressed;

public class BlockBedrockium extends BlockCompressed
{
    public BlockBedrockium() {
        super(Material.rock.getMaterialMapColor());
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setBlockName("extrautils:block_bedrockium");
        this.setBlockTextureName("extrautils:bedrockiumBlock");
        this.setHardness(1000.0f);
        this.setResistance(6000000.0f);
    }
    
    public boolean canDropFromExplosion(final Explosion par1Explosion) {
        return false;
    }
    
    public void onBlockExploded(final World world, final int x, final int y, final int z, final Explosion explosion) {
    }
    
    public boolean canEntityDestroy(final IBlockAccess world, final int x, final int y, final int z, final Entity entity) {
        return false;
    }
    
    public static class ItemBedrockium extends ItemBlock
    {
        public ItemBedrockium(final Block p_i45328_1_) {
            super(p_i45328_1_);
        }
        
        public void onUpdate(final ItemStack itemStack, final World world, final Entity entity, final int i, final boolean b) {
            super.onUpdate(itemStack, world, entity, i, b);
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 10, 3));
            }
        }
        
        public int getEntityLifespan(final ItemStack itemStack, final World world) {
            return 2147473647;
        }
    }
}

