// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import java.util.Map;
import cpw.mods.fml.common.Loader;
import net.minecraft.item.ItemBlock;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.ModContainer;
import java.util.Arrays;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.IInventory;

public class InvHelper
{
    public static int[] getSlots(final IInventory inv, final int side) {
        if (inv instanceof ISidedInventory) {
            return ((ISidedInventory)inv).getAccessibleSlotsFromSide(side);
        }
        if (inv != null) {
            final int size = inv.getSizeInventory();
            final int[] arr = new int[size];
            for (int i = 0; i < size; ++i) {
                arr[i] = i;
            }
            return arr;
        }
        return new int[0];
    }
    
    public static boolean canStack(final ItemStack a, final ItemStack b) {
        return a != null && b != null && a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage() && ItemStack.areItemStackTagsEqual(a, b);
    }
    
    public static boolean sameType(final ItemStack a, final ItemStack b) {
        if (a == null || b == null) {
            return false;
        }
        if (canStack(a, b)) {
            return true;
        }
        final int[] t = OreDictionary.getOreIDs(a);
        return t.length > 0 && Arrays.equals(t, OreDictionary.getOreIDs(b));
    }
    
    public static boolean sameMod(final ItemStack a, final ItemStack b) {
        return a != null && b != null && (canStack(a, b) || a.getItem() == b.getItem() || getModID(a).equals(getModID(b)));
    }
    
    public static String getModID(final ItemStack item) {
        final ModContainer ID = getModForItemStack(item);
        if (ID == null || ID.getModId() == null) {
            return "Unknown";
        }
        return ID.getModId();
    }
    
    public static ModContainer getModForItemStack(final ItemStack stack) {
        final Item item = stack.getItem();
        Class klazz = null;
        if (item == null) {
            return null;
        }
        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(item);
        klazz = item.getClass();
        if (identifier == null && item instanceof ItemBlock) {
            final Block block = ((ItemBlock)item).field_150939_a;
            identifier = GameRegistry.findUniqueIdentifierFor(block);
            klazz = block.getClass();
        }
        final Map<String, ModContainer> modList = (Map<String, ModContainer>)Loader.instance().getIndexedModList();
        if (identifier != null) {
            final ModContainer container = modList.get(identifier.modId);
            if (container != null) {
                return container;
            }
        }
        final String[] itemClassParts = klazz.getName().split("\\.");
        ModContainer closestMatch = null;
        int mostMatchingPackages = 0;
        for (final Map.Entry<String, ModContainer> entry : modList.entrySet()) {
            final Object mod = entry.getValue().getMod();
            if (mod == null) {
                continue;
            }
            final String[] modClassParts = mod.getClass().getName().split("\\.");
            int packageMatches = 0;
            for (int i = 0; i < modClassParts.length && i < itemClassParts.length && itemClassParts[i] != null && itemClassParts[i].equals(modClassParts[i]); ++i) {
                ++packageMatches;
            }
            if (packageMatches <= mostMatchingPackages) {
                continue;
            }
            mostMatchingPackages = packageMatches;
            closestMatch = entry.getValue();
        }
        return closestMatch;
    }
}


