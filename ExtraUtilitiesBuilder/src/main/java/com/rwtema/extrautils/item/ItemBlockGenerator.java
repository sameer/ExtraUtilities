// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import net.minecraft.item.Item;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;

public class ItemBlockGenerator extends ItemBlockMetadata
{
    public ItemBlockGenerator(final Block par1) {
        super(par1);
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack par1ItemStack) {
        if (this.field_150939_a != ExtraUtils.generator) {
            return Item.getItemFromBlock(ExtraUtils.generator).getUnlocalizedName(par1ItemStack);
        }
        return super.getUnlocalizedName(par1ItemStack);
    }
}
