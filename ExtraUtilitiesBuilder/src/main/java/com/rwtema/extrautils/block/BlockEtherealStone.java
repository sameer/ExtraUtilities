// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import com.rwtema.extrautils.ExtraUtilsProxy;
import net.minecraft.world.IBlockAccess;
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

public class BlockEtherealStone extends Block
{
    private static final int numTypes = 6;
    private IIcon[] icon;
    private final boolean[] dark;
    private final boolean[] polarity;
    
    public BlockEtherealStone() {
        super(Material.glass);
        this.icon = new IIcon[16];
        this.polarity = new boolean[16];
        for (int i = 3; i < 6; ++i) {
            this.polarity[i] = true;
        }
        (this.dark = new boolean[16])[5] = (this.dark[2] = true);
        this.setLightOpacity(0);
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setBlockName("extrautils:etherealglass");
        this.setBlockTextureName("extrautils:etherealglass");
        this.setHardness(0.5f);
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item p_149666_1_, final CreativeTabs p_149666_2_, final List p_149666_3_) {
        for (int i = 0; i < 6; ++i) {
            p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        final IIcon[] icon = this.icon;
        final int n = 0;
        final IconConnectedTexture blockIcon = new IconConnectedTexture(par1IIconRegister, "extrautils:ConnectedTextures/etherealglass");
        icon[n] = (IIcon)blockIcon;
        this.blockIcon = (IIcon)blockIcon;
        this.icon[1] = (IIcon)new IconConnectedTexture(par1IIconRegister, "extrautils:ConnectedTextures/etherealglass1");
        this.icon[2] = (IIcon)new IconConnectedTexture(par1IIconRegister, "extrautils:ConnectedTextures/etherealdarkglass");
        this.icon[3] = (IIcon)new IconConnectedTexture(par1IIconRegister, "extrautils:ConnectedTextures/untherealglass1");
        this.icon[4] = (IIcon)new IconConnectedTexture(par1IIconRegister, "extrautils:ConnectedTextures/untherealglass");
        this.icon[5] = (IIcon)new IconConnectedTexture(par1IIconRegister, "extrautils:ConnectedTextures/untherealdarkglass");
    }
    
    public boolean getBlocksMovement(final IBlockAccess world, final int x, final int y, final int z) {
        final int blockMetadata = world.getBlockMetadata(x, y, z);
        return blockMetadata < 6 && this.polarity[blockMetadata];
    }
    
    public int getLightOpacity(final IBlockAccess world, final int x, final int y, final int z) {
        if (this.dark[world.getBlockMetadata(x, y, z)]) {
            return 255;
        }
        return super.getLightOpacity(world, x, y, z);
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int meta) {
        return this.icon[meta % 6];
    }
    
    public int getRenderType() {
        return ExtraUtilsProxy.connectedTextureEtheralID;
    }
    
    public boolean isOpaqueCube() {
        return false;
    }
    
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    public boolean isNormalCube() {
        return false;
    }
    
    public boolean isNormalCube(final IBlockAccess world, final int x, final int y, final int z) {
        return false;
    }
    
    public boolean canCollideCheck(final int p_149678_1_, final boolean p_149678_2_) {
        return super.canCollideCheck(p_149678_1_, p_149678_2_);
    }
    
    public void onEntityCollidedWithBlock(final World p_149670_1_, final int p_149670_2_, final int p_149670_3_, final int p_149670_4_, final Entity p_149670_5_) {
        super.onEntityCollidedWithBlock(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, p_149670_5_);
    }
    
    public void addCollisionBoxesToList(final World world, final int x, final int y, final int z, final AxisAlignedBB bbs, final List list, final Entity entity) {
        if (this.polarity[world.getBlockMetadata(x, y, z)]) {
            if (entity instanceof EntityPlayer) {
                super.addCollisionBoxesToList(world, x, y, z, bbs, list, entity);
            }
        }
        else {
            if (entity instanceof EntityPlayer && !entity.isSneaking()) {
                return;
            }
            super.addCollisionBoxesToList(world, x, y, z, bbs, list, entity);
        }
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
        final int meta = world.getBlockMetadata(x, y, z);
        if (meta < 6 && this.polarity[meta]) {
            return AxisAlignedBB.getBoundingBox((double)x, y + 0.001, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1));
        }
        return AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1));
    }
    
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4, final int par5) {
        return par1IBlockAccess.getBlock(par2, par3, par4) != this;
    }
    
    public int damageDropped(final int p_149692_1_) {
        return p_149692_1_;
    }
    
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
        return AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1));
    }
    
    public boolean isBlockSolid(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4, final int par5) {
        return !this.polarity[par1IBlockAccess.getBlockMetadata(par2, par3, par4) % 6];
    }
}


