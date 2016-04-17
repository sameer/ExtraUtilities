// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block.render;

import com.rwtema.extrautils.ExtraUtilsProxy;
import net.minecraft.util.IIcon;
import java.util.Iterator;
import com.rwtema.extrautils.block.BoxModel;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.Tessellator;
import com.rwtema.extrautils.block.Box;
import org.lwjgl.opengl.GL11;
import com.rwtema.extrautils.block.IMultiBoxBlock;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.block.Block;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

@SideOnly(Side.CLIENT)
public class RenderBlockMultiBlock implements ISimpleBlockRenderingHandler
{
    static boolean rendering;
    
    public void renderInventoryBlock(final Block block, final int metadata, final int modelID, final RenderBlocks renderer) {
        if (!(block instanceof IMultiBoxBlock)) {
            return;
        }
        final BoxModel boxes = ((IMultiBoxBlock)block).getInventoryModel(metadata);
        if (boxes == null) {
            return;
        }
        if (boxes.size() == 0) {
            return;
        }
        ((IMultiBoxBlock)block).prepareForRender(boxes.label);
        final Box union = boxes.boundingBox();
        final float dx = (union.maxX + union.minX) / 2.0f - 0.5f;
        final float dy = (union.maxY + union.minY) / 2.0f - 0.5f;
        final float dz = (union.maxZ + union.minZ) / 2.0f - 0.5f;
        GL11.glTranslatef(-dx, -dy, -dz);
        GL11.glRotatef((float)boxes.invModelRotate, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
        for (final Box b : boxes) {
            block.setBlockBounds(b.minX, b.minY, b.minZ, b.maxX, b.maxY, b.maxZ);
            ((IMultiBoxBlock)block).prepareForRender(b.label);
            final Tessellator tessellator = Tessellator.instance;
            renderer.setRenderBoundsFromBlock(block);
            GL11.glColor3f((b.color >> 16 & 0xFF) / 255.0f, (b.color >> 8 & 0xFF) / 255.0f, (b.color & 0xFF) / 255.0f);
            renderer.uvRotateEast = b.uvRotateEast;
            renderer.uvRotateWest = b.uvRotateWest;
            renderer.uvRotateSouth = b.uvRotateSouth;
            renderer.uvRotateNorth = b.uvRotateNorth;
            renderer.uvRotateTop = b.uvRotateTop;
            renderer.uvRotateBottom = b.uvRotateBottom;
            if (!b.invisibleSide[0]) {
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, -1.0f, 0.0f);
                renderer.renderFaceYNeg(block, 0.0, 0.0, 0.0, this.getTexture(null, 0, 0, 0, renderer, b, block, 0, metadata));
                tessellator.draw();
            }
            if (!b.invisibleSide[1]) {
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 1.0f, 0.0f);
                renderer.renderFaceYPos(block, 0.0, 0.0, 0.0, this.getTexture(null, 0, 0, 0, renderer, b, block, 1, metadata));
                tessellator.draw();
            }
            if (!b.invisibleSide[2]) {
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1.0f, 0.0f, 0.0f);
                renderer.renderFaceXNeg(block, 0.0, 0.0, 0.0, this.getTexture(null, 0, 0, 0, renderer, b, block, 2, metadata));
                tessellator.draw();
            }
            if (!b.invisibleSide[3]) {
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0f, 0.0f, 0.0f);
                renderer.flipTexture = true;
                renderer.renderFaceXPos(block, 0.0, 0.0, 0.0, this.getTexture(null, 0, 0, 0, renderer, b, block, 3, metadata));
                renderer.flipTexture = false;
                tessellator.draw();
            }
            if (!b.invisibleSide[4]) {
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 0.0f, -1.0f);
                renderer.renderFaceZNeg(block, 0.0, 0.0, 0.0, this.getTexture(null, 0, 0, 0, renderer, b, block, 4, metadata));
                tessellator.draw();
            }
            if (!b.invisibleSide[5]) {
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 0.0f, 1.0f);
                renderer.flipTexture = true;
                renderer.renderFaceZPos(block, 0.0, 0.0, 0.0, this.getTexture(null, 0, 0, 0, renderer, b, block, 5, metadata));
                renderer.flipTexture = false;
                tessellator.draw();
            }
            renderer.uvRotateEast = 0;
            renderer.uvRotateWest = 0;
            renderer.uvRotateSouth = 0;
            renderer.uvRotateNorth = 0;
            renderer.uvRotateTop = 0;
            renderer.uvRotateBottom = 0;
        }
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        GL11.glTranslatef(dx, dy, dz);
    }
    
    public IIcon getTexture(final IBlockAccess world, final int x, final int y, final int z, final RenderBlocks renderer, final Box box, final Block block, final int side, final int metadata) {
        if (box.textureSide[side] != null) {
            return box.textureSide[side];
        }
        if (box.texture != null) {
            return box.texture;
        }
        if (world == null) {
            return renderer.getBlockIconFromSideAndMetadata(block, side, metadata);
        }
        return renderer.getBlockIcon(block, world, x, y, z, side);
    }
    
    public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block, final int modelId, final RenderBlocks renderer) {
        final int metadata = world.getBlockMetadata(x, y, z);
        if (!(block instanceof IMultiBoxBlock)) {
            return false;
        }
        final BoxModel boxes = ((IMultiBoxBlock)block).getWorldModel(world, x, y, z);
        if (boxes == null || boxes.size() == 0) {
            return false;
        }
        ((IMultiBoxBlock)block).prepareForRender(boxes.label);
        for (final Box b1 : boxes) {
            final float r = (b1.color >> 16 & 0xFF) / 255.0f;
            final float g = (b1.color >> 8 & 0xFF) / 255.0f;
            final float b2 = (b1.color & 0xFF) / 255.0f;
            ((IMultiBoxBlock)block).prepareForRender(b1.label);
            block.setBlockBounds(b1.minX, b1.minY, b1.minZ, b1.maxX, b1.maxY, b1.maxZ);
            renderer.uvRotateEast = b1.uvRotateEast;
            renderer.uvRotateWest = b1.uvRotateWest;
            renderer.uvRotateSouth = b1.uvRotateSouth;
            renderer.uvRotateNorth = b1.uvRotateNorth;
            renderer.uvRotateTop = b1.uvRotateTop;
            renderer.uvRotateBottom = b1.uvRotateBottom;
            renderer.setRenderBoundsFromBlock(block);
            if (b1.renderAsNormalBlock) {
                renderer.renderStandardBlock(block, x + b1.offsetx, y + b1.offsety, z + b1.offsetz);
            }
            else {
                renderer.enableAO = false;
                final Tessellator tessellator = Tessellator.instance;
                tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x + b1.offsetx, y + b1.offsety, z + b1.offsetz));
                tessellator.setColorOpaque_F(r * 0.5f, g * 0.5f, b2 * 0.5f);
                renderer.flipTexture = false;
                if (!b1.invisibleSide[0]) {
                    renderer.renderFaceYNeg(block, (double)(x + b1.offsetx), (double)(y + b1.offsety), (double)(z + b1.offsetz), this.getTexture(world, x, y, z, renderer, b1, block, 0, metadata));
                }
                tessellator.setColorOpaque_F(r, g, b2);
                if (!b1.invisibleSide[1]) {
                    renderer.renderFaceYPos(block, (double)(x + b1.offsetx), (double)(y + b1.offsety), (double)(z + b1.offsetz), this.getTexture(world, x, y, z, renderer, b1, block, 1, metadata));
                }
                tessellator.setColorOpaque_F(r * 0.8f, g * 0.8f, b2 * 0.8f);
                renderer.flipTexture = true;
                if (!b1.invisibleSide[2]) {
                    renderer.renderFaceZNeg(block, (double)(x + b1.offsetx), (double)(y + b1.offsety), (double)(z + b1.offsetz), this.getTexture(world, x, y, z, renderer, b1, block, 2, metadata));
                }
                renderer.flipTexture = false;
                if (!b1.invisibleSide[3]) {
                    renderer.renderFaceZPos(block, (double)(x + b1.offsetx), (double)(y + b1.offsety), (double)(z + b1.offsetz), this.getTexture(world, x, y, z, renderer, b1, block, 3, metadata));
                }
                tessellator.setColorOpaque_F(r * 0.6f, g * 0.6f, b2 * 0.6f);
                if (!b1.invisibleSide[4]) {
                    renderer.renderFaceXNeg(block, (double)(x + b1.offsetx), (double)(y + b1.offsety), (double)(z + b1.offsetz), this.getTexture(world, x, y, z, renderer, b1, block, 4, metadata));
                }
                renderer.flipTexture = true;
                if (!b1.invisibleSide[5]) {
                    renderer.renderFaceXPos(block, (double)(x + b1.offsetx), (double)(y + b1.offsety), (double)(z + b1.offsetz), this.getTexture(world, x, y, z, renderer, b1, block, 5, metadata));
                }
                renderer.flipTexture = false;
            }
            renderer.uvRotateBottom = 0;
            renderer.uvRotateTop = 0;
            renderer.uvRotateSouth = 0;
            renderer.uvRotateNorth = 0;
            renderer.uvRotateWest = 0;
            renderer.uvRotateEast = 0;
        }
        block.setBlockBoundsBasedOnState(world, x, y, z);
        return true;
    }
    
    public boolean shouldRender3DInInventory(final int modelId) {
        return true;
    }
    
    public int getRenderId() {
        return ExtraUtilsProxy.multiBlockID;
    }
    
    static {
        RenderBlockMultiBlock.rendering = true;
    }
}
