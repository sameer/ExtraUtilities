// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockRetrievalNode extends BlockTransferNode
{
    public static IIcon nodeSideLiquidRemote;
    public static IIcon nodeSideExtractRemote;
    
    public BlockRetrievalNode() {
        this.setBlockName("extrautils:extractor_base_remote");
        this.setBlockTextureName("extrautils:extractor_base");
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        BlockRetrievalNode.nodeSideLiquidRemote = par1IIconRegister.registerIcon("extrautils:extractor_liquid_remote");
        BlockRetrievalNode.nodeSideExtractRemote = par1IIconRegister.registerIcon("extrautils:extractor_extract_remote");
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(final int par1, final int par2) {
        if (par2 < 6) {
            return (par1 == par2 % 6) ? BlockRetrievalNode.nodeBase : BlockRetrievalNode.nodeSideExtractRemote;
        }
        if (par2 < 12) {
            return (par1 == par2 % 6) ? BlockRetrievalNode.nodeBase : BlockRetrievalNode.nodeSideLiquidRemote;
        }
        return BlockRetrievalNode.nodeSideEnergy;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 6));
    }
    
    @Override
    public boolean hasTileEntity(final int metadata) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(final World world, final int metadata) {
        if (metadata == 12) {
            return new TileEntityTransferNodeEnergy();
        }
        if (metadata >= 6 && metadata < 12) {
            return new TileEntityRetrievalNodeLiquid();
        }
        return new TileEntityRetrievalNodeInventory();
    }
}
