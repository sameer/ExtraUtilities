// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import net.minecraftforge.common.util.ForgeDirection;
import java.util.Random;
import com.rwtema.extrautils.tileentity.TileEntityAntiMobTorch;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.block.Block;

public class BlockMagnumTorch extends Block
{
    private IIcon iconTop;
    private IIcon iconBase;
    
    public BlockMagnumTorch() {
        super(Material.circuits);
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setBlockName("extrautils:magnumTorch");
        this.setBlockTextureName("extrautils:magnumTorch");
        this.setHardness(1.2f);
        this.setBlockBounds(0.375f, 0.0f, 0.375f, 0.625f, 1.0f, 0.625f);
        this.setLightLevel(1.0f);
        this.setLightOpacity(0);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        this.blockIcon = par1IIconRegister.registerIcon("extrautils:magnumTorch");
        this.iconTop = par1IIconRegister.registerIcon("extrautils:magnumTorchTop");
        this.iconBase = par1IIconRegister.registerIcon("extrautils:magnumTorchBase");
    }
    
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    public boolean isOpaqueCube() {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, final int par2) {
        switch (par1) {
            case 0: {
                return this.iconBase;
            }
            case 1: {
                return this.iconTop;
            }
            default: {
                return this.blockIcon;
            }
        }
    }
    
    public boolean hasTileEntity(final int metadata) {
        return true;
    }
    
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileEntityAntiMobTorch();
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final World par1World, final int par2, final int par3, final int par4, final Random par5Random) {
        final float dx = (float)((Math.random() * 2.0 - 1.0) * 0.125);
        final float dy = (float)((Math.random() * 2.0 - 1.0) * 0.0625);
        final float dz = (float)((Math.random() * 2.0 - 1.0) * 0.125);
        par1World.spawnParticle("smoke", (double)(par2 + 0.5f + dx), (double)(par3 + 1 + dy), (double)(par4 + 0.5f + dz), 0.0, 0.0, 0.0);
        par1World.spawnParticle("flame", (double)(par2 + 0.5f + dx), (double)(par3 + 1 + dy), (double)(par4 + 0.5f + dz), 0.0, 0.0, 0.0);
    }
    
    public boolean canPlaceBlockAt(final World par1World, final int par2, final int par3, final int par4) {
        return par1World.isSideSolid(par2, par3 - 1, par4, ForgeDirection.UP, true);
    }
    
    public void onNeighborBlockChange(final World par1World, final int par2, final int par3, final int par4, final Block par5) {
        if (!this.canPlaceBlockAt(par1World, par2, par3, par4) && par1World.getBlock(par2, par3, par4) == this) {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
        }
    }
}

