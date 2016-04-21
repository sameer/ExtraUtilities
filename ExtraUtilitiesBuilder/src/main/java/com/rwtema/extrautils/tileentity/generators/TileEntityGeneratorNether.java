// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.generators;

import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import net.minecraft.world.Explosion;
import net.minecraft.util.DamageSource;
import net.minecraft.potion.PotionEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityLivingBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.potion.Potion;
import java.util.Random;

public class TileEntityGeneratorNether extends TileEntityGeneratorFurnace
{
    private final int range = 10;
    
    @Override
    public double genLevel() {
        return 40000.0;
    }
    
    @Override
    public int transferLimit() {
        return 100000;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void doRandomDisplayTickR(final Random random) {
        super.doRandomDisplayTickR(random);
        if (this.shouldSoundPlay()) {
            final int col = Potion.wither.getLiquidColor();
            final double d0 = (col >> 16 & 0xFF) / 255.0;
            final double d2 = (col >> 8 & 0xFF) / 255.0;
            final double d3 = (col >> 0 & 0xFF) / 255.0;
            this.worldObj.spawnParticle("mobSpell", (double)(this.x() + random.nextFloat()), this.y() + 0.9, (double)(this.z() + random.nextFloat()), d0, d2, d3);
            for (int i = 0; i < 50; ++i) {
                final double dx = this.x() + 0.5 - 10.0 + random.nextInt(22);
                final double dy = this.y() + 0.5 - 10.0 + random.nextInt(22);
                final double dz = this.z() + 0.5 - 10.0 + random.nextInt(22);
                if (this.getDistanceFrom(dx, dy, dz) < 100.0) {
                    this.worldObj.spawnParticle("mobSpell", dx, dy, dz, d0, d2, d3);
                }
            }
        }
    }
    
    @Override
    public void doSpecial() {
        if (this.coolDown > 0.0) {
            for (final Object ent : this.worldObj.getEntitiesWithinAABB((Class)EntityLivingBase.class, AxisAlignedBB.getBoundingBox((double)this.x(), (double)this.y(), (double)this.z(), (double)(this.x() + 1), (double)(this.y() + 1), (double)(this.z() + 1)).expand(10.0, 10.0, 10.0))) {
                final double d = 10.0 - ((EntityLivingBase)ent).getDistance(this.x() + 0.5, this.y() + 0.5, this.z() + 0.5);
                if (d > 0.0 && (!(ent instanceof EntityPlayer) || !((EntityPlayer)ent).capabilities.isCreativeMode)) {
                    ((EntityLivingBase)ent).addPotionEffect(new PotionEffect(Potion.wither.getId(), 200, 2));
                    if (((EntityLivingBase)ent).isEntityUndead()) {
                        continue;
                    }
                    ((EntityLivingBase)ent).attackEntityFrom(DamageSource.setExplosionSource((Explosion)null), (float)d);
                }
            }
        }
    }
    
    @Override
    public double getFuelBurn(final ItemStack item) {
        if (item != null && (item.getItem() == Items.nether_star || item.getItem() == Item.getItemFromBlock(Blocks.dragon_egg))) {
            return 2400.0;
        }
        return 0.0;
    }
}


