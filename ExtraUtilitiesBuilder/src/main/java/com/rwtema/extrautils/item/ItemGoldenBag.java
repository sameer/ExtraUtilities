// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.item;

import java.util.List;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.rwtema.extrautils.texture.TextureColorBlockBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.util.IIcon;
import net.minecraft.item.Item;

public class ItemGoldenBag extends Item
{
    public IIcon bwIcon;
    
    public ItemGoldenBag() {
        this.setUnlocalizedName("extrautils:golden_bag");
        this.setTextureName("extrautils:golden_bag");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setMaxStackSize(1);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IIconRegister) {
        super.registerIcons(par1IIconRegister);
        final String t = this.getIconString();
        this.bwIcon = (IIcon)((TextureMap)par1IIconRegister).getTextureExtry("extrautils:bw_(" + t + ")");
        if (this.bwIcon == null) {
            final TextureColorBlockBase t2 = new TextureColorBlockBase(t, "items");
            t2.scale = 20.0f;
            this.bwIcon = (IIcon)t2;
            ((TextureMap)par1IIconRegister).setTextureEntry("extrautils:bw_(" + t + ")", (TextureAtlasSprite)t2);
        }
    }
    
    public static boolean isMagic(final ItemStack item) {
        return item.hasTagCompound() && item.getTagCompound().hasKey("enchanted");
    }
    
    public static void setMagic(final ItemStack item) {
        NBTTagCompound tag = item.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setBoolean("enchanted", true);
        item.setTagCompound(tag);
    }
    
    public static ItemStack clearMagic(final ItemStack item) {
        if (item == null) {
            return null;
        }
        NBTTagCompound tag = item.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        if (tag.hasKey("enchanted")) {
            tag.removeTag("enchanted");
            if (tag.hasNoTags()) {
                tag = null;
            }
            item.setTagCompound(tag);
        }
        return item;
    }
    
    public ItemStack onItemRightClick(final ItemStack par1ItemStack, final World par2World, final EntityPlayer par3EntityPlayer) {
        if (!par2World.isRemote && !XUHelper.isPlayerFake(par3EntityPlayer)) {
            par3EntityPlayer.openGui((Object)ExtraUtilsMod.instance, 1, par2World, par3EntityPlayer.inventory.currentItem, 0, 0);
        }
        return par1ItemStack;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(final ItemStack par1ItemStack, final int pass) {
        return isMagic(par1ItemStack);
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final List par3List, final boolean par4) {
        if (isMagic(par1ItemStack)) {
            par3List.add("Reincarnating I");
        }
    }
    
    public int getColorFromItemStack(final ItemStack p_82790_1_, final int p_82790_2_) {
        return this.getColor(p_82790_1_);
    }
    
    public IIcon getIcon(final ItemStack stack, final int renderPass, final EntityPlayer player, final ItemStack usingItem, final int useRemaining) {
        return this.getIconIndex(stack);
    }
    
    public IIcon getIconIndex(final ItemStack item) {
        return this.hasColor(item) ? this.bwIcon : super.getIconIndex(item);
    }
    
    public boolean hasColor(final ItemStack item) {
        return item.hasTagCompound() && item.getTagCompound().hasKey("Color");
    }
    
    public int getColor(final ItemStack item) {
        return this.hasColor(item) ? item.getTagCompound().getInteger("Color") : 16777215;
    }
    
    public ItemStack setColor(final ItemStack item, final int color) {
        NBTTagCompound tag = item.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setInteger("Color", color);
        item.setTagCompound(tag);
        return item;
    }
}


