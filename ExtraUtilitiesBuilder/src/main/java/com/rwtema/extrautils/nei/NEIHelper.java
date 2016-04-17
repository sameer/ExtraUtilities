// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.nei;

import codechicken.nei.recipe.DefaultOverlayHandler;
import codechicken.nei.recipe.RecipeInfo;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiContainer;

public class NEIHelper
{
    public static boolean isCraftingGUI(final GuiContainer gui) {
        return gui.getClass() == GuiCrafting.class || (RecipeInfo.hasOverlayHandler(gui, "crafting") && RecipeInfo.getOverlayHandler(gui, "crafting").getClass() == DefaultOverlayHandler.class);
    }
}
