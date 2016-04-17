// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.crafting;

import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import java.util.HashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class RecipeDifficultySpecific extends ShapedRecipes
{
    private static boolean isRemote;
    boolean[] validDifficulty;
    private ItemStack fakeStack;
    
    public RecipeDifficultySpecific(final int par1, final int par2, final ItemStack[] par3ArrayOfItemStack, final ItemStack par4ItemStack, final boolean[] validDifficulties, final ItemStack fakeStack) {
        super(par1, par2, par3ArrayOfItemStack, par4ItemStack);
        this.validDifficulty = new boolean[4];
        this.validDifficulty = validDifficulties;
        this.fakeStack = fakeStack;
    }
    
    public static RecipeDifficultySpecific addRecipe(final boolean[] validDifficulties, final ItemStack par1ItemStack, final String[] LoreText, final Object... par2ArrayOfObj) {
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
                aitemstack[i2] = hashmap.get(c0).copy();
            }
            else {
                aitemstack[i2] = null;
            }
        }
        final NBTTagList nbttaglist = new NBTTagList();
        for (final String aLoreText : LoreText) {
            nbttaglist.appendTag((NBTBase)new NBTTagString(aLoreText));
        }
        final NBTTagCompound display = new NBTTagCompound();
        display.setTag("Lore", (NBTBase)nbttaglist);
        final NBTTagCompound base = new NBTTagCompound();
        base.setTag("display", (NBTBase)display);
        final ItemStack item = par1ItemStack.copy();
        item.setTagCompound(base);
        final RecipeDifficultySpecific shapedrecipes = new RecipeDifficultySpecific(j, k, aitemstack, par1ItemStack, validDifficulties, item);
        return shapedrecipes;
    }
    
    public boolean matches(final InventoryCrafting par1InventoryCrafting, final World par2World) {
        if (par2World != null && par2World.difficultySetting != null) {
            RecipeDifficultySpecific.isRemote = par2World.isRemote;
            if (par2World.difficultySetting.getDifficultyId() >= 0 && par2World.difficultySetting.getDifficultyId() <= 3 && (par2World.isRemote || this.validDifficulty[par2World.difficultySetting.getDifficultyId()])) {
                return super.matches(par1InventoryCrafting, par2World);
            }
        }
        return false;
    }
    
    public ItemStack getCraftingResult(final InventoryCrafting par1InventoryCrafting) {
        final ItemStack item = super.getCraftingResult(par1InventoryCrafting);
        if (RecipeDifficultySpecific.isRemote && this.fakeStack != null) {
            return this.fakeStack;
        }
        return item;
    }
    
    public ItemStack getRecipeOutput() {
        return this.fakeStack;
    }
    
    static {
        RecipeDifficultySpecific.isRemote = false;
    }
}
