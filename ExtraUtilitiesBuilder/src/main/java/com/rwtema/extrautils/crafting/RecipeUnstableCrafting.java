// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.crafting;

import com.rwtema.extrautils.item.ItemUnstableIngot;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import java.util.Iterator;
import com.rwtema.extrautils.modintegration.EE3Integration;
import net.minecraft.init.Items;
import com.rwtema.extrautils.ExtraUtils;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeUnstableCrafting extends ShapedOreRecipe implements IRecipe
{
    public static final NBTTagCompound nbt;
    private ItemStack stableOutput;
    ShapedOreRecipe checker;
    
    public RecipeUnstableCrafting(final ItemStack result, final Object... recipe) {
        super(result, recipe);
        this.checker = new ShapedOreRecipe(result, recipe);
        final Object[] input = super.getInput();
        final ArrayList<Object> ee3input = new ArrayList<Object>();
        for (final Object anInput : input) {
            boolean flag = true;
            if (anInput instanceof ArrayList) {
                final ArrayList<ItemStack> itemStacks = (ArrayList<ItemStack>)anInput;
                for (final ItemStack itemStack : itemStacks) {
                    if (itemStack.getItem() == ExtraUtils.unstableIngot && itemStack.getItemDamage() == 2) {
                        itemStack.setTagCompound(RecipeUnstableCrafting.nbt);
                        ee3input.add(Items.diamond);
                        ee3input.add(Items.iron_ingot);
                        flag = false;
                        break;
                    }
                }
            }
            if (flag) {
                ee3input.add(anInput);
            }
        }
        EE3Integration.addRecipe(this.getRecipeOutput().copy(), ee3input.toArray());
    }
    
    public RecipeUnstableCrafting setStableItem(final Item stack) {
        if (stack != null) {
            this.stableOutput = new ItemStack(stack);
        }
        return this;
    }
    
    public RecipeUnstableCrafting setStable(final ItemStack stack) {
        this.stableOutput = stack;
        return this;
    }
    
    public RecipeUnstableCrafting addStableEnchant(final Enchantment enchantment, final int level) {
        if (this.stableOutput == null) {
            this.stableOutput = this.getRecipeOutput().copy();
        }
        this.stableOutput.addEnchantment(enchantment, level);
        return this;
    }
    
    public boolean matches(final InventoryCrafting par1InventoryCrafting, final World par2World) {
        return par2World != null && this.checker.matches(par1InventoryCrafting, par2World) && !this.hasExpired(par1InventoryCrafting, par2World);
    }
    
    public boolean hasExpired(final InventoryCrafting par1InventoryCrafting, final World par2World) {
        for (int i = 0; i < par1InventoryCrafting.getSizeInventory(); ++i) {
            final ItemStack item = par1InventoryCrafting.getStackInSlot(i);
            if (item != null && item.getItem() == ExtraUtils.unstableIngot && item.getItemDamage() == 0 && item.hasTagCompound()) {
                if (!item.getTagCompound().hasKey("creative")) {
                    if (!item.getTagCompound().hasKey("stable")) {
                        if (!item.getTagCompound().hasKey("dimension") && !item.getTagCompound().hasKey("time") && item.getTagCompound().hasKey("crafting")) {
                            return true;
                        }
                        final long t = (200L - (par2World.getTotalWorldTime() - item.getTagCompound().getLong("time"))) / 20L;
                        if (par2World.provider.dimensionId != item.getTagCompound().getInteger("dimension") || t < 0L) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public boolean hasStable(final InventoryCrafting par1InventoryCrafting) {
        for (int i = 0; i < par1InventoryCrafting.getSizeInventory(); ++i) {
            final ItemStack item = par1InventoryCrafting.getStackInSlot(i);
            if (item != null && item.getItem() == ExtraUtils.unstableIngot && item.getItemDamage() == 0 && item.hasTagCompound()) {
                if (!ItemUnstableIngot.isStable(item)) {
                    return false;
                }
                if (!ItemUnstableIngot.isSuperStable(item)) {
                    return false;
                }
                if (item.getTagCompound().hasKey("time")) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public ItemStack getCraftingResult(final InventoryCrafting par1InventoryCrafting) {
        if (this.stableOutput != null && this.hasStable(par1InventoryCrafting)) {
            return this.stableOutput.copy();
        }
        return super.getCraftingResult(par1InventoryCrafting);
    }
    
    public static RecipeUnstableCrafting addRecipe(final ItemStack itemStack, final Object... objects) {
        return new RecipeUnstableCrafting(itemStack, objects);
    }
    
    static {
        (nbt = new NBTTagCompound()).setBoolean("isNEI", true);
    }
}

