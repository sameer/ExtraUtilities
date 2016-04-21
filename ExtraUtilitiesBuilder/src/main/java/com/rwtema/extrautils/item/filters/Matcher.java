// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.item.filters;

import net.minecraft.item.Item;
import java.util.HashSet;
import net.minecraftforge.oredict.OreDictionary;
import gnu.trove.map.hash.TIntByteHashMap;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.util.StatCollector;
import java.util.Locale;

public class Matcher
{
    public Type type;
    public final String name;
    public final String unlocalizedName;
    int index;
    private boolean addToNEI;
    
    public static String getUnlocalizedName(String name) {
        name = name.replaceAll("[^{A-Za-z0-9}]", "").toLowerCase(Locale.ENGLISH);
        return "item.extrautils:nodeUpgrade.10.program." + name;
    }
    
    private static String titleCase(final String prefix) {
        return prefix.substring(0, 1).toUpperCase(Locale.ENGLISH) + prefix.substring(1, prefix.length());
    }
    
    public String getLocalizedName() {
        if (!StatCollector.canTranslate(this.unlocalizedName)) {
            return this.name + ".exe";
        }
        return StatCollector.translateToLocal(this.unlocalizedName);
    }
    
    public boolean isSelectable() {
        return true;
    }
    
    public Matcher(final String name) {
        this(name, true);
    }
    
    public Matcher(final String name, final boolean addToNEI) {
        this.name = name;
        this.addToNEI = addToNEI;
        this.unlocalizedName = getUnlocalizedName(name);
        final boolean hasFluid = this.methodExists("matchFluid", this.getClass(), FluidStack.class);
        final boolean hasItem = this.methodExists("matchItem", this.getClass(), ItemStack.class);
        final int t = (hasFluid ? 1 : 0) + (hasItem ? 2 : 0);
        Type type = null;
        switch (t) {
            default: {
                throw new RuntimeException("No overrided methods");
            }
            case 1: {
                type = Type.FLUID;
                break;
            }
            case 2: {
                type = Type.ITEM;
                break;
            }
            case 3: {
                type = Type.BOTH;
                break;
            }
        }
        this.type = type;
    }
    
    public boolean methodExists(final String method, Class<?> clazz, final Class... classes) {
        try {
            clazz.getDeclaredMethod(method, (Class<?>[])classes);
            return true;
        }
        catch (NoSuchMethodException e) {
            clazz = clazz.getSuperclass();
            return clazz != Matcher.class && this.methodExists(method, clazz, classes);
        }
    }
    
    public boolean matchFluid(final FluidStack fluid) {
        return false;
    }
    
    public boolean matchItem(final ItemStack item) {
        return false;
    }
    
    public boolean shouldAddToNEI() {
        return this.addToNEI;
    }
    
    public enum Type
    {
        FLUID, 
        ITEM, 
        BOTH;
    }
    
    public static class MatcherTool extends Matcher
    {
        private final String tool;
        
        public MatcherTool(final String tool) {
            super("Tool" + titleCase(tool));
            this.tool = tool;
        }
        
        @Override
        public boolean matchItem(final ItemStack item) {
            for (final String s : item.getItem().getToolClasses(item)) {
                if (this.tool.equals(s)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public static class MatcherOreDic extends Matcher
    {
        private final String prefix;
        private final TIntByteHashMap map;
        
        public MatcherOreDic(final String prefix) {
            super("OreDic" + titleCase(prefix));
            this.map = new TIntByteHashMap();
            this.prefix = prefix;
        }
        
        @Override
        public boolean matchItem(final ItemStack item) {
            for (final int i : OreDictionary.getOreIDs(item)) {
                if (this.map.containsKey(i)) {
                    if (this.map.get(i) != 0) {
                        return true;
                    }
                }
                else {
                    this.map.put(i, (byte)(byte)(OreDictionary.getOreName(i).startsWith(this.prefix) ? 1 : 0));
                }
            }
            return false;
        }
        
        @Override
        public boolean isSelectable() {
            for (final String s : OreDictionary.getOreNames()) {
                if (s.startsWith(this.prefix)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public abstract static class MatcherItem extends Matcher
    {
        HashSet<Item> entries;
        
        public MatcherItem(final String name) {
            super(name);
        }
        
        public void buildMap() {
            this.entries = new HashSet<Item>();
            for (final Object anItemRegistry : Item.itemRegistry) {
                final Item item = (Item)anItemRegistry;
                if (this.matchItem(item)) {
                    this.entries.add(item);
                }
            }
        }
        
        @Override
        public boolean matchItem(final ItemStack item) {
            if (this.entries == null) {
                this.buildMap();
            }
            return this.entries.contains(item.getItem());
        }
        
        @Override
        public boolean isSelectable() {
            if (this.entries == null) {
                this.buildMap();
            }
            return !this.entries.isEmpty();
        }
        
        protected abstract boolean matchItem(final Item p0);
    }
    
    public static class MatcherOreDicPair extends Matcher
    {
        private final String prefix;
        private final String prefix2;
        private final TIntByteHashMap map;
        
        public MatcherOreDicPair(final String prefix, final String prefix2) {
            super("OrePair" + titleCase(prefix) + titleCase(prefix2));
            this.map = new TIntByteHashMap();
            this.prefix = prefix;
            this.prefix2 = prefix2;
        }
        
        @Override
        public boolean matchItem(final ItemStack item) {
            for (final int i : OreDictionary.getOreIDs(item)) {
                final String oreName = OreDictionary.getOreName(i);
                boolean isOre = oreName.startsWith(this.prefix);
                isOre = (isOre && this.oreExists(oreName.replaceFirst(this.prefix, this.prefix2)));
                this.map.put(i, (byte)(byte)(isOre ? 1 : 0));
                if (isOre) {
                    return true;
                }
            }
            return false;
        }
        
        public boolean oreExists(final String k) {
            for (final String s : OreDictionary.getOreNames()) {
                if (k.equals(s) && !OreDictionary.getOres(k, false).isEmpty()) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public boolean isSelectable() {
            for (final String s : OreDictionary.getOreNames()) {
                if (s.startsWith(this.prefix) && this.oreExists(s.replace(this.prefix, this.prefix2))) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public static class InverseMatch extends Matcher
    {
        private final Matcher matcher;
        
        public InverseMatch(final String item, final Matcher matcher) {
            super(item);
            this.matcher = matcher;
            this.type = matcher.type;
        }
        
        @Override
        public boolean matchItem(final ItemStack item) {
            return !this.matcher.matchItem(item);
        }
        
        @Override
        public boolean matchFluid(final FluidStack fluid) {
            return !this.matcher.matchFluid(fluid);
        }
    }
}


