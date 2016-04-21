// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block.render;

import com.rwtema.extrautils.ExtraUtilsProxy;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.EntityRenderer;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.block.Block;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

@SideOnly(Side.CLIENT)
public class RenderBlockConnectedTextures implements ISimpleBlockRenderingHandler
{
    public static FakeRenderBlocks fakeRender;
    
    public FakeRenderBlocks getFakeRender() {
        return RenderBlockConnectedTextures.fakeRender;
    }
    
    public void renderInventoryBlock(final Block block, final int metadata, final int modelID, final RenderBlocks renderer) {
        final Tessellator var4 = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(-0.5f, -0.0f, -0.5f);
        float f = 1.0f;
        float f2 = 1.0f;
        float f3 = 1.0f;
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
        if (block.getIcon(0, metadata) == null) {
            return;
        }
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
    
    public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block, final int modelId, final RenderBlocks renderer) {
        if (renderer.hasOverrideBlockTexture()) {
            return renderer.renderStandardBlock(block, x, y, z);
        }
        this.getFakeRender().setWorld(renderer.blockAccess);
        this.getFakeRender().curBlock = world.getBlock(x, y, z);
        this.getFakeRender().curMeta = world.getBlockMetadata(x, y, z);
        block.setBlockBoundsBasedOnState(RenderBlockConnectedTextures.fakeRender.blockAccess, x, y, z);
        this.getFakeRender().setRenderBoundsFromBlock(block);
        return this.getFakeRender().renderStandardBlock(block, x, y, z);
    }
    
    public boolean shouldRender3DInInventory(final int modelId) {
        return true;
    }
    
    public int getRenderId() {
        return ExtraUtilsProxy.connectedTextureID;
    }
    
    static {
        RenderBlockConnectedTextures.fakeRender = new FakeRenderBlocks();
    }
}

