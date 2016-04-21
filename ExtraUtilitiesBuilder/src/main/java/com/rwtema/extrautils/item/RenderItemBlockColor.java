// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.item;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.EntityRenderer;
import com.rwtema.extrautils.tileentity.TileEntityBlockColorData;
import com.rwtema.extrautils.block.BlockColorData;
import com.rwtema.extrautils.block.BlockColor;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.helper.XURandom;
import java.util.Random;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.IItemRenderer;

@SideOnly(Side.CLIENT)
public class RenderItemBlockColor implements IItemRenderer
{
    private Random rand;
    
    public RenderItemBlockColor() {
        this.rand = XURandom.getInstance();
    }
    
    public boolean handleRenderType(final ItemStack item, final IItemRenderer.ItemRenderType type) {
        switch (type) {
            case ENTITY: {
                return true;
            }
            case EQUIPPED: {
                return true;
            }
            case EQUIPPED_FIRST_PERSON: {
                return true;
            }
            case INVENTORY: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public boolean shouldUseRenderHelper(final IItemRenderer.ItemRenderType type, final ItemStack item, final IItemRenderer.ItemRendererHelper helper) {
        return true;
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        if (item == null || !(item.getItem() instanceof ItemBlock)) {
            return;
        }
        final Block block = ((ItemBlock)item.getItem()).field_150939_a;
        if (block == null || data == null || data.length == 0) {
            return;
        }
        int metadata = item.getItemDamage();
        if (metadata < 0 || metadata >= 16) {
            metadata = this.rand.nextInt(16);
        }
        final RenderBlocks renderer = (RenderBlocks)data[0];
        Entity holder = null;
        if (data.length > 1 && data[1] instanceof Entity) {
            holder = (Entity)data[1];
        }
        if (holder == null) {
            holder = (Entity)Minecraft.getMinecraft().thePlayer;
        }
        final Tessellator var4 = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
        switch (type) {
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON: {
                GL11.glTranslatef(-1.0f, 0.5f, 0.0f);
                break;
            }
            default: {
                GL11.glTranslatef(-0.5f, -0.0f, -0.5f);
                break;
            }
        }
        float f = BlockColor.initColor[metadata][0];
        float f2 = BlockColor.initColor[metadata][1];
        float f3 = BlockColor.initColor[metadata][2];
        if (holder != null && holder.worldObj != null) {
            final TileEntity tiledata = holder.worldObj.getTileEntity(BlockColorData.dataBlockX((int)Math.floor(holder.posX)), BlockColorData.dataBlockY((int)holder.posY), BlockColorData.dataBlockZ((int)Math.floor(holder.posZ)));
            if (tiledata instanceof TileEntityBlockColorData) {
                f = ((TileEntityBlockColorData)tiledata).palette[metadata][0];
                f2 = ((TileEntityBlockColorData)tiledata).palette[metadata][1];
                f3 = ((TileEntityBlockColorData)tiledata).palette[metadata][2];
            }
        }
        if (EntityRenderer.anaglyphEnable) {
            final float f4 = (f * 30.0f + f2 * 59.0f + f3 * 11.0f) / 100.0f;
            final float f5 = (f * 30.0f + f2 * 70.0f) / 100.0f;
            final float f6 = (f * 30.0f + f3 * 70.0f) / 100.0f;
            f = f4;
            f2 = f5;
            f3 = f6;
        }
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glEnable(3008);
        GL11.glColor4f(f, f2, f3, 1.0f);
        final RenderBlocks renderBlocks = renderer;
        renderBlocks.colorRedTopLeft *= f;
        final RenderBlocks renderBlocks2 = renderer;
        renderBlocks2.colorRedTopRight *= f;
        final RenderBlocks renderBlocks3 = renderer;
        renderBlocks3.colorRedBottomLeft *= f;
        final RenderBlocks renderBlocks4 = renderer;
        renderBlocks4.colorRedBottomRight *= f;
        final RenderBlocks renderBlocks5 = renderer;
        renderBlocks5.colorGreenTopLeft *= f2;
        final RenderBlocks renderBlocks6 = renderer;
        renderBlocks6.colorGreenTopRight *= f2;
        final RenderBlocks renderBlocks7 = renderer;
        renderBlocks7.colorGreenBottomLeft *= f2;
        final RenderBlocks renderBlocks8 = renderer;
        renderBlocks8.colorGreenBottomRight *= f2;
        final RenderBlocks renderBlocks9 = renderer;
        renderBlocks9.colorBlueTopLeft *= f3;
        final RenderBlocks renderBlocks10 = renderer;
        renderBlocks10.colorBlueTopRight *= f3;
        final RenderBlocks renderBlocks11 = renderer;
        renderBlocks11.colorBlueBottomLeft *= f3;
        final RenderBlocks renderBlocks12 = renderer;
        renderBlocks12.colorBlueBottomRight *= f3;
        var4.startDrawingQuads();
        var4.setNormal(0.0f, -1.0f, 0.0f);
        renderer.renderFaceYNeg(block, 0.0, -0.5, 0.0, block.getIcon(0, metadata));
        var4.draw();
        var4.startDrawingQuads();
        var4.setNormal(0.0f, 1.0f, 0.0f);
        renderer.renderFaceYPos(block, 0.0, -0.5, 0.0, block.getIcon(1, metadata));
        var4.draw();
        var4.startDrawingQuads();
        var4.setNormal(0.0f, 0.0f, -1.0f);
        renderer.renderFaceXPos(block, 0.0, -0.5, 0.0, block.getIcon(2, metadata));
        var4.draw();
        var4.startDrawingQuads();
        var4.setNormal(0.0f, 0.0f, 1.0f);
        renderer.renderFaceXNeg(block, 0.0, -0.5, 0.0, block.getIcon(3, metadata));
        var4.draw();
        var4.startDrawingQuads();
        var4.setNormal(-1.0f, 0.0f, 0.0f);
        renderer.renderFaceZNeg(block, 0.0, -0.5, 0.0, block.getIcon(4, metadata));
        var4.draw();
        var4.startDrawingQuads();
        var4.setNormal(1.0f, 0.0f, 0.0f);
        renderer.renderFaceZPos(block, 0.0, -0.5, 0.0, block.getIcon(5, metadata));
        var4.draw();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glTranslatef(0.5f, 0.0f, 0.5f);
    }
}


