// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.multipart.microblock;

import com.rwtema.extrautils.helper.XURandom;
import codechicken.microblock.MicroMaterialRegistry;
import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;

public class CreativeTabMicroBlocks extends CreativeTabs
{
    public static CreativeTabMicroBlocks instance;
    public static Random rand;
    private static ItemStack item;
    
    public CreativeTabMicroBlocks() {
        super("extraUtil_FMP");
    }
    
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        if (CreativeTabMicroBlocks.item == null) {
            CreativeTabMicroBlocks.item = ItemMicroBlock.getStack(new ItemStack(ExtraUtils.microBlocks, 1, CreativeTabMicroBlocks.rand.nextBoolean() ? 2 : 1), (String)MicroMaterialRegistry.getIdMap()[CreativeTabMicroBlocks.rand.nextInt(MicroMaterialRegistry.getIdMap().length)]._1());
        }
        return CreativeTabMicroBlocks.item;
    }
    
    static {
        CreativeTabMicroBlocks.instance = null;
        CreativeTabMicroBlocks.rand = XURandom.getInstance();
    }
}
