// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.crafting;

import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.inventory.InventoryCrafting;
import com.rwtema.extrautils.item.ItemGoldenBag;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import java.util.HashMap;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class RecipeGBEnchanting extends ShapedRecipes
{
    public RecipeGBEnchanting(final int par1, final int par2, final ItemStack[] par3ArrayOfItemStack, final ItemStack par4ItemStack) {
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
                aitemstack[i2] = hashmap.get(c0).copy();
            }
            else {
                aitemstack[i2] = null;
            }
        }
        ItemGoldenBag.setMagic(par1ItemStack);
        final IRecipe shapedrecipes = (IRecipe)new RecipeGBEnchanting(j, k, aitemstack, par1ItemStack);
        return shapedrecipes;
    }
    
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        final ItemStack item = super.getCraftingResult(inv);
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).getItem() == ExtraUtils.goldenBag) {
                final ItemStack item2 = inv.getStackInSlot(i).copy();
                ItemGoldenBag.setMagic(item2);
                return item2;
            }
        }
        return item;
    }
    
    public int getRecipeSize() {
        return 3;
    }
}

