// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.particle;

import net.minecraft.client.renderer.Tessellator;
import com.rwtema.extrautils.block.BlockPortal;
import net.minecraft.world.World;
import net.minecraft.client.particle.EntityFX;

public class ParticlePortal extends EntityFX
{
    public final double startX;
    public final double startY;
    public final double startZ;
    
    public ParticlePortal(final World world, final double x, final double y, final double z, final float r, final float g, final float b) {
        super(world, x, y, z);
        this.startX = x;
        this.startY = y;
        this.startZ = z;
        this.noClip = true;
        this.particleRed = r;
        this.particleGreen = g;
        this.particleBlue = b;
        this.particleScale = (float)(0.20000000298023224 + 0.20000000298023224 * Math.random());
        this.motionY = 0.20000000298023224 * (1.0 + Math.random()) / 4.0;
        this.particleIcon = BlockPortal.particle;
        this.particleMaxAge = (int)(80.0 / (Math.random() * 0.6 + 0.4));
    }
    
    public void renderParticle(final Tessellator tessellator, final float partialTickTime, final float rotationX, final float rotationXZ, final float rotationZ, final float rotationYZ, final float rotationXY) {
        this.particleAlpha = 1.0f - (this.particleAge + partialTickTime) / this.particleMaxAge;
        float u1 = this.particleTextureIndexX / 16.0f;
        float u2 = u1 + 0.0624375f;
        float v1 = this.particleTextureIndexY / 16.0f;
        float v2 = v1 + 0.0624375f;
        final float size = 0.1f * this.particleScale;
        if (this.particleIcon != null) {
            u1 = this.particleIcon.getMinU();
            u2 = this.particleIcon.getMaxU();
            v1 = this.particleIcon.getMinV();
            v2 = this.particleIcon.getMaxV();
        }
        final float x = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTickTime - ParticlePortal.interpPosX);
        final float y = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTickTime - ParticlePortal.interpPosY);
        final float z = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTickTime - ParticlePortal.interpPosZ);
        final float sx = (float)(this.startX - ParticlePortal.interpPosX);
        final float sy = (float)(this.startY - ParticlePortal.interpPosY);
        final float sz = (float)(this.startZ - ParticlePortal.interpPosZ);
        tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
        tessellator.addVertexWithUV((double)(x - rotationX * size + rotationYZ * size), (double)(y + rotationXZ * size), (double)(z - rotationZ * size + rotationXY * size), (double)u2, (double)v1);
        tessellator.addVertexWithUV((double)(x + rotationX * size + rotationYZ * size), (double)(y + rotationXZ * size), (double)(z + rotationZ * size + rotationXY * size), (double)u1, (double)v1);
        tessellator.addVertexWithUV((double)(sx + rotationX * size - rotationYZ * size), (double)(sy - rotationXZ * size), (double)(sz + rotationZ * size - rotationXY * size), (double)u1, (double)v2);
        tessellator.addVertexWithUV((double)(sx - rotationX * size - rotationYZ * size), (double)(sy - rotationXZ * size), (double)(sz - rotationZ * size - rotationXY * size), (double)u2, (double)v2);
    }
    
    public int getFXLayer() {
        return 1;
    }
}


