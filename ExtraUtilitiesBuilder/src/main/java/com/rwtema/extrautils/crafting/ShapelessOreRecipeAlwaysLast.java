// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessOreRecipeAlwaysLast extends ShapelessOreRecipe
{
    public ShapelessOreRecipeAlwaysLast(final Block result, final Object... recipe) {
        super(result, recipe);
    }
    
    public ShapelessOreRecipeAlwaysLast(final Item result, final Object... recipe) {
        super(result, recipe);
    }
    
    public ShapelessOreRecipeAlwaysLast(final ItemStack result, final Object... recipe) {
        super(result, recipe);
    }
}


