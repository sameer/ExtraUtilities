// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.crafting;

import java.util.Iterator;
import java.util.Map;
import java.util.LinkedHashMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;

public class RecipeUnEnchanting implements IRecipe
{
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
        if (!s.hasTagCompound() || !s.getTagCompound().hasKey("damage")) {
            return null;
        }
        if (n.getItem() == Items.iron_ingot && d.getItem() == Items.diamond) {
            return null;
        }
        final Map ne = EnchantmentHelper.getEnchantments(n);
        final Map de = EnchantmentHelper.getEnchantments(d);
        if (de == null || de.isEmpty()) {
            if (d.getItem() == Items.book && n.getItem() != Items.book && ne != null && !ne.isEmpty()) {
                final LinkedHashMap re = new LinkedHashMap();
                for (final Object o : ne.keySet()) {
                    final int id = (int)o;
                    final int level = ne.get(id);
                    if (level > 1) {
                        re.put(id, level - 1);
                    }
                }
                final ItemStack r = n.copy();
                if (r.hasTagCompound()) {
                    r.getTagCompound().removeTag("ench");
                }
                EnchantmentHelper.setEnchantments((Map)re, r);
                return r;
            }
            if (ne == null || ne.isEmpty()) {
                return null;
            }
        }
        final LinkedHashMap re = new LinkedHashMap();
        boolean overlap = false;
        for (final Object o2 : ne.keySet()) {
            final int id2 = (int)o2;
            int level2 = ne.get(id2);
            if (de != null && de.containsKey(id2)) {
                overlap = true;
                level2 -= de.get(id2);
                if (level2 <= 0) {
                    continue;
                }
                re.put(id2, level2);
            }
            else {
                re.put(id2, level2);
            }
        }
        if (!overlap) {
            return null;
        }
        final ItemStack r2 = n.copy();
        if (r2.hasTagCompound()) {
            r2.getTagCompound().removeTag("ench");
        }
        EnchantmentHelper.setEnchantments((Map)re, r2);
        return r2;
    }
    
    public int getRecipeSize() {
        return 3;
    }
    
    public ItemStack getRecipeOutput() {
        return null;
    }
}
