// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.block.render;

import com.rwtema.extrautils.ExtraUtilsProxy;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.block.Block;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

@SideOnly(Side.CLIENT)
public class RenderBlockSpike implements ISimpleBlockRenderingHandler
{
    public void renderInventoryBlock(final Block block, final int metadata, final int modelID, final RenderBlocks renderer) {
        final Tessellator t = Tessellator.instance;
        GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
        this.renderSpikeBlock((IBlockAccess)Minecraft.getMinecraft().theWorld, 0, 0, 0, 1, 0, block, renderer, -1);
        GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
    }
    
    public boolean renderSpikeBlock(final IBlockAccess world, final int x, final int y, final int z, final int side, final int type, final Block block, final RenderBlocks renderer, final int brightness) {
        float ax = 0.0f;
        float ay = 0.0f;
        float az = 0.0f;
        float bx = 1.0f;
        float by = 0.0f;
        float bz = 0.0f;
        float cx = 0.0f;
        float cy = 0.0f;
        float cz = 1.0f;
        float dx = 1.0f;
        float dy = 0.0f;
        float dz = 1.0f;
        float ex = 0.0f;
        float ey = 1.0f;
        float ez = 0.0f;
        float fx = 1.0f;
        float fy = 1.0f;
        float fz = 0.0f;
        float gx = 0.0f;
        float gy = 1.0f;
        float gz = 1.0f;
        float hx = 1.0f;
        float hy = 1.0f;
        float hz = 1.0f;
        switch (side) {
            case 0: {
                az = (ax = (bx = (bz = (cx = (cz = (dx = (dz = 0.5f)))))));
                break;
            }
            case 1: {
                ez = (ex = (fx = (fz = (gx = (gz = (hx = (hz = 0.5f)))))));
                break;
            }
            case 2: {
                by = (ay = (ey = (fy = (ax = (bx = (ex = (fx = 0.5f)))))));
                break;
            }
            case 3: {
                dy = (cy = (gy = (hy = (cx = (dx = (gx = (hx = 0.5f)))))));
                break;
            }
            case 4: {
                cy = (ay = (ey = (gy = (az = (cz = (ez = (gz = 0.5f)))))));
                break;
            }
            case 5: {
                dy = (by = (fy = (hy = (bz = (dz = (fz = (hz = 0.5f)))))));
                break;
            }
            default: {
                return false;
            }
        }
        IIcon texture = block.getIcon(side, type);
        if (renderer.hasOverrideBlockTexture()) {
            texture = renderer.overrideBlockTexture;
        }
        final Tessellator tessellator = Tessellator.instance;
        if (brightness >= 0) {
            tessellator.setBrightness(brightness);
        }
        final boolean inventory = brightness < 0;
        if (brightness >= 0) {
            tessellator.setColorOpaque_F(0.5f, 0.5f, 0.5f);
        }
        if (!renderer.hasOverrideBlockTexture()) {
            texture = block.getIcon(0, side + type * 6);
        }
        if (inventory) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
        }
        if (side != 0) {
            final float[] u = { ax, bx, dx, cx };
            final float[] v = { az, bz, dz, cz };
            final int rotation = this.calcRotation(0, side);
            tessellator.addVertexWithUV((double)(x + ax), (double)(y + ay), (double)(z + az), (double)this.getU(0, texture, rotation, u, v), (double)this.getV(0, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + bx), (double)(y + by), (double)(z + bz), (double)this.getU(1, texture, rotation, u, v), (double)this.getV(1, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + dx), (double)(y + dy), (double)(z + dz), (double)this.getU(2, texture, rotation, u, v), (double)this.getV(2, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + cx), (double)(y + cy), (double)(z + cz), (double)this.getU(3, texture, rotation, u, v), (double)this.getV(3, texture, rotation, u, v));
        }
        if (inventory) {
            tessellator.draw();
        }
        if (inventory) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 1.0f, 0.0f);
        }
        if (brightness >= 0) {
            tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        }
        if (!renderer.hasOverrideBlockTexture()) {
            texture = block.getIcon(1, side + type * 6);
        }
        if (side != 1) {
            final float[] u = { ex, gx, hx, fx };
            final float[] v = { ez, gz, hz, fz };
            final int rotation = this.calcRotation(1, side);
            tessellator.addVertexWithUV((double)(x + ex), (double)(y + ey), (double)(z + ez), (double)this.getU(0, texture, rotation, u, v), (double)this.getV(0, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + gx), (double)(y + gy), (double)(z + gz), (double)this.getU(1, texture, rotation, u, v), (double)this.getV(1, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + hx), (double)(y + hy), (double)(z + hz), (double)this.getU(2, texture, rotation, u, v), (double)this.getV(2, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + fx), (double)(y + fy), (double)(z + fz), (double)this.getU(3, texture, rotation, u, v), (double)this.getV(3, texture, rotation, u, v));
        }
        if (inventory) {
            tessellator.draw();
        }
        if (brightness >= 0) {
            if (side == 0) {
                tessellator.setColorOpaque_F(0.65f, 0.65f, 0.65f);
            }
            else if (side == 1) {
                tessellator.setColorOpaque_F(0.9f, 0.9f, 0.9f);
            }
            else {
                tessellator.setColorOpaque_F(0.8f, 0.8f, 0.8f);
            }
        }
        if (!renderer.hasOverrideBlockTexture()) {
            texture = block.getIcon(2, side + type * 6);
        }
        if (inventory) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.445f, 0.894f);
        }
        if (side != 2) {
            final float[] u = { 1.0f - ax, 1.0f - ex, 1.0f - fx, 1.0f - bx };
            final float[] v = { 1.0f - ay, 1.0f - ey, 1.0f - fy, 1.0f - by };
            final int rotation = this.calcRotation(2, side);
            tessellator.addVertexWithUV((double)(x + ax), (double)(y + ay), (double)(z + az), (double)this.getU(0, texture, rotation, u, v), (double)this.getV(0, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + ex), (double)(y + ey), (double)(z + ez), (double)this.getU(1, texture, rotation, u, v), (double)this.getV(1, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + fx), (double)(y + fy), (double)(z + fz), (double)this.getU(2, texture, rotation, u, v), (double)this.getV(2, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + bx), (double)(y + by), (double)(z + bz), (double)this.getU(3, texture, rotation, u, v), (double)this.getV(3, texture, rotation, u, v));
        }
        if (inventory) {
            tessellator.draw();
        }
        if (brightness >= 0) {
            if (side == 0) {
                tessellator.setColorOpaque_F(0.65f, 0.65f, 0.65f);
            }
            else if (side == 1) {
                tessellator.setColorOpaque_F(0.9f, 0.9f, 0.9f);
            }
            else {
                tessellator.setColorOpaque_F(0.8f, 0.8f, 0.8f);
            }
        }
        if (!renderer.hasOverrideBlockTexture()) {
            texture = block.getIcon(3, side + type * 6);
        }
        if (inventory) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.445f, -0.894f);
        }
        if (side != 3) {
            final float[] u = { dx, hx, gx, cx };
            final float[] v = { 1.0f - dy, 1.0f - hy, 1.0f - gy, 1.0f - cy };
            final int rotation = this.calcRotation(3, side);
            tessellator.addVertexWithUV((double)(x + dx), (double)(y + dy), (double)(z + dz), (double)this.getU(0, texture, rotation, u, v), (double)this.getV(0, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + hx), (double)(y + hy), (double)(z + hz), (double)this.getU(1, texture, rotation, u, v), (double)this.getV(1, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + gx), (double)(y + gy), (double)(z + gz), (double)this.getU(2, texture, rotation, u, v), (double)this.getV(2, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + cx), (double)(y + cy), (double)(z + cz), (double)this.getU(3, texture, rotation, u, v), (double)this.getV(3, texture, rotation, u, v));
        }
        if (inventory) {
            tessellator.draw();
        }
        if (brightness >= 0) {
            if (side == 0) {
                tessellator.setColorOpaque_F(0.55f, 0.55f, 0.55f);
            }
            else if (side == 1) {
                tessellator.setColorOpaque_F(0.7f, 0.7f, 0.7f);
            }
            else {
                tessellator.setColorOpaque_F(0.6f, 0.6f, 0.6f);
            }
        }
        if (!renderer.hasOverrideBlockTexture()) {
            texture = block.getIcon(4, side + type * 6);
        }
        if (inventory) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.894f, 0.445f, 0.0f);
        }
        if (side != 4) {
            final float[] u = { cz, gz, ez, az };
            final float[] v = { 1.0f - cy, 1.0f - gy, 1.0f - ey, 1.0f - ay };
            final int rotation = this.calcRotation(4, side);
            tessellator.addVertexWithUV((double)(x + cx), (double)(y + cy), (double)(z + cz), (double)this.getU(0, texture, rotation, u, v), (double)this.getV(0, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + gx), (double)(y + gy), (double)(z + gz), (double)this.getU(1, texture, rotation, u, v), (double)this.getV(1, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + ex), (double)(y + ey), (double)(z + ez), (double)this.getU(2, texture, rotation, u, v), (double)this.getV(2, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + ax), (double)(y + ay), (double)(z + az), (double)this.getU(3, texture, rotation, u, v), (double)this.getV(3, texture, rotation, u, v));
        }
        if (inventory) {
            tessellator.draw();
        }
        if (brightness >= 0) {
            if (side == 0) {
                tessellator.setColorOpaque_F(0.55f, 0.55f, 0.55f);
            }
            else if (side == 1) {
                tessellator.setColorOpaque_F(0.7f, 0.7f, 0.7f);
            }
            else {
                tessellator.setColorOpaque_F(0.6f, 0.6f, 0.6f);
            }
        }
        if (!renderer.hasOverrideBlockTexture()) {
            texture = block.getIcon(5, side + type * 6);
        }
        if (inventory) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(-0.894f, 0.445f, 0.0f);
        }
        if (side != 5) {
            final float[] u = { 1.0f - bz, 1.0f - fz, 1.0f - hz, 1.0f - dz };
            final float[] v = { 1.0f - by, 1.0f - fy, 1.0f - hy, 1.0f - dy };
            final int rotation = this.calcRotation(5, side);
            tessellator.addVertexWithUV((double)(x + bx), (double)(y + by), (double)(z + bz), (double)this.getU(0, texture, rotation, u, v), (double)this.getV(0, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + fx), (double)(y + fy), (double)(z + fz), (double)this.getU(1, texture, rotation, u, v), (double)this.getV(1, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + hx), (double)(y + hy), (double)(z + hz), (double)this.getU(2, texture, rotation, u, v), (double)this.getV(2, texture, rotation, u, v));
            tessellator.addVertexWithUV((double)(x + dx), (double)(y + dy), (double)(z + dz), (double)this.getU(3, texture, rotation, u, v), (double)this.getV(3, texture, rotation, u, v));
        }
        if (inventory) {
            tessellator.draw();
        }
        return true;
    }
    
    public float getU(final int i, final IIcon texture, final int rotation, final float[] u, final float[] v) {
        switch (rotation % 4) {
            case 0: {
                return texture.getInterpolatedU((double)(u[i % 4] * 16.0f));
            }
            case 1: {
                return texture.getInterpolatedU((double)(v[i % 4] * 16.0f));
            }
            case 2: {
                return texture.getInterpolatedU((double)(16.0f - u[i % 4] * 16.0f));
            }
            case 3: {
                return texture.getInterpolatedU((double)(16.0f - v[i % 4] * 16.0f));
            }
            default: {
                return 0.0f;
            }
        }
    }
    
    public float getV(final int i, final IIcon texture, final int rotation, final float[] u, final float[] v) {
        switch (rotation % 4) {
            case 0: {
                return texture.getInterpolatedV((double)(v[i % 4] * 16.0f));
            }
            case 1: {
                return texture.getInterpolatedV((double)(16.0f - u[i % 4] * 16.0f));
            }
            case 2: {
                return texture.getInterpolatedV((double)(16.0f - v[i % 4] * 16.0f));
            }
            case 3: {
                return texture.getInterpolatedV((double)(u[i % 4] * 16.0f));
            }
            default: {
                return 0.0f;
            }
        }
    }
    
    public int calcRotation(final int side, final int direction) {
        if (side == direction) {
            return 0;
        }
        if (side == Facing.oppositeSide[direction]) {
            return 0;
        }
        if (direction == 1) {
            return 0;
        }
        if (direction == 0) {
            return 2;
        }
        if (side == 0 || side == 1) {
            return (new int[] { 0, 2, 3, 1 })[direction - 2];
        }
        return 1 + (side + direction + direction / 2) % 2 * 2;
    }
    
    public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block, final int modelId, final RenderBlocks renderer) {
        final int side = world.getBlockMetadata(x, y, z) % 6;
        final int type = (world.getBlockMetadata(x, y, z) - side) / 6;
        final int brightness = block.getMixedBrightnessForBlock(world, x, y, z);
        return this.renderSpikeBlock(world, x, y, z, side, type, block, renderer, brightness);
    }
    
    public boolean shouldRender3DInInventory(final int modelId) {
        return true;
    }
    
    public int getRenderId() {
        return ExtraUtilsProxy.spikeBlockID;
    }
}


