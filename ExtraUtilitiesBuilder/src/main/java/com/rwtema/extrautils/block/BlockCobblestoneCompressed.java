// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.block;

import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import java.util.Locale;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.rwtema.extrautils.texture.TextureComprBlock;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.ForgeHooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.world.Explosion;
import net.minecraft.init.Blocks;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import com.rwtema.extrautils.ICreativeTabSorting;
import net.minecraft.block.Block;

public class BlockCobblestoneCompressed extends Block implements IBlockTooltip, ICreativeTabSorting
{
    private IIcon[] icons;
    
    public BlockCobblestoneCompressed(final Material par2Material) {
        super(par2Material);
        this.icons = new IIcon[16];
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(2.0f);
        this.setStepSound(BlockCobblestoneCompressed.soundTypeStone);
        this.setResistance(10.0f);
        this.setBlockName("extrautils:cobblestone_compressed");
        this.setBlockTextureName("extrautils:cobblestone_compressed");
    }
    
    public static String getOreDictName(final int metadata) {
        if (metadata < 8) {
            return "Cobblestone";
        }
        if (metadata < 12) {
            return "Dirt";
        }
        if (metadata < 14) {
            return "Gravel";
        }
        return "Sand";
    }
    
    public static Block getBlock(final int metadata) {
        if (metadata < 8) {
            return Blocks.cobblestone;
        }
        if (metadata < 12) {
            return Blocks.dirt;
        }
        if (metadata < 14) {
            return Blocks.gravel;
        }
        return (Block)Blocks.sand;
    }
    
    public static boolean isBaseBlock(final int metadata) {
        return metadata == 0 || metadata == 8 || metadata == 12 || metadata == 14;
    }
    
    public static int getCompr(final int metadata) {
        if (metadata < 8) {
            return metadata;
        }
        if (metadata < 12) {
            return metadata - 8;
        }
        if (metadata < 14) {
            return metadata - 12;
        }
        return metadata - 14;
    }
    
    public boolean canDropFromExplosion(final Explosion par1Explosion) {
        return par1Explosion == null || !(par1Explosion.exploder instanceof EntityWitherSkull);
    }
    
    public void onBlockExploded(final World world, final int x, final int y, final int z, final Explosion explosion) {
        if (!world.isRemote && this.canDropFromExplosion(explosion)) {
            super.onBlockExploded(world, x, y, z, explosion);
        }
    }
    
    public float getPlayerRelativeBlockHardness(final EntityPlayer par1EntityPlayer, final World par2World, final int par3, final int par4, final int par5) {
        return ForgeHooks.blockStrength(getBlock(par2World.getBlockMetadata(par3, par4, par5)), par1EntityPlayer, par2World, par3, par4, par5);
    }
    
    public boolean canHarvestBlock(final EntityPlayer player, final int meta) {
        return ForgeHooks.canHarvestBlock(getBlock(meta), player, meta);
    }
    
    public boolean isFireSource(final World world, final int x, final int y, final int z, final ForgeDirection side) {
        return getBlock(world.getBlockMetadata(x, y, z)).isFireSource(world, x, y, z, side);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        if (!(par1IIconRegister instanceof TextureMap)) {
            return;
        }
        for (int i = 0; i < 16; ++i) {
            if (getBlock(i).getIcon(0, 0) == null) {
                getBlock(i).registerBlockIcons(par1IIconRegister);
            }
            final String icon_name = getBlock(i).getIcon(0, 0).getIconName();
            final int c = getCompr(i);
            final String t = "extrautils:" + icon_name + "_compressed_" + (c + 1);
            this.icons[i] = (IIcon)((TextureMap)par1IIconRegister).getTextureExtry(t);
            if (this.icons[i] == null) {
                final TextureAtlasSprite t2 = new TextureComprBlock(t, icon_name, 1 + c);
                this.icons[i] = (IIcon)t2;
                ((TextureMap)par1IIconRegister).setTextureEntry(t, t2);
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, final int par2) {
        return this.icons[par2];
    }
    
    public int damageDropped(final int par1) {
        return par1;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        for (int var4 = 0; var4 < 16; ++var4) {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
    
    public void addInformation(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final List par3List, final boolean par4) {
        final int i = par1ItemStack.getItemDamage();
        par3List.add(String.format(Locale.ENGLISH, "%,d", (int)Math.pow(9.0, getCompr(i) + 1)) + " " + getBlock(i).getLocalizedName());
    }
    
    public String getSortingName(final ItemStack item) {
        final ItemStack i = item.copy();
        i.setItemDamage(i.getItemDamage() - getCompr(i.getItemDamage()));
        return i.getDisplayName();
    }
    
    public boolean canEntityDestroy(final IBlockAccess world, final int x, final int y, final int z, final Entity entity) {
        return getCompr(world.getBlockMetadata(x, y, z)) < 6;
    }
    
    public float getExplosionResistance(final Entity par1Entity, final World world, final int x, final int y, final int z, final double explosionX, final double explosionY, final double explosionZ) {
        final int metadata = world.getBlockMetadata(x, y, z);
        return getBlock(metadata).getExplosionResistance(par1Entity) * (int)Math.pow(1.5, 1 + getCompr(metadata));
    }
    
    public float getBlockHardness(final World world, final int x, final int y, final int z) {
        final int metadata = world.getBlockMetadata(x, y, z);
        return (int)(getBlock(metadata).getBlockHardness(world, x, y, z) * Math.pow(2.5, 1 + getCompr(metadata)));
    }
}


