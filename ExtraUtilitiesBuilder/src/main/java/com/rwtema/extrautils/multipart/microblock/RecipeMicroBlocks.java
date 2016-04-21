// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.multipart.microblock;

import com.rwtema.extrautils.multipart.FMPBase;
import net.minecraft.nbt.NBTTagCompound;
import codechicken.microblock.ItemMicroPart;
import codechicken.microblock.MicroRecipe;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;

public class RecipeMicroBlocks implements IRecipe
{
    public static Item microID;
    public final int recipeWidth;
    public final int recipeHeight;
    public final Object[] recipeItems;
    public final Item recipeOutputItemID;
    private ItemStack recipeOutput;
    private boolean field_92101_f;
    
    public RecipeMicroBlocks(final int par1, final int par2, final Object[] par3ArrayOfItemStack, final ItemStack par4ItemStack) {
        this.recipeOutputItemID = par4ItemStack.getItem();
        this.recipeWidth = par1;
        this.recipeHeight = par2;
        this.recipeItems = par3ArrayOfItemStack;
        this.recipeOutput = par4ItemStack;
    }
    
    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }
    
    public boolean matches(final InventoryCrafting par1InventoryCrafting, final World par2World) {
        for (int i = 0; i <= 3 - this.recipeWidth; ++i) {
            for (int j = 0; j <= 3 - this.recipeHeight; ++j) {
                if (this.checkMatch(par1InventoryCrafting, i, j, true)) {
                    return true;
                }
                if (this.checkMatch(par1InventoryCrafting, i, j, false)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public ItemStack getRecipeItem(final int a) {
        if (this.recipeItems[a] instanceof ItemStack) {
            return (ItemStack)this.recipeItems[a];
        }
        if (this.recipeItems[a] instanceof Integer) {
            final int damage = (int)this.recipeItems[a];
            if (this.getMicroID() != null) {
                return new ItemStack(this.getMicroID(), 1, damage);
            }
        }
        return null;
    }
    
    private boolean checkMatch(final InventoryCrafting par1InventoryCrafting, final int par2, final int par3, final boolean par4) {
        if (this.getMicroID() == null) {
            return false;
        }
        int curMat = 0;
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 3; ++l) {
                final int i1 = k - par2;
                final int j1 = l - par3;
                ItemStack itemstack = null;
                int m = -1;
                if (i1 >= 0 && j1 >= 0 && i1 < this.recipeWidth && j1 < this.recipeHeight) {
                    if (par4) {
                        m = this.recipeWidth - i1 - 1 + j1 * this.recipeWidth;
                    }
                    else {
                        m = i1 + j1 * this.recipeWidth;
                    }
                }
                final ItemStack itemstack2 = par1InventoryCrafting.getStackInRowAndColumn(k, l);
                if (this.recipeItems[m] == null || this.recipeItems[m] instanceof ItemStack) {
                    itemstack = ((this.recipeItems[m] != null) ? ((ItemStack)this.recipeItems[m]) : null);
                    if (itemstack2 != null || itemstack != null) {
                        if ((itemstack2 == null && itemstack != null) || (itemstack2 != null && itemstack == null)) {
                            return false;
                        }
                        if (itemstack.getItem() != itemstack2.getItem()) {
                            return false;
                        }
                        if (itemstack.getItemDamage() != 32767 && itemstack.getItemDamage() != itemstack2.getItemDamage()) {
                            return false;
                        }
                    }
                }
                else if (this.recipeItems[m] instanceof Integer) {
                    final int damage = (int)this.recipeItems[m];
                    if (itemstack2 == null) {
                        return false;
                    }
                    if (damage == 0) {
                        if (itemstack2.getItem() == null || itemstack2.getItem() == this.getMicroID()) {
                            return false;
                        }
                        final int mat = MicroRecipe.findMaterial(itemstack2);
                        if (mat <= 0) {
                            return false;
                        }
                        if (curMat == 0) {
                            curMat = mat;
                        }
                        else if (curMat != mat) {
                            return false;
                        }
                    }
                    else {
                        if (itemstack2.getItem() != this.getMicroID()) {
                            return false;
                        }
                        if (!itemstack2.hasTagCompound()) {
                            return false;
                        }
                        final int s = ItemMicroPart.getMaterialID(itemstack2);
                        if (s == 0) {
                            return false;
                        }
                        if (curMat == 0) {
                            curMat = s;
                        }
                        else if (curMat != s) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    
    public ItemStack getCraftingResult(final InventoryCrafting par1InventoryCrafting) {
        final ItemStack itemstack = this.getRecipeOutput().copy();
        for (int i = 0; i < par1InventoryCrafting.getSizeInventory(); ++i) {
            final ItemStack itemstack2 = par1InventoryCrafting.getStackInSlot(i);
            if (itemstack2 != null && itemstack2.getItem() == this.getMicroID() && itemstack2.hasTagCompound()) {
                NBTTagCompound tag = itemstack.getTagCompound();
                if (tag == null) {
                    tag = new NBTTagCompound();
                }
                tag.setString("mat", itemstack2.getTagCompound().getString("mat"));
                itemstack.setTagCompound((NBTTagCompound)itemstack2.stackTagCompound.copy());
                return itemstack;
            }
        }
        return itemstack;
    }
    
    private Item getMicroID() {
        if (RecipeMicroBlocks.microID == null) {
            RecipeMicroBlocks.microID = FMPBase.getMicroBlockItemId();
        }
        return RecipeMicroBlocks.microID;
    }
    
    public int getRecipeSize() {
        return this.recipeWidth * this.recipeHeight;
    }
    
    public ItemStack[] getRecipeItems() {
        final ItemStack[] t = new ItemStack[this.recipeItems.length];
        for (int i = 0; i < this.recipeItems.length; ++i) {
            t[i] = this.getRecipeItem(i);
        }
        return t;
    }
    
    static {
        RecipeMicroBlocks.microID = null;
    }
}

