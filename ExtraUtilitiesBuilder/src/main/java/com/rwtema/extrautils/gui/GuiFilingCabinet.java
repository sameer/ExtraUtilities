// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.gui;

import java.util.Iterator;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import net.minecraft.inventory.Slot;
import java.util.ArrayList;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import com.rwtema.extrautils.tileentity.TileEntityFilingCabinet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiFilingCabinet extends GuiContainer
{
    private static final ResourceLocation texture;
    ItemSorter sorter;
    private int numItems;
    private int currentScroll;
    private boolean isScrolling;
    private int prevn;
    private TileEntityFilingCabinet cabinet;
    
    public GuiFilingCabinet(final IInventory player, final TileEntityFilingCabinet cabinet) {
        super((Container)new ContainerFilingCabinet(player, cabinet, true));
        this.sorter = new ItemSorter();
        this.numItems = 0;
        this.currentScroll = 0;
        this.isScrolling = false;
        this.prevn = -1;
        this.xSize = 176;
        this.ySize = 240;
        this.prevn = cabinet.getMaxSlots();
        this.cabinet = cabinet;
    }
    
    public void sortItems() {
        final List<Slot> items = new ArrayList<Slot>();
        items.clear();
        this.numItems = 0;
        for (int i = 0; i < this.cabinet.getMaxSlots(); ++i) {
            if (((Slot)this.inventorySlots.inventorySlots.get(i)).getHasStack()) {
                ++this.numItems;
            }
            else if (i > this.prevn) {
                break;
            }
            items.add((Slot)this.inventorySlots.inventorySlots.get(i));
        }
        this.prevn = this.numItems + 1;
        Collections.sort(items, this.sorter);
        int start = this.getStartSlot();
        if (start > this.numItems - 54) {
            start = this.numItems - 54;
        }
        if (start < 0) {
            start = 0;
        }
        for (int j = 0; j < items.size(); ++j) {
            if (j < start || j >= start + 54) {
                items.get(j).xDisplayPosition = Integer.MIN_VALUE;
                items.get(j).yDisplayPosition = Integer.MIN_VALUE;
            }
            else {
                final int x = (j - start) % 9;
                final int y = (j - start - x) / 9;
                items.get(j).xDisplayPosition = 8 + x * 18;
                items.get(j).yDisplayPosition = 18 + y * 18;
            }
        }
        ContainerFilingCabinet.updated = false;
    }
    
    public int getStartSlot() {
        float t = this.currentScroll;
        t /= 144.0f;
        return (int)Math.floor(t * Math.ceil((this.numItems - 54 + 1) / 1.0f));
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int i, final int j) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(GuiFilingCabinet.texture);
        final int k = (this.width - this.xSize) / 2;
        final int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        if (this.numItems <= 54) {
            this.drawTexturedModalRect(k + this.ScrollX(), l + 128, 176, 9, 17, 8);
        }
        else {
            this.drawTexturedModalRect(k + this.ScrollX(), l + 128, 176, 0, 17, 8);
        }
    }
    
    public void setScroll(final int mX) {
        final int prevScroll = this.getStartSlot();
        if (this.numItems <= 54) {
            this.currentScroll = 0;
        }
        else {
            this.currentScroll = mX;
        }
        if (prevScroll != this.getStartSlot()) {
            this.sortItems();
        }
    }
    
    public int ScrollX() {
        if (this.numItems <= 54) {
            return 8;
        }
        if (this.currentScroll < 0) {
            return 8;
        }
        if (this.currentScroll > 143) {
            return 151;
        }
        return 8 + this.currentScroll;
    }
    
    protected void mouseMovedOrUp(final int par1, final int par2, final int par3) {
        if (par3 >= 0) {
            this.isScrolling = false;
        }
        super.mouseMovedOrUp(par1, par2, par3);
    }
    
    protected void mouseClicked(final int par1, final int par2, final int par3) {
        if (!this.isScrolling && par3 == 0 && this.func_146978_c(8, 128, 162, 8, par1, par2)) {
            this.isScrolling = true;
            this.setScroll(par1 - this.guiLeft - 8 - 9);
        }
        super.mouseClicked(par1, par2, par3);
    }
    
    protected void mouseClickMove(final int par1, final int par2, final int par3, final long par4) {
        if (this.isScrolling) {
            this.setScroll(par1 - this.guiLeft - 8 - 9);
        }
        super.mouseClickMove(par1, par2, par3, par4);
    }
    
    public void handleMouseInput() {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0 && this.numItems > 54) {
            if (i > 0) {
                i = 1;
            }
            if (i < 0) {
                i = -1;
            }
            this.currentScroll -= i * 9;
            if (this.currentScroll < 0) {
                this.currentScroll = 0;
            }
            if (this.currentScroll > 153) {
                this.currentScroll = 153;
            }
            this.setScroll(this.currentScroll);
            this.sortItems();
        }
    }
    
    public void drawScreen(final int par1, final int par2, final float par3) {
        if (ContainerFilingCabinet.updated) {
            this.sortItems();
        }
        SlotFilingCabinet.drawing = true;
        super.drawScreen(par1, par2, par3);
        SlotFilingCabinet.drawing = false;
    }
    
    static {
        texture = new ResourceLocation("extrautils", "textures/guiFilingCabinet.png");
    }
    
    public static class ItemSorter implements Comparator<Slot>
    {
        @Override
        public int compare(final Slot arg0, final Slot arg1) {
            if (!arg0.getHasStack()) {
                if (!arg1.getHasStack()) {
                    return 0;
                }
                return 1;
            }
            else {
                if (!arg1.getHasStack()) {
                    return -1;
                }
                final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
                final boolean sneak = player.movementInput.sneak;
                player.movementInput.sneak = false;
                final String i1 = this.concat(arg0.getStack().getTooltip((EntityPlayer)Minecraft.getMinecraft().thePlayer, true));
                final String i2 = this.concat(arg1.getStack().getTooltip((EntityPlayer)Minecraft.getMinecraft().thePlayer, true));
                player.movementInput.sneak = sneak;
                final int a = i1.compareTo(i2);
                if (a != 0) {
                    return a;
                }
                final int b = arg0.getStack().getItem().getUnlocalizedName().compareTo(arg0.getStack().getItem().getUnlocalizedName());
                if (b != 0) {
                    return b;
                }
                final int c = this.intCompare(arg0.getStack().getItemDamage(), arg0.getStack().getItemDamage());
                if (c != 0) {
                    return c;
                }
                return 0;
            }
        }
        
        public int intCompare(final int a, final int b) {
            if (a == b) {
                return 0;
            }
            if (a > b) {
                return 1;
            }
            return -1;
        }
        
        public String concat(final List list) {
            String s = "";
            for (final Object aList : list) {
                s = s + aList + "\n";
            }
            return s;
        }
    }
}

