// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.gui;

import org.lwjgl.opengl.GL11;
import net.minecraft.util.StatCollector;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;

@SideOnly(Side.CLIENT)
public class GuiHoldingBag extends GuiContainer
{
    private static final ResourceLocation texture;
    private EntityPlayer player;
    private int currentFilter;
    
    public GuiHoldingBag(final EntityPlayer player, final int currentFilter) {
        super((Container)new ContainerHoldingBag(player, currentFilter));
        this.currentFilter = -1;
        this.currentFilter = currentFilter;
        this.player = player;
        this.xSize = 176;
        this.ySize = 222;
    }
    
    protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
        if (this.player.inventory.getStackInSlot(this.currentFilter) != null) {
            this.fontRendererObj.drawString(this.player.inventory.getStackInSlot(this.currentFilter).getDisplayName(), 8, 6, 4210752);
        }
        this.fontRendererObj.drawString(this.player.inventory.hasCustomInventoryName() ? this.player.inventory.getInventoryName() : StatCollector.translateToLocal(this.player.inventory.getInventoryName()), 8, this.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int i, final int j) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(GuiHoldingBag.texture);
        final int k = (this.width - this.xSize) / 2;
        final int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
    
    static {
        texture = new ResourceLocation("textures/gui/container/generic_54.png");
    }
}
