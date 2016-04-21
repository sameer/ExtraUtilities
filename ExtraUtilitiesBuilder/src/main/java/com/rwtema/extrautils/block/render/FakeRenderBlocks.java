// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block.render;

import com.rwtema.extrautils.block.IconConnectedTextureFlipped;
import net.minecraft.util.IIcon;
import net.minecraft.util.Facing;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.block.IBlockAppearance;
import com.rwtema.extrautils.block.IconConnectedTexture;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.Block;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;

@SideOnly(Side.CLIENT)
public class FakeRenderBlocks extends RenderBlocks
{
    public static final double[] u;
    public static final double[] v;
    public Block curBlock;
    public int curMeta;
    public boolean isOpaque;
    
    public FakeRenderBlocks() {
        this.curBlock = null;
        this.curMeta = 0;
        this.isOpaque = false;
    }
    
    public void setWorld(final IBlockAccess blockAccess) {
        this.blockAccess = blockAccess;
    }
    
    public void setLightAndColor(final double u2, final double v2, final int side) {
        if (this.enableAO) {
            final Tessellator t = Tessellator.instance;
            double u3 = 0.0;
            double v3 = 0.0;
            if (side == 0 || side == 1) {
                u3 = 1.0 - u2;
                v3 = 1.0 - v2;
            }
            else if (side == 2) {
                u3 = v2;
                v3 = 1.0 - u2;
            }
            else if (side == 3) {
                u3 = u2;
                v3 = v2;
            }
            else if (side == 4) {
                u3 = v2;
                v3 = 1.0 - u2;
            }
            else if (side == 5) {
                u3 = 1.0 - v2;
                v3 = u2;
            }
            t.setBrightness(this.mixAoBrightness(this.brightnessTopLeft, this.brightnessTopRight, this.brightnessBottomLeft, this.brightnessBottomRight, u3 * v3, v3 * (1.0 - u3), (1.0 - v3) * u3, (1.0 - u3) * (1.0 - v3)));
            t.setColorOpaque_F(this.mix(this.colorRedTopLeft, this.colorRedTopRight, this.colorRedBottomLeft, this.colorRedBottomRight, u3, v3), this.mix(this.colorGreenTopLeft, this.colorGreenTopRight, this.colorGreenBottomLeft, this.colorGreenBottomRight, u3, v3), this.mix(this.colorBlueTopLeft, this.colorBlueTopRight, this.colorBlueBottomLeft, this.colorBlueBottomRight, u3, v3));
        }
    }
    
    public float mix(final double tl, final double tr, final double bl, final double br, final double u, final double v) {
        return (float)(tl * u * v + tr * (1.0 - u) * v + bl * u * (1.0 - v) + br * (1.0 - u) * (1.0 - v));
    }
    
    public void renderSide(final Block block, final double x, final double y, final double z, final double ox, final double oy, final double oz, final int ax, final int ay, final int az, final int bx, final int by, final int bz, final IconConnectedTexture icon, final int side, final double rx, final double ry, final double rz) {
        final Tessellator t = Tessellator.instance;
        final byte[] i = new byte[4];
        this.isOpaque = block.isOpaqueCube();
        boolean areSame = true;
        for (int j = 0; j < 4; ++j) {
            i[j] = this.getType(block, side, (int)x, (int)y, (int)z, ax * (int)FakeRenderBlocks.u[j], ay * (int)FakeRenderBlocks.u[j], az * (int)FakeRenderBlocks.u[j], bx * (int)FakeRenderBlocks.v[j], by * (int)FakeRenderBlocks.v[j], bz * (int)FakeRenderBlocks.v[j], (int)(ox * 2.0 - 1.0), (int)(oy * 2.0 - 1.0), (int)(oz * 2.0 - 1.0));
            if (areSame && j > 0 && i[j] != i[0]) {
                areSame = false;
            }
        }
        if (areSame) {
            icon.setType(i[0]);
            for (int j = 0; j < 4; ++j) {
                final double cx = x + rx + ox + FakeRenderBlocks.u[j] * ax * 0.5 + FakeRenderBlocks.v[j] * bx * 0.5;
                final double cy = y + ry + oy + FakeRenderBlocks.u[j] * ay * 0.5 + FakeRenderBlocks.v[j] * by * 0.5;
                final double cz = z + rz + oz + FakeRenderBlocks.u[j] * az * 0.5 + FakeRenderBlocks.v[j] * bz * 0.5;
                this.setLightAndColor(0.5 + FakeRenderBlocks.u[j] * 0.5, 0.5 + FakeRenderBlocks.v[j] * 0.5, side);
                t.addVertexWithUV(cx, cy, cz, (double)icon.getInterpolatedU(16.0 - (8.0 + FakeRenderBlocks.u[j] * 8.0)), (double)icon.getInterpolatedV(16.0 - (8.0 + FakeRenderBlocks.v[j] * 8.0)));
            }
            icon.resetType();
            return;
        }
        for (int j = 0; j < 4; ++j) {
            icon.setType(i[j]);
            final double cx = x + rx + ox + ax * FakeRenderBlocks.u[j] / 4.0 + bx * FakeRenderBlocks.v[j] / 4.0;
            final double cy = y + ry + oy + ay * FakeRenderBlocks.u[j] / 4.0 + by * FakeRenderBlocks.v[j] / 4.0;
            final double cz = z + rz + oz + az * FakeRenderBlocks.u[j] / 4.0 + bz * FakeRenderBlocks.v[j] / 4.0;
            for (int k = 0; k < 4; ++k) {
                this.setLightAndColor(0.5 + FakeRenderBlocks.u[j] * 0.25 + FakeRenderBlocks.u[k] * 0.25, 0.5 + FakeRenderBlocks.v[j] * 0.25 + FakeRenderBlocks.v[k] * 0.25, side);
                t.addVertexWithUV(cx + FakeRenderBlocks.u[k] * ax * 0.25 + FakeRenderBlocks.v[k] * bx * 0.25, cy + FakeRenderBlocks.u[k] * ay * 0.25 + FakeRenderBlocks.v[k] * by * 0.25, cz + FakeRenderBlocks.u[k] * az * 0.25 + FakeRenderBlocks.v[k] * bz * 0.25, (double)icon.getInterpolatedU(16.0 - (8.0 + FakeRenderBlocks.u[j] * 4.0 + FakeRenderBlocks.u[k] * 4.0)), (double)icon.getInterpolatedV(16.0 - (8.0 + FakeRenderBlocks.v[j] * 4.0 + FakeRenderBlocks.v[k] * 4.0)));
            }
            icon.resetType();
        }
    }
    
    public int getSideFromDir(final int dx, final int dy, final int dz) {
        if (dy < 0) {
            return 0;
        }
        if (dy > 0) {
            return 1;
        }
        if (dz < 0) {
            return 2;
        }
        if (dz > 0) {
            return 3;
        }
        if (dx < 0) {
            return 4;
        }
        if (dx > 0) {
            return 5;
        }
        return 0;
    }
    
    public boolean matchBlock(final int side2, final int x2, final int y2, final int z2) {
        final Block block = this.blockAccess.getBlock(x2, y2, z2);
        if (block == this.curBlock) {
            return this.curMeta == this.blockAccess.getBlockMetadata(x2, y2, z2);
        }
        if (block instanceof IBlockAppearance) {
            final IBlockAppearance block2 = (IBlockAppearance)block;
            return block2.supportsVisualConnections() && this.curBlock == block2.getVisualBlock(this.blockAccess, x2, y2, z2, ForgeDirection.getOrientation(side2)) && this.curMeta == block2.getVisualMeta(this.blockAccess, x2, y2, z2, ForgeDirection.getOrientation(side2));
        }
        return false;
    }
    
    public byte getType(final Block block, final int side, final int x, final int y, final int z, final int ax, final int ay, final int az, final int bx, final int by, final int bz, final int cx, final int cy, final int cz) {
        final int sidea = this.getSideFromDir(ax, ay, az);
        final int sideb = this.getSideFromDir(bx, by, bz);
        final boolean a = this.matchBlock(side, x + ax, y + ay, z + az) && !this.matchBlock(sidea, x + cx, y + cy, z + cz) && !this.matchBlock(Facing.oppositeSide[sidea], x + ax + cx, y + ay + cy, z + az + cz);
        final boolean b = this.matchBlock(side, x + bx, y + by, z + bz) && !this.matchBlock(sideb, x + cx, y + cy, z + cz) && !this.matchBlock(Facing.oppositeSide[sideb], x + bx + cx, y + by + cy, z + bz + cz);
        if (a) {
            if (!b) {
                return 2;
            }
            if (!this.matchBlock(side, x + ax + bx, y + ay + by, z + az + bz)) {
                return 4;
            }
            if (this.matchBlock(Facing.oppositeSide[sidea], x + ax + bx + cx, y + ay + by + cy, z + az + bz + cz) || this.matchBlock(Facing.oppositeSide[sideb], x + ax + bx + cx, y + ay + by + cy, z + az + bz + cz) || this.matchBlock(sidea, x + bx + cx, y + by + cy, z + bz + cz) || this.matchBlock(sideb, x + ax + cx, y + ay + cy, z + az + cz)) {
                return 4;
            }
            return 3;
        }
        else {
            if (b) {
                return 1;
            }
            return 0;
        }
    }
    
    public void renderFaceYNeg(final Block block, final double x, final double y, final double z, IIcon IIcon) {
        if (this.hasOverrideBlockTexture()) {
            IIcon = this.overrideBlockTexture;
        }
        if (IIcon instanceof IconConnectedTexture) {
            this.renderSide(block, x, y, z, 0.5, 0.0, 0.5, -1, 0, 0, 0, 0, 1, new IconConnectedTextureFlipped((IconConnectedTexture)IIcon), 0, 0.0, 0.0, 0.0);
        }
        else {
            super.renderFaceYNeg(block, x, y, z, IIcon);
        }
    }
    
    public void renderFaceYPos(final Block block, final double x, final double y, final double z, IIcon IIcon) {
        if (this.hasOverrideBlockTexture()) {
            IIcon = this.overrideBlockTexture;
        }
        if (IIcon instanceof IconConnectedTexture) {
            this.renderSide(block, x, y, z, 0.5, 1.0, 0.5, -1, 0, 0, 0, 0, -1, (IconConnectedTexture)IIcon, 1, 0.0, 0.0, 0.0);
        }
        else {
            super.renderFaceYPos(block, x, y, z, IIcon);
        }
    }
    
    public void renderFaceZNeg(final Block block, final double x, final double y, final double z, IIcon IIcon) {
        if (this.hasOverrideBlockTexture()) {
            IIcon = this.overrideBlockTexture;
        }
        if (IIcon instanceof IconConnectedTexture) {
            this.renderSide(block, x, y, z, 0.5, 0.5, 0.0, 1, 0, 0, 0, 1, 0, (IconConnectedTexture)IIcon, 2, 0.0, 0.0, 0.0);
        }
        else {
            super.renderFaceZNeg(block, x, y, z, IIcon);
        }
    }
    
    public void renderFaceZPos(final Block block, final double x, final double y, final double z, IIcon IIcon) {
        if (this.hasOverrideBlockTexture()) {
            IIcon = this.overrideBlockTexture;
        }
        if (IIcon instanceof IconConnectedTexture) {
            this.renderSide(block, x, y, z, 0.5, 0.5, 1.0, -1, 0, 0, 0, 1, 0, (IconConnectedTexture)IIcon, 3, 0.0, 0.0, 0.0);
        }
        else {
            super.renderFaceZPos(block, x, y, z, IIcon);
        }
    }
    
    public void renderFaceXNeg(final Block block, final double x, final double y, final double z, IIcon IIcon) {
        if (this.hasOverrideBlockTexture()) {
            IIcon = this.overrideBlockTexture;
        }
        if (IIcon instanceof IconConnectedTexture) {
            this.renderSide(block, x, y, z, 0.0, 0.5, 0.5, 0, 0, -1, 0, 1, 0, (IconConnectedTexture)IIcon, 4, 0.0, 0.0, 0.0);
        }
        else {
            super.renderFaceXNeg(block, x, y, z, IIcon);
        }
    }
    
    public void renderFaceXPos(final Block block, final double x, final double y, final double z, IIcon IIcon) {
        if (this.hasOverrideBlockTexture()) {
            IIcon = this.overrideBlockTexture;
        }
        if (IIcon instanceof IconConnectedTexture) {
            this.renderSide(block, x, y, z, 1.0, 0.5, 0.5, 0, 0, 1, 0, 1, 0, (IconConnectedTexture)IIcon, 5, 0.0, 0.0, 0.0);
        }
        else {
            super.renderFaceXPos(block, x, y, z, IIcon);
        }
    }
    
    static {
        u = new double[] { -1.0, 1.0, 1.0, -1.0 };
        v = new double[] { 1.0, 1.0, -1.0, -1.0 };
    }
}

