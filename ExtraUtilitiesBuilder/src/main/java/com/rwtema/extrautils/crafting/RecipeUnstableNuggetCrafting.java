// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.crafting;

import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import java.util.HashMap;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.ItemStack;

public class RecipeUnstableNuggetCrafting extends RecipeUnstableIngotCrafting
{
    public RecipeUnstableNuggetCrafting(final int par1, final int par2, final ItemStack[] par3ArrayOfItemStack, final ItemStack par4ItemStack) {
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
            hashmap.put(character, itemstack1);
            i += 2;
        }
        final ItemStack[] aitemstack = new ItemStack[j * k];
        for (int i2 = 0; i2 < j * k; ++i2) {
            final char c0 = s.charAt(i2);
            if (hashmap.containsKey(c0)) {
                aitemstack[i2] = (ItemStack)hashmap.get(c0).copy();
            }
            else {
                aitemstack[i2] = null;
            }
        }
        final NBTTagCompound tags = new NBTTagCompound();
        tags.setBoolean("Bug", true);
        par1ItemStack.setTagCompound(tags);
        return (IRecipe)new RecipeUnstableNuggetCrafting(j, k, aitemstack, par1ItemStack);
    }
    
    @Override
    public ItemStack makeResult(final ItemStack n, final ItemStack d, final ItemStack s) {
        if (n.getItem() == Items.gold_nugget && d.getItem() == Items.diamond) {
            final ItemStack item = new ItemStack(ExtraUtils.unstableIngot, 1, 1);
            return item;
        }
        return null;
    }
}


