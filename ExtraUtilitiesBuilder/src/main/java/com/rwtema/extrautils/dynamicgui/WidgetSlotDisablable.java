// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.dynamicgui;

import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class WidgetSlotDisablable extends WidgetSlot
{
    boolean enabled;
    String methodName;
    
    public WidgetSlotDisablable(final IInventory inv, final int slot, final int x, final int y, final String methodName) {
        super(inv, slot, x, y);
        this.enabled = true;
        this.methodName = methodName;
    }
    
    @Override
    public boolean canTakeStack(final EntityPlayer par1EntityPlayer) {
        return this.enabled;
    }
    
    public boolean isEnabled() {
        try {
            return (boolean)this.inventory.getClass().getMethod(this.methodName, (Class<?>[])new Class[0]).invoke(this.inventory);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void renderForeground(final TextureManager manager, final DynamicGui gui, final int guiLeft, final int guiTop) {
        if (!this.enabled) {
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
    
    @Override
    public void handleDescriptionPacket(final NBTTagCompound packet) {
        this.enabled = packet.getBoolean("a");
    }
    
    @Override
    public NBTTagCompound getDescriptionPacket(final boolean changesOnly) {
        final boolean newEnabled = this.isEnabled();
        if (!changesOnly || newEnabled != this.enabled) {
            this.enabled = newEnabled;
            final NBTTagCompound tags = new NBTTagCompound();
            tags.setBoolean("a", this.enabled);
            return tags;
        }
        return null;
    }
}

