// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.enderconstructor;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import java.util.HashMap;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import java.util.Iterator;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import java.util.Collection;
import com.rwtema.extrautils.helper.ThaumcraftHelper;
import cpw.mods.fml.common.Loader;
import com.rwtema.extrautils.LogHelper;
import net.minecraft.item.crafting.CraftingManager;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import java.util.ArrayList;

public class EnderConstructorRecipesHandler
{
    public static ArrayList<IRecipe> recipes;
    
    public static void addRecipe(final ItemStack par1ItemStack, final Object... par2ArrayOfObj) {
        registerRecipe((IRecipe)newRecipe(par1ItemStack, par2ArrayOfObj));
    }
    
    public static void registerRecipe(final IRecipe recipe) {
        EnderConstructorRecipesHandler.recipes.add(recipe);
    }
    
    public static void postInit() {
        if (ExtraUtils.qed == null) {
            return;
        }
        if (ExtraUtils.qedList.isEmpty()) {
            return;
        }
        final ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        for (final Object o : CraftingManager.getInstance().getRecipeList()) {
            final IRecipe recipe = (IRecipe)o;
            if (recipe == null) {
                continue;
            }
            ItemStack item;
            try {
                item = recipe.getRecipeOutput();
            }
            catch (Exception e) {
                LogHelper.info("Caught error from Recipe", new Object[0]);
                e.printStackTrace();
                continue;
            }
            if (item == null) {
                continue;
            }
            if (item.getItem() == null) {
                new RuntimeException("ItemStack with null item found in " + recipe.getClass().getSimpleName() + " getRecipeOutput()").printStackTrace();
            }
            else {
                String s;
                try {
                    s = item.getUnlocalizedName();
                }
                catch (Exception e2) {
                    new RuntimeException("Caught error from ItemStack in getRecipeOutput()", e2).printStackTrace();
                    continue;
                }
                if (!ExtraUtils.qedList.contains(s)) {
                    continue;
                }
                items.add(item);
                EnderConstructorRecipesHandler.recipes.add(recipe);
            }
        }
        if (Loader.isModLoaded("Thaumcraft")) {
            ThaumcraftHelper.handleQEDRecipes(items);
        }
        CraftingManager.getInstance().getRecipeList().removeAll(EnderConstructorRecipesHandler.recipes);
        if (!ExtraUtils.disableQEDIngotSmeltRecipes) {
            for (final String oreName : OreDictionary.getOreNames()) {
                if (oreName.startsWith("ore")) {
                    for (final ItemStack ore : OreDictionary.getOres(oreName)) {
                        final ItemStack smeltingResult = FurnaceRecipes.smelting().getSmeltingResult(ore);
                        if (smeltingResult == null) {
                            continue;
                        }
                        for (final int i : OreDictionary.getOreIDs(smeltingResult)) {
                            if (OreDictionary.getOreName(i).startsWith("ingot")) {
                                final ItemStack copy;
                                final ItemStack result = copy = smeltingResult.copy();
                                copy.stackSize *= 3;
                                if (result.stackSize > result.getMaxStackSize()) {
                                    result.stackSize = result.getMaxStackSize();
                                }
                                EnderConstructorRecipesHandler.recipes.add((IRecipe)new ShapedOreRecipe(result, new Object[] { "Oc", 'O', ore, 'c', Items.coal }));
                                EnderConstructorRecipesHandler.recipes.add((IRecipe)new ShapelessOreRecipe(result, new Object[] { ore, Items.coal }));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static ItemStack findMatchingRecipe(final InventoryCrafting par1InventoryCrafting, final World par2World) {
        for (final IRecipe irecipe : EnderConstructorRecipesHandler.recipes) {
            if (irecipe.matches(par1InventoryCrafting, par2World)) {
                return irecipe.getCraftingResult(par1InventoryCrafting);
            }
        }
        return null;
    }
    
    public static ShapedRecipes newRecipe(final ItemStack par1ItemStack, final Object... par2ArrayOfObj) {
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
        final HashMap<Character, ItemStack> hashmap = new HashMap<Character, ItemStack>();
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
                aitemstack[i2] = ((ItemStack)hashmap.get(c0)).copy();
            }
            else {
                aitemstack[i2] = null;
            }
        }
        final ShapedRecipes shapedrecipes = new ShapedRecipes(j, k, aitemstack, par1ItemStack);
        return shapedrecipes;
    }
    
    static {
        EnderConstructorRecipesHandler.recipes = new ArrayList<IRecipe>();
    }
}



