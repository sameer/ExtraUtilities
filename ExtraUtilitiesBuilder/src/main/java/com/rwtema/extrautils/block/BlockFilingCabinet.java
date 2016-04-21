// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import com.rwtema.extrautils.helper.XUHelper;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.stats.StatList;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.ExtraUtilsMod;
import net.minecraft.entity.player.EntityPlayer;
import com.rwtema.extrautils.tileentity.TileEntityFilingCabinet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.Facing;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.block.Block;

public class BlockFilingCabinet extends Block implements IBlockTooltip
{
    private IIcon[] icon;
    
    public BlockFilingCabinet() {
        super(Material.rock);
        this.icon = new IIcon[6];
        this.setBlockName("extrautils:filing");
        this.setHardness(1.5f);
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        this.icon[0] = par1IIconRegister.registerIcon("extrautils:filingcabinet");
        this.icon[1] = par1IIconRegister.registerIcon("extrautils:filingcabinet_side");
        this.icon[2] = par1IIconRegister.registerIcon("extrautils:filingcabinet_back");
        this.icon[3] = par1IIconRegister.registerIcon("extrautils:filingcabinet_diamond");
        this.icon[4] = par1IIconRegister.registerIcon("extrautils:filingcabinet_side_diamond");
        this.icon[5] = par1IIconRegister.registerIcon("extrautils:filingcabinet_back_diamond");
    }
    
    public int damageDropped(final int par1) {
        return par1 / 6 % 2;
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, final int par2) {
        final int side = par2 % 6;
        final int type = par2 / 6;
        if (type > 1) {
            return null;
        }
        if (par2 < 2) {
            if (par1 == 4) {
                return this.icon[par2 * 3];
            }
            if (par1 == 5) {
                return this.icon[2 + par2 * 3];
            }
            return this.icon[1 + par2 * 3];
        }
        else {
            if (par1 == side) {
                return this.icon[type * 3];
            }
            if (par1 == Facing.oppositeSide[side]) {
                return this.icon[2 + type * 3];
            }
            return this.icon[1 + type * 3];
        }
    }
    
    public boolean hasTileEntity(final int metadata) {
        return true;
    }
    
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileEntityFilingCabinet();
    }
    
    public boolean onBlockActivated(final World par1World, final int par2, final int par3, final int par4, final EntityPlayer par5EntityPlayer, final int par6, final float par7, final float par8, final float par9) {
        if (par1World.isRemote) {
            return true;
        }
        final TileEntity tile = par1World.getTileEntity(par2, par3, par4);
        par5EntityPlayer.openGui((Object)ExtraUtilsMod.instance, 0, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
        return true;
    }
    
    public boolean removedByPlayer(final World world, final EntityPlayer player, final int x, final int y, final int z, final boolean willHarvest) {
        final ArrayList<ItemStack> items = this.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        if (world.setBlockToAir(x, y, z)) {
            if (!world.isRemote) {
                for (final ItemStack item : items) {
                    if (player == null || !player.capabilities.isCreativeMode || item.hasTagCompound()) {
                        this.dropBlockAsItem(world, x, y, z, item);
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    public ArrayList<ItemStack> getDrops(final World world, final int x, final int y, final int z, final int metadata, final int fortune) {
        final ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        final ItemStack item = new ItemStack((Block)this, 1, this.damageDropped(metadata));
        if (world.getTileEntity(x, y, z) instanceof TileEntityFilingCabinet) {
            final NBTTagCompound tags = new NBTTagCompound();
            ((TileEntityFilingCabinet)world.getTileEntity(x, y, z)).writeInvToTags(tags);
            if (!tags.hasNoTags()) {
                item.setTagCompound(tags);
            }
        }
        ret.add(item);
        return ret;
    }
    
    public void harvestBlock(final World par1World, final EntityPlayer par2EntityPlayer, final int par3, final int par4, final int par5, final int par6) {
        par2EntityPlayer.addStat(StatList.mineBlockStatArray[getIdFromBlock((Block)this)], 1);
        par2EntityPlayer.addExhaustion(0.025f);
    }
    
    public void onBlockPlacedBy(final World par1World, final int par2, final int par3, final int par4, final EntityLivingBase par5EntityLivingBase, final ItemStack par6ItemStack) {
        final int l = MathHelper.floor_double(par5EntityLivingBase.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
        int metadata = 0;
        if (l == 0) {
            metadata = 2;
        }
        if (l == 1) {
            metadata = 5;
        }
        if (l == 2) {
            metadata = 3;
        }
        if (l == 3) {
            metadata = 4;
        }
        metadata += par6ItemStack.getItemDamage() % 2 * 6;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, metadata, 2);
        if (par6ItemStack.hasTagCompound()) {
            final TileEntity cabinet = par1World.getTileEntity(par2, par3, par4);
            if (cabinet != null && cabinet instanceof TileEntityFilingCabinet) {
                ((TileEntityFilingCabinet)cabinet).readInvFromTags(par6ItemStack.getTagCompound());
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }
    
    public void addInformation(final ItemStack item, final EntityPlayer par2EntityPlayer, final List par3List, final boolean par4) {
        if (item.hasTagCompound()) {
            final NBTTagCompound tags = item.getTagCompound();
            if (tags.hasKey("item_no")) {
                final int n = item.getTagCompound().getInteger("item_no");
                int k = 0;
                for (int i = 0; i < n; ++i) {
                    k += tags.getCompoundTag("item_" + i).getInteger("Size");
                }
                par3List.add("contains " + k + " item" + XUHelper.s(k) + " of " + n + " type" + XUHelper.s(k));
            }
        }
    }
}

