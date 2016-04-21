// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class BlockSpikeGold extends BlockSpike
{
    public BlockSpikeGold() {
        super(new ItemStack(Items.golden_sword));
        this.setBlockName("extrautils:spike_base_gold");
        this.setBlockTextureName("extrautils:spike_base_gold");
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        super.registerBlockIcons(par1IIconRegister);
        this.ironIcon = par1IIconRegister.registerIcon("extrautils:spike_side_gold");
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World world, final int x, final int y, final int z, final Entity target) {
        if (world.isRemote || !(world instanceof WorldServer)) {
            return;
        }
        final FakePlayer fakeplayer = FakePlayerFactory.getMinecraft((WorldServer)world);
        if (target instanceof EntityLivingBase) {
            final TileEntity tile = world.getTileEntity(x, y, z);
            final float damage = this.getDamageMultipliers(4.0f, tile, (EntityLivingBase)target);
            final float h = ((EntityLivingBase)target).getHealth();
            boolean flag = false;
            if (h > damage || target instanceof EntityPlayer) {
                flag = target.attackEntityFrom(DamageSource.cactus, damage);
            }
            else if (h > 0.5f) {
                flag = target.attackEntityFrom(DamageSource.generic, h - 0.001f);
            }
            else if (world.rand.nextInt(3) == 0) {
                flag = this.doPlayerLastHit(world, target, tile);
            }
            else {
                flag = target.attackEntityFrom(DamageSource.cactus, 40.0f);
            }
            if (flag) {
                this.doPostAttack(world, target, tile, x, y, z);
            }
        }
    }
}

