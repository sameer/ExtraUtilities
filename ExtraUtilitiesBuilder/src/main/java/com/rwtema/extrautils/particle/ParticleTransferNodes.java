// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.particle;

import com.rwtema.extrautils.helper.XURandom;
import net.minecraft.client.renderer.Tessellator;
import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferNode;
import net.minecraft.world.World;
import java.util.Random;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityFX;

@SideOnly(Side.CLIENT)
public class ParticleTransferNodes extends EntityFX
{
    float baseScale;
    public static Random rand;
    float[][] col;
    private static final int[][] dirs;
    
    public static double offset() {
        return ParticleTransferNodes.rand.nextGaussian() * 0.0125;
    }
    
    public int getFXLayer() {
        return 1;
    }
    
    public ParticleTransferNodes(final World par1World, final double par2, final double par4, final double par6, final double r, final double g, final double b) {
        super(par1World, par2 + offset(), par4 + offset(), par6 + offset(), 0.0, 0.0, 0.0);
        this.col = new float[8][3];
        this.particleIcon = BlockTransferNode.particle;
        this.motionX *= 0.2;
        this.motionY *= 0.2;
        this.motionZ *= 0.2;
        for (int i = 0; i < 8; ++i) {
            final float f4 = (float)Math.random() * 0.4f + 0.6f;
            final float[] array = this.col[i];
            final int n = 0;
            final float particleRed = (float)((Math.random() * 0.20000000298023224 + 0.800000011920929) * r * f4);
            array[n] = particleRed;
            this.particleRed = particleRed;
            final float[] array2 = this.col[i];
            final int n2 = 1;
            final float particleGreen = (float)((Math.random() * 0.20000000298023224 + 0.800000011920929) * g * f4);
            array2[n2] = particleGreen;
            this.particleGreen = particleGreen;
            final float[] array3 = this.col[i];
            final int n3 = 2;
            final float particleBlue = (float)((Math.random() * 0.20000000298023224 + 0.800000011920929) * b * f4);
            array3[n3] = particleBlue;
            this.particleBlue = particleBlue;
        }
        this.particleScale = 0.5f;
        this.baseScale = this.particleScale;
        this.particleMaxAge = (int)(10.0 / (Math.random() * 0.2 + 0.6));
        this.noClip = true;
    }
    
    public void renderParticle(final Tessellator par1Tessellator, final float x, final float y, final float z, final float r, final float g, final float b) {
        float f6 = 1.0f - (this.particleAge + x) / this.particleMaxAge;
        if (f6 < 0.0f) {
            f6 = 0.0f;
        }
        if (f6 > 1.0f) {
            f6 = 1.0f;
        }
        this.particleScale = this.baseScale * (1.0f - f6 * 0.5f) * Math.min(1.0f, 2.0f * f6);
        final float h = 0.125f + (1.0f - f6) * 0.075f;
        for (int i = 0; i < 8; ++i) {
            this.renderParticles(par1Tessellator, x, y, z, r, g, b, h * ParticleTransferNodes.dirs[i][0], h * ParticleTransferNodes.dirs[i][1], h * ParticleTransferNodes.dirs[i][2], this.col[i]);
        }
    }
    
    public void renderParticles(final Tessellator par1Tessellator, final float x, final float y, final float z, final float r, final float g, final float b, final float dx, final float dy, final float dz, final float[] cols) {
        float f6 = this.particleTextureIndexX / 16.0f;
        float f7 = f6 + 0.0624375f;
        float f8 = this.particleTextureIndexY / 16.0f;
        float f9 = f8 + 0.0624375f;
        final float f10 = 0.1f * this.particleScale;
        if (this.particleIcon != null) {
            f6 = this.particleIcon.getMinU();
            f7 = this.particleIcon.getMaxU();
            f8 = this.particleIcon.getMinV();
            f9 = this.particleIcon.getMaxV();
        }
        final double f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * x - ParticleTransferNodes.interpPosX) + dx;
        final double f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * x - ParticleTransferNodes.interpPosY) + dy;
        final double f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * x - ParticleTransferNodes.interpPosZ) + dz;
        par1Tessellator.setColorRGBA_F(cols[0], cols[1], cols[2], this.particleAlpha);
        par1Tessellator.addVertexWithUV(f11 - y * f10 - g * f10, f12 - z * f10, f13 - r * f10 - b * f10, (double)f7, (double)f9);
        par1Tessellator.addVertexWithUV(f11 - y * f10 + g * f10, f12 + z * f10, f13 - r * f10 + b * f10, (double)f7, (double)f8);
        par1Tessellator.addVertexWithUV(f11 + y * f10 + g * f10, f12 + z * f10, f13 + r * f10 + b * f10, (double)f6, (double)f8);
        par1Tessellator.addVertexWithUV(f11 + y * f10 - g * f10, f12 - z * f10, f13 + r * f10 - b * f10, (double)f6, (double)f9);
    }
    
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
    }
    
    static {
        ParticleTransferNodes.rand = XURandom.getInstance();
        dirs = new int[][] { { -1, -1, -1 }, { -1, -1, 1 }, { 1, -1, -1 }, { 1, -1, 1 }, { -1, 1, -1 }, { -1, 1, 1 }, { 1, 1, -1 }, { 1, 1, 1 } };
    }
}

