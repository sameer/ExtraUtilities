// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.generators;

import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.passive.EntityAnimal;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import java.util.Iterator;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import java.util.List;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.oredict.OreDictionary;
import com.rwtema.extrautils.helper.XURandom;
import java.util.Random;
import net.minecraft.item.ItemStack;
import java.util.HashSet;

public class TileEntityGeneratorPink extends TileEntityGeneratorFurnace
{
    public static HashSet<ItemStack> pink;
    public Random rand;
    
    public TileEntityGeneratorPink() {
        this.rand = XURandom.getInstance();
    }
    
    public static void genPink() {
        final String[] dyes = { "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" };
        final HashSet<Integer> dyeids = new HashSet<Integer>();
        for (final String dye : dyes) {
            dyeids.add(OreDictionary.getOreID(dye));
        }
        final int pinkid = OreDictionary.getOreID("dyePink");
        (TileEntityGeneratorPink.pink = new HashSet<ItemStack>()).add(new ItemStack(Items.dye, 1, 9));
        TileEntityGeneratorPink.pink.add(new ItemStack(Blocks.wool, 1, 6));
        for (final Object recipe : CraftingManager.getInstance().getRecipeList()) {
            if (((IRecipe)recipe).getRecipeOutput() == null) {
                continue;
            }
            boolean flag = false;
            for (final int oreid : OreDictionary.getOreIDs(((IRecipe)recipe).getRecipeOutput())) {
                if (!dyeids.contains(oreid)) {
                    if (pinkid == oreid) {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag) {
                if (isPinkItem(((IRecipe)recipe).getRecipeOutput())) {
                    continue;
                }
                if (recipe instanceof ShapelessOreRecipe) {
                    if (!isPinkRecipe(((ShapelessOreRecipe)recipe).getInput())) {
                        continue;
                    }
                }
                else if (recipe instanceof ShapedOreRecipe) {
                    if (!isPinkRecipe(((ShapedOreRecipe)recipe).getInput())) {
                        continue;
                    }
                }
                else if (recipe instanceof ShapelessRecipes) {
                    if (!isPinkRecipe(((ShapelessRecipes)recipe).recipeItems)) {
                        continue;
                    }
                }
                else {
                    if (!(recipe instanceof ShapedRecipes)) {
                        continue;
                    }
                    if (!isPinkRecipe(((ShapedRecipes)recipe).recipeItems)) {
                        continue;
                    }
                }
            }
            TileEntityGeneratorPink.pink.add(((IRecipe)recipe).getRecipeOutput().copy());
        }
    }
    
    public static boolean isPinkRecipe(final List recipe) {
        for (final Object item : recipe) {
            if (isPinkItem(item)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isPinkRecipe(final Object[] recipe) {
        for (final Object item : recipe) {
            if (isPinkItem(item)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isPinkItem(final Object item) {
        if (item instanceof ItemStack) {
            final ItemStack p = (ItemStack)item;
            if (p.getItem() == Items.dye && p.getItemDamage() == 9) {
                return true;
            }
            if (p.getItem() == Item.getItemFromBlock(Blocks.wool) && p.getItemDamage() == 6) {
                return true;
            }
        }
        else if (item instanceof ItemStack[]) {
            for (final ItemStack i : (ItemStack[])item) {
                if (isPinkItem(i)) {
                    return true;
                }
            }
        }
        else if (item instanceof List) {
            for (final Object j : (List)item) {
                if (isPinkItem(j)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public int transferLimit() {
        return 160;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void doRandomDisplayTickR(final Random random) {
        if (this.rand.nextInt(2) != 0) {
            return;
        }
        if (!this.shouldSoundPlay()) {
            return;
        }
        final double d0 = this.rand.nextGaussian() * 0.5;
        final double d2 = this.rand.nextGaussian() * 0.5;
        final double d3 = this.rand.nextGaussian() * 0.5;
        this.worldObj.spawnParticle("heart", this.x() + 0.2 + 0.6 * this.rand.nextFloat(), this.y() + 0.95, this.z() + 0.2 + 0.6 * this.rand.nextFloat(), d0, d2, d3);
        super.doRandomDisplayTickR(random);
    }
    
    @Override
    public void doSpecial() {
        if (this.coolDown > 0.0 && this.rand.nextInt(40) == 0) {
            for (final Object entity : this.worldObj.getEntitiesWithinAABB((Class)EntityAnimal.class, AxisAlignedBB.getBoundingBox((double)this.x(), (double)this.y(), (double)this.z(), (double)(this.x() + 1), (double)(this.y() + 1), (double)(this.z() + 1)).expand(10.0, 10.0, 10.0))) {
                final EntityAnimal animal = (EntityAnimal)entity;
                if (animal.getGrowingAge() < 0) {
                    animal.addGrowth(this.rand.nextInt(40));
                }
                else if (animal.getGrowingAge() > 0) {
                    int j = animal.getGrowingAge();
                    j -= this.rand.nextInt(40);
                    if (j < 0) {
                        j = 0;
                    }
                    animal.setGrowingAge(j);
                }
                else {
                    if (animal.isInLove() || this.rand.nextInt(40) != 0) {
                        continue;
                    }
                    if (animal.worldObj.getEntitiesWithinAABB((Class)entity.getClass(), animal.boundingBox.expand(8.0, 8.0, 8.0)).size() > 32) {
                        return;
                    }
                    animal.func_146082_f((EntityPlayer)null);
                }
            }
        }
    }
    
    public boolean isPink(final ItemStack item) {
        if (item == null) {
            return false;
        }
        if (item.getUnlocalizedName() != null && item.getUnlocalizedName().contains("pink")) {
            return true;
        }
        if (TileEntityGeneratorPink.pink == null) {
            final long t = System.nanoTime();
            genPink();
            LogHelper.info("Pink recipes gened in " + (System.nanoTime() - t) / 1000000.0, new Object[0]);
        }
        for (final ItemStack pinkItem : TileEntityGeneratorPink.pink) {
            if (XUHelper.canItemsStack(item, pinkItem, false, true)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public double genLevel() {
        return 1.0;
    }
    
    @Override
    public double getFuelBurn(final ItemStack item) {
        if (this.isPink(item)) {
            return 600.0;
        }
        return 0.0;
    }
}


