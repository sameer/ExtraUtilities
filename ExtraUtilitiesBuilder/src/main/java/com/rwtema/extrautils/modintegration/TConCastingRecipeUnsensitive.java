// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.modintegration;

import tconstruct.library.client.FluidRenderProperties;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.item.ItemStack;
import tconstruct.library.crafting.CastingRecipe;

public class TConCastingRecipeUnsensitive extends CastingRecipe
{
    public TConCastingRecipeUnsensitive(final ItemStack replacement, final FluidStack metal, final ItemStack cast, final boolean consume, final int delay, final FluidRenderProperties props) {
        super(replacement, metal, cast, consume, delay, props);
    }
    
    public TConCastingRecipeUnsensitive(final CastingRecipe recipe) {
        super(recipe.output.copy(), recipe.castingMetal.copy(), recipe.cast.copy(), recipe.consumeCast, recipe.coolTime, recipe.fluidRenderProperties);
    }
    
    public boolean matches(final FluidStack metal, final ItemStack inputCast) {
        return this.castingMetal != null && this.castingMetal.isFluidEqual(metal) && inputCast != null && this.cast != null && inputCast.getItem() == this.cast.getItem() && (this.cast.getItemDamage() == 32767 || this.cast.getItemDamage() == inputCast.getItemDamage());
    }
}
