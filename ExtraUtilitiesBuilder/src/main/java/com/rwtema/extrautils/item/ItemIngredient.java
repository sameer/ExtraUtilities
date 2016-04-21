// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import net.minecraft.client.renderer.texture.IIconRegister;
import java.util.HashSet;
import net.minecraft.util.IIcon;
import java.util.ArrayList;
import net.minecraft.item.Item;

public class ItemIngredient extends Item
{
    public ArrayList<String> textures;
    public ArrayList<String> names;
    public ArrayList<IIcon> icons;
    public HashSet<Integer> ids;
    public int numItems;
    
    public ItemIngredient() {
        this.textures = new ArrayList<String>();
        this.names = new ArrayList<String>();
        this.icons = new ArrayList<IIcon>();
        this.ids = new HashSet<Integer>();
        this.numItems = 1;
        this.addItem(0, "Error", "error");
        this.ids.remove(0);
        this.setHasSubtypes(true);
    }
    
    public void addItem(final int metadata, final String name, final String texture) {
        if (this.numItems < 1 + metadata) {
            this.numItems = 1 + metadata;
            this.textures.ensureCapacity(this.numItems);
            this.names.ensureCapacity(this.numItems);
            this.icons.ensureCapacity(this.numItems);
        }
        this.textures.set(metadata, texture);
        this.names.set(metadata, name);
        this.ids.add(metadata);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IIconRegister) {
        for (final Integer i : this.ids) {
            this.icons.set(i, par1IIconRegister.registerIcon("extrautils:" + this.textures.get(i)));
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        for (final Integer id : this.ids) {
            par3List.add(new ItemStack(par1, 1, (int)id));
        }
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int par1) {
        return this.icons.get(this.getMetaData(par1));
    }
    
    public int getMetaData(final ItemStack item) {
        return this.getMetaData(item.getItemDamage());
    }
    
    public int getMetaData(final int metadata) {
        if (!this.ids.contains(metadata)) {
            return 0;
        }
        return metadata;
    }
    
    public String getUnlocalizedName(final ItemStack par1ItemStack) {
        return this.getUnlocalizedName() + "." + this.getMetaData(par1ItemStack);
    }
}

