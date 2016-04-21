// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.dynamicgui;

import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class WidgetSlot extends Slot implements IWidget
{
    public boolean playerSlot;
    boolean isISided;
    int side;
    
    public WidgetSlot(final IInventory inv, final int slot, final int x, final int y) {
        super(inv, slot, x + 1, y + 1);
        this.isISided = (inv instanceof ISidedInventory);
        this.side = 0;
        if (this.isISided) {
            this.side = 0;
            while (this.side < 6) {
                final int[] arr$;
                final int[] slots = arr$ = ((ISidedInventory)inv).getAccessibleSlotsFromSide(this.side);
                for (final int s : arr$) {
                    if (s == slot) {
                        return;
                    }
                }
                ++this.side;
            }
        }
    }
    
    public boolean isItemValid(final ItemStack par1ItemStack) {
        return this.inventory.isItemValidForSlot(this.slotNumber, par1ItemStack);
    }
    
    public boolean canTakeStack(final EntityPlayer par1EntityPlayer) {
        return true;
    }
    
    public int getX() {
        return this.xDisplayPosition - 1;
    }
    
    public int getY() {
        return this.yDisplayPosition - 1;
    }
    
    public int getW() {
        return 18;
    }
    
    public int getH() {
        return 18;
    }
    
    public void addToContainer(final DynamicContainer container) {
        container.addSlot(this);
    }
    
    @SideOnly(Side.CLIENT)
    public void renderBackground(final TextureManager manager, final DynamicGui gui, final int x, final int y) {
        gui.drawTexturedModalRect(x + this.getX(), y + this.getY(), 0, 0, 18, 18);
    }
    
    public void handleDescriptionPacket(final NBTTagCompound packet) {
    }
    
    public void renderForeground(final TextureManager manager, final DynamicGui gui, final int guiLeft, final int guiTop) {
    }
    
    public NBTTagCompound getDescriptionPacket(final boolean changesOnly) {
        return null;
    }
    
    public List<String> getToolTip() {
        return null;
    }
}

