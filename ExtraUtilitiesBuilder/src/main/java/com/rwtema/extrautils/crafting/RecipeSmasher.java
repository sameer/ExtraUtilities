// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.crafting;

import net.minecraft.init.Items;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;

public class RecipeSmasher implements IRecipe
{
    public boolean matches(final InventoryCrafting craft, final World p_77569_2_) {
        return this.getCraftingResult(craft) != null;
    }
    
    public ItemStack getCraftingResult(final InventoryCrafting craft) {
        ItemStack ore = null;
        for (int i = 0; i < craft.getSizeInventory(); ++i) {
            final ItemStack stackInSlot = craft.getStackInSlot(i);
            if (stackInSlot != null) {
                if (ore != null) {
                    return null;
                }
                ore = stackInSlot;
            }
        }
        if (ore == null) {
            return null;
        }
        if (!this.isOreType(ore, "ore")) {
            return null;
        }
        final ItemStack smeltingResult = FurnaceRecipes.smelting().getSmeltingResult(ore);
        if (smeltingResult == null) {
            return null;
        }
        if (!this.isOreType(smeltingResult, "ingot")) {
            return null;
        }
        final ItemStack copy;
        final ItemStack result = copy = smeltingResult.copy();
        copy.stackSize *= 3;
        if (result.stackSize < result.getMaxStackSize()) {
            result.stackSize = result.getMaxStackSize();
        }
        return result;
    }
    
    public boolean isOreType(final ItemStack ore, final String type) {
        for (final String s : this.getOreNames(ore)) {
            if (s.startsWith(type)) {
                return true;
            }
        }
        return false;
    }
    
    public String[] getOreNames(final ItemStack ore) {
        final int[] oreIDs = OreDictionary.getOreIDs(ore);
        final String[] result = new String[oreIDs.length];
        for (int i = 0; i < oreIDs.length; ++i) {
            result[i] = OreDictionary.getOreName(oreIDs[i]);
        }
        return result;
    }
    
    public int getRecipeSize() {
        return 1;
    }
    
    public ItemStack getRecipeOutput() {
        return new ItemStack(Items.iron_ingot);
    }
}

