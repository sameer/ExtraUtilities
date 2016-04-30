// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.block.render;

import com.rwtema.extrautils.ExtraUtilsProxy;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.Minecraft;
import com.rwtema.extrautils.tileentity.TileEntityBlockColorData;
import com.rwtema.extrautils.block.BlockColorData;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.EntityRenderer;
import com.rwtema.extrautils.block.BlockColor;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.block.Block;
import com.rwtema.extrautils.helper.XURandom;
import java.util.Random;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

@SideOnly(Side.CLIENT)
public class RenderBlockColor implements ISimpleBlockRenderingHandler
{
    Random rand;
    
    public RenderBlockColor() {
        this.rand = XURandom.getInstance();
    }
    
    public void renderInventoryBlock(final Block block, int metadata, final int modelID, final RenderBlocks renderer) {
        final Tessellator var4 = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(-0.5f, -0.0f, -0.5f);
        if (metadata >= 16 || metadata < 0) {
            metadata = this.rand.nextInt(16);
        }
        float f = BlockColor.initColor[metadata][0];
        float f2 = BlockColor.initColor[metadata][1];
        float f3 = BlockColor.initColor[metadata][2];
        if (EntityRenderer.anaglyphEnable) {
            final float f4 = (f * 30.0f + f2 * 59.0f + f3 * 11.0f) / 100.0f;
            final float f5 = (f * 30.0f + f2 * 70.0f) / 100.0f;
            final float f6 = (f * 30.0f + f3 * 70.0f) / 100.0f;
            f = f4;
            f2 = f5;
            f3 = f6;
        }
        GL11.glColor4f(f, f2, f3, 1.0f);
        renderer.colorRedTopLeft *= f;
        renderer.colorRedTopRight *= f;
        renderer.colorRedBottomLeft *= f;
        renderer.colorRedBottomRight *= f;
        renderer.colorGreenTopLeft *= f2;
        renderer.colorGreenTopRight *= f2;
        renderer.colorGreenBottomLeft *= f2;
        renderer.colorGreenBottomRight *= f2;
        renderer.colorBlueTopLeft *= f3;
        renderer.colorBlueTopRight *= f3;
        renderer.colorBlueBottomLeft *= f3;
        renderer.colorBlueBottomRight *= f3;
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
        GL11.glTranslatef(0.5f, 0.0f, 0.5f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public boolean renderWorldBlock(final IBlockAccess world, final int par2, final int par3, final int par4, final Block par1Block, final int modelId, final RenderBlocks renderer) {
        final Tessellator var8 = Tessellator.instance;
        final int i = world.getBlockMetadata(par2, par3, par4);
        float f = BlockColor.initColor[i][0];
        float f2 = BlockColor.initColor[i][1];
        float f3 = BlockColor.initColor[i][2];
        final TileEntity data = world.getTileEntity(BlockColorData.dataBlockX(par2), BlockColorData.dataBlockY(par3), BlockColorData.dataBlockZ(par4));
        if (data instanceof TileEntityBlockColorData) {
            f = ((TileEntityBlockColorData)data).palette[i][0];
            f2 = ((TileEntityBlockColorData)data).palette[i][1];
            f3 = ((TileEntityBlockColorData)data).palette[i][2];
        }
        if (EntityRenderer.anaglyphEnable) {
            final float f4 = (f * 30.0f + f2 * 59.0f + f3 * 11.0f) / 100.0f;
            final float f5 = (f * 30.0f + f2 * 70.0f) / 100.0f;
            final float f6 = (f * 30.0f + f3 * 70.0f) / 100.0f;
            f = f4;
            f2 = f5;
            f3 = f6;
        }
        return (Minecraft.isAmbientOcclusionEnabled() && par1Block.getLightValue() == 0) ? renderer.renderStandardBlockWithAmbientOcclusion(par1Block, par2, par3, par4, f, f2, f3) : renderer.renderStandardBlockWithColorMultiplier(par1Block, par2, par3, par4, f, f2, f3);
    }
    
    public boolean shouldRender3DInInventory(final int modelId) {
        return true;
    }
    
    public int getRenderId() {
        return ExtraUtilsProxy.colorBlockID;
    }
}


