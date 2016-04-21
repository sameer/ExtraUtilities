// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;

public class RecipeSoul implements IRecipe
{
    public boolean matches(final InventoryCrafting var1, final World var2) {
        if (var1.getSizeInventory() != 4) {
            return false;
        }
        boolean foundSword = false;
        for (int i = 0; i < var1.getSizeInventory(); ++i) {
            if (var1.getStackInSlot(i) != null) {
                if (foundSword) {
                    return false;
                }
                if (var1.getStackInSlot(i).getItem() != ExtraUtils.ethericSword) {
                    return false;
                }
                foundSword = true;
            }
        }
        return foundSword;
    }
    
    public ItemStack getCraftingResult(final InventoryCrafting var1) {
        return new ItemStack((Item)ExtraUtils.soul, 1, 2);
    }
    
    public int getRecipeSize() {
        return 1;
    }
    
    public ItemStack getRecipeOutput() {
        return new ItemStack((Item)ExtraUtils.soul);
    }
}


