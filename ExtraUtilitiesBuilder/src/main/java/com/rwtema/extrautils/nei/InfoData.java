// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.nei;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import java.util.List;

public class InfoData
{
    public static List<InfoData> data;
    public ItemStack item;
    public String[] info;
    public String name;
    public String url;
    public boolean isBlock;
    public boolean precise;
    
    public InfoData(final ItemStack item, final String[] info, final String name, final String url, final boolean precise) {
        this.isBlock = false;
        this.precise = false;
        this.item = item;
        this.info = info;
        this.name = name;
        if (url != null && !url.endsWith(".png")) {
            throw new RuntimeException(name + " is missing .png from url : " + url);
        }
        this.url = url;
        this.precise = precise;
        this.isBlock = (item.getItem() instanceof ItemBlock);
    }
    
    @Deprecated
    public static InfoData add(final Object item, final String name, final String url, final String... info) {
        InfoData newData = null;
        if (item instanceof ItemStack) {
            newData = new InfoData((ItemStack)item, info, name, url, true);
        }
        else if (item instanceof Item) {
            newData = new InfoData(new ItemStack((Item)item), info, name, url, false);
        }
        else if (item instanceof Block) {
            newData = new InfoData(new ItemStack((Block)item), info, name, url, false);
        }
        InfoData.data.add(newData);
        return newData;
    }
    
    public boolean matches(final ItemStack item) {
        if (item == null) {
            return false;
        }
        if (this.precise) {
            return ItemStack.areItemStacksEqual(item, this.item);
        }
        return item == this.item;
    }
    
    static {
        InfoData.data = new ArrayList<InfoData>();
    }
}


