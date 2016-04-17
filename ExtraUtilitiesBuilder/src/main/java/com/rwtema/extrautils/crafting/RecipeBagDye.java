// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.crafting;

import net.minecraft.block.BlockColored;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.item.ItemGoldenBag;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;

public class RecipeBagDye implements IRecipe
{
    public boolean matches(final InventoryCrafting inv, final World p_77569_2_) {
        boolean foundBag = false;
        boolean foundDye = false;
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            final ItemStack item = inv.getStackInSlot(i);
            if (item != null) {
                if (item.getItem() instanceof ItemGoldenBag) {
                    if (foundBag) {
                        return false;
                    }
                    foundBag = true;
                }
                else {
                    if (XUHelper.getDyeFromItemStack(item) == -1) {
                        return false;
                    }
                    foundDye = true;
                }
            }
        }
        return foundBag && foundDye;
    }
    
    public ItemStack getCraftingResult(final InventoryCrafting p_77572_1_) {
        ItemStack result = null;
        final int[] color = new int[3];
        int intensity = 0;
        int numDyes = 0;
        ItemGoldenBag itemBag = null;
        for (int k = 0; k < p_77572_1_.getSizeInventory(); ++k) {
            final ItemStack itemstack = p_77572_1_.getStackInSlot(k);
            if (itemstack != null) {
                if (itemstack.getItem() instanceof ItemGoldenBag) {
                    itemBag = (ItemGoldenBag)itemstack.getItem();
                    if (result != null) {
                        return null;
                    }
                    result = itemstack.copy();
                    result.stackSize = 1;
                    if (itemBag.hasColor(itemstack)) {
                        final int col = itemBag.getColor(result);
                        final float r = (col >> 16 & 0xFF) / 255.0f;
                        final float g = (col >> 8 & 0xFF) / 255.0f;
                        final float b = (col & 0xFF) / 255.0f;
                        intensity += (int)(Math.max(r, Math.max(g, b)) * 255.0f);
                        color[0] += (int)(r * 255.0f);
                        color[1] += (int)(g * 255.0f);
                        color[2] += (int)(b * 255.0f);
                        ++numDyes;
                    }
                }
                else {
                    final int c = XUHelper.getDyeFromItemStack(itemstack);
                    if (c == -1) {
                        return null;
                    }
                    final float[] afloat = EntitySheep.fleeceColorTable[BlockColored.func_150032_b(c)];
                    final int r2 = (int)(afloat[0] * 255.0f);
                    final int g2 = (int)(afloat[1] * 255.0f);
                    final int b2 = (int)(afloat[2] * 255.0f);
                    intensity += Math.max(r2, Math.max(g2, b2));
                    final int[] array = color;
                    final int n = 0;
                    array[n] += r2;
                    final int[] array2 = color;
                    final int n2 = 1;
                    array2[n2] += g2;
                    final int[] array3 = color;
                    final int n3 = 2;
                    array3[n3] += b2;
                    ++numDyes;
                }
            }
        }
        if (itemBag == null) {
            return null;
        }
        int r3 = color[0] / numDyes;
        int g3 = color[1] / numDyes;
        int b3 = color[2] / numDyes;
        final float i = intensity / numDyes;
        final float max = Math.max(r3, Math.max(g3, b3));
        r3 = (int)(r3 * i / max);
        g3 = (int)(g3 * i / max);
        b3 = (int)(b3 * i / max);
        final int col2 = ((r3 << 8) + g3 << 8) + b3;
        itemBag.setColor(result, col2);
        return result;
    }
    
    public int getRecipeSize() {
        return 10;
    }
    
    public ItemStack getRecipeOutput() {
        return null;
    }
}
