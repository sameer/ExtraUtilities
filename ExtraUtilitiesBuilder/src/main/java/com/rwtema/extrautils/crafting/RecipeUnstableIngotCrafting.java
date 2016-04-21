// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.crafting;

import net.minecraft.init.Items;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import java.util.HashMap;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class RecipeUnstableIngotCrafting extends ShapedRecipes
{
    public RecipeUnstableIngotCrafting(final int par1, final int par2, final ItemStack[] par3ArrayOfItemStack, final ItemStack par4ItemStack) {
        super(par1, par2, par3ArrayOfItemStack, par4ItemStack);
    }
    
    public static IRecipe addRecipe(final ItemStack par1ItemStack, final Object... par2ArrayOfObj) {
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;
        if (par2ArrayOfObj[i] instanceof String[]) {
            final String[] arr$;
            final String[] astring = arr$ = (String[])par2ArrayOfObj[i++];
            for (final String s2 : arr$) {
                ++k;
                j = s2.length();
                s += s2;
            }
        }
        else {
            while (par2ArrayOfObj[i] instanceof String) {
                final String s3 = (String)par2ArrayOfObj[i++];
                ++k;
                j = s3.length();
                s += s3;
            }
        }
        final HashMap hashmap = new HashMap();
        while (i < par2ArrayOfObj.length) {
            final Character character = (Character)par2ArrayOfObj[i];
            ItemStack itemstack1 = null;
            if (par2ArrayOfObj[i + 1] instanceof Item) {
                itemstack1 = new ItemStack((Item)par2ArrayOfObj[i + 1]);
            }
            else if (par2ArrayOfObj[i + 1] instanceof Block) {
                itemstack1 = new ItemStack((Block)par2ArrayOfObj[i + 1], 1, 32767);
            }
            else if (par2ArrayOfObj[i + 1] instanceof ItemStack) {
                itemstack1 = (ItemStack)par2ArrayOfObj[i + 1];
            }
            hashmap.put(character, itemstack1);
            i += 2;
        }
        final ItemStack[] aitemstack = new ItemStack[j * k];
        for (int i2 = 0; i2 < j * k; ++i2) {
            final char c0 = s.charAt(i2);
            if (hashmap.containsKey(c0)) {
                aitemstack[i2] = ((ItemStack)hashmap.get(c0)).copy();
            }
            else {
                aitemstack[i2] = null;
            }
        }
        final NBTTagCompound tags = new NBTTagCompound();
        tags.setBoolean("Bug", true);
        par1ItemStack.setTagCompound(tags);
        final IRecipe shapedrecipes = (IRecipe)new RecipeUnstableIngotCrafting(j, k, aitemstack, par1ItemStack);
        return shapedrecipes;
    }
    
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
        if (s.getItem() != ExtraUtils.divisionSigil) {
            return null;
        }
        if (!s.hasTagCompound() || (!s.getTagCompound().hasKey("damage") && !s.getTagCompound().hasKey("stable"))) {
            return null;
        }
        return this.makeResult(n, d, s);
    }
    
    public ItemStack makeResult(final ItemStack n, final ItemStack d, final ItemStack s) {
        if (n.getItem() == Items.iron_ingot && d.getItem() == Items.diamond) {
            if (!s.getTagCompound().hasKey("stable")) {
                final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
                if (stackTrace.length < 4 || (!"net.minecraft.inventory.ContainerWorkbench".equals(stackTrace[4].getClassName()) && !"net.minecraft.inventory.ContainerWorkbench".equals(stackTrace[3].getClassName()))) {
                    return null;
                }
            }
            final ItemStack item = new ItemStack(ExtraUtils.unstableIngot, 1);
            if (s.getTagCompound().hasKey("stable")) {
                item.setItemDamage(2);
            }
            else {
                final NBTTagCompound tags = new NBTTagCompound();
                tags.setBoolean("crafting", true);
                item.setTagCompound(tags);
            }
            return item;
        }
        return null;
    }
    
    public int getRecipeSize() {
        return 3;
    }
}



