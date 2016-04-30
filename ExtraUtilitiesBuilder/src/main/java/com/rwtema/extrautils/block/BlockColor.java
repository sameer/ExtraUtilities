// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.block;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.nbt.NBTTagCompound;
import com.rwtema.extrautils.item.ItemPaintbrush;
import net.minecraft.init.Items;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.passive.EntitySheep;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.network.packets.PacketTempChat;
import net.minecraft.item.ItemArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import com.rwtema.extrautils.ExtraUtilsProxy;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.rwtema.extrautils.texture.TextureColorBlockBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.IIconRegister;
import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;

public class BlockColor extends Block
{
    public static float[][] initColor;
    public int curMetadata;
    public boolean specialTexture;
    public String oreName;
    public Block baseBlock;
    public Object[] customRecipe;
    public int customRecipeNo;
    
    public BlockColor(final Block b, final String orename) {
        this(b, orename, (String)ReflectionHelper.getPrivateValue(Block.class, b, new String[] { "textureName", "field_149768_d" }));
    }
    
    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        return super.getBlockColor();
    }
    
    public BlockColor(final Block b, final String orename, final String texture) {
        super(b.getMaterial());
        this.curMetadata = 0;
        this.customRecipe = null;
        this.customRecipeNo = 0;
        this.setHardness((Float)ReflectionHelper.getPrivateValue(Block.class, b, new String[] { "blockHardness", "field_149782_v" }));
        this.setResistance((Float)ReflectionHelper.getPrivateValue(Block.class, b, new String[] { "blockResistance", "field_149781_w" }));
        this.setStepSound(b.stepSound);
        this.setBlockTextureName(texture);
        this.setBlockName("extrautils:color_" + b.getUnlocalizedName().substring(5));
        this.setLightLevel(b.getLightValue() / 15.0f);
        this.setLightOpacity(b.getLightOpacity());
        this.oreName = orename;
        this.baseBlock = b;
        ExtraUtils.colorblocks.add(this);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        if (!(par1IIconRegister instanceof TextureMap)) {
            return;
        }
        final String t = this.getTextureName();
        this.blockIcon = (IIcon)((TextureMap)par1IIconRegister).getTextureExtry("extrautils:bw_(" + t + ")");
        if (this.blockIcon == null) {
            final TextureAtlasSprite t2 = new TextureColorBlockBase(t);
            this.blockIcon = (IIcon)t2;
            ((TextureMap)par1IIconRegister).setTextureEntry("extrautils:bw_(" + t + ")", t2);
        }
    }
    
    public void setBlockBoundsBasedOnState(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, final int par2) {
        this.curMetadata = par2;
        return super.getIcon(par1, par2);
    }
    
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4) {
        if (par1IBlockAccess.getBlock(par2, par3, par4) == this) {
            this.curMetadata = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        }
        final float[] col = BlockColorData.getColorData(par1IBlockAccess, par2, par3, par4, this.curMetadata);
        return (int)(col[0] * 255.0f) << 16 | (int)(col[1] * 255.0f) << 8 | (int)(col[2] * 255.0f);
    }
    
    @SideOnly(Side.CLIENT)
    public int getRenderColor(final int p_149741_1_) {
        final float[] col = BlockColor.initColor[p_149741_1_];
        return (int)(col[0] * 255.0f) << 16 | (int)(col[1] * 255.0f) << 8 | (int)(col[2] * 255.0f);
    }
    
    public int getRenderType() {
        return ExtraUtilsProxy.colorBlockID;
    }
    
    public int damageDropped(final int par1) {
        return par1;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        for (int j = 0; j < 16; ++j) {
            par3List.add(new ItemStack(par1, 1, j));
        }
    }
    
    public boolean onBlockActivated(final World par1World, final int par2, final int par3, final int par4, final EntityPlayer par5EntityPlayer, final int par6, final float par7, final float par8, final float par9) {
        if (par5EntityPlayer != null && par5EntityPlayer.getCurrentEquippedItem() != null) {
            final ItemStack itemstack = par5EntityPlayer.getCurrentEquippedItem();
            final int metadata = par1World.getBlockMetadata(par2, par3, par4);
            if (itemstack.getItem() instanceof ItemArmor) {
                final ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
                if (itemarmor.hasColor(itemstack)) {
                    final int l = itemarmor.getColor(itemstack);
                    final float r = (l >> 16 & 0xFF) / 255.0f;
                    final float g = (l >> 8 & 0xFF) / 255.0f;
                    final float b = (l & 0xFF) / 255.0f;
                    if (BlockColorData.changeColorData(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), r, g, b)) {
                        return true;
                    }
                    PacketTempChat.sendChat(par5EntityPlayer, "Unable to change color at this location");
                }
            }
            else if (XUHelper.getDyeFromItemStack(itemstack) >= 0) {
                final float p = 0.9f;
                final float[] col1 = BlockColorData.getColorData((IBlockAccess)par1World, par2, par3, par4);
                final float[] col2 = EntitySheep.fleeceColorTable[BlockColored.func_150032_b(XUHelper.getDyeFromItemStack(itemstack))];
                float r2 = (col1[0] + col2[0]) / 2.0f;
                float g2 = (col1[1] + col2[1]) / 2.0f;
                float b2 = (col1[2] + col2[2]) / 2.0f;
                final float f = (Math.max(Math.max(col1[0], col1[1]), col1[2]) + Math.max(Math.max(col2[0], col2[1]), col2[2])) / 2.0f;
                final float f2 = Math.max(r2, Math.max(g2, b2));
                r2 = r2 * f / f2;
                g2 = g2 * f / f2;
                b2 = b2 * f / f2;
                r2 = col1[0] * p + col2[0] * (1.0f - p);
                g2 = col1[1] * p + col2[1] * (1.0f - p);
                b2 = col1[2] * p + col2[2] * (1.0f - p);
                if (BlockColorData.changeColorData(par1World, par2, par3, par4, metadata, r2, g2, b2)) {
                    return true;
                }
                PacketTempChat.sendChat(par5EntityPlayer, "Unable to change color at this location");
            }
            else if (itemstack.getItem() == Items.potionitem & (Items.potionitem.getEffects(itemstack) == null || Items.potionitem.getEffects(itemstack).isEmpty())) {
                final float r3 = BlockColor.initColor[metadata][0];
                final float g3 = BlockColor.initColor[metadata][1];
                final float b3 = BlockColor.initColor[metadata][2];
                if (BlockColorData.changeColorData(par1World, par2, par3, par4, metadata, r3, g3, b3)) {
                    return true;
                }
                PacketTempChat.sendChat(par5EntityPlayer, "Unable to change color at this location");
            }
            else if (itemstack.getItem() == ExtraUtils.paintBrush) {
                if (!par5EntityPlayer.isSneaking()) {
                    float r3 = 1.0f;
                    float g3 = 1.0f;
                    float b3 = 1.0f;
                    final NBTTagCompound tag = itemstack.getTagCompound();
                    if (tag != null) {
                        if (tag.hasKey("r")) {
                            r3 = tag.getInteger("r") / 255.0f;
                        }
                        if (tag.hasKey("g")) {
                            g3 = tag.getInteger("g") / 255.0f;
                        }
                        if (tag.hasKey("b")) {
                            b3 = tag.getInteger("b") / 255.0f;
                        }
                    }
                    if (BlockColorData.changeColorData(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), r3, g3, b3)) {
                        return true;
                    }
                    PacketTempChat.sendChat(par5EntityPlayer, "Unable to change color at this location");
                }
                else {
                    final float[] col3 = BlockColorData.getColorData((IBlockAccess)par1World, par2, par3, par4);
                    ItemPaintbrush.setColor(itemstack, (int)(col3[0] * 255.0f), (int)(col3[1] * 255.0f), (int)(col3[2] * 255.0f), metadata);
                }
            }
        }
        return false;
    }
    
    public boolean recolourBlock(final World world, final int x, final int y, final int z, final ForgeDirection side, final int colour) {
        final int metadata = world.getBlockMetadata(x, y, z);
        final float p = 0.9f;
        final float[] col1 = BlockColorData.getColorData((IBlockAccess)world, x, y, z);
        final float[] col2 = BlockColor.initColor[colour];
        final float r = col1[0] * p + col2[0] * (1.0f - p);
        final float g = col1[1] * p + col2[1] * (1.0f - p);
        final float b = col1[2] * p + col2[2] * (1.0f - p);
        return BlockColorData.changeColorData(world, x, y, z, metadata, r, g, b);
    }
    
    public BlockColor setCustomRecipe(final int customRecipeNo, final Object... customRecipe) {
        this.customRecipe = customRecipe;
        this.customRecipeNo = customRecipeNo;
        return this;
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
        return this.baseBlock.getCollisionBoundingBoxFromPool(world, x, y, z);
    }
    
    public void onEntityCollidedWithBlock(final World p_149670_1_, final int p_149670_2_, final int p_149670_3_, final int p_149670_4_, final Entity p_149670_5_) {
        this.baseBlock.onEntityCollidedWithBlock(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, p_149670_5_);
    }
    
    public boolean canProvidePower() {
        return this.baseBlock.canProvidePower();
    }
    
    public int isProvidingWeakPower(final IBlockAccess p_149709_1_, final int p_149709_2_, final int p_149709_3_, final int p_149709_4_, final int p_149709_5_) {
        return this.baseBlock.isProvidingWeakPower(p_149709_1_, p_149709_2_, p_149709_3_, p_149709_4_, p_149709_5_);
    }
    
    static {
        BlockColor.initColor = new float[16][3];
        final float saturation = 0.85f;
        for (int i = 0; i < 16; ++i) {
            final float r = EntitySheep.fleeceColorTable[i][0];
            final float g = EntitySheep.fleeceColorTable[i][1];
            final float b = EntitySheep.fleeceColorTable[i][2];
            final float m = (r + g + b) / 3.0f * (1.0f - saturation);
            BlockColor.initColor[i][0] = r * saturation + m;
            BlockColor.initColor[i][1] = g * saturation + m;
            BlockColor.initColor[i][2] = b * saturation + m;
        }
    }
}


