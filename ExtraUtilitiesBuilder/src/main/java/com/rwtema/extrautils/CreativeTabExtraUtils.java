// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item;
import java.util.Iterator;
import java.util.Collection;
import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;

public class CreativeTabExtraUtils extends CreativeTabs
{
    private itemSorter alphabeticalSorter;
    
    public CreativeTabExtraUtils(final String label) {
        super(label);
        this.alphabeticalSorter = new itemSorter();
    }
    
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        if (ExtraUtils.angelBlock != null) {
            return new ItemStack(ExtraUtils.angelBlock, 1, 0);
        }
        if (ExtraUtils.creativeTabIcon != null) {
            return new ItemStack(ExtraUtils.creativeTabIcon, 1, 0);
        }
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    public void displayAllReleventItems(final List par1List) {
        final List newList = new ArrayList();
        super.displayAllReleventItems(newList);
        Collections.sort((List<Object>)newList, this.alphabeticalSorter);
        par1List.addAll(newList);
        for (final ItemStack item : (ArrayList<ItemStack>)newList) {
            if (item.getDisplayName().endsWith(".name")) {
                LogHelper.debug("Missing localization data for " + item.getDisplayName(), new Object[0]);
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return null;
    }
    
    private static class itemSorter implements Comparator
    {
        @Override
        public int compare(final Object arg0, final Object arg1) {
            final ItemStack i0 = (ItemStack)arg0;
            final ItemStack i2 = (ItemStack)arg1;
            if (i0.getItem() instanceof ItemBlock && !(i2.getItem() instanceof ItemBlock)) {
                return -1;
            }
            if (i2.getItem() instanceof ItemBlock && !(i0.getItem() instanceof ItemBlock)) {
                return 1;
            }
            final String a = this.getString(i0);
            final String b = this.getString(i2);
            return (int)Math.signum(a.compareToIgnoreCase(b));
        }
        
        public String getString(final ItemStack item) {
            if (item.getItem() instanceof ICreativeTabSorting) {
                return ((ICreativeTabSorting)item.getItem()).getSortingName(item);
            }
            if (item.getItem() instanceof ItemBlock) {
                final Block block_id = ((ItemBlock)item.getItem()).field_150939_a;
                if (block_id instanceof ICreativeTabSorting) {
                    ((ICreativeTabSorting)block_id).getSortingName(item);
                }
            }
            return item.getDisplayName();
        }
    }
}



