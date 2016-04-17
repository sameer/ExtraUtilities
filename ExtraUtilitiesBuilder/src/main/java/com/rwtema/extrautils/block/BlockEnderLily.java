// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.item.EntityItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.BlockCrops;

public class BlockEnderLily extends BlockCrops
{
    public static final long period_fast = 3700L;
    public static final long period = 24000L;
    public static final long period_grass = 96000L;
    
    public BlockEnderLily() {
        this.setBlockTextureName("extrautils:plant/ender_lilly");
        this.setBlockName("extrautils:plant/ender_lilly");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    }
    
    public boolean canBlockStay(final World world, final int x, final int y, final int z) {
        return super.canBlockStay(world, x, y, z) || this.canBePlantedHere(world, x, y, z);
    }
    
    protected void checkAndDropBlock(final World p_149855_1_, final int p_149855_2_, final int p_149855_3_, final int p_149855_4_) {
        if (!this.canBlockStay(p_149855_1_, p_149855_2_, p_149855_3_, p_149855_4_) && !this.isEndStone(p_149855_1_, p_149855_2_, p_149855_3_ - 1, p_149855_4_)) {
            this.dropBlockAsItem(p_149855_1_, p_149855_2_, p_149855_3_, p_149855_4_, p_149855_1_.getBlockMetadata(p_149855_2_, p_149855_3_, p_149855_4_), 0);
            p_149855_1_.setBlock(p_149855_2_, p_149855_3_, p_149855_4_, getBlockById(0), 0, 2);
        }
    }
    
    public int getRenderType() {
        return 1;
    }
    
    public boolean canEntityDestroy(final IBlockAccess world, final int x, final int y, final int z, final Entity entity) {
        return !(entity instanceof EntityDragon);
    }
    
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer par5EntityPlayer, final int par6, final float par7, final float par8, final float par9) {
        if (par5EntityPlayer.capabilities.isCreativeMode && par5EntityPlayer.getCurrentEquippedItem() == null && par5EntityPlayer.isSneaking()) {
            if (world.isRemote) {
                return true;
            }
            world.setBlockMetadataWithNotify(x, y, z, (world.getBlockMetadata(x, y, z) + 1) % 8, 2);
        }
        return false;
    }
    
    public boolean canBePlantedHere(final World world, final int x, final int y, final int z) {
        return (world.isAirBlock(x, y, z) && (this.canThisPlantGrowOnThisBlockID(world.getBlock(x, y - 1, z)) || this.isEndStone(world, x, y - 1, z) || this.isSuperEndStone(world, x, y - 1, z))) || this.isFluid(world, x, y - 1, z);
    }
    
    public boolean isWater(final World world, final int x, final int y, final int z) {
        final Block block = world.getBlock(x, y, z);
        return (block == Blocks.water || block == Blocks.flowing_water) && world.getBlockMetadata(x, y, z) == 0;
    }
    
    protected boolean canThisPlantGrowOnThisBlockID(final Block par1) {
        return par1 == Blocks.grass || par1 == Blocks.dirt || par1 == Blocks.end_stone || par1 == Blocks.farmland;
    }
    
    public boolean isSuperEndStone(final World world, final int x, final int y, final int z) {
        final Block id = world.getBlock(x, y, z);
        return id instanceof BlockDecoration && ((BlockDecoration)id).isSuperEnder[world.getBlockMetadata(x, y, z)];
    }
    
    public boolean isEndStone(final World world, final int x, final int y, final int z) {
        final Block id = world.getBlock(x, y, z);
        return id == Blocks.end_stone || (id instanceof BlockDecoration && ((BlockDecoration)id).isEnder[world.getBlockMetadata(x, y, z)]);
    }
    
    public void updateTick(final World par1World, final int par2, final int par3, final int par4, final Random par5Random) {
        this.checkAndDropBlock(par1World, par2, par3, par4);
        int l = par1World.getBlockMetadata(par2, par3, par4);
        if (l < 7) {
            if (this.isSuperEndStone(par1World, par2, par3 - 1, par4)) {
                if (par5Random.nextInt(40) == 0) {
                    ++l;
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
                }
            }
            else if (this.isEndStone(par1World, par2, par3 - 1, par4)) {
                if (l % 2 == 0 == par1World.getWorldTime() % 48000L < 24000L && par5Random.nextInt(10) == 0) {
                    ++l;
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
                }
            }
            else if (l % 2 == 0 == par1World.getWorldTime() % 192000L < 96000L && par5Random.nextInt(40) == 0) {
                ++l;
                par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final World par1World, final int par2, final int par3, final int par4, final Random par5Random) {
        if (par5Random.nextInt(5) != 0 || par1World.getBlockMetadata(par2, par3, par4) < 7) {
            return;
        }
        double d0 = par2 + par5Random.nextFloat();
        final double d2 = par3 + par5Random.nextFloat();
        d0 = par4 + par5Random.nextFloat();
        double d3 = 0.0;
        double d4 = 0.0;
        double d5 = 0.0;
        final int i1 = par5Random.nextInt(2) * 2 - 1;
        final int j1 = par5Random.nextInt(2) * 2 - 1;
        d3 = (par5Random.nextFloat() - 0.5) * 0.125;
        d4 = (par5Random.nextFloat() - 0.5) * 0.125;
        d5 = (par5Random.nextFloat() - 0.5) * 0.125;
        final double d6 = par4 + 0.5 + 0.25 * j1;
        d5 = par5Random.nextFloat() * 1.0f * j1;
        final double d7 = par2 + 0.5 + 0.25 * i1;
        d3 = par5Random.nextFloat() * 1.0f * i1;
        par1World.spawnParticle("portal", d7, d2, d6, d3, d4, d5);
    }
    
    public void onEntityCollidedWithBlock(final World par1World, final int par2, final int par3, final int par4, final Entity par5Entity) {
        if (par1World.getBlockMetadata(par2, par3, par4) >= 3) {
            if (par5Entity instanceof EntityItem) {
                final ItemStack item = ((EntityItem)par5Entity).getEntityItem();
                if (item != null && (item.getItem() == this.getSeedItem() || item.getItem() == this.getCropItem())) {
                    return;
                }
                if (par1World.isRemote) {
                    par1World.spawnParticle("crit", par5Entity.posX, par5Entity.posY, par5Entity.posZ, 0.0, 0.0, 0.0);
                }
            }
            if (par5Entity instanceof EntityEnderman) {
                return;
            }
            par5Entity.attackEntityFrom(DamageSource.cactus, 0.1f);
        }
    }
    
    public ArrayList<ItemStack> getDrops(final World world, final int x, final int y, final int z, final int metadata, final int fortune) {
        final ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this.getSeedItem(), 1, 0));
        if (metadata >= 7) {
            ret.add(new ItemStack(this.getCropItem(), 1, 0));
            if (this.isEndStone(world, x, y - 1, z)) {
                while (world.rand.nextInt(50) == 0) {
                    ret.add(new ItemStack(this.getSeedItem(), 1, 0));
                }
            }
            if (this.isSuperEndStone(world, x, y - 1, z)) {
                while (world.rand.nextInt(20) == 0) {
                    ret.add(new ItemStack(this.getSeedItem(), 1, 0));
                }
            }
        }
        return ret;
    }
    
    public void func_149863_m(final World par1World, final int par2, final int par3, final int par4) {
        int l = par1World.getBlockMetadata(par2, par3, par4);
        if (l == 0) {
            par1World.func_147480_a(par2, par3, par4, true);
        }
        else {
            l -= MathHelper.getRandomIntegerInRange(par1World.rand, 1, 5);
            if (l < 0) {
                l = 0;
            }
            par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
        }
    }
    
    protected Item getSeedItem() {
        return Item.getItemFromBlock((Block)this);
    }
    
    protected Item getCropItem() {
        return Items.ender_pearl;
    }
    
    @SideOnly(Side.CLIENT)
    public String getItemIconName() {
        return "extrautils:ender_lilly_seed";
    }
    
    public boolean isFluid(final World world, final int x, final int y, final int z) {
        return this.isWater(world, x, y, z);
    }
}
