// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.nei;

import java.util.Iterator;
import com.rwtema.extrautils.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.inventory.GuiInventory;
import codechicken.nei.api.IOverlayHandler;
import net.minecraft.client.gui.inventory.GuiCrafting;
import cpw.mods.fml.common.Loader;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.ICraftingHandler;
import com.rwtema.extrautils.item.filters.Matcher;
import com.rwtema.extrautils.item.filters.AdvancedNodeUpgrades;
import com.rwtema.extrautils.block.BlockDecoration;
import net.minecraft.block.Block;
import com.rwtema.extrautils.block.BlockColor;
import codechicken.nei.api.ItemFilter;
import codechicken.nei.api.API;
import net.minecraft.item.Item;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.nei.ping.NEIPing;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import codechicken.nei.api.IConfigureNEI;

@SideOnly(Side.CLIENT)
public class NEIInfoHandlerConfig implements IConfigureNEI
{
    public void loadConfig() {
        NEIPing.init();
        if (ExtraUtils.drum != null) {
            API.addSubset("Extra Common.Drums", (ItemFilter)new SubsetItemsNBT(Item.getItemFromBlock(ExtraUtils.drum)));
        }
        if (ExtraUtils.microBlocks != null) {
            API.addSubset("Extra Common.Extra Microblocks", (ItemFilter)new SubsetItemsNBT(ExtraUtils.microBlocks));
        }
        if (ExtraUtils.colorBlockDataEnabled) {
            API.addSubset("Extra Common.Colored Blocks", (ItemFilter)new SubsetBlockClass(BlockColor.class));
        }
        if (ExtraUtils.greenScreen != null) {
            API.addSubset("Extra Common.Lapis Caelestis", (ItemFilter)new SubsetItems(new Item[] { Item.getItemFromBlock((Block)ExtraUtils.greenScreen) }));
        }
        if (ExtraUtils.cobblestoneCompr != null) {
            API.addSubset("Extra Common.Compressed Blocks", (ItemFilter)new SubsetItems(new Item[] { Item.getItemFromBlock((Block)ExtraUtils.cobblestoneCompr) }));
        }
        if (ExtraUtils.decorative1Enabled || ExtraUtils.decorative2Enabled) {
            API.addSubset("Extra Common.Decorative Blocks", (ItemFilter)new SubsetBlockClass(BlockDecoration.class));
        }
        if (ExtraUtils.generator2 != null) {
            final SubsetItems s = new SubsetItems(new Item[] { Item.getItemFromBlock(ExtraUtils.generator2) });
            if (ExtraUtils.generator3 != null) {
                s.addItem(Item.getItemFromBlock(ExtraUtils.generator3));
            }
            API.addSubset("Extra Common.Higher Tier Generators", (ItemFilter)s);
        }
        for (final Matcher matcher : AdvancedNodeUpgrades.entryList) {
            if (matcher != AdvancedNodeUpgrades.nullMatcher && matcher.type == Matcher.Type.ITEM && matcher.shouldAddToNEI() && matcher.isSelectable()) {
                String localizedName = matcher.getLocalizedName();
                localizedName = localizedName.replace(".exe", "");
                localizedName = localizedName.replaceAll("\\.", "");
                API.addSubset("Extra Filtering." + localizedName, (ItemFilter)new ItemFilterWrapper(matcher));
            }
        }
        API.registerRecipeHandler((ICraftingHandler)new EnderConstructorHandler());
        API.registerUsageHandler((IUsageHandler)new EnderConstructorHandler());
        API.registerRecipeHandler((ICraftingHandler)new InfoHandler());
        API.registerUsageHandler((IUsageHandler)new InfoHandler());
        API.registerRecipeHandler((ICraftingHandler)new SoulHandler());
        API.registerUsageHandler((IUsageHandler)new SoulHandler());
        if (Loader.isModLoaded("ForgeMultipart")) {
            API.registerRecipeHandler((ICraftingHandler)new FMPMicroBlocksHandler());
            API.registerUsageHandler((IUsageHandler)new FMPMicroBlocksHandler());
            API.registerRecipeHandler((ICraftingHandler)new MicroBlocksHandler());
            API.registerUsageHandler((IUsageHandler)new MicroBlocksHandler());
            API.registerGuiOverlayHandler((Class)GuiCrafting.class, (IOverlayHandler)new FMPMicroBlocksOverlayHandler(), "microblocks");
            API.registerGuiOverlayHandler((Class)GuiInventory.class, (IOverlayHandler)new FMPMicroBlocksOverlayHandler(63, 20), "microblocks2x2");
        }
        if (ExtraUtils.colorBlockData != null) {
            API.hideItem(new ItemStack(ExtraUtils.colorBlockData));
        }
        LogHelper.info("Added NEI integration", new Object[0]);
    }
    
    public String getName() {
        return "Extra Utilities: Nei Integration";
    }
    
    public String getVersion() {
        return "1";
    }
}
