// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.crafting;

import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;

public class RecipeFourByFourShapeless extends ShapelessRecipes implements IRecipe
{
    public RecipeFourByFourShapeless(final ItemStack par1ItemStack, final List par2List) {
        super(par1ItemStack, par2List);
    }
    
    public boolean matches(final InventoryCrafting par1InventoryCrafting, final World par2World) {
        return par1InventoryCrafting.getSizeInventory() == 4 && super.matches(par1InventoryCrafting, par2World);
    }
}


