// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.gui;

import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.fluids.FluidStack;
import com.rwtema.extrautils.helper.XUHelper;
import java.util.ArrayList;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraft.inventory.Slot;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiFilter extends GuiContainer
{
    private static final ResourceLocation texture;
    private EntityPlayer player;
    private int currentFilter;
    
    public GuiFilter(final EntityPlayer player, final int currentFilter) {
        super((Container)new ContainerFilter(player, currentFilter));
        this.currentFilter = -1;
        this.currentFilter = currentFilter;
        this.player = player;
        this.xSize = 176;
        this.ySize = 131;
    }
    
    public List<String> handleItemTooltip(final ItemStack stack, final int mousex, final int mousey, final List<String> tooltip) {
        final List<String> overide = this.getOveride(stack, mousex, mousey);
        if (overide != null) {
            return overide;
        }
        return tooltip;
    }
    
    public List<String> getOveride(final ItemStack par1ItemStack, final int par2, final int par3) {
        int j1 = 0;
        while (j1 < this.inventorySlots.inventorySlots.size()) {
            final Slot slot = (Slot) this.inventorySlots.inventorySlots.get(j1);
            if (slot instanceof SlotGhostItemContainer && slot.getHasStack()) {
                if (!this.func_146978_c(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, par2, par3) || !slot.func_111238_b()) {
                    return null;
                }
                final ItemStack filter = this.player.inventory.getStackInSlot(this.currentFilter);
                if (filter != null && filter.hasTagCompound() && filter.getTagCompound().hasKey("isLiquid_" + slot.slotNumber)) {
                    final FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(slot.getStack());
                    final List t = new ArrayList();
                    t.add(XUHelper.getFluidName(liquid));
                    return (List<String>)t;
                }
                return null;
            }
            else {
                ++j1;
            }
        }
        return null;
    }
    
    protected void drawSlotInventory(final Slot slot) {
        final int i = slot.slotNumber;
        if (slot instanceof SlotGhostItemContainer && slot.getHasStack()) {
            final ItemStack filter = this.player.inventory.getStackInSlot(this.currentFilter);
            if (filter != null && filter.hasTagCompound() && filter.getTagCompound().hasKey("isLiquid_" + i)) {
                final FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(slot.getStack());
                if (liquid != null && liquid.getFluid().getIcon() != null) {
                    GL11.glDisable(2896);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    this.drawTexturedModelRectFromIcon(slot.xDisplayPosition, slot.yDisplayPosition, liquid.getFluid().getIcon(liquid), 16, 16);
                    GL11.glEnable(2896);
                }
            }
        }
    }
    
    protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
        if (this.player.inventory.getStackInSlot(this.currentFilter) != null) {
            this.fontRendererObj.drawString(this.player.inventory.getStackInSlot(this.currentFilter).getDisplayName(), 8, 6, 4210752);
        }
        this.fontRendererObj.drawString(this.player.inventory.hasCustomInventoryName() ? this.player.inventory.getInventoryName() : StatCollector.translateToLocal(this.player.inventory.getInventoryName()), 8, this.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int i, final int j) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(GuiFilter.texture);
        final int k = (this.width - this.xSize) / 2;
        final int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
    
    static {
        texture = new ResourceLocation("extrautils", "textures/guiFilter.png");
    }
}


