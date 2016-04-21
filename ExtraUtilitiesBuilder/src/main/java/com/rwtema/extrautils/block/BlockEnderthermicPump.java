// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import com.rwtema.extrautils.tileentity.TileEntityEnderThermicLavaPump;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.block.Block;

public class BlockEnderthermicPump extends Block
{
    IIcon pump;
    IIcon pumpTop;
    IIcon pumpBottom;
    
    public BlockEnderthermicPump() {
        super(Material.rock);
        this.setBlockName("extrautils:enderThermicPump");
        this.setBlockTextureName("extrautils:enderThermicPump");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(1.0f);
        this.setStepSound(BlockEnderthermicPump.soundTypeStone);
    }
    
    public boolean hasTileEntity(final int metadata) {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        this.pump = par1IIconRegister.registerIcon("extrautils:enderThermicPump_side");
        this.pumpTop = par1IIconRegister.registerIcon("extrautils:enderThermicPump_top");
        this.pumpBottom = par1IIconRegister.registerIcon("extrautils:enderThermicPump");
        super.registerBlockIcons(par1IIconRegister);
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, final int par2) {
        if (par1 == 0) {
            return this.pumpBottom;
        }
        if (par1 == 1) {
            return this.pumpTop;
        }
        return this.pump;
    }
    
    public TileEntity createTileEntity(final World world, final int metadata) {
        return (TileEntity)new TileEntityEnderThermicLavaPump();
    }
    
    public void onBlockPlacedBy(final World par1World, final int par2, final int par3, final int par4, final EntityLivingBase par5EntityLiving, final ItemStack par6ItemStack) {
        final TileEntity tile = par1World.getTileEntity(par2, par3, par4);
        if (tile instanceof TileEntityEnderThermicLavaPump && par5EntityLiving instanceof EntityPlayer) {
            ((TileEntityEnderThermicLavaPump)tile).owner = (EntityPlayer)par5EntityLiving;
        }
    }
}

