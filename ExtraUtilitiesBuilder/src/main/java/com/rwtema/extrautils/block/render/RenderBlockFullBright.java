// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block.render;

import com.rwtema.extrautils.ExtraUtilsProxy;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.block.Block;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

@SideOnly(Side.CLIENT)
public class RenderBlockFullBright implements ISimpleBlockRenderingHandler
{
    public void renderInventoryBlock(final Block block, final int metadata, final int modelID, final RenderBlocks renderer) {
        final Tessellator var4 = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(-0.5f, -0.0f, -0.5f);
        final int l = block.getRenderColor(metadata);
        final float r = (l >> 16 & 0xFF) / 255.0f;
        final float g = (l >> 8 & 0xFF) / 255.0f;
        final float b = (l & 0xFF) / 255.0f;
        GL11.glColor4f(r, g, b, 1.0f);
        GL11.glDisable(2896);
        renderer.enableAO = false;
        var4.startDrawingQuads();
        var4.setNormal(0.0f, 1.0f, 0.0f);
        renderer.renderFaceYNeg(block, 0.0, -0.5, 0.0, block.getIcon(0, metadata));
        renderer.renderFaceYPos(block, 0.0, -0.5, 0.0, block.getIcon(1, metadata));
        renderer.renderFaceZNeg(block, 0.0, -0.5, 0.0, block.getIcon(2, metadata));
        renderer.renderFaceZPos(block, 0.0, -0.5, 0.0, block.getIcon(3, metadata));
        renderer.renderFaceXNeg(block, 0.0, -0.5, 0.0, block.getIcon(4, metadata));
        renderer.renderFaceXPos(block, 0.0, -0.5, 0.0, block.getIcon(5, metadata));
        var4.draw();
        GL11.glTranslatef(0.5f, 0.0f, 0.5f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(2896);
    }
    
    public boolean renderWorldBlock(final IBlockAccess world, final int par2, final int par3, final int par4, final Block par1Block, final int modelId, final RenderBlocks renderer) {
        renderer.enableAO = false;
        final Tessellator tessellator = Tessellator.instance;
        boolean flag = false;
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        tessellator.setBrightness(240);
        final int meta = world.getBlockMetadata(par2, par3, par4);
        final int l = par1Block.getRenderColor(meta);
        final float r = (l >> 16 & 0xFF) / 255.0f;
        final float g = (l >> 8 & 0xFF) / 255.0f;
        final float b = (l & 0xFF) / 255.0f;
        tessellator.setColorOpaque_F(r, g, b);
        if (renderer.renderAllFaces || par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3 - 1, par4, 0)) {
            renderer.renderFaceYNeg(par1Block, (double)par2, (double)par3, (double)par4, renderer.getBlockIcon(par1Block, renderer.blockAccess, par2, par3, par4, 0));
            flag = true;
        }
        if (renderer.renderAllFaces || par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3 + 1, par4, 1)) {
            renderer.renderFaceYPos(par1Block, (double)par2, (double)par3, (double)par4, renderer.getBlockIcon(par1Block, renderer.blockAccess, par2, par3, par4, 1));
            flag = true;
        }
        if (renderer.renderAllFaces || par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3, par4 - 1, 2)) {
            renderer.renderFaceZNeg(par1Block, (double)par2, (double)par3, (double)par4, renderer.getBlockIcon(par1Block, renderer.blockAccess, par2, par3, par4, 2));
            flag = true;
        }
        if (renderer.renderAllFaces || par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3, par4 + 1, 3)) {
            renderer.renderFaceZPos(par1Block, (double)par2, (double)par3, (double)par4, renderer.getBlockIcon(par1Block, renderer.blockAccess, par2, par3, par4, 3));
            flag = true;
        }
        if (renderer.renderAllFaces || par1Block.shouldSideBeRendered(renderer.blockAccess, par2 - 1, par3, par4, 4)) {
            renderer.renderFaceXNeg(par1Block, (double)par2, (double)par3, (double)par4, renderer.getBlockIcon(par1Block, renderer.blockAccess, par2, par3, par4, 4));
            flag = true;
        }
        if (renderer.renderAllFaces || par1Block.shouldSideBeRendered(renderer.blockAccess, par2 + 1, par3, par4, 5)) {
            renderer.renderFaceXPos(par1Block, (double)par2, (double)par3, (double)par4, renderer.getBlockIcon(par1Block, renderer.blockAccess, par2, par3, par4, 5));
            flag = true;
        }
        return flag;
    }
    
    public boolean shouldRender3DInInventory(final int modelId) {
        return true;
    }
    
    public int getRenderId() {
        return ExtraUtilsProxy.fullBrightBlockID;
    }
}

