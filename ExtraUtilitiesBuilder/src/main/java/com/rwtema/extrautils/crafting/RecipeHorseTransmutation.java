// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.crafting;

import net.minecraft.init.Items;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;

public class RecipeHorseTransmutation implements IRecipe
{
    public boolean matches(final InventoryCrafting inventorycrafting, final World world) {
        return this.getCraftingResult(inventorycrafting) != null;
    }
    
    public ItemStack getCraftingResult(final InventoryCrafting inventorycrafting) {
        if (inventorycrafting.getSizeInventory() != 9) {
            return null;
        }
        int curRow = -1;
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                if (inventorycrafting.getStackInRowAndColumn(x, y) != null) {
                    if (curRow == -1) {
                        if (y != 0) {
                            return null;
                        }
                        curRow = x;
                    }
                    else if (x != curRow) {
                        return null;
                    }
                }
                else if (curRow == x) {
                    return null;
                }
            }
        }
        if (curRow == -1) {
            return null;
        }
        int x = curRow;
        final ItemStack n = inventorycrafting.getStackInRowAndColumn(x, 0);
        final ItemStack s = inventorycrafting.getStackInRowAndColumn(x, 1);
        final ItemStack d = inventorycrafting.getStackInRowAndColumn(x, 2);
        if (ExtraUtils.divisionSigil == null || s.getItem() != ExtraUtils.divisionSigil) {
            return null;
        }
        if (!s.hasTagCompound() || !s.getTagCompound().hasKey("damage")) {
            return null;
        }
        if (n.getItem() == Items.iron_ingot || d.getItem() == Items.diamond) {
            return null;
        }
        if (n.getItem() != ExtraUtils.goldenLasso || !n.hasTagCompound()) {
            return null;
        }
        if (!n.getTagCompound().hasKey("id") || !n.getTagCompound().getString("id").equals("EntityHorse")) {
            return null;
        }
        if (!n.getTagCompound().hasKey("Type")) {
            return null;
        }
        final int type = n.getTagCompound().getInteger("Type");
        final ItemStack r = n.copy();
        if (d.getItem() == Items.rotten_flesh && type != 3) {
            r.getTagCompound().setInteger("Type", 3);
        }
        if (d.getItem() == Items.bone && type != 4) {
            r.getTagCompound().setInteger("Type", 4);
        }
        return r;
    }
    
    public int getRecipeSize() {
        return 4;
    }
    
    public ItemStack getRecipeOutput() {
        return null;
    }
}


