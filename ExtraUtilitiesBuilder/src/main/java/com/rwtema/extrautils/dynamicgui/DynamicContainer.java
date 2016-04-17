// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.dynamicgui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;
import com.rwtema.extrautils.ExtraUtilsMod;
import invtweaks.api.container.ContainerSectionCallback;
import com.rwtema.extrautils.gui.InventoryTweaksHelper;
import invtweaks.api.container.ContainerSection;
import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.IInventory;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.packets.PacketGUIWidget;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.LinkedList;
import java.util.List;
import com.rwtema.extrautils.ISidedFunction;
import net.minecraft.inventory.Container;

public abstract class DynamicContainer extends Container
{
    public static final int playerInvWidth = 162;
    public static final int playerInvHeight = 95;
    public static final ISidedFunction<String, Integer> stringWidth;
    public List<IWidget> widgets;
    public int playerSlotsStart;
    public LinkedList<EntityPlayerMP> playerCrafters;
    public int width;
    public int height;
    public boolean changesOnly;
    
    public DynamicContainer() {
        this.widgets = new ArrayList<IWidget>();
        this.playerSlotsStart = -1;
        this.playerCrafters = new LinkedList<EntityPlayerMP>();
        this.width = 176;
        this.height = 166;
        this.changesOnly = false;
    }
    
    public void addSlot(final WidgetSlot slot) {
        this.addSlotToContainer((Slot)slot);
    }
    
    public void addCraftingToCrafters(final ICrafting par1ICrafting) {
        if (par1ICrafting instanceof EntityPlayerMP) {
            this.playerCrafters.add((EntityPlayerMP)par1ICrafting);
        }
        this.changesOnly = false;
        super.addCraftingToCrafters(par1ICrafting);
    }
    
    public void recieveDescriptionPacket(final NBTTagCompound tag) {
        for (int n = this.widgets.size(), j = 0; j < n; ++j) {
            if (tag.hasKey(Integer.toString(j))) {
                final NBTBase nbtobj = tag.getTag(Integer.toString(j));
                if (nbtobj instanceof NBTTagCompound) {
                    final NBTTagCompound desc = (NBTTagCompound)nbtobj;
                    if (desc.hasKey("name")) {
                        final int i = desc.getInteger("name");
                        if (i >= 0 && i < this.widgets.size()) {
                            this.widgets.get(i).handleDescriptionPacket(desc);
                        }
                    }
                }
            }
        }
    }
    
    public void detectAndSendChanges() {
        NBTTagCompound tag = null;
        for (int i = 0; i < this.widgets.size(); ++i) {
            final NBTTagCompound t = this.widgets.get(i).getDescriptionPacket(this.changesOnly);
            if (t != null) {
                if (tag == null) {
                    tag = new NBTTagCompound();
                }
                t.setInteger("name", i);
                tag.setTag(Integer.toString(i), (NBTBase)t);
            }
        }
        this.changesOnly = true;
        if (tag != null) {
            for (final EntityPlayerMP player : this.playerCrafters) {
                NetworkHandler.sendPacketToPlayer(new PacketGUIWidget(this.windowId, tag), (EntityPlayer)player);
            }
        }
        super.detectAndSendChanges();
    }
    
    @SideOnly(Side.CLIENT)
    public void removeCraftingFromCrafters(final ICrafting par1ICrafting) {
        if (par1ICrafting instanceof EntityPlayerMP) {
            this.playerCrafters.remove(par1ICrafting);
        }
        super.removeCraftingFromCrafters(par1ICrafting);
    }
    
    protected void validate() {
        for (final IWidget widget : this.widgets) {
            widget.addToContainer(this);
        }
    }
    
    public void addPlayerSlotsToBottom(final IInventory inventory) {
        this.addPlayerSlots(inventory, (this.width - 162) / 2, this.height - 95);
    }
    
    public void crop(final int border) {
        int maxx = 18;
        int maxy = 18;
        for (final IWidget widget : this.widgets) {
            maxx = Math.max(maxx, widget.getX() + widget.getW());
            maxy = Math.max(maxy, widget.getY() + widget.getH());
        }
        this.width = maxx + border;
        this.height = maxy + border;
    }
    
    public void cropAndAddPlayerSlots(final IInventory inventory) {
        this.crop(4);
        this.height += 95;
        if (this.width < 170) {
            this.width = 170;
        }
        this.addPlayerSlotsToBottom(inventory);
    }
    
    public void addPlayerSlots(final IInventory inventory, final int x, final int y) {
        this.playerSlotsStart = 0;
        for (final IWidget w : this.widgets) {
            if (w instanceof Slot) {
                ++this.playerSlotsStart;
            }
        }
        this.widgets.add(new WidgetTextTranslate(x, y, inventory.getInventoryName(), 162));
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                final WidgetSlot w2 = new WidgetSlot(inventory, k + j * 9 + 9, x + k * 18, y + 14 + j * 18);
                this.addWidget(w2);
            }
        }
        for (int j = 0; j < 9; ++j) {
            final WidgetSlot w3 = new WidgetSlot(inventory, j, x + j * 18, y + 14 + 58);
            this.addWidget(w3);
        }
    }
    
    public void addWidget(final IWidget w) {
        this.widgets.add(w);
    }
    
    public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par2) {
        ItemStack itemstack = null;
        final Slot slot = this.inventorySlots.get(par2);
        if (this.playerSlotsStart > 0 && slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (par2 < this.playerSlotsStart) {
                if (!this.mergeItemStack(itemstack2, this.playerSlotsStart, this.inventorySlots.size(), true)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack2, 0, this.playerSlotsStart, false)) {
                return null;
            }
            if (itemstack2.stackSize == 0) {
                slot.putStack((ItemStack)null);
            }
            else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
    
    protected boolean mergeItemStack(final ItemStack p_75135_1_, final int p_75135_2_, final int p_75135_3_, final boolean p_75135_4_) {
        boolean flag1 = false;
        int k = p_75135_2_;
        if (p_75135_4_) {
            k = p_75135_3_ - 1;
        }
        if (p_75135_1_.isStackable()) {
            while (p_75135_1_.stackSize > 0 && ((!p_75135_4_ && k < p_75135_3_) || (p_75135_4_ && k >= p_75135_2_))) {
                final Slot slot = this.inventorySlots.get(k);
                final ItemStack itemstack1 = slot.getStack();
                if (slot.isItemValid(p_75135_1_) && itemstack1 != null && itemstack1.getItem() == p_75135_1_.getItem() && (!p_75135_1_.getHasSubtypes() || p_75135_1_.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(p_75135_1_, itemstack1)) {
                    final int l = itemstack1.stackSize + p_75135_1_.stackSize;
                    if (l <= p_75135_1_.getMaxStackSize()) {
                        p_75135_1_.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                    else if (itemstack1.stackSize < p_75135_1_.getMaxStackSize()) {
                        p_75135_1_.stackSize -= p_75135_1_.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = p_75135_1_.getMaxStackSize();
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }
                if (p_75135_4_) {
                    --k;
                }
                else {
                    ++k;
                }
            }
        }
        if (p_75135_1_.stackSize > 0) {
            if (p_75135_4_) {
                k = p_75135_3_ - 1;
            }
            else {
                k = p_75135_2_;
            }
            while ((!p_75135_4_ && k < p_75135_3_) || (p_75135_4_ && k >= p_75135_2_)) {
                final Slot slot = this.inventorySlots.get(k);
                final ItemStack itemstack1 = slot.getStack();
                if (itemstack1 == null && slot.isItemValid(p_75135_1_)) {
                    slot.putStack(p_75135_1_.copy());
                    slot.onSlotChanged();
                    p_75135_1_.stackSize = 0;
                    flag1 = true;
                    break;
                }
                if (p_75135_4_) {
                    --k;
                }
                else {
                    ++k;
                }
            }
        }
        return flag1;
    }
    
    @ContainerSectionCallback
    public Map<ContainerSection, List<Slot>> getSlots() {
        return InventoryTweaksHelper.getSlots(this, false);
    }
    
    public int getStringWidth(final String text) {
        return ExtraUtilsMod.proxy.apply(DynamicContainer.stringWidth, text);
    }
    
    public void addTitle(final String name, final boolean translate) {
        WidgetText e;
        if (translate) {
            e = new WidgetTextTranslate(5, 5, name, ExtraUtilsMod.proxy.apply(DynamicContainer.stringWidth, StatCollector.translateToLocal(name)));
        }
        else {
            e = new WidgetText(5, 5, name, ExtraUtilsMod.proxy.apply(DynamicContainer.stringWidth, name));
        }
        this.widgets.add(e);
    }
    
    static {
        stringWidth = new ISidedFunction<String, Integer>() {
            @SideOnly(Side.SERVER)
            @Override
            public Integer applyServer(final String input) {
                return (input == null) ? 0 : input.length();
            }
            
            @SideOnly(Side.CLIENT)
            @Override
            public Integer applyClient(final String input) {
                return (input == null) ? 0 : Minecraft.getMinecraft().fontRenderer.getStringWidth(input);
            }
        };
    }
}
