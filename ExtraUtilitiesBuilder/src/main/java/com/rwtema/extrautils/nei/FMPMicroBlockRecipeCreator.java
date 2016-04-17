// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.nei;

import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import codechicken.microblock.handler.MicroblockProxy;
import codechicken.microblock.MicroRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import java.util.ArrayList;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.InventoryCrafting;

public class FMPMicroBlockRecipeCreator extends InventoryCrafting
{
    public static final int[] validClasses;
    public static final int[] validSizes;
    public static final int[] validSizes2;
    public static ItemStack stone;
    public static FMPMicroBlockRecipeCreator craft;
    public static int mat;
    public static ItemStack saw;
    InventoryBasic inv;
    
    public FMPMicroBlockRecipeCreator() {
        super((Container)null, 3, 3);
        this.inv = new InventoryBasic("Craft Matrix", false, 9);
    }
    
    public static ArrayList<ShapedRecipes> loadRecipes() {
        final ArrayList<ShapedRecipes> set = new ArrayList<ShapedRecipes>();
        FMPMicroBlockRecipeCreator.mat = MicroRecipe.findMaterial(FMPMicroBlockRecipeCreator.stone);
        if (FMPMicroBlockRecipeCreator.mat == -1) {
            return set;
        }
        FMPMicroBlockRecipeCreator.saw = new ItemStack(MicroblockProxy.sawDiamond());
        if (FMPMicroBlockRecipeCreator.saw == null) {
            return set;
        }
        loadThinningRecipes(set);
        loadSplittingRecipes(set);
        loadHollowRecipes(set);
        loadHollowFillingRecipes(set);
        loadGluingRecipes(set);
        return set;
    }
    
    public static void loadThinningRecipes(final ArrayList<ShapedRecipes> recipes) {
        FMPMicroBlockRecipeCreator.craft.clear();
        FMPMicroBlockRecipeCreator.craft.setInventorySlotContents(0, 0, FMPMicroBlockRecipeCreator.saw);
        for (final int mclass : FMPMicroBlockRecipeCreator.validClasses) {
            for (final int msize : FMPMicroBlockRecipeCreator.validSizes2) {
                if (msize != 8 || mclass == 0) {
                    final ItemStack a = MicroRecipe.create(1, mclass, msize, FMPMicroBlockRecipeCreator.mat);
                    FMPMicroBlockRecipeCreator.craft.setInventorySlotContents(0, 1, a);
                    final ItemStack b = MicroRecipe.getCraftingResult((InventoryCrafting)FMPMicroBlockRecipeCreator.craft);
                    if (b != null) {
                        recipes.add(new ShapedRecipes(1, 2, new ItemStack[] { FMPMicroBlockRecipeCreator.saw, a }, b));
                    }
                }
            }
        }
    }
    
    public static void loadSplittingRecipes(final ArrayList<ShapedRecipes> recipes) {
        FMPMicroBlockRecipeCreator.craft.clear();
        FMPMicroBlockRecipeCreator.craft.setInventorySlotContents(0, 0, FMPMicroBlockRecipeCreator.saw);
        for (final int mclass : FMPMicroBlockRecipeCreator.validClasses) {
            for (final int msize : FMPMicroBlockRecipeCreator.validSizes2) {
                if (msize != 8 || mclass == 0) {
                    final ItemStack a = MicroRecipe.create(1, mclass, msize, FMPMicroBlockRecipeCreator.mat);
                    FMPMicroBlockRecipeCreator.craft.setInventorySlotContents(1, 0, a);
                    final ItemStack b = MicroRecipe.getCraftingResult((InventoryCrafting)FMPMicroBlockRecipeCreator.craft);
                    if (b != null) {
                        recipes.add(new ShapedRecipes(2, 1, new ItemStack[] { FMPMicroBlockRecipeCreator.saw, a }, b));
                    }
                }
            }
        }
    }
    
    public static void loadHollowRecipes(final ArrayList<ShapedRecipes> recipes) {
        FMPMicroBlockRecipeCreator.craft.clear();
        for (final int msize : FMPMicroBlockRecipeCreator.validSizes) {
            final ItemStack a = MicroRecipe.create(1, 0, msize, FMPMicroBlockRecipeCreator.mat);
            for (int i = 0; i < 9; ++i) {
                if (i != 4) {
                    FMPMicroBlockRecipeCreator.craft.setInventorySlotContents(i, a);
                }
            }
            final ItemStack b = MicroRecipe.getCraftingResult((InventoryCrafting)FMPMicroBlockRecipeCreator.craft);
            if (b != null) {
                recipes.add(new ShapedRecipes(3, 3, new ItemStack[] { a, a, a, a, null, a, a, a, a }, b));
            }
        }
    }
    
    public static void loadHollowFillingRecipes(final ArrayList<ShapedRecipes> recipes) {
        FMPMicroBlockRecipeCreator.craft.clear();
        for (final int msize : FMPMicroBlockRecipeCreator.validSizes) {
            final ItemStack a = MicroRecipe.create(1, 1, msize, FMPMicroBlockRecipeCreator.mat);
            FMPMicroBlockRecipeCreator.craft.setInventorySlotContents(0, a);
            final ItemStack b = MicroRecipe.getCraftingResult((InventoryCrafting)FMPMicroBlockRecipeCreator.craft);
            if (b != null) {
                recipes.add(new ShapedRecipes(1, 1, new ItemStack[] { a }, b));
            }
        }
    }
    
    public static void loadGluingRecipes(final ArrayList<ShapedRecipes> recipes) {
        FMPMicroBlockRecipeCreator.craft.clear();
        for (final int mClass : FMPMicroBlockRecipeCreator.validClasses) {
            for (final int msize : FMPMicroBlockRecipeCreator.validSizes) {
                FMPMicroBlockRecipeCreator.craft.clear();
                final ItemStack a = MicroRecipe.create(1, mClass, msize, FMPMicroBlockRecipeCreator.mat);
                FMPMicroBlockRecipeCreator.craft.setInventorySlotContents(0, a);
                FMPMicroBlockRecipeCreator.craft.setInventorySlotContents(1, a);
                ItemStack b = MicroRecipe.getCraftingResult((InventoryCrafting)FMPMicroBlockRecipeCreator.craft);
                if (b != null) {
                    recipes.add(new ShapedRecipes(2, 1, new ItemStack[] { a, a }, b));
                }
                FMPMicroBlockRecipeCreator.craft.setInventorySlotContents(2, a);
                FMPMicroBlockRecipeCreator.craft.setInventorySlotContents(3, a);
                b = MicroRecipe.getCraftingResult((InventoryCrafting)FMPMicroBlockRecipeCreator.craft);
                if (b != null) {
                    recipes.add(new ShapedRecipes(2, 2, new ItemStack[] { a, a, a, a }, b));
                }
                FMPMicroBlockRecipeCreator.craft.setInventorySlotContents(4, a);
                FMPMicroBlockRecipeCreator.craft.setInventorySlotContents(5, a);
                b = MicroRecipe.getCraftingResult((InventoryCrafting)FMPMicroBlockRecipeCreator.craft);
                if (b != null) {
                    recipes.add(new ShapedRecipes(2, 3, new ItemStack[] { a, a, a, a, a, a }, b));
                }
                FMPMicroBlockRecipeCreator.craft.setInventorySlotContents(6, a);
                FMPMicroBlockRecipeCreator.craft.setInventorySlotContents(7, a);
                b = MicroRecipe.getCraftingResult((InventoryCrafting)FMPMicroBlockRecipeCreator.craft);
                if (b != null) {
                    recipes.add(new ShapedRecipes(3, 3, new ItemStack[] { a, a, a, a, a, a, a, a, null }, b));
                }
            }
        }
    }
    
    public void clear() {
        for (int i = 0; i < 9; ++i) {
            this.inv.setInventorySlotContents(i, (ItemStack)null);
        }
    }
    
    public ItemStack getStackInRowAndColumn(final int par1, final int par2) {
        if (par1 >= 0 && par1 < 3) {
            final int k = par1 + par2 * 3;
            return this.inv.getStackInSlot(k);
        }
        return null;
    }
    
    public void setInventorySlotContents(final int par1, final int par2, final ItemStack itemstack) {
        this.inv.setInventorySlotContents(par1 + par2 * 3, itemstack);
    }
    
    public int getSizeInventory() {
        return this.inv.getSizeInventory();
    }
    
    public ItemStack getStackInSlot(final int i) {
        return this.inv.getStackInSlot(i);
    }
    
    public ItemStack decrStackSize(final int i, final int j) {
        return this.inv.decrStackSize(i, j);
    }
    
    public ItemStack getStackInSlotOnClosing(final int i) {
        return this.inv.getStackInSlotOnClosing(i);
    }
    
    public void setInventorySlotContents(final int i, final ItemStack itemstack) {
        this.inv.setInventorySlotContents(i, itemstack);
    }
    
    public String getInventoryName() {
        return this.inv.getInventoryName();
    }
    
    public boolean hasCustomInventoryName() {
        return this.inv.hasCustomInventoryName();
    }
    
    public int getInventoryStackLimit() {
        return this.inv.getInventoryStackLimit();
    }
    
    public boolean isUseableByPlayer(final EntityPlayer entityplayer) {
        return false;
    }
    
    public void openInventory() {
        this.inv.openInventory();
    }
    
    public void closeInventory() {
        this.inv.closeInventory();
    }
    
    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return this.inv.isItemValidForSlot(i, itemstack);
    }
    
    static {
        validClasses = new int[] { 0, 1, 2, 3 };
        validSizes = new int[] { 4, 2, 1 };
        validSizes2 = new int[] { 8, 4, 2, 1 };
        FMPMicroBlockRecipeCreator.stone = new ItemStack(Blocks.stone);
        FMPMicroBlockRecipeCreator.craft = new FMPMicroBlockRecipeCreator();
    }
}
