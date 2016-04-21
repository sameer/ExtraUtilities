// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.crafting;

import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import java.util.HashMap;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class RecipeEnchantedItems extends ShapedRecipes
{
    public RecipeEnchantedItems(final int par1, final int par2, final ItemStack[] par3ArrayOfItemStack, final ItemStack par4ItemStack) {
        super(par1, par2, par3ArrayOfItemStack, par4ItemStack);
    }
    
    public static IRecipe addRecipe(final ItemStack par1ItemStack, final Object... par2ArrayOfObj) {
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;
        if (par2ArrayOfObj[i] instanceof String[]) {
            final String[] arr$;
            final String[] astring = arr$ = (String[])par2ArrayOfObj[i++];
            for (final String s2 : arr$) {
                ++k;
                j = s2.length();
                s += s2;
            }
        }
        else {
            while (par2ArrayOfObj[i] instanceof String) {
                final String s3 = (String)par2ArrayOfObj[i++];
                ++k;
                j = s3.length();
                s += s3;
            }
        }
        final HashMap hashmap = new HashMap();
        while (i < par2ArrayOfObj.length) {
            final Character character = (Character)par2ArrayOfObj[i];
            ItemStack itemstack1 = null;
            if (par2ArrayOfObj[i + 1] instanceof Item) {
                itemstack1 = new ItemStack((Item)par2ArrayOfObj[i + 1]);
            }
            else if (par2ArrayOfObj[i + 1] instanceof Block) {
                itemstack1 = new ItemStack((Block)par2ArrayOfObj[i + 1], 1, 32767);
            }
            else if (par2ArrayOfObj[i + 1] instanceof ItemStack) {
                itemstack1 = (ItemStack)par2ArrayOfObj[i + 1];
            }
            if (itemstack1.isItemEnchantable()) {
                if (!itemstack1.hasTagCompound()) {
                    itemstack1.setTagCompound(new NBTTagCompound());
                }
                if (!itemstack1.getTagCompound().hasKey("ench")) {
                    itemstack1.getTagCompound().setTag("ench", (NBTBase)new NBTTagCompound());
                }
            }
            hashmap.put(character, itemstack1);
            i += 2;
        }
        final ItemStack[] aitemstack = new ItemStack[j * k];
        for (int i2 = 0; i2 < j * k; ++i2) {
            final char c0 = s.charAt(i2);
            if (hashmap.containsKey(c0)) {
                aitemstack[i2] = hashmap.get(c0).copy();
            }
            else {
                aitemstack[i2] = null;
            }
        }
        final IRecipe shapedrecipes = (IRecipe)new RecipeEnchantedItems(j, k, aitemstack, par1ItemStack);
        return shapedrecipes;
    }
    
    public boolean matches(final InventoryCrafting inv, final World par2World) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).isItemEnchantable()) {
                return false;
            }
        }
        return super.matches(inv, par2World);
    }
}

