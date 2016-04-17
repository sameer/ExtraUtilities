// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.crafting;

import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.block.Block;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class RecipeMagicalWood extends ShapedRecipes
{
    public static final ItemStack gold;
    public static final ItemStack bookshelf;
    public static final ItemStack book;
    
    public RecipeMagicalWood() {
        super(3, 3, new ItemStack[] { RecipeMagicalWood.gold, RecipeMagicalWood.book, RecipeMagicalWood.gold, RecipeMagicalWood.book, RecipeMagicalWood.bookshelf, RecipeMagicalWood.book, RecipeMagicalWood.gold, RecipeMagicalWood.book, RecipeMagicalWood.gold }, new ItemStack((Block)ExtraUtils.decorative1, 1, 8));
    }
    
    public int getEnchantmentLevel(final ItemStack book) {
        final NBTTagList nbttaglist = (book.getItem() == Items.enchanted_book) ? Items.enchanted_book.func_92110_g(book) : book.getEnchantmentTagList();
        int m = 0;
        if (nbttaglist != null) {
            if (nbttaglist.tagCount() == 0) {
                return 0;
            }
            int j = book.getItem().getItemEnchantability();
            j /= 2;
            j = 1 + 2 * (j >> 1);
            if (j < 1) {
                j = 1;
            }
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final short short1 = nbttaglist.getCompoundTagAt(i).getShort("id");
                final short short2 = nbttaglist.getCompoundTagAt(i).getShort("lvl");
                float k = Enchantment.enchantmentsList[short1].getMinEnchantability((int)short2);
                k -= 0.5f;
                k *= 0.869f;
                k -= j;
                if (k < 1.0f) {
                    k = 1.0f;
                }
                m = Math.max(m, (int)Math.floor(k));
            }
        }
        return m;
    }
    
    public boolean matches(final InventoryCrafting par1InventoryCrafting, final World par2World) {
        return this.checkMatch(par1InventoryCrafting) > 0;
    }
    
    private int checkMatch(final InventoryCrafting par1InventoryCrafting) {
        int n = 0;
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 3; ++l) {
                final int i1 = k;
                final int j1 = l;
                final ItemStack itemstack = this.recipeItems[i1 + j1 * this.recipeWidth];
                final ItemStack itemstack2 = par1InventoryCrafting.getStackInRowAndColumn(k, l);
                if (itemstack2 == null) {
                    return 0;
                }
                if (itemstack.getItem() == RecipeMagicalWood.book.getItem()) {
                    final int m = this.getEnchantmentLevel(itemstack2);
                    if (m == 0) {
                        return 0;
                    }
                    n += m;
                }
                else {
                    if (itemstack.getItem() != itemstack2.getItem()) {
                        return 0;
                    }
                    if (itemstack.getItemDamage() != 32767 && itemstack.getItemDamage() != itemstack2.getItemDamage()) {
                        return 0;
                    }
                }
            }
        }
        n /= 8;
        if (n < 1) {
            n = 1;
        }
        if (n > 64) {
            return 64;
        }
        return n;
    }
    
    public ItemStack getCraftingResult(final InventoryCrafting par1InventoryCrafting) {
        final ItemStack itemstack = this.getRecipeOutput().copy();
        itemstack.stackSize = this.checkMatch(par1InventoryCrafting);
        return itemstack;
    }
    
    static {
        gold = new ItemStack(Items.gold_ingot);
        bookshelf = new ItemStack(Blocks.bookshelf);
        book = new ItemStack((Item)Items.enchanted_book);
    }
}
