// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeGlove extends ShapedOreRecipe
{
    public RecipeGlove(final Item glove) {
        super(glove, new Object[] { "sW", "Ws", 'W', new ItemStack(Blocks.wool, 1, 32767), 's', Items.string });
    }
    
    public ItemStack getCraftingResult(final InventoryCrafting var1) {
        int a = -1;
        int b = -1;
        for (int i = 0; i < var1.getSizeInventory(); ++i) {
            final ItemStack stackInSlot = var1.getStackInSlot(i);
            if (stackInSlot != null && stackInSlot.getItem() == Item.getItemFromBlock(Blocks.wool)) {
                if (a != -1) {
                    b = stackInSlot.getItemDamage();
                }
                else {
                    a = stackInSlot.getItemDamage();
                }
            }
        }
        if (a < 0 || b < 0 || b >= 16 || a >= 16) {
            return super.getCraftingResult(var1);
        }
        final ItemStack craftingResult = super.getCraftingResult(var1);
        craftingResult.setItemDamage(a << 4 | b);
        return craftingResult;
    }
}


