// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.crafting;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import com.rwtema.extrautils.helper.XURandom;
import java.util.Random;
import net.minecraft.item.crafting.IRecipe;

public class RecipeUnsigil implements IRecipe
{
    Random rand;
    
    public RecipeUnsigil() {
        this.rand = XURandom.getInstance();
    }
    
    public boolean matches(final InventoryCrafting inv, final World world) {
        boolean hasSigil = false;
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).getItem() == ExtraUtils.divisionSigil) {
                if (inv.getStackInSlot(i).hasTagCompound()) {
                    return false;
                }
                hasSigil = true;
            }
        }
        return hasSigil;
    }
    
    public ItemStack getCraftingResult(final InventoryCrafting inventorycrafting) {
        return new ItemStack(Items.iron_ingot, 1 + this.rand.nextInt(1 + this.rand.nextInt(1 + this.rand.nextInt(1 + this.rand.nextInt(1 + this.rand.nextInt(63))))));
    }
    
    public int getRecipeSize() {
        return 1;
    }
    
    public ItemStack getRecipeOutput() {
        return new ItemStack(Items.iron_ingot, 1);
    }
}


