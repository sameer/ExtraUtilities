// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.gui;

import com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeLiquid;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.texture.TextureMap;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeLiquid;
import java.util.Locale;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeEnergy;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeInventory;
import net.minecraft.util.StatCollector;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeInventory;
import net.minecraft.inventory.Container;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.inventory.IInventory;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;

@SideOnly(Side.CLIENT)
public class GuiTransferNode extends GuiContainer
{
    private static final ResourceLocation texture;
    TileEntityTransferNode node;
    IInventory player;
    FluidStack liquid;
    
    public GuiTransferNode(final IInventory player, final TileEntityTransferNode node) {
        super((Container)new ContainerTransferNode(player, node));
        this.node = node;
        this.player = player;
        this.xSize = 176;
        this.ySize = 222;
    }
    
    protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
        if (this.node instanceof TileEntityTransferNodeInventory) {
            this.fontRendererObj.drawString(((IInventory)this.node).hasCustomInventoryName() ? ((IInventory)this.node).getInventoryName() : StatCollector.translateToLocal(((IInventory)this.node).getInventoryName()), 8, 6, 4210752);
            if (this.inventorySlots.getSlot(0).getHasStack() || this.node instanceof TileEntityRetrievalNodeInventory) {
                String msg = StatCollector.translateToLocal("gui.transferNode.searching");
                this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 26, 4210752);
                msg = "x: " + this.formatRelCoord(this.node.pipe_x) + " y: " + this.formatRelCoord(this.node.pipe_y) + " z: " + this.formatRelCoord(this.node.pipe_z);
                this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 36, 4210752);
            }
        }
        else if (this.node instanceof TileEntityTransferNodeEnergy) {
            String msg = "Holding: " + String.format(Locale.ENGLISH, "%,d", ((ContainerTransferNode)this.inventorySlots).lastenergy) + " RF";
            this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 26, 4210752);
            msg = "Powering: " + ((ContainerTransferNode)this.inventorySlots).lastenergycount + " Connection";
            if (((ContainerTransferNode)this.inventorySlots).lastenergycount != 1) {
                msg += "s";
            }
            this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 36, 4210752);
            msg = StatCollector.translateToLocal("gui.transferNode.searching");
            this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 46, 4210752);
            msg = "x: " + this.formatRelCoord(this.node.pipe_x) + " y: " + this.formatRelCoord(this.node.pipe_y) + " z: " + this.formatRelCoord(this.node.pipe_z);
            this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 56, 4210752);
        }
        else if (this.node instanceof TileEntityTransferNodeLiquid) {
            if (((ContainerTransferNode)this.inventorySlots).liquid_amount > 0) {
                final FluidStack liquid = new FluidStack(((ContainerTransferNode)this.inventorySlots).liquid_type, ((ContainerTransferNode)this.inventorySlots).liquid_amount);
                String msg2 = XUHelper.getFluidName(liquid);
                msg2 = "Holding: " + String.format(Locale.ENGLISH, "%,d", liquid.amount) + "mB of " + msg2;
                this.fontRendererObj.drawString(msg2, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg2) / 2, 26, 4210752);
                msg2 = "x: " + this.formatRelCoord(this.node.pipe_x) + " y: " + this.formatRelCoord(this.node.pipe_y) + " z: " + this.formatRelCoord(this.node.pipe_z);
                this.fontRendererObj.drawString(msg2, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg2) / 2, 36, 4210752);
                if (liquid.getFluid().getIcon() != null) {
                    this.mc.renderEngine.bindTexture((liquid.getFluid().getSpriteNumber() == 0) ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
                    final int col = liquid.getFluid().getColor(liquid);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    this.drawTexturedModelRectFromIcon(80, 83, liquid.getFluid().getIcon(liquid), 16, 16);
                }
            }
            else if (this.node instanceof TileEntityRetrievalNodeLiquid) {
                String msg = StatCollector.translateToLocal("gui.transferNode.searching");
                this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 26, 4210752);
                msg = "x: " + this.formatRelCoord(this.node.pipe_x) + " y: " + this.formatRelCoord(this.node.pipe_y) + " z: " + this.formatRelCoord(this.node.pipe_z);
                this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 36, 4210752);
            }
        }
    }
    
    private String formatRelCoord(final int no) {
        if (no > 0) {
            return "+" + no;
        }
        if (no == 0) {
            return " " + no;
        }
        return "" + no;
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int i, final int j) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(GuiTransferNode.texture);
        final int k = (this.width - this.xSize) / 2;
        final int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
    
    static {
        texture = new ResourceLocation("extrautils", "textures/guiTransferNode.png");
    }
}
