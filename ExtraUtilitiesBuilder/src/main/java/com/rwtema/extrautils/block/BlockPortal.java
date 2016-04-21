// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import net.minecraft.client.particle.EntityFX;
import com.rwtema.extrautils.particle.ParticleHelperClient;
import com.rwtema.extrautils.particle.ParticlePortal;
import net.minecraft.util.MathHelper;
import com.rwtema.extrautils.tileentity.TileEntityPortal;
import net.minecraft.tileentity.TileEntity;
import com.rwtema.extrautils.worldgen.endoftime.TeleporterEndOfTime;
import net.minecraft.world.Teleporter;
import com.rwtema.extrautils.worldgen.Underdark.TeleporterUnderdark;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.world.Explosion;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import com.rwtema.extrautils.item.IBlockLocalization;
import net.minecraft.block.Block;

public class BlockPortal extends Block implements IBlockLocalization
{
    public static IIcon particle;
    private IIcon lightPortal;
    public static ItemStack darkPortalItemStack;
    public static ItemStack lightPortalItemStack;
    
    public BlockPortal() {
        super(Material.rock);
        this.setBlockTextureName("extrautils:dark_portal");
        this.setBlockName("extrautils:dark_portal");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(2.0f);
        BlockPortal.darkPortalItemStack = new ItemStack((Block)this, 1, 0);
        BlockPortal.lightPortalItemStack = new ItemStack((Block)this, 1, 2);
    }
    
    public void getSubBlocks(final Item p_149666_1_, final CreativeTabs p_149666_2_, final List p_149666_3_) {
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 2));
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int p_149691_1_, final int p_149691_2_) {
        if (p_149691_2_ >> 1 == 1) {
            return this.lightPortal;
        }
        return super.getIcon(p_149691_1_, p_149691_2_);
    }
    
    public int getLightValue(final IBlockAccess world, final int x, final int y, final int z) {
        return (world.getBlockMetadata(x, y, z) >> 1 == 0) ? 15 : 0;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        super.registerBlockIcons(par1IIconRegister);
        this.lightPortal = par1IIconRegister.registerIcon("extrautils:light_portal");
        BlockPortal.particle = par1IIconRegister.registerIcon("extrautils:particle_blue");
    }
    
    public void updateTick(final World par1World, final int par2, final int par3, final int par4, final Random par5Random) {
        par1World.setBlock(par2, par3 + 1, par4, Blocks.torch);
    }
    
    public float getBlockHardness(final World world, final int x, final int y, final int z) {
        return ((world.getBlockMetadata(x, y, z) & 0x1) == 0x1) ? -1.0f : 2.0f;
    }
    
    public boolean canEntityDestroy(final IBlockAccess world, final int x, final int y, final int z, final Entity entity) {
        return (world.getBlockMetadata(x, y, z) & 0x1) != 0x1;
    }
    
    public float getExplosionResistance(final Entity par1Entity, final World world, final int x, final int y, final int z, final double explosionX, final double explosionY, final double explosionZ) {
        return ((world.getBlockMetadata(x, y, z) & 0x1) == 0x1) ? 1.0E20f : super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
    }
    
    public boolean canDropFromExplosion(final Explosion par1Explosion) {
        return par1Explosion == null || !(par1Explosion.exploder instanceof EntityWitherSkull);
    }
    
    public void onBlockExploded(final World world, final int x, final int y, final int z, final Explosion explosion) {
        if (!world.isRemote && this.canDropFromExplosion(explosion) && (world.getBlockMetadata(x, y, z) & 0x1) == 0x0) {
            super.onBlockExploded(world, x, y, z, explosion);
        }
    }
    
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer entity, final int par6, final float par7, final float par8, final float par9) {
        return world.isRemote || this.transferPlayer(world, x, y, z, (Entity)entity);
    }
    
    public boolean transferPlayer(final World world, final int x, final int y, final int z, final Entity entity) {
        if (entity.ridingEntity == null && entity.riddenByEntity == null && entity instanceof EntityPlayerMP) {
            final EntityPlayerMP thePlayer = (EntityPlayerMP)entity;
            if (XUHelper.isPlayerFake(thePlayer)) {
                return false;
            }
            final int type = world.getBlockMetadata(x, y, z) >> 1;
            if (type == 0) {
                if (ExtraUtils.underdarkDimID == 0) {
                    return false;
                }
                if (thePlayer.dimension != ExtraUtils.underdarkDimID) {
                    thePlayer.setLocationAndAngles(x + 0.5, thePlayer.posY, z + 0.5, thePlayer.rotationYaw, thePlayer.rotationPitch);
                    thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, ExtraUtils.underdarkDimID, (Teleporter)new TeleporterUnderdark(thePlayer.mcServer.worldServerForDimension(ExtraUtils.underdarkDimID)));
                }
                else {
                    thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, 0, (Teleporter)new TeleporterUnderdark(thePlayer.mcServer.worldServerForDimension(0)));
                }
                return true;
            }
            else if (type == 1) {
                if (ExtraUtils.endoftimeDimID == 0) {
                    return false;
                }
                if (thePlayer.dimension != ExtraUtils.endoftimeDimID) {
                    thePlayer.setLocationAndAngles(x + 0.5, thePlayer.posY, z + 0.5, thePlayer.rotationYaw, thePlayer.rotationPitch);
                    thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, ExtraUtils.endoftimeDimID, (Teleporter)new TeleporterEndOfTime(thePlayer.mcServer.worldServerForDimension(ExtraUtils.endoftimeDimID)));
                }
                else {
                    thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, 0, (Teleporter)new TeleporterEndOfTime(thePlayer.mcServer.worldServerForDimension(0)));
                }
                return true;
            }
        }
        return false;
    }
    
    public boolean hasTileEntity(final int metadata) {
        return true;
    }
    
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileEntityPortal();
    }
    
    public String getUnlocalizedName(final ItemStack par1ItemStack) {
        final int type = par1ItemStack.getItemDamage() >> 1;
        return this.getUnlocalizedName() + ((type == 0) ? "" : ("." + type));
    }
    
    public int damageDropped(final int meta) {
        return meta & 0xE;
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random r) {
        if (world.getBlockMetadata(x, y, z) >> 1 == 0) {
            return;
        }
        final double dx = MathHelper.clamp_double(0.5 + 0.2 * r.nextGaussian(), 0.0, 1.0);
        final double dz = MathHelper.clamp_double(0.5 + 0.2 * r.nextGaussian(), 0.0, 1.0);
        ParticleHelperClient.addParticle(new ParticlePortal(world, x + dx, y + 1, z + dz, 1.0f, 1.0f, 1.0f));
    }
}

