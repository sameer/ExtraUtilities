// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block.render;

import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.Tessellator;
import com.rwtema.extrautils.block.IconConnectedTexture;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.block.Block;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FakeRenderEtherealBlocks extends FakeRenderBlocks
{
    private static final double h = 0.005;
    private static final float darken = 0.75f;
    
    @Override
    public void setLightAndColor(final double u2, final double v2, final int side) {
    }
    
    public boolean renderStandardBlock(final Block p_147784_1_, final int p_147784_2_, final int p_147784_3_, final int p_147784_4_) {
        final int l = p_147784_1_.colorMultiplier(this.blockAccess, p_147784_2_, p_147784_3_, p_147784_4_);
        float f = (l >> 16 & 0xFF) / 255.0f;
        float f2 = (l >> 8 & 0xFF) / 255.0f;
        float f3 = (l & 0xFF) / 255.0f;
        if (EntityRenderer.anaglyphEnable) {
            final float f4 = (f * 30.0f + f2 * 59.0f + f3 * 11.0f) / 100.0f;
            final float f5 = (f * 30.0f + f2 * 70.0f) / 100.0f;
            final float f6 = (f * 30.0f + f3 * 70.0f) / 100.0f;
            f = f4;
            f2 = f5;
            f3 = f6;
        }
        f *= 0.75f;
        f2 *= 0.75f;
        f3 *= 0.75f;
        return this.renderStandardBlockWithColorMultiplier(p_147784_1_, p_147784_2_, p_147784_3_, p_147784_4_, f, f2, f3);
    }
    
    @Override
    public void renderSide(final Block block, final double x, final double y, final double z, final double ox, final double oy, final double oz, final int ax, final int ay, final int az, final int bx, final int by, final int bz, final IconConnectedTexture icon, final int side, final double rx, final double ry, final double rz) {
        final Tessellator t = Tessellator.instance;
        final byte[] i = new byte[4];
        this.isOpaque = block.isOpaqueCube();
        boolean areSame = true;
        for (int j = 0; j < 4; ++j) {
            i[j] = this.getType(block, side, (int)x, (int)y, (int)z, ax * (int)FakeRenderEtherealBlocks.u[j], ay * (int)FakeRenderEtherealBlocks.u[j], az * (int)FakeRenderEtherealBlocks.u[j], bx * (int)FakeRenderEtherealBlocks.v[j], by * (int)FakeRenderEtherealBlocks.v[j], bz * (int)FakeRenderEtherealBlocks.v[j], (int)(ox * 2.0 - 1.0), (int)(oy * 2.0 - 1.0), (int)(oz * 2.0 - 1.0));
            if (areSame && j > 0 && i[j] != i[0]) {
                areSame = false;
            }
        }
        if (areSame) {
            icon.setType(i[0]);
            final double cx = x + rx + ox;
            final double cy = y + ry + oy;
            final double cz = z + rz + oz;
            for (int k = 3; k >= 0; --k) {
                this.setLightAndColor(0.5 + FakeRenderEtherealBlocks.u[k] * 0.5, 0.5 + FakeRenderEtherealBlocks.v[k] * 0.5, side);
                t.addVertexWithUV(cx + FakeRenderEtherealBlocks.u[k] * ax * 0.5 + FakeRenderEtherealBlocks.v[k] * bx * 0.5, cy + FakeRenderEtherealBlocks.u[k] * ay * 0.5 + FakeRenderEtherealBlocks.v[k] * by * 0.5, cz + FakeRenderEtherealBlocks.u[k] * az * 0.5 + FakeRenderEtherealBlocks.v[k] * bz * 0.5, (double)icon.getInterpolatedU(16.0 - (8.0 + FakeRenderEtherealBlocks.u[k] * 8.0)), (double)icon.getInterpolatedV(16.0 - (8.0 + FakeRenderEtherealBlocks.v[k] * 8.0)));
            }
            icon.resetType();
            return;
        }
        for (int j = 0; j < 4; ++j) {
            icon.setType(i[j]);
            final double cx2 = x + rx + ox + ax * FakeRenderEtherealBlocks.u[j] / 4.0 + bx * FakeRenderEtherealBlocks.v[j] / 4.0;
            final double cy2 = y + ry + oy + ay * FakeRenderEtherealBlocks.u[j] / 4.0 + by * FakeRenderEtherealBlocks.v[j] / 4.0;
            final double cz2 = z + rz + oz + az * FakeRenderEtherealBlocks.u[j] / 4.0 + bz * FakeRenderEtherealBlocks.v[j] / 4.0;
            for (int l = 3; l >= 0; --l) {
                this.setLightAndColor(0.5 + FakeRenderEtherealBlocks.u[j] * 0.25 + FakeRenderEtherealBlocks.u[l] * 0.25, 0.5 + FakeRenderEtherealBlocks.v[j] * 0.25 + FakeRenderEtherealBlocks.v[l] * 0.25, side);
                t.addVertexWithUV(cx2 + FakeRenderEtherealBlocks.u[l] * ax * 0.25 + FakeRenderEtherealBlocks.v[l] * bx * 0.25, cy2 + FakeRenderEtherealBlocks.u[l] * ay * 0.25 + FakeRenderEtherealBlocks.v[l] * by * 0.25, cz2 + FakeRenderEtherealBlocks.u[l] * az * 0.25 + FakeRenderEtherealBlocks.v[l] * bz * 0.25, (double)icon.getInterpolatedU(16.0 - (8.0 + FakeRenderEtherealBlocks.u[j] * 4.0 + FakeRenderEtherealBlocks.u[l] * 4.0)), (double)icon.getInterpolatedV(16.0 - (8.0 + FakeRenderEtherealBlocks.v[j] * 4.0 + FakeRenderEtherealBlocks.v[l] * 4.0)));
            }
            icon.resetType();
        }
    }
    
    @Override
    public void renderFaceYNeg(final Block block, final double x, final double y, final double z, IIcon IIcon) {
        if (this.hasOverrideBlockTexture()) {
            IIcon = this.overrideBlockTexture;
        }
        if (IIcon instanceof IconConnectedTexture) {
            this.renderSide(block, x, y, z, 0.5, 0.0, 0.5, 1, 0, 0, 0, 0, -1, (IconConnectedTexture)IIcon, 0, 0.0, 0.005, 0.0);
        }
        else {
            super.renderFaceYPos(block, x, y - 1.0 + 0.005, z, IIcon);
        }
    }
    
    @Override
    public void renderFaceYPos(final Block block, final double x, final double y, final double z, IIcon IIcon) {
        if (this.hasOverrideBlockTexture()) {
            IIcon = this.overrideBlockTexture;
        }
        if (IIcon instanceof IconConnectedTexture) {
            this.renderSide(block, x, y, z, 0.5, 1.0, 0.5, -1, 0, 0, 0, 0, -1, (IconConnectedTexture)IIcon, 1, 0.0, -0.005, 0.0);
        }
        else {
            super.renderFaceYNeg(block, x, y + 1.0 - 0.005, z, IIcon);
        }
    }
    
    @Override
    public void renderFaceZNeg(final Block block, final double x, final double y, final double z, IIcon IIcon) {
        if (this.hasOverrideBlockTexture()) {
            IIcon = this.overrideBlockTexture;
        }
        if (IIcon instanceof IconConnectedTexture) {
            this.renderSide(block, x, y, z, 0.5, 0.5, 0.0, 1, 0, 0, 0, 1, 0, (IconConnectedTexture)IIcon, 2, 0.0, 0.0, 0.005);
        }
        else {
            super.renderFaceZPos(block, x, y, z - 1.0 + 0.005, IIcon);
        }
    }
    
    @Override
    public void renderFaceZPos(final Block block, final double x, final double y, final double z, IIcon IIcon) {
        if (this.hasOverrideBlockTexture()) {
            IIcon = this.overrideBlockTexture;
        }
        if (IIcon instanceof IconConnectedTexture) {
            this.renderSide(block, x, y, z, 0.5, 0.5, 1.0, -1, 0, 0, 0, 1, 0, (IconConnectedTexture)IIcon, 3, 0.0, 0.0, -0.005);
        }
        else {
            super.renderFaceZNeg(block, x, y, z + 1.0 - 0.005, IIcon);
        }
    }
    
    @Override
    public void renderFaceXNeg(final Block block, final double x, final double y, final double z, IIcon IIcon) {
        if (this.hasOverrideBlockTexture()) {
            IIcon = this.overrideBlockTexture;
        }
        if (IIcon instanceof IconConnectedTexture) {
            this.renderSide(block, x, y, z, 0.0, 0.5, 0.5, 0, 0, -1, 0, 1, 0, (IconConnectedTexture)IIcon, 4, 0.005, 0.0, 0.0);
        }
        else {
            super.renderFaceXPos(block, x, y, z, IIcon);
        }
    }
    
    @Override
    public void renderFaceXPos(final Block block, final double x, final double y, final double z, IIcon IIcon) {
        if (this.hasOverrideBlockTexture()) {
            IIcon = this.overrideBlockTexture;
        }
        if (IIcon instanceof IconConnectedTexture) {
            this.renderSide(block, x, y, z, 1.0, 0.5, 0.5, 0, 0, 1, 0, 1, 0, (IconConnectedTexture)IIcon, 5, -0.005, 0.0, 0.0);
        }
        else {
            super.renderFaceXNeg(block, x + 1.0 - 0.005, y, z, IIcon);
        }
    }
}
