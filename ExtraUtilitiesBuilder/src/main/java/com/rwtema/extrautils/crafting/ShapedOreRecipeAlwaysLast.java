// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedOreRecipeAlwaysLast extends ShapedOreRecipe
{
    public ShapedOreRecipeAlwaysLast(final Block result, final Object... recipe) {
        super(result, recipe);
    }
    
    public ShapedOreRecipeAlwaysLast(final Item result, final Object... recipe) {
        super(result, recipe);
    }
    
    public ShapedOreRecipeAlwaysLast(final ItemStack result, final Object... recipe) {
        super(result, recipe);
    }
}


