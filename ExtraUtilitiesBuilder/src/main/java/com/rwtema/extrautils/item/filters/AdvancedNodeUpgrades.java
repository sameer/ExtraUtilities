// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item.filters;

import java.util.Iterator;
import com.rwtema.extrautils.LogHelper;
import net.minecraftforge.common.IPlantable;
import net.minecraft.block.BlockDispenser;
import net.minecraft.util.RegistrySimple;
import com.rwtema.extrautils.asm.RemoteCallFactory;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.ItemFood;
import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.Loader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.HashMap;

public class AdvancedNodeUpgrades
{
    public static HashMap<String, Matcher> matcherMap;
    public static ArrayList<Matcher> entryList;
    public static Matcher nullMatcher;
    public static boolean problemCodePresent;
    
    public static Matcher getMatcher(final ItemStack itemStack) {
        if (itemStack.hasTagCompound()) {
            final Matcher matcher = AdvancedNodeUpgrades.matcherMap.get(itemStack.getTagCompound().getString("Matcher"));
            if (matcher != null) {
                return matcher;
            }
        }
        return AdvancedNodeUpgrades.nullMatcher;
    }
    
    public static Matcher nextEntry(final ItemStack itemStack, final boolean next) {
        int i = 0;
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        final NBTTagCompound tags = itemStack.getTagCompound();
        Matcher matcher = AdvancedNodeUpgrades.matcherMap.get(tags.getString("Matcher"));
        if (matcher != null) {
            i = matcher.index;
        }
        do {
            if (next) {
                if (++i >= AdvancedNodeUpgrades.entryList.size()) {
                    i = 0;
                }
            }
            else if (--i < 0) {
                i = AdvancedNodeUpgrades.entryList.size() - 1;
            }
            matcher = AdvancedNodeUpgrades.entryList.get(i);
        } while (!matcher.isSelectable());
        tags.setString("Matcher", matcher.name);
        return matcher;
    }
    
    public static void addEntry(final Matcher matcher) {
        final String entry = matcher.name;
        matcher.index = AdvancedNodeUpgrades.entryList.size();
        AdvancedNodeUpgrades.entryList.add(matcher);
        AdvancedNodeUpgrades.matcherMap.put(entry, matcher);
    }
    
    static {
        AdvancedNodeUpgrades.matcherMap = new HashMap<String, Matcher>();
        AdvancedNodeUpgrades.entryList = new ArrayList<Matcher>();
        AdvancedNodeUpgrades.problemCodePresent = (Loader.isModLoaded("gregtech") || Loader.isModLoaded("gregapi") || ModAPIManager.INSTANCE.hasAPI("gregapi"));
        addEntry(AdvancedNodeUpgrades.nullMatcher = new Matcher("Default", false) {
            @Override
            public boolean matchFluid(final FluidStack fluid) {
                return true;
            }
            
            @Override
            public boolean matchItem(final ItemStack fluid) {
                return true;
            }
        });
        final Matcher m;
        addEntry(m = new Matcher.MatcherItem("Block") {
            @Override
            protected boolean matchItem(final Item item) {
                return item instanceof ItemBlock;
            }
        });
        addEntry(new Matcher.InverseMatch("Item", m));
        addEntry(new Matcher.MatcherItem("HasSubTypes") {
            @Override
            protected boolean matchItem(final Item item) {
                return item.getHasSubtypes();
            }
        });
        addEntry(new Matcher("StackSize1") {
            @Override
            public boolean matchItem(final ItemStack item) {
                return item.getMaxStackSize() == 1;
            }
        });
        addEntry(new Matcher("StackSize64") {
            @Override
            public boolean matchItem(final ItemStack item) {
                return item.getMaxStackSize() == 64;
            }
        });
        addEntry(new Matcher.MatcherOreDic("ore"));
        addEntry(new Matcher.MatcherOreDic("ingot"));
        addEntry(new Matcher.MatcherOreDic("nugget"));
        addEntry(new Matcher.MatcherOreDic("block"));
        addEntry(new Matcher.MatcherOreDic("gem"));
        addEntry(new Matcher.MatcherOreDic("dust"));
        addEntry(new Matcher.MatcherItem("EnergyItem") {
            @Override
            public boolean matchItem(final ItemStack item) {
                return super.matchItem(item) && ((IEnergyContainerItem)item.getItem()).getMaxEnergyStored(item) != 0;
            }
            
            @Override
            protected boolean matchItem(final Item item) {
                return item instanceof IEnergyContainerItem;
            }
        });
        addEntry(new Matcher.MatcherItem("EnergyItemEmpty") {
            @Override
            public boolean matchItem(final ItemStack item) {
                return super.matchItem(item) && ((IEnergyContainerItem)item.getItem()).getMaxEnergyStored(item) > 0 && ((IEnergyContainerItem)item.getItem()).getEnergyStored(item) == 0;
            }
            
            @Override
            protected boolean matchItem(final Item item) {
                return item instanceof IEnergyContainerItem;
            }
        });
        addEntry(new Matcher.MatcherItem("EnergyItem<50") {
            @Override
            public boolean matchItem(final ItemStack item) {
                if (!super.matchItem(item)) {
                    return false;
                }
                final IEnergyContainerItem energyContainerItem = (IEnergyContainerItem)item.getItem();
                final int maxEnergyStored = energyContainerItem.getMaxEnergyStored(item);
                return maxEnergyStored > 0 && energyContainerItem.getEnergyStored(item) <= maxEnergyStored >> 1;
            }
            
            @Override
            protected boolean matchItem(final Item item) {
                return item instanceof IEnergyContainerItem;
            }
        });
        addEntry(new Matcher.MatcherItem("EnergyItemFull") {
            @Override
            public boolean matchItem(final ItemStack item) {
                if (!super.matchItem(item)) {
                    return false;
                }
                final IEnergyContainerItem energyContainerItem = (IEnergyContainerItem)item.getItem();
                final int maxEnergyStored = energyContainerItem.getMaxEnergyStored(item);
                return maxEnergyStored != 0 && energyContainerItem.getEnergyStored(item) == maxEnergyStored;
            }
            
            @Override
            protected boolean matchItem(final Item item) {
                return item instanceof IEnergyContainerItem;
            }
        });
        addEntry(new Matcher.MatcherItem("Food") {
            @Override
            protected boolean matchItem(final Item item) {
                return item instanceof ItemFood;
            }
        });
        addEntry(new Matcher("Smeltable") {
            @Override
            public boolean matchItem(final ItemStack item) {
                return FurnaceRecipes.smelting().getSmeltingResult(item) != null;
            }
        });
        if (RemoteCallFactory.pulverizer != null) {
            addEntry(new Matcher("Pulverizer") {
                @Override
                public boolean matchItem(final ItemStack item) {
                    return RemoteCallFactory.pulverizer.evaluate(item);
                }
            });
        }
        addEntry(new Matcher("Enchanted") {
            @Override
            public boolean matchItem(final ItemStack item) {
                return item.isItemEnchanted();
            }
        });
        addEntry(new Matcher("Enchantable") {
            @Override
            public boolean matchItem(final ItemStack item) {
                return item.isItemEnchantable();
            }
        });
        if (!AdvancedNodeUpgrades.problemCodePresent) {
            addEntry(new Matcher("HasContainerItem") {
                @Override
                public boolean matchItem(final ItemStack item) {
                    return item.getItem().hasContainerItem(item);
                }
            });
        }
        addEntry(new Matcher("DurabilityBarShown") {
            @Override
            public boolean matchItem(final ItemStack item) {
                return item.getItem().showDurabilityBar(item);
            }
        });
        addEntry(new Matcher("DurabilityBarFull") {
            @Override
            public boolean matchItem(final ItemStack stack) {
                final Item item = stack.getItem();
                return item.showDurabilityBar(stack) && item.getDurabilityForDisplay(stack) <= 0.0;
            }
        });
        addEntry(new Matcher("DurabilityBar<90", false) {
            @Override
            public boolean matchItem(final ItemStack stack) {
                final Item item = stack.getItem();
                return item.showDurabilityBar(stack) && item.getDurabilityForDisplay(stack) >= 0.5;
            }
        });
        addEntry(new Matcher("DurabilityBar<50", false) {
            @Override
            public boolean matchItem(final ItemStack stack) {
                final Item item = stack.getItem();
                return item.showDurabilityBar(stack) && item.getDurabilityForDisplay(stack) >= 0.5;
            }
        });
        addEntry(new Matcher("DurabilityBar<10", false) {
            @Override
            public boolean matchItem(final ItemStack stack) {
                final Item item = stack.getItem();
                return item.showDurabilityBar(stack) && item.getDurabilityForDisplay(stack) >= 0.9;
            }
        });
        addEntry(new Matcher("DurabilityBarEmpty") {
            @Override
            public boolean matchItem(final ItemStack stack) {
                final Item item = stack.getItem();
                return item.showDurabilityBar(stack) && item.getDurabilityForDisplay(stack) >= 1.0;
            }
        });
        addEntry(new Matcher("HasDisplayName", false) {
            @Override
            public boolean matchItem(final ItemStack stack) {
                return stack.hasDisplayName();
            }
        });
        addEntry(new Matcher("Repairable") {
            @Override
            public boolean matchItem(final ItemStack stack) {
                final Item item = stack.getItem();
                return item.isRepairable();
            }
        });
        addEntry(new Matcher.MatcherItem("HasDispenserBehaviour") {
            public boolean matchItem(final Item item) {
                return ((RegistrySimple)BlockDispenser.dispenseBehaviorRegistry).containsKey((Object)item);
            }
        });
        addEntry(new Matcher.MatcherItem("Plantable") {
            @Override
            protected boolean matchItem(final Item item) {
                return item instanceof IPlantable;
            }
        });
        if (LogHelper.isDeObf) {
            final StringBuilder builder = new StringBuilder();
            for (final Matcher matcher : AdvancedNodeUpgrades.entryList) {
                builder.append('\n');
                builder.append(matcher.unlocalizedName);
                builder.append("=");
                builder.append(matcher.name);
                builder.append(".exe");
            }
            LogHelper.info(builder.toString(), new Object[0]);
        }
    }
}

