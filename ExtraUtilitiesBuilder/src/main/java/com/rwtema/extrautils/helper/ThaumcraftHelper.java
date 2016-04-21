// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.helper;

import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import thaumcraft.api.ThaumcraftApiHelper;
import java.util.ArrayList;
import java.util.Iterator;
import thaumcraft.api.aspects.AspectList;
import com.rwtema.extrautils.block.BlockColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import thaumcraft.api.aspects.Aspect;
import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.common.Loader;

public class ThaumcraftHelper
{
    public static final int[] pi;
    
    public static void registerItems() {
        if (Loader.isModLoaded("Thaumcraft")) {
            registerItems_do();
        }
    }
    
    private static void registerItems_do() {
        addAspectsDivSigil();
        addAspectRecipe(ExtraUtils.unstableIngot, Aspect.METAL, 4, Aspect.ELDRITCH, 4, Aspect.ENERGY, 16);
        addAspectRecipe(ExtraUtils.cursedEarth, Aspect.EARTH, 1, Aspect.DARKNESS, 1, Aspect.UNDEAD, 4, Aspect.ELDRITCH, 1, Aspect.EXCHANGE, 1);
        addAspectRecipe((Block)ExtraUtils.enderLily, Aspect.DARKNESS, 1, Items.wheat_seeds, Items.ender_pearl, Aspect.ELDRITCH, 16);
        addAspectRecipe(ExtraUtils.transferPipe, new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, Aspect.TRAVEL, 1, Aspect.ORDER, 1, Aspect.EARTH, 1);
        addAspectRecipe(ExtraUtils.buildersWand, Aspect.TOOL, 4, Blocks.obsidian, Aspect.ELDRITCH, 4);
        addAspectRecipe(ExtraUtils.buildersWand, 32767, Aspect.TOOL, 4, Blocks.obsidian, Aspect.ELDRITCH, 4, Items.nether_star);
        addAspectRecipe(ExtraUtils.trashCan, Aspect.VOID, 8, Blocks.cobblestone, Aspect.ENTROPY, 4);
        if (ExtraUtils.spike != null) {
            addAspectRecipe(ExtraUtils.spike.itemSpike, -1, null, Aspect.WEAPON, 18, Aspect.METAL, 14);
        }
        if (ExtraUtils.spikeDiamond != null) {
            addAspectRecipe(ExtraUtils.spikeDiamond.itemSpike, -1, null, Aspect.WEAPON, 18, Aspect.METAL, 14);
        }
        if (ExtraUtils.spikeGold != null) {
            addAspectRecipe(ExtraUtils.spikeGold.itemSpike, -1, null, Aspect.WEAPON, 18, Aspect.METAL, 14);
        }
        if (ExtraUtils.spikeWood != null) {
            addAspectRecipe(ExtraUtils.spikeWood.itemSpike, -1, null, Aspect.WEAPON, 18, Aspect.METAL, 14);
        }
        addAspectRecipe(ExtraUtils.wateringCan, -1, new ItemStack((Item)ExtraUtils.wateringCan, 1, 1), Aspect.WATER, 1, Aspect.LIFE, 1, Aspect.EARTH, 2);
        addAspectRecipe(ExtraUtils.conveyor, -1, null, Blocks.rail, Aspect.TRAVEL, 4);
        if (ExtraUtils.decorative1 != null) {
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 4), Blocks.stonebrick);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 7), Blocks.stonebrick);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 0), Blocks.stonebrick);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 1), Blocks.obsidian, Items.ender_pearl);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 2), Blocks.quartz_block, Aspect.FIRE, 4);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 3), Blocks.stone, Blocks.ice);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 6), Blocks.gravel);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 10), Blocks.gravel, Blocks.gravel, Aspect.TRAVEL, 1);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 9), Blocks.sand, Blocks.glass);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 13), Blocks.sand, Blocks.end_stone);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 14), Blocks.stonebrick, Aspect.SENSES, 2, Aspect.ELDRITCH, 2);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 8), null, Aspect.MAGIC, 16, Aspect.METAL, 4, Aspect.GREED, 4, Aspect.MIND, 8, Aspect.TREE, 8);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 11), null, Aspect.ELDRITCH, 16);
        }
        if (ExtraUtils.decorative2 != null) {
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 10), Aspect.CRYSTAL, 2, Aspect.DARKNESS, 4);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 0), Aspect.CRYSTAL, 2);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 3), Aspect.CRYSTAL, 2, Aspect.ENTROPY, 1);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 3), Aspect.CRYSTAL, 2, Aspect.ENTROPY, 1);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 4), Aspect.CRYSTAL, 2, Aspect.GREED, 1);
            addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 8), Aspect.CRYSTAL, 2, Aspect.LIFE, 1, Aspect.HEAL, 1);
        }
        for (final BlockColor colorblock : ExtraUtils.colorblocks) {
            final AspectList aspectList = new AspectList(new ItemStack(colorblock.baseBlock, 1));
            if (aspectList.visSize() > 0) {
                for (int i = 0; i < 16; ++i) {
                    addAspectRecipe(new ItemStack((Block)colorblock, 1, i), aspectList, Aspect.SENSES, 1);
                }
            }
        }
        addAspectRecipe(ExtraUtils.lawSword, Aspect.WEAPON, 64, Aspect.LIFE, 32, Aspect.MAGIC, 16);
    }
    
    public static void handleQEDRecipes(final ArrayList<ItemStack> items) {
        handleQEDRecipes_do(items);
    }
    
    private static void handleQEDRecipes_do(final ArrayList<ItemStack> items) {
        for (final ItemStack item : items) {
            ThaumcraftApiHelper.getObjectAspects(item);
        }
    }
    
    private static void addAspectsDivSigil() {
        if (ExtraUtils.divisionSigil == null) {
            return;
        }
        final ArrayList<Aspect> a = new ArrayList<Aspect>();
        a.add(Aspect.AURA);
        a.add(Aspect.EXCHANGE);
        a.add(Aspect.TOOL);
        a.add(Aspect.CRAFT);
        a.add(Aspect.ELDRITCH);
        a.add(Aspect.SOUL);
        Collections.sort(a, new Comparator<Aspect>() {
            @Override
            public int compare(final Aspect o1, final Aspect o2) {
                return o1.getTag().compareTo(o2.getTag());
            }
        });
        final AspectList b = new AspectList();
        for (int i = 0; i < a.size(); ++i) {
            b.add((Aspect)a.get(i), ThaumcraftHelper.pi[i]);
        }
        ThaumcraftApi.registerObjectTag(new ItemStack(ExtraUtils.divisionSigil, 1, 32767), b);
    }
    
    private static void addAspectRecipe(final Block block, final Object... ingredients) {
        if (block != null) {
            addAspectRecipe(new ItemStack(block), ingredients);
        }
    }
    
    private static void addAspectRecipe(final Item item, final Object... ingredients) {
        if (item != null) {
            addAspectRecipe(new ItemStack(item), ingredients);
        }
    }
    
    private static void addAspectRecipe(final ItemStack result, final Object... ingredients) {
        if (result == null || result.getItem() == null) {
            return;
        }
        int[] meta = null;
        final AspectList al = new AspectList(result);
        for (int i = 0; i < ingredients.length; ++i) {
            final Object o = ingredients[i];
            if (o == null) {
                al.add(new AspectList(result));
            }
            else if (i == 0 && o instanceof Integer) {
                int newmeta = (Integer)o;
                if (newmeta == -1) {
                    newmeta = 32767;
                }
                result.setItemDamage(newmeta);
            }
            else if (o instanceof int[]) {
                meta = (int[])o;
            }
            else if (o instanceof AspectList) {
                al.add((AspectList)o);
            }
            else if (o instanceof Aspect) {
                final Aspect a = (Aspect)o;
                int a_num = 1;
                if (i + 1 < ingredients.length && ingredients[i + 1] instanceof Integer) {
                    a_num = (Integer)ingredients[i + 1];
                    ++i;
                }
                al.add(a, a_num);
            }
            else if (o instanceof ItemStack) {
                final AspectList aspectList = new AspectList((ItemStack)o);
                al.add(aspectList);
            }
            else if (o instanceof Item) {
                final AspectList aspectList = new AspectList(new ItemStack((Item)o));
                al.add(aspectList);
            }
            else if (o instanceof Block) {
                final AspectList aspectList = new AspectList(new ItemStack((Block)o));
                al.add(aspectList);
            }
            else if (o instanceof String) {
                final AspectList aspectList = new AspectList();
                for (final ItemStack itemStack : OreDictionary.getOres((String)o)) {
                    aspectList.merge(new AspectList(itemStack));
                }
                al.add(aspectList);
            }
        }
        if (meta != null) {
            ThaumcraftApi.registerObjectTag(result, meta, al);
        }
        else {
            ThaumcraftApi.registerObjectTag(result, al);
        }
    }
    
    static {
        pi = new int[] { 3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 2, 3, 8, 4, 6, 2, 6, 4, 3, 3, 8, 3, 2, 7, 9, 5 };
    }
}

