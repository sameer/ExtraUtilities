// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.dynamicgui;

import com.rwtema.extrautils.helper.XUHelperClient;
import java.util.ArrayList;
import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;

public class WidgetEnergy implements IWidget
{
    int curEnergy;
    int curMax;
    IEnergyHandler tile;
    ForgeDirection dir;
    int x;
    int y;
    
    public WidgetEnergy(final IEnergyHandler tile, final ForgeDirection dir, final int x, final int y) {
        this.dir = dir;
        this.tile = tile;
        this.x = x;
        this.y = y;
        this.curEnergy = 0;
        this.curMax = 0;
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
        return 18;
    }
    
    @Override
    public int getH() {
        return 53;
    }
    
    @Override
    public NBTTagCompound getDescriptionPacket(final boolean changesOnly) {
        NBTTagCompound tag = null;
        if (!changesOnly || this.curEnergy != this.tile.getEnergyStored(this.dir)) {
            tag = new NBTTagCompound();
            tag.setInteger("cur", this.tile.getEnergyStored(this.dir));
        }
        if (!changesOnly || this.curMax != this.tile.getMaxEnergyStored(this.dir)) {
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            tag.setInteger("max", this.tile.getMaxEnergyStored(this.dir));
        }
        this.curEnergy = this.tile.getEnergyStored(this.dir);
        this.curMax = this.tile.getMaxEnergyStored(this.dir);
        return tag;
    }
    
    @Override
    public void handleDescriptionPacket(final NBTTagCompound packet) {
        if (packet.hasKey("cur")) {
            this.curEnergy = packet.getInteger("cur");
        }
        if (packet.hasKey("max")) {
            this.curMax = packet.getInteger("max");
        }
    }
    
    @Override
    public void renderForeground(final TextureManager manager, final DynamicGui gui, final int guiLeft, final int guiTop) {
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void renderBackground(final TextureManager manager, final DynamicGui gui, final int guiLeft, final int guiTop) {
        manager.bindTexture(gui.getWidgets());
        int y = 0;
        if (this.curMax > 0 && this.curEnergy > 0) {
            y = 54 * this.curEnergy / this.curMax;
            if (y < 0) {
                y = 0;
            }
        }
        gui.drawTexturedModalRect(guiLeft + this.getX(), guiTop + this.getY(), 160, 0, 18, 54 - y);
        gui.drawTexturedModalRect(guiLeft + this.getX(), guiTop + this.getY() + 54 - y, 178, 54 - y, 18, y);
    }
    
    @Override
    public void addToContainer(final DynamicContainer container) {
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public List<String> getToolTip() {
        if (this.curMax > 0) {
            final List l = new ArrayList();
            l.add(XUHelperClient.commaDelimited(this.curEnergy) + " / " + XUHelperClient.commaDelimited(this.curMax) + " RF");
            return (List<String>)l;
        }
        return null;
    }
}

