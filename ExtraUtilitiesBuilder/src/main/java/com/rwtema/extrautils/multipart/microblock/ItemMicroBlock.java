// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.multipart.microblock;

import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import codechicken.lib.vec.BlockCoord;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import scala.Tuple2;
import java.util.Iterator;
import java.util.List;
import codechicken.microblock.MicroMaterialRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ICreativeTabSorting;
import net.minecraft.item.Item;

public class ItemMicroBlock extends Item implements ICreativeTabSorting
{
    public static ItemMicroBlock instance;
    
    public ItemMicroBlock() {
        ItemMicroBlock.instance = this;
        if (ExtraUtils.showMultiblocksTab) {
            this.setCreativeTab((CreativeTabs)CreativeTabMicroBlocks.instance);
        }
        this.setUnlocalizedName("extrautils:microblocks");
        this.setHasSubtypes(true);
    }
    
    public static ItemStack getStack(ItemStack item, final String material) {
        item = item.copy();
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setString("mat", material);
        item.setTagCompound(tag);
        return item;
    }
    
    public String getItemStackDisplayName(final ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return "ERR";
        }
        final String material = stack.getTagCompound().getString("mat");
        if (MicroMaterialRegistry.getMaterial(material) == null) {
            return "ERR";
        }
        return MicroMaterialRegistry.getMaterial(material).getLocalizedName() + " " + super.getItemStackDisplayName(stack);
    }
    
    public String getUnlocalizedName(final ItemStack par1ItemStack) {
        return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        for (final IMicroBlock microBlock : RegisterMicroBlocks.mParts.values()) {
            if (!microBlock.hideCreativeTab()) {
                final int meta = microBlock.getMetadata();
                final ItemStack item = new ItemStack(par1, 1, meta);
                for (final Tuple2<String, MicroMaterialRegistry.IMicroMaterial> t : MicroMaterialRegistry.getIdMap()) {
                    par3List.add(getStack(item, (String)t._1()));
                }
            }
        }
    }
    
    public double getHitDepth(final Vector3 vhit, final int side) {
        return vhit.copy().scalarProject(Rotation.axes[side]) + (side % 2 ^ 0x1);
    }
    
    public boolean onItemUse(final ItemStack item, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ) {
        final BlockCoord pos = new BlockCoord(x, y, z);
        final Vector3 vhit = new Vector3((double)hitX, (double)hitY, (double)hitZ);
        final double d = this.getHitDepth(vhit, side);
        if (d < 1.0 && this.place(item, player, world, pos, side, vhit)) {
            return true;
        }
        pos.offset(side);
        return this.place(item, player, world, pos, side, vhit);
    }
    
    public boolean place(final ItemStack stack, final EntityPlayer player, final World world, final BlockCoord pos, final int side, final Vector3 arg5) {
        final TMultiPart part = this.newPart(stack, player, world, pos, side, arg5);
        if (part == null || !TileMultipart.canPlacePart(world, pos, part)) {
            return false;
        }
        if (!world.isRemote) {
            TileMultipart.addPart(world, pos, part);
        }
        if (!player.capabilities.isCreativeMode) {
            --stack.stackSize;
        }
        return true;
    }
    
    public TMultiPart newPart(final ItemStack stack, final EntityPlayer player, final World world, final BlockCoord pos, final int side, final Vector3 arg5) {
        if (!stack.hasTagCompound()) {
            return null;
        }
        final String material = stack.getTagCompound().getString("mat");
        if (material.equals("") || MicroMaterialRegistry.getMaterial(material) == null) {
            return null;
        }
        final IMicroBlock part = RegisterMicroBlocks.mParts.get(stack.getItemDamage());
        if (part != null) {
            return part.placePart(stack, player, world, pos, side, arg5, MicroMaterialRegistry.materialID(material));
        }
        return null;
    }
    
    public String getSortingName(final ItemStack par1ItemStack) {
        return par1ItemStack.getUnlocalizedName() + "_" + par1ItemStack.getItemDamage() + "_" + par1ItemStack.getDisplayName();
    }
}
