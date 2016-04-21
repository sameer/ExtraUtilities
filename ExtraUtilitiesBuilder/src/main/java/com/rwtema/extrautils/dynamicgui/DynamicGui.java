// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.dynamicgui;

import java.util.List;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;

@SideOnly(Side.CLIENT)
public class DynamicGui extends GuiContainer
{
    private static final ResourceLocation texBackground;
    private static final ResourceLocation texWidgets;
    public static int border;
    private DynamicContainer container;
    
    public DynamicGui(final DynamicContainer container) {
        super((Container)container);
        this.xSize = container.width;
        this.ySize = container.height;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        this.container = container;
    }
    
    public float getZLevel() {
        return this.zLevel;
    }
    
    public int getXSize() {
        return this.xSize;
    }
    
    public int getYSize() {
        return this.ySize;
    }
    
    public FontRenderer getFontRenderer() {
        return super.fontRendererObj;
    }
    
    public ResourceLocation getBackground() {
        return DynamicGui.texBackground;
    }
    
    public ResourceLocation getWidgets() {
        return DynamicGui.texWidgets;
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int a, final int b) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(this.getBackground());
        this.xSize = this.container.width;
        this.ySize = this.container.height;
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize - 4, this.ySize - 4);
        this.drawTexturedModalRect(this.guiLeft + this.xSize - 4, this.guiTop, 252, 0, 4, 4);
        this.drawTexturedModalRect(this.guiLeft + this.xSize - 4, this.guiTop + 4, 252, 260 - this.ySize, 4, this.ySize - 4);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop + this.ySize - 4, 0, 252, 4, 4);
        this.drawTexturedModalRect(this.guiLeft + 4, this.guiTop + this.ySize - 4, 260 - this.xSize, 252, this.xSize - 8, 4);
        this.mc.renderEngine.bindTexture(this.getWidgets());
        for (int i = 0; i < this.container.widgets.size(); ++i) {
            this.container.widgets.get(i).renderBackground(this.mc.renderEngine, this, this.guiLeft, this.guiTop);
        }
    }
    
    protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
        List<String> tooltip = null;
        GL11.glDisable(32826);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        for (int i = 0; i < this.container.widgets.size(); ++i) {
            this.mc.renderEngine.bindTexture(this.getWidgets());
            this.container.widgets.get(i).renderForeground(this.mc.renderEngine, this, 0, 0);
            if (this.isInArea(par1 - this.guiLeft, par2 - this.guiTop, this.container.widgets.get(i))) {
                final List<String> t = this.container.widgets.get(i).getToolTip();
                if (t != null) {
                    tooltip = t;
                }
            }
        }
        if (tooltip != null) {
            this.drawHoveringText((List)tooltip, par1 - this.guiLeft, par2 - this.guiTop, this.getFontRenderer());
            GL11.glEnable(2896);
            GL11.glEnable(2929);
            RenderHelper.enableStandardItemLighting();
        }
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(32826);
    }
    
    public boolean isInArea(final int x, final int y, final IWidget w) {
        return x >= w.getX() && x < w.getX() + w.getW() && y >= w.getY() && y < w.getY() + w.getH();
    }
    
    static {
        texBackground = new ResourceLocation("extrautils", "textures/guiBase.png");
        texWidgets = new ResourceLocation("extrautils", "textures/guiWidget.png");
        DynamicGui.border = 5;
    }
}

