// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class BlockSpikeDiamond extends BlockSpike
{
    public ItemStack lootah;
    
    public static ItemStack newLootah() {
        final ItemStack lootah = new ItemStack(Items.diamond_sword);
        lootah.setItemDamage(lootah.getMaxDamage());
        return lootah;
    }
    
    public BlockSpikeDiamond() {
        super(new ItemStack(Items.diamond_sword));
        this.lootah = newLootah();
        this.setBlockName("extrautils:spike_base_diamond");
        this.setBlockTextureName("extrautils:spike_base_diamond");
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        super.registerBlockIcons(par1IIconRegister);
        this.ironIcon = par1IIconRegister.registerIcon("extrautils:spike_side_diamond");
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World world, final int x, final int y, final int z, final Entity target) {
        if (world.isRemote || !(world instanceof WorldServer)) {
            return;
        }
        boolean flag = false;
        if (target instanceof EntityLivingBase) {
            final TileEntity tile = world.getTileEntity(x, y, z);
            final float damage = this.getDamageMultipliers(10.0f, tile, (EntityLivingBase)target);
            final float h = ((EntityLivingBase)target).getHealth();
            if (h > damage || target instanceof EntityPlayer) {
                flag = target.attackEntityFrom(DamageSource.cactus, damage - 0.01f);
            }
            else if (h > 0.5f) {
                flag = target.attackEntityFrom(DamageSource.generic, h - 0.001f);
            }
            else {
                flag = this.doPlayerLastHit(world, target, tile);
            }
            if (flag) {
                this.doPostAttack(world, target, tile, x, y, z);
            }
        }
    }
}


