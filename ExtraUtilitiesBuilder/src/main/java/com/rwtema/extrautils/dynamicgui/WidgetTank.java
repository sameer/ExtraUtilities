// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.dynamicgui;

import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.helper.XUHelperClient;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.FluidStack;

public class WidgetTank implements IWidget
{
    public static final int[] ux2;
    public static final int[] uy2;
    public static final int[] uw2;
    public static final int[] uh2;
    public static final int[] ux;
    public static final int[] uy;
    public static final int[] uw;
    public static final int[] uh;
    FluidStack curFluid;
    int curCapacity;
    FluidTankInfo tankInfo;
    int shape;
    int x;
    int y;
    
    public WidgetTank(final FluidTankInfo tankInfo, final int x, final int y) {
        this(tankInfo, x, y, 0);
    }
    
    public WidgetTank(final FluidTankInfo tankInfo, final int x, final int y, final int shape) {
        this.curFluid = null;
        this.curCapacity = 0;
        this.tankInfo = tankInfo;
        this.shape = shape;
        this.x = x;
        this.y = y;
    }
    
    @SideOnly(Side.CLIENT)
    public static void renderLiquid(final FluidStack fluid, final TextureManager manager, final DynamicGui gui, final int x, final int y, final int w, final int h) {
        if (fluid == null) {
            return;
        }
        if (w == 0 || h == 0) {
            return;
        }
        manager.bindTexture((fluid.getFluid().getSpriteNumber() == 0) ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
        final int color = fluid.getFluid().getColor(fluid);
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, 1.0f);
        final IIcon icon = fluid.getFluid().getIcon(fluid);
        final Tessellator tessellator = Tessellator.instance;
        for (int dx1 = x; dx1 < x + w; dx1 += 16) {
            for (int dy1 = y; dy1 < y + h; dy1 += 16) {
                final int dx2 = Math.min(dx1 + 16, x + w);
                final int dy2 = Math.min(dy1 + 16, y + h);
                tessellator.startDrawingQuads();
                tessellator.addVertexWithUV((double)dx1, (double)dy2, (double)gui.getZLevel(), (double)icon.getMinU(), (double)(icon.getMinV() + (icon.getMaxV() - icon.getMinV()) * (dy2 - dy1) / 16.0f));
                tessellator.addVertexWithUV((double)dx2, (double)dy2, (double)gui.getZLevel(), (double)(icon.getMinU() + (icon.getMaxU() - icon.getMinU()) * (dx2 - dx1) / 16.0f), (double)(icon.getMinV() + (icon.getMaxV() - icon.getMinV()) * (dy2 - dy1) / 16.0f));
                tessellator.addVertexWithUV((double)dx2, (double)dy1, (double)gui.getZLevel(), (double)(icon.getMinU() + (icon.getMaxU() - icon.getMinU()) * (dx2 - dx1) / 16.0f), (double)icon.getMinV());
                tessellator.addVertexWithUV((double)dx1, (double)dy1, (double)gui.getZLevel(), (double)icon.getMinU(), (double)icon.getMinV());
                tessellator.draw();
            }
        }
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
        return WidgetTank.uw[this.shape];
    }
    
    @Override
    public int getH() {
        return WidgetTank.uh[this.shape];
    }
    
    @Override
    public NBTTagCompound getDescriptionPacket(final boolean changesOnly) {
        final FluidStack newFluid = this.tankInfo.fluid;
        if (changesOnly && this.curCapacity == this.tankInfo.capacity) {
            if (this.curFluid == null) {
                if (newFluid == null) {
                    return null;
                }
            }
            else if (this.curFluid.isFluidEqual(newFluid) && newFluid.amount == this.curFluid.amount) {
                return null;
            }
        }
        this.curFluid = ((newFluid != null) ? newFluid.copy() : null);
        this.curCapacity = this.tankInfo.capacity;
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("capacity", this.curCapacity);
        if (this.curFluid != null) {
            this.curFluid.writeToNBT(tag);
        }
        return tag;
    }
    
    @Override
    public void handleDescriptionPacket(final NBTTagCompound packet) {
        this.curCapacity = packet.getInteger("capacity");
        this.curFluid = FluidStack.loadFluidStackFromNBT(packet);
    }
    
    @Override
    public void renderForeground(final TextureManager manager, final DynamicGui gui, final int guiLeft, final int guiTop) {
        if (this.curFluid != null && this.curFluid.getFluid() != null && this.curCapacity > 0) {
            final int a = this.curFluid.amount * (this.getH() - 2) / this.curCapacity;
            renderLiquid(this.curFluid, manager, gui, guiLeft + this.getX() + 1, guiTop + this.getY() - 1 + this.getH() - a, this.getW() - 2, a);
        }
        manager.bindTexture(gui.getWidgets());
        gui.drawTexturedModalRect(guiLeft + this.getX() + this.getW() - WidgetTank.uw2[this.shape] - 1, guiTop + this.getY() + 1, WidgetTank.ux2[this.shape] + WidgetTank.uw2[this.shape], WidgetTank.uy2[this.shape], WidgetTank.uw2[this.shape], Math.min(this.getH() - 2, WidgetTank.uh2[this.shape]));
        gui.drawTexturedModalRect(guiLeft + this.getX() + 1, guiTop + this.getY() + 1, WidgetTank.ux2[this.shape], WidgetTank.uy2[this.shape], WidgetTank.uw2[this.shape], Math.min(this.getH() - 2, WidgetTank.uh2[this.shape]));
    }
    
    @Override
    public void renderBackground(final TextureManager manager, final DynamicGui gui, final int guiLeft, final int guiTop) {
        manager.bindTexture(gui.getWidgets());
        gui.drawTexturedModalRect(guiLeft + this.getX(), guiTop + this.getY(), WidgetTank.ux[this.shape], WidgetTank.uy[this.shape], WidgetTank.uw[this.shape], WidgetTank.uh[this.shape]);
    }
    
    @Override
    public void addToContainer(final DynamicContainer container) {
    }
    
    @Override
    public List<String> getToolTip() {
        if (this.curCapacity > 0) {
            final List l = new ArrayList();
            if (this.curFluid == null) {
                l.add("0 / " + XUHelperClient.commaDelimited(this.curCapacity));
            }
            else {
                l.add(XUHelper.getFluidName(this.curFluid) + ": " + XUHelperClient.commaDelimited(this.curFluid.amount) + " / " + XUHelperClient.commaDelimited(this.curCapacity));
            }
            return (List<String>)l;
        }
        return null;
    }
    
    static {
        ux2 = new int[] { 18, 18, 18 };
        uy2 = new int[] { 0, 0, 0 };
        uw2 = new int[] { 7, 7, 7 };
        uh2 = new int[] { 64, 64, 64 };
        ux = new int[] { 32, 0, 50 };
        uy = new int[] { 0, 0, 0 };
        uw = new int[] { 18, 18, 18 };
        uh = new int[] { 33, 18, 65 };
    }
}

