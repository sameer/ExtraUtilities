// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.dynamicgui;

import org.lwjgl.opengl.GL11;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.inventory.IInventory;

public class WidgetSlotGhost extends WidgetSlot
{
    public WidgetSlotGhost(final IInventory inv, final int slot, final int x, final int y) {
        super(inv, slot, x, y);
    }
    
    @Override
    public void renderBackground(final TextureManager manager, final DynamicGui gui, final int x, final int y) {
    }
    
    @Override
    public boolean isItemValid(final ItemStack par1ItemStack) {
        return false;
    }
    
    @Override
    public boolean canTakeStack(final EntityPlayer par1EntityPlayer) {
        return false;
    }
    
    public void putStack(final ItemStack p_75215_1_) {
    }
    
    @Override
    public void renderForeground(final TextureManager manager, final DynamicGui gui, final int guiLeft, final int guiTop) {
        final boolean blendLevel = GL11.glIsEnabled(3042);
        if (!blendLevel) {
            GL11.glEnable(3042);
        }
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.4f);
        gui.drawTexturedModalRect(guiLeft + this.getX(), guiTop + this.getY(), 0, 0, 18, 18);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (!blendLevel) {
            GL11.glDisable(3042);
        }
    }
}

