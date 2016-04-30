// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.nei;

import net.minecraft.client.Minecraft;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import net.minecraft.inventory.Container;
import net.minecraft.client.gui.inventory.GuiContainer;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.item.Item;
import java.util.List;
import java.util.Collection;
import net.minecraft.util.StatCollector;
import java.util.ArrayList;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.FontRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;

@SideOnly(Side.CLIENT)
public class InfoHandler implements IUsageHandler, ICraftingHandler
{
    public static FontRenderer fontRenderer;
    public static int color;
    ItemStack displayItem;
    boolean precise;
    String id;
    String name;
    String[] info;
    public boolean checkedOrder;
    int noLinesPerPage;
    public final String suffix = ".documentation";
    
    public InfoHandler() {
        this.precise = false;
        this.checkedOrder = false;
        this.noLinesPerPage = 12;
        this.displayItem = null;
    }
    
    public boolean checkOrder() {
        if (this.checkedOrder) {
            return false;
        }
        this.checkedOrder = true;
        return this.changeOrder(GuiUsageRecipe.usagehandlers) | this.changeOrder(GuiCraftingRecipe.craftinghandlers);
    }
    
    public boolean changeOrder(final ArrayList list) {
        int j = -1;
        for (int i = 0; i < list.size() - 1; ++i) {
            if (list.get(i).getClass() == this.getClass()) {
                j = i;
                break;
            }
        }
        if (j >= 0) {
            list.add(list.remove(j));
        }
        return false;
    }
    
    public InfoHandler(final ItemStack item) {
        this.precise = false;
        this.checkedOrder = false;
        this.noLinesPerPage = 12;
        if (StatCollector.canTranslate(item.getUnlocalizedName() + ".documentation") || StatCollector.canTranslate(item.getUnlocalizedName() + ".documentation" + ".0")) {
            this.id = item.getUnlocalizedName();
            this.name = StatCollector.translateToLocal(item.getUnlocalizedName());
            this.precise = true;
        }
        else {
            this.id = item.getItem().getUnlocalizedName();
            this.name = StatCollector.translateToLocal(item.getItem().getUnlocalizedName());
            this.precise = false;
        }
        if (StatCollector.canTranslate(this.id + ".documentation")) {
            final List<String> strings = this.splitString(StatCollector.translateToLocal(this.id + ".documentation"));
            this.info = strings.toArray(new String[strings.size()]);
        }
        else {
            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; StatCollector.canTranslate(this.id + ".documentation" + "." + i); ++i) {
                final String a = StatCollector.translateToLocal(this.id + ".documentation" + "." + i);
                list.addAll(this.splitString(a));
            }
            this.info = list.toArray(new String[list.size()]);
        }
        this.displayItem = item.copy();
        this.displayItem.stackSize = 1;
    }
    
    public List<String> splitString(final String a) {
        final ArrayList<String> list = new ArrayList<String>();
        final List b = InfoHandler.fontRenderer.listFormattedStringToWidth(a, this.getWidth() - 8);
        if (b.size() < this.noLinesPerPage) {
            list.add(a);
        }
        else {
            String c = "";
            for (int j = 0; j < b.size(); ++j) {
                c = c + b.get(j) + " ";
                if (j > 0 && j % this.noLinesPerPage == 0) {
                    final String d = c.trim();
                    list.add(d);
                    c = "";
                }
            }
            c = c.trim();
            if (!"".equals(c)) {
                list.add(c);
            }
        }
        return list;
    }
    
    public String getRecipeName() {
        if (this.displayItem == null) {
            return "Documentation";
        }
        final String s = Item.itemRegistry.getNameForObject((Object)this.displayItem.getItem());
        final String modid = s.split(":")[0];
        if ("minecraft".equals(modid)) {
            return "Minecraft";
        }
        final ModContainer selectedMod = Loader.instance().getIndexedModList().get(modid);
        if (selectedMod == null) {
            return modid;
        }
        if (!selectedMod.getMetadata().autogenerated) {
            return selectedMod.getMetadata().name;
        }
        return selectedMod.getName();
    }
    
    public int numRecipes() {
        return (this.displayItem == null || this.info == null) ? 0 : this.info.length;
    }
    
    public void drawBackground(final int recipe) {
    }
    
    public int getWidth() {
        return 166;
    }
    
    public PositionedStack getResultStack(final int recipe) {
        return new PositionedStack((Object)this.displayItem, this.getWidth() / 2 - 9, 0, false);
    }
    
    public void drawForeground(final int recipe) {
        final List<String> text = InfoHandler.fontRenderer.listFormattedStringToWidth(this.info[recipe], this.getWidth() - 8);
        for (int i = 0; i < text.size(); ++i) {
            final String t = text.get(i);
            GuiDraw.drawString(t, this.getWidth() / 2 - GuiDraw.getStringWidth(t) / 2, 18 + i * 8, InfoHandler.color, false);
        }
    }
    
    public List<PositionedStack> getIngredientStacks(final int recipe) {
        return new ArrayList<PositionedStack>();
    }
    
    public List<PositionedStack> getOtherStacks(final int recipetype) {
        return new ArrayList<PositionedStack>();
    }
    
    public void onUpdate() {
    }
    
    public boolean hasOverlay(final GuiContainer gui, final Container container, final int recipe) {
        return false;
    }
    
    public IRecipeOverlayRenderer getOverlayRenderer(final GuiContainer gui, final int recipe) {
        return null;
    }
    
    public IOverlayHandler getOverlayHandler(final GuiContainer gui, final int recipe) {
        return null;
    }
    
    public int recipiesPerPage() {
        return 1;
    }
    
    public List<String> handleTooltip(final GuiRecipe gui, final List<String> currenttip, final int recipe) {
        return currenttip;
    }
    
    public List<String> handleItemTooltip(final GuiRecipe gui, final ItemStack stack, final List<String> currenttip, final int recipe) {
        return currenttip;
    }
    
    public boolean keyTyped(final GuiRecipe gui, final char keyChar, final int keyCode, final int recipe) {
        return false;
    }
    
    public boolean mouseClicked(final GuiRecipe gui, final int button, final int recipe) {
        return false;
    }
    
    public boolean isValidItem(final ItemStack item) {
        return StatCollector.canTranslate(item.getUnlocalizedName() + ".documentation") || StatCollector.canTranslate(item.getItem().getUnlocalizedName() + ".documentation") || StatCollector.canTranslate(item.getUnlocalizedName() + ".documentation" + ".0") || StatCollector.canTranslate(item.getItem().getUnlocalizedName() + ".documentation" + ".0");
    }
    
    public IUsageHandler getUsageHandler(final String inputId, final Object... ingredients) {
        if (!inputId.equals("item")) {
            return (IUsageHandler)this;
        }
        for (final Object ingredient : ingredients) {
            if (ingredient instanceof ItemStack && this.isValidItem((ItemStack)ingredient)) {
                return (IUsageHandler)new InfoHandler((ItemStack)ingredient);
            }
        }
        return (IUsageHandler)this;
    }
    
    public ICraftingHandler getRecipeHandler(final String outputId, final Object... results) {
        if (!outputId.equals("item")) {
            return (ICraftingHandler)this;
        }
        for (final Object result : results) {
            if (result instanceof ItemStack && this.isValidItem((ItemStack)result)) {
                return (ICraftingHandler)new InfoHandler((ItemStack)result);
            }
        }
        return (ICraftingHandler)this;
    }
    
    static {
        InfoHandler.fontRenderer = Minecraft.getMinecraft().fontRenderer;
        InfoHandler.color = -12566464;
    }
}


