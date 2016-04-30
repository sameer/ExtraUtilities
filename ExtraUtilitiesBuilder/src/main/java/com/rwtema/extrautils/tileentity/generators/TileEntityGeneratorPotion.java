// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.generators;

import java.util.HashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import java.util.Iterator;
import java.util.List;
import net.minecraft.potion.PotionHelper;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import net.minecraft.item.Item;
import java.util.HashSet;
import com.rwtema.extrautils.helper.XUHelper;
import java.util.Map;

public class TileEntityGeneratorPotion extends TileEntityGeneratorFurnace
{
    public static final int MAX_META_TO_CHECK = 256;
    public static Map<Integer, Integer> powerMap;
    int curLevel;
    
    public TileEntityGeneratorPotion() {
        this.curLevel = 0;
    }
    
    public static void genPotionLevels() {
        XUHelper.resetTimer();
        final HashSet<Item> ingredientIDs = new HashSet<Item>();
        final List<Integer> potionIDs = new ArrayList<Integer>();
        for (final Object anItemRegistry : Item.itemRegistry) {
            final Item i = (Item)anItemRegistry;
            for (int num = getMaxMeta(i), meta = 0; meta < num; ++meta) {
                if (i.isPotionIngredient(new ItemStack(i, 1, meta))) {
                    ingredientIDs.add(i);
                }
            }
        }
        TileEntityGeneratorPotion.powerMap.put(0, 0);
        potionIDs.add(0);
        for (int j = 0; j < potionIDs.size(); ++j) {
            final int potion = potionIDs.get(j);
            for (final Item ingredient : ingredientIDs) {
                String k = "";
                String s = "";
                for (int num2 = getMaxMeta(ingredient), meta2 = 0; meta2 < num2 || !k.equals(s); ++meta2) {
                    if (ingredient.isPotionIngredient(new ItemStack(ingredient, 1, meta2))) {
                        try {
                            s = ingredient.getPotionEffect(new ItemStack(ingredient, 1, meta2));
                            final int c = PotionHelper.applyIngredient(potion, s);
                            if (!potionIDs.contains(c)) {
                                potionIDs.add(c);
                                TileEntityGeneratorPotion.powerMap.put(c, TileEntityGeneratorPotion.powerMap.get(potion) + 1);
                            }
                            k = ((s == null) ? "" : s);
                        }
                        catch (Exception err) {
                            throw new RuntimeException("Caught error while applying potion ingredient " + ingredient.toString() + " to " + potion, err);
                        }
                    }
                }
            }
        }
        XUHelper.printTimer("Potion generation");
    }
    
    public static int getMaxMeta(final Item i) {
        return isSpecial(i) ? 256 : 1;
    }
    
    public static boolean isSpecial(final Item i) {
        return i.getClass() != Item.class && i.getClass() != ItemBlock.class && i.getHasSubtypes();
    }
    
    @Override
    public int transferLimit() {
        return Math.max(400, (int)this.genLevel());
    }
    
    @Override
    public double genLevel() {
        return 20 * (1 << this.curLevel);
    }
    
    public int getPotionLevel(final ItemStack item) {
        if (item != null && item.getItem() == Items.potionitem && TileEntityGeneratorPotion.powerMap.containsKey(item.getItemDamage())) {
            return TileEntityGeneratorPotion.powerMap.get(item.getItemDamage());
        }
        return 0;
    }
    
    @Override
    public double getFuelBurn(final ItemStack item) {
        if (this.getPotionLevel(item) == 0) {
            return 0.0;
        }
        return 800.0;
    }
    
    @Override
    public double getGenLevelForStack(final ItemStack itemStack) {
        final int c = this.getPotionLevel(itemStack);
        return 10 * (1 << c);
    }
    
    @Override
    public boolean processInput() {
        if (this.coolDown > 0.0) {
            return false;
        }
        final int c = this.getPotionLevel(this.inv.getStackInSlot(0));
        if (c > 0) {
            this.addCoolDown(this.getFuelBurn(this.inv.getStackInSlot(0)), false);
            this.curLevel = c;
            this.inv.getStackInSlot(0).setItemDamage(0);
            this.markDirty();
            return true;
        }
        return false;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.curLevel = nbt.getInteger("curLevel");
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("curLevel", this.curLevel);
    }
    
    static {
        TileEntityGeneratorPotion.powerMap = new HashMap<Integer, Integer>();
    }
}


