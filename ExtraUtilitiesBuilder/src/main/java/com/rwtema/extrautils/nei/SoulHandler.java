// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.nei;

import net.minecraft.item.Item;
import codechicken.nei.PositionedStack;
import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class SoulHandler extends TemplateRecipeHandler
{
    public String getGuiTexture() {
        return "textures/gui/container/inventory.png";
    }
    
    @SideOnly(Side.CLIENT)
    public void drawForeground(final int recipe) {
        final int x = (GuiDraw.getStringWidth("+") + 10) / 2;
        GuiDraw.drawString("+", 60 - x, 40, -12566464, false);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GuiDraw.changeTexture(Gui.icons);
        GuiDraw.drawTexturedModalRect(60 + x - 9, 40, 16, 0, 9, 9);
        GuiDraw.drawTexturedModalRect(60 + x - 9, 40, 52, 0, 9, 9);
    }
    
    public void drawBackground(final int recipe) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(44, 0, 85, 23, 78, 40);
    }
    
    public void drawExtras(final int recipe) {
        super.drawExtras(recipe);
    }
    
    public boolean isValid() {
        return ExtraUtils.soulEnabled && ExtraUtils.ethericSwordEnabled;
    }
    
    public void loadCraftingRecipes(final ItemStack result) {
        if (this.isValid() && result.getItem() == ExtraUtils.soul) {
            this.arecipes.add(new SoulRecipe());
        }
    }
    
    public void loadUsageRecipes(final ItemStack ingredient) {
        if (this.isValid() && ingredient.getItem() == ExtraUtils.ethericSword) {
            this.arecipes.add(new SoulRecipe());
        }
    }
    
    public String getRecipeName() {
        return "Soul Crafting";
    }
    
    public class SoulRecipe extends TemplateRecipeHandler.CachedRecipe
    {
        public SoulRecipe() {
            super((TemplateRecipeHandler)SoulHandler.this);
        }
        
        public PositionedStack getResult() {
            return new PositionedStack((Object)new ItemStack((Item)ExtraUtils.soul), 103, 13);
        }
        
        public PositionedStack getIngredient() {
            return new PositionedStack((Object)new ItemStack(ExtraUtils.ethericSword), 47, 3);
        }
    }
}
