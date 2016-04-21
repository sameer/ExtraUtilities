// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import java.util.Iterator;
import java.util.ArrayList;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.util.IIcon;
import net.minecraft.item.Item;

public class ItemPaintbrush extends Item
{
    private IIcon[] icons;
    
    public ItemPaintbrush() {
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setContainerItem((Item)this);
        this.setUnlocalizedName("extrautils:paintbrush");
    }
    
    public static ItemStack setColor(final ItemStack par1ItemStack, final int color, final int damage) {
        return setColor(par1ItemStack, color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, damage);
    }
    
    public static ItemStack setColor(final ItemStack par1ItemStack, final int r, final int g, final int b, final int damage) {
        if (par1ItemStack.getTagCompound() == null) {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }
        par1ItemStack.getTagCompound().setInteger("r", r & 0xFF);
        par1ItemStack.getTagCompound().setInteger("g", g & 0xFF);
        par1ItemStack.getTagCompound().setInteger("b", b & 0xFF);
        if (damage >= 0) {
            par1ItemStack.getTagCompound().setInteger("damage", damage);
        }
        return par1ItemStack;
    }
    
    public static int getColor(final ItemStack par1ItemStack) {
        int r = 255;
        int g = 255;
        int b = 255;
        if (par1ItemStack.getTagCompound() != null) {
            if (par1ItemStack.getTagCompound().hasKey("r")) {
                r = par1ItemStack.getTagCompound().getInteger("r");
            }
            if (par1ItemStack.getTagCompound().hasKey("g")) {
                g = par1ItemStack.getTagCompound().getInteger("g");
            }
            if (par1ItemStack.getTagCompound().hasKey("b")) {
                b = par1ItemStack.getTagCompound().getInteger("b");
            }
        }
        return r << 16 | g << 8 | b;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IIconRegister) {
        (this.icons = new IIcon[2])[0] = par1IIconRegister.registerIcon("extrautils:paintbrush_base");
        this.icons[1] = par1IIconRegister.registerIcon("extrautils:paintbrush_brush");
    }
    
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(final ItemStack par1ItemStack, final int par2) {
        if (par2 == 1) {
            return getColor(par1ItemStack);
        }
        return 16777215;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }
    
    public IIcon getIcon(final ItemStack stack, final int pass) {
        return this.icons[pass];
    }
    
    public boolean doesSneakBypassUse(final World world, final int x, final int y, final int z, final EntityPlayer player) {
        return true;
    }
    
    public boolean doesContainerItemLeaveCraftingGrid(final ItemStack par1ItemStack) {
        return false;
    }
    
    public boolean onItemUse(final ItemStack item, final EntityPlayer par2EntityPlayer, final World world, final int x, final int y, final int z, final int par7, final float par8, final float par9, final float par10) {
        if (world.isRemote) {
            return true;
        }
        if (item.getTagCompound() != null && item.getTagCompound().hasKey("damage")) {
            if (ExtraUtils.colorBlockBrick != null && world.getBlock(x, y, z) == Blocks.stonebrick && world.getBlockMetadata(x, y, z) == 0) {
                world.setBlock(x, y, z, (Block)ExtraUtils.colorBlockBrick, item.getTagCompound().getInteger("damage") & 0xF, 3);
                return true;
            }
            if (ExtraUtils.coloredWood != null) {
                final Block id = world.getBlock(x, y, z);
                if (!world.isAirBlock(x, y, z)) {
                    final ArrayList<ItemStack> items = (ArrayList<ItemStack>)id.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                    if (items != null && items.size() == 1 && items.get(0).stackSize == 1 && items.get(0).getItem() == Item.getItemFromBlock(world.getBlock(x, y, z))) {
                        for (final ItemStack target : OreDictionary.getOres("plankWood")) {
                            if (OreDictionary.itemMatches(target, (ItemStack)items.get(0), false)) {
                                world.setBlock(x, y, z, (Block)ExtraUtils.coloredWood, item.getTagCompound().getInteger("damage") & 0xF, 3);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}

