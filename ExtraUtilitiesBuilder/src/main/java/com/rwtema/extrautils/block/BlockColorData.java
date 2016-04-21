// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.world.World;
import com.rwtema.extrautils.tileentity.TileEntityBlockColorData;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.Block;

public class BlockColorData extends Block implements ITileEntityProvider
{
    public BlockColorData() {
        super(Material.air);
        this.setLightLevel(0.0f);
        this.setLightOpacity(0);
        this.setBlockName("extrautils:datablock");
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        this.setHardness(0.0f);
    }
    
    public static int dataBlockX(final int x) {
        return x >> 4 << 4;
    }
    
    public static int dataBlockY(final int y) {
        return 255;
    }
    
    public static int dataBlockZ(final int z) {
        return z >> 4 << 4;
    }
    
    public static float[] getColorData(final IBlockAccess world, final int x, final int y, final int z) {
        return getColorData(world, x, y, z, world.getBlockMetadata(x, y, z));
    }
    
    public static float[] getColorData(final IBlockAccess world, int x, int y, int z, final int metadata) {
        x = dataBlockX(x);
        y = dataBlockY(y);
        z = dataBlockZ(z);
        TileEntityBlockColorData datablock = null;
        if (world.getTileEntity(x, y, z) instanceof TileEntityBlockColorData) {
            datablock = (TileEntityBlockColorData)world.getTileEntity(x, y, z);
            return datablock.palette[metadata];
        }
        return BlockColor.initColor[metadata];
    }
    
    public static boolean changeColorData(final World world, int x, int y, int z, final int metadata, final float r, final float g, final float b) {
        x = dataBlockX(x);
        y = dataBlockY(y);
        z = dataBlockZ(z);
        TileEntityBlockColorData datablock = null;
        if (world.getTileEntity(x, y, z) instanceof TileEntityBlockColorData) {
            datablock = (TileEntityBlockColorData)world.getTileEntity(x, y, z);
        }
        else {
            if (!world.isAirBlock(x, y, z)) {
                return false;
            }
            world.setBlock(x, y, z, ExtraUtils.colorBlockData);
            datablock = (TileEntityBlockColorData)world.getTileEntity(x, y, z);
        }
        if (datablock == null) {
            return false;
        }
        datablock.setColor(metadata, r, g, b);
        world.markBlockForUpdate(x, y, z);
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
    }
    
    public int getRenderType() {
        return -1;
    }
    
    public int getMobilityFlag() {
        return 1;
    }
    
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(final World par1World, final int par2, final int par3, final int par4) {
        return null;
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World par1World, final int par2, final int par3, final int par4) {
        return null;
    }
    
    public boolean isOpaqueCube() {
        return false;
    }
    
    public boolean isCollidable() {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4, final int par5) {
        return false;
    }
    
    public boolean isAir(final IBlockAccess world, final int x, final int y, final int z) {
        return true;
    }
    
    public TileEntity createNewTileEntity(final World var1, final int var2) {
        return new TileEntityBlockColorData();
    }
}

