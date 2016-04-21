// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import net.minecraft.client.renderer.texture.IIconRegister;
import com.rwtema.extrautils.ExtraUtilsProxy;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import cofh.api.block.IBlockAppearance;
import net.minecraft.block.Block;

public class BlockDecoration extends Block implements IBlockAppearance
{
    public static boolean gettingConnectedTextures;
    public String[][] texture;
    public boolean[][] ctexture;
    public int[] light;
    public float[] hardness;
    public float[] resistance;
    public boolean[] opaque;
    public int[] opacity;
    public boolean[] flipTopBottom;
    public float[] enchantBonus;
    public boolean solid;
    public boolean[] isSuperEnder;
    public boolean[] isEnder;
    private IIcon[][] icons;
    public String[] name;
    private int numBlocks;
    public int[] fireEncouragement;
    public int[] fireFlammability;
    public boolean[] fireSource;
    
    public BlockDecoration(final boolean solid) {
        super(solid ? Material.rock : Material.glass);
        this.texture = new String[16][6];
        this.ctexture = new boolean[16][6];
        this.light = new int[16];
        this.hardness = new float[16];
        this.resistance = new float[16];
        this.opaque = new boolean[16];
        this.opacity = new int[16];
        this.flipTopBottom = new boolean[16];
        this.enchantBonus = new float[16];
        this.isSuperEnder = new boolean[16];
        this.isEnder = new boolean[16];
        this.icons = new IIcon[16][6];
        this.name = new String[16];
        this.numBlocks = 0;
        this.fireEncouragement = new int[16];
        this.fireFlammability = new int[16];
        this.fireSource = new boolean[16];
        this.solid = solid;
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(0.45f).setResistance(10.0f).setStepSound(BlockDecoration.soundTypeStone);
    }
    
    public boolean canSustainPlant(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection direction, final IPlantable plant) {
        return (this.isSuperEnder[world.getBlockMetadata(x, y, z)] && plant instanceof BlockEnderLily) || super.canSustainPlant(world, x, y, z, direction, plant);
    }
    
    public int getLightOpacity(final IBlockAccess world, final int x, final int y, final int z) {
        if (world instanceof World && !((World)world).blockExists(x, y, z)) {
            return 0;
        }
        return this.opacity[world.getBlockMetadata(x, y, z)];
    }
    
    public int getLightValue(final IBlockAccess world, final int x, final int y, final int z) {
        final Block block = world.getBlock(x, y, z);
        if (block != null && block != this) {
            return block.getLightValue(world, x, y, z);
        }
        return this.light[world.getBlockMetadata(x, y, z)];
    }
    
    public float getBlockHardness(final World par1World, final int par2, final int par3, final int par4) {
        if (par1World == null) {
            return this.blockHardness;
        }
        return this.hardness[this.getMetadataSafe((IBlockAccess)par1World, par2, par3, par4)];
    }
    
    public float getExplosionResistance(final Entity par1Entity, final World world, final int x, final int y, final int z, final double explosionX, final double explosionY, final double explosionZ) {
        if (world == null) {
            return this.getExplosionResistance(par1Entity);
        }
        return this.resistance[this.getMetadataSafe((IBlockAccess)world, x, y, z)] / 5.0f;
    }
    
    public boolean canPlaceTorchOnTop(final World world, final int x, final int y, final int z) {
        return true;
    }
    
    public boolean isOpaqueCube() {
        return this.solid;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4, final int par5) {
        return this.solid ? super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5) : (!par1IBlockAccess.getBlock(par2, par3, par4).isOpaqueCube() && par1IBlockAccess.getBlock(par2, par3, par4) != this);
    }
    
    public boolean renderAsNormalBlock() {
        return this.solid;
    }
    
    public void addBlock(final int id, final String defaultname, final String texture) {
        this.addBlock(id, defaultname, texture, false, true);
    }
    
    public void addBlock(final int id, final String defaultname, final String texture, final boolean connectedTexture, final boolean opaque) {
        if (id >= 0 && id < 16) {
            assert this.name[id] != null;
            this.name[id] = texture;
            for (int side = 0; side < 6; ++side) {
                this.texture[id][side] = texture;
                this.ctexture[id][side] = connectedTexture;
            }
            this.hardness[id] = this.blockHardness;
            this.resistance[id] = this.blockHardness * 5.0f;
            this.opaque[id] = opaque;
            this.opacity[id] = (this.solid ? 255 : 0);
            this.enchantBonus[id] = 0.0f;
            this.isEnder[id] = false;
            this.isSuperEnder[id] = false;
        }
    }
    
    public float getEnchantPowerBonus(final World world, final int x, final int y, final int z) {
        return this.enchantBonus[this.getMetadataSafe((IBlockAccess)world, x, y, z)];
    }
    
    public int getMetadataSafe(final IBlockAccess world, final int x, final int y, final int z) {
        return world.getBlockMetadata(x, y, z);
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, final int par2) {
        if (par1 <= 1 && this.flipTopBottom[par2 & 0xF] && this.icons[par2 & 0xF][par1] instanceof IconConnectedTexture) {
            return (IIcon)new IconConnectedTextureFlipped((IconConnectedTexture)this.icons[par2 & 0xF][par1]);
        }
        return this.icons[par2 & 0xF][par1];
    }
    
    public int damageDropped(final int par1) {
        return par1 & 0xF;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        for (int i = 0; i < 16; ++i) {
            if (this.name[i] != null) {
                par3List.add(new ItemStack(par1, 1, i));
            }
        }
    }
    
    public int getRenderType() {
        return ExtraUtilsProxy.connectedTextureID;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        for (int i = 0; i < 16; ++i) {
            for (int side = 0; side < 6; ++side) {
                if (this.texture[i][side] != null && !this.texture[i][side].equals("")) {
                    if (this.ctexture[i][side]) {
                        this.icons[i][side] = (IIcon)new IconConnectedTexture(par1IIconRegister, this.texture[i][side]);
                    }
                    else {
                        this.icons[i][side] = par1IIconRegister.registerIcon(this.texture[i][side]);
                    }
                }
            }
        }
    }
    
    public Block getVisualBlock(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection side) {
        return this;
    }
    
    public int getVisualMeta(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection side) {
        return world.getBlockMetadata(x, y, z);
    }
    
    public boolean supportsVisualConnections() {
        return true;
    }
    
    public boolean isFireSource(final World world, final int x, final int y, final int z, final ForgeDirection side) {
        return this.fireSource[this.getMetadataSafe((IBlockAccess)world, x, y, z)];
    }
    
    public int getFireSpreadSpeed(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection face) {
        return this.fireEncouragement[this.getMetadataSafe(world, x, y, z)];
    }
    
    public int getFlammability(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection face) {
        return this.fireFlammability[this.getMetadataSafe(world, x, y, z)];
    }
    
    static {
        BlockDecoration.gettingConnectedTextures = false;
    }
}

