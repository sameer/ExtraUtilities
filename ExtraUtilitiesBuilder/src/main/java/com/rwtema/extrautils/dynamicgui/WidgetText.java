// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.dynamicgui;

import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.nbt.NBTTagCompound;

public class WidgetText implements IWidget
{
    public int x;
    public int y;
    public int w;
    public int h;
    public int align;
    public int color;
    public String msg;
    
    public WidgetText(final int x, final int y, final String msg, final int w) {
        this(x, y, w, 9, 1, 4210752, msg);
    }
    
    public WidgetText(final int x, final int y, final int align, final int color, final String msg) {
        this(x, y, msg.length() * 12, 9, align, color, msg);
    }
    
    public WidgetText(final int x, final int y, final int w, final int h, final int align, final int color, final String msg) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.align = align;
        this.color = color;
        this.msg = msg;
    }
    
    @Override
    public int getX() {
        return this.x;
    }
    
    @Override
    public int getY() {
        return this.y;
    }
    
    @Override
    public int getW() {
        return this.w;
    }
    
    @Override
    public int getH() {
        return this.h;
    }
    
    @Override
    public NBTTagCompound getDescriptionPacket(final boolean changesOnly) {
        return null;
    }
    
    @Override
    public void handleDescriptionPacket(final NBTTagCompound packet) {
    }
    
    @Override
    public void renderForeground(final TextureManager manager, final DynamicGui gui, final int guiLeft, final int guiTop) {
    }
    
    public String getMsgClient() {
        return this.msg;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void renderBackground(final TextureManager manager, final DynamicGui gui, final int guiLeft, final int guiTop) {
        final int x = this.getX() + (1 - this.align) * (this.getW() - gui.getFontRenderer().getStringWidth(this.getMsgClient())) / 2;
        gui.getFontRenderer().drawString(this.getMsgClient(), guiLeft + x, guiTop + this.getY(), 4210752);
        manager.bindTexture(gui.getWidgets());
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public void addToContainer(final DynamicContainer container) {
    }
    
    @Override
    public List<String> getToolTip() {
        return null;
    }
}

