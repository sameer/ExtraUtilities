// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.block;

import net.minecraft.util.DamageSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.block.material.Material;

public class BlockSpikeWood extends BlockSpike
{
    public BlockSpikeWood() {
        super(Material.wood, new ItemStack(Items.wooden_sword));
        this.setBlockName("extrautils:spike_base_wood");
        this.setBlockTextureName("extrautils:spike_base_wood");
        this.setHardness(2.0f);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        super.registerBlockIcons(par1IIconRegister);
        this.ironIcon = par1IIconRegister.registerIcon("extrautils:spike_side_wood");
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World world, final int x, final int y, final int z, final Entity target) {
        if (world.isRemote || !(world instanceof WorldServer)) {
            return;
        }
        if (target instanceof EntityLivingBase) {
            final float h = ((EntityLivingBase)target).getHealth();
            final float damage = this.getDamageMultipliers(1.0f, world.getTileEntity(x, y, z), (EntityLivingBase)target);
            if (h > damage && target.attackEntityFrom(DamageSource.cactus, damage)) {
                this.doPostAttack(world, target, world.getTileEntity(x, y, z), x, y, z);
            }
        }
    }
}


