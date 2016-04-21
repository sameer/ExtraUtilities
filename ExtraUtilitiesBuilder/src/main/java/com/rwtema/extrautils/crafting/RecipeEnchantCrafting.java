// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.crafting;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import com.rwtema.extrautils.modintegration.EE3Integration;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import java.util.HashMap;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeEnchantCrafting extends ShapedOreRecipe implements IRecipe
{
    private static final int MAX_CRAFT_GRID_WIDTH = 3;
    private static final int MAX_CRAFT_GRID_HEIGHT = 3;
    private ItemStack output;
    private Object[] input;
    private int width;
    private int height;
    private boolean mirrored;
    
    public RecipeEnchantCrafting(final Block result, final Object... recipe) {
        this(new ItemStack(result), recipe);
    }
    
    public RecipeEnchantCrafting(final Item result, final Object... recipe) {
        this(new ItemStack(result), recipe);
    }
    
    public RecipeEnchantCrafting(final ItemStack result, Object... recipe) {
        super(result, recipe);
        this.output = null;
        this.input = null;
        this.width = 0;
        this.height = 0;
        this.mirrored = true;
        this.output = result.copy();
        String shape = "";
        int idx = 0;
        if (recipe[idx] instanceof Boolean) {
            this.mirrored = (boolean)recipe[idx];
            if (recipe[idx + 1] instanceof Object[]) {
                recipe = (Object[])recipe[idx + 1];
            }
            else {
                idx = 1;
            }
        }
        if (recipe[idx] instanceof String[]) {
            final String[] arr$;
            final String[] parts = arr$ = (String[])recipe[idx++];
            for (final String s : arr$) {
                this.width = s.length();
                shape += s;
            }
            this.height = parts.length;
        }
        else {
            while (recipe[idx] instanceof String) {
                final String s2 = (String)recipe[idx++];
                shape += s2;
                this.width = s2.length();
                ++this.height;
            }
        }
        if (this.width * this.height != shape.length()) {
            String ret = "Invalid shaped ore recipe: ";
            for (final Object tmp : recipe) {
                ret = ret + tmp + ", ";
            }
            ret += this.output;
            throw new RuntimeException(ret);
        }
        final HashMap<Character, Object> itemMap = new HashMap<Character, Object>();
        while (idx < recipe.length) {
            final Character chr = (Character)recipe[idx];
            final Object in = recipe[idx + 1];
            if (in instanceof ItemStack) {
                itemMap.put(chr, ((ItemStack)in).copy());
            }
            else if (in instanceof Item) {
                itemMap.put(chr, new ItemStack((Item)in));
            }
            else if (in instanceof Block) {
                itemMap.put(chr, new ItemStack((Block)in, 1, 32767));
            }
            else {
                if (!(in instanceof String)) {
                    String ret2 = "Invalid shaped ore recipe: ";
                    for (final Object tmp2 : recipe) {
                        ret2 = ret2 + tmp2 + ", ";
                    }
                    ret2 += this.output;
                    throw new RuntimeException(ret2);
                }
                itemMap.put(chr, OreDictionary.getOres((String)in));
            }
            idx += 2;
        }
        this.input = new Object[this.width * this.height];
        int x = 0;
        for (final char chr2 : shape.toCharArray()) {
            this.input[x++] = itemMap.get(chr2);
        }
        final Object[] copyInput = new Object[this.input.length];
        for (int i = 0; i < this.input.length; ++i) {
            copyInput[i] = this.input[i];
            if (this.input[i] instanceof ItemStack) {
                ItemStack itemStack = (ItemStack)this.input[i];
                if (itemStack.isItemEnchanted()) {
                    itemStack = itemStack.copy();
                    itemStack.setTagCompound((NBTTagCompound)null);
                    copyInput[i] = itemStack;
                }
            }
        }
        EE3Integration.addRecipe(this.output, copyInput);
    }
    
    public ItemStack getCraftingResult(final InventoryCrafting var1) {
        return this.output.copy();
    }
    
    public int getRecipeSize() {
        return this.input.length;
    }
    
    public ItemStack getRecipeOutput() {
        return this.output;
    }
    
    public boolean matches(final InventoryCrafting inv, final World world) {
        for (int x = 0; x <= 3 - this.width; ++x) {
            for (int y = 0; y <= 3 - this.height; ++y) {
                if (this.checkMatch(inv, x, y, false)) {
                    return true;
                }
                if (this.mirrored && this.checkMatch(inv, x, y, true)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean checkMatch(final InventoryCrafting inv, final int startX, final int startY, final boolean mirror) {
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                final int subX = x - startX;
                final int subY = y - startY;
                Object target = null;
                if (subX >= 0 && subY >= 0 && subX < this.width && subY < this.height) {
                    if (mirror) {
                        target = this.input[this.width - subX - 1 + subY * this.width];
                    }
                    else {
                        target = this.input[subX + subY * this.width];
                    }
                }
                final ItemStack slot = inv.getStackInRowAndColumn(x, y);
                if (target instanceof ItemStack) {
                    final ItemStack stack = (ItemStack)target;
                    if (!OreDictionary.itemMatches(stack, slot, false)) {
                        return false;
                    }
                    final Map<Integer, Integer> enchants = (Map<Integer, Integer>)EnchantmentHelper.getEnchantments(stack);
                    if (!enchants.isEmpty()) {
                        final Map<Integer, Integer> other = (Map<Integer, Integer>)EnchantmentHelper.getEnchantments(slot);
                        for (final Map.Entry<Integer, Integer> entry : enchants.entrySet()) {
                            final Integer t = other.get(entry.getKey());
                            if (t == null || t < entry.getValue()) {
                                return false;
                            }
                        }
                    }
                }
                else if (target instanceof ArrayList) {
                    boolean matched = false;
                    for (Iterator<ItemStack> itr = ((ArrayList)target).iterator(); itr.hasNext() && !matched; matched = OreDictionary.itemMatches((ItemStack)itr.next(), slot, false)) {}
                    if (!matched) {
                        return false;
                    }
                }
                else if (target == null && slot != null) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public RecipeEnchantCrafting setMirrored(final boolean mirror) {
        this.mirrored = mirror;
        return this;
    }
    
    public Object[] getInput() {
        return this.input;
    }
}

