// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.modintegration;

import cpw.mods.fml.common.Loader;
import java.util.ArrayList;
import java.util.Iterator;
import com.rwtema.extrautils.block.BlockColor;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.ExtraUtils;
import com.google.common.base.Throwables;
import java.util.List;
import java.lang.reflect.Method;

public class EE3Integration
{
    static final boolean EE3Present;
    public static Method addRecipe;
    public static Method setAsNotLearnable;
    public static Method setAsNotRecoverable;
    public static Method addPreAssignedEnergyValue;
    static List<EE3Recipe> recipes;
    
    public static void addPreAssignedEnergyValue(final Object object, final float val) {
        if (!EE3Integration.EE3Present) {
            return;
        }
        try {
            EE3Integration.addPreAssignedEnergyValue.invoke(null, object, val);
        }
        catch (Exception e) {
            throw Throwables.propagate((Throwable)e);
        }
    }
    
    public static void setAsNotLearnable(final Object o) {
        if (!EE3Integration.EE3Present) {
            return;
        }
        try {
            EE3Integration.setAsNotLearnable.invoke(null, o);
        }
        catch (Exception e) {
            throw Throwables.propagate((Throwable)e);
        }
    }
    
    public static void setAsNotRecoverable(final Object o) {
        if (!EE3Integration.EE3Present) {
            return;
        }
        try {
            EE3Integration.setAsNotRecoverable.invoke(null, o);
        }
        catch (Exception e) {
            throw Throwables.propagate((Throwable)e);
        }
    }
    
    public static void addEMCRecipes() {
        if (!EE3Integration.EE3Present) {
            return;
        }
        if (ExtraUtils.cursedEarthEnabled) {
            addRecipe(new ItemStack(ExtraUtils.cursedEarth), Blocks.dirt, Items.rotten_flesh);
        }
        if (ExtraUtils.enderLilyEnabled) {
            addRecipe(new ItemStack((Block)ExtraUtils.enderLily), new ItemStack(Items.ender_pearl, 64));
        }
        if (ExtraUtils.divisionSigilEnabled) {
            addRecipe(new ItemStack(ExtraUtils.divisionSigil, 2), new ItemStack(Items.ender_pearl, 4), Items.nether_star);
        }
        if (ExtraUtils.wateringCanEnabled) {
            addRecipe(new ItemStack((Item)ExtraUtils.wateringCan, 1, 0), new ItemStack((Item)ExtraUtils.wateringCan, 1, 1), new FluidStack(FluidRegistry.WATER, 1000));
        }
        if (ExtraUtils.decorative1 != null) {
            addRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 8), Blocks.bookshelf, new ItemStack(Items.gold_ingot, 4), new ItemStack(Items.ender_pearl, 4));
            addRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 2), Blocks.quartz_block);
        }
        for (final BlockColor colorblock : ExtraUtils.colorblocks) {
            for (int i = 0; i < 16; ++i) {
                addRecipe(new ItemStack((Block)colorblock, 1, i), colorblock.baseBlock);
            }
        }
        if (ExtraUtils.decorative2 != null) {
            addRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 0), Blocks.glass);
        }
        if (ExtraUtils.unstableIngot != null) {
            setAsNotLearnable(new ItemStack(ExtraUtils.unstableIngot, 1, 32767));
            addRecipe(new ItemStack(ExtraUtils.unstableIngot, 1, 0), ExtraUtils.unstableIngot, 9, 2);
        }
        if (ExtraUtils.soul != null) {
            addPreAssignedEnergyValue(new ItemStack((Item)ExtraUtils.soul), 1515413.0f);
            setAsNotLearnable(new ItemStack((Item)ExtraUtils.soul, 1, 32767));
        }
    }
    
    public static void addRecipe(final ItemStack itemStack, final Object... inputs) {
        if (!EE3Integration.EE3Present) {
            return;
        }
        final ArrayList<Object> items = new ArrayList<Object>(inputs.length);
        for (final Object a : inputs) {
            if (a != null) {
                if (a instanceof ItemStack) {
                    ItemStack input = (ItemStack)a;
                    if (input.stackSize != 0) {
                        if (input.stackSize == 1) {
                            items.add(input.copy());
                        }
                        else {
                            final int k = input.stackSize;
                            input = input.copy();
                            input.stackSize = 1;
                            for (int i = 0; i < k; ++i) {
                                items.add(input.copy());
                            }
                        }
                    }
                }
                else {
                    items.add(a);
                }
            }
        }
        if (items.isEmpty()) {
            return;
        }
        EE3Integration.recipes.add(new EE3Recipe(itemStack, items));
    }
    
    public static void finalRegister() {
        if (!EE3Integration.EE3Present) {
            return;
        }
        try {
            for (final EE3Recipe recipe : EE3Integration.recipes) {
                EE3Integration.addRecipe.invoke(null, recipe.output, recipe.inputs);
            }
        }
        catch (Exception e) {
            throw Throwables.propagate((Throwable)e);
        }
    }
    
    static {
        boolean found = false;
        if (Loader.isModLoaded("EE3")) {
            try {
                Class<?> aClass = Class.forName("com.pahimar.ee3.api.exchange.RecipeRegistryProxy");
                EE3Integration.addRecipe = aClass.getDeclaredMethod("addRecipe", Object.class, List.class);
                aClass = Class.forName("com.pahimar.ee3.api.knowledge.AbilityRegistryProxy");
                EE3Integration.setAsNotLearnable = aClass.getDeclaredMethod("setAsNotLearnable", Object.class);
                EE3Integration.setAsNotRecoverable = aClass.getDeclaredMethod("setAsNotRecoverable", Object.class);
                aClass = Class.forName("com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy");
                EE3Integration.addPreAssignedEnergyValue = aClass.getDeclaredMethod("addPreAssignedEnergyValue", Object.class, Float.TYPE);
                found = true;
            }
            catch (Exception e) {
                found = false;
            }
        }
        EE3Present = found;
        EE3Integration.recipes = new ArrayList<EE3Recipe>();
    }
    
    public static class EE3Recipe
    {
        ItemStack output;
        List<?> inputs;
        
        public EE3Recipe(final ItemStack itemStack, final List object) {
            this.output = itemStack;
            this.inputs = (List<?>)object;
        }
    }
}
