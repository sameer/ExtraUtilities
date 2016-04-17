// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import net.minecraft.client.particle.EntityFX;

public class ParticleLine extends EntityFX
{
    public final Vec3 start;
    public final Vec3 end;
    
    public ParticleLine(final World world, final Vec3 start, final Vec3 end, final float r, final float g, final float b, final IIcon particle) {
        super(world, start.xCoord, start.yCoord, start.zCoord);
        this.start = start;
        this.end = end;
        this.noClip = true;
        this.particleRed = r;
        this.particleGreen = g;
        this.particleBlue = b;
        this.particleScale = (float)(0.20000000298023224 + 0.20000000298023224 * Math.random());
        this.particleIcon = particle;
        this.particleMaxAge = (int)(10.0 / (Math.random() * 0.6 + 0.4));
    }
    
    public void renderParticle(final Tessellator tessellator, final float partialTickTime, final float rotationX, final float rotationXZ, final float rotationZ, final float rotationYZ, final float rotationXY) {
        this.particleAlpha = 1.0f - (this.particleAge + partialTickTime) / this.particleMaxAge;
        final float size = 0.25f * this.particleScale;
        final float u1 = this.particleIcon.getMinU();
        final float u2 = this.particleIcon.getMaxU();
        final float v1 = this.particleIcon.getMinV();
        final float v2 = this.particleIcon.getMaxV();
        final float ax = (float)(this.start.xCoord - ParticleLine.interpPosX);
        final float ay = (float)(this.start.yCoord - ParticleLine.interpPosY);
        final float az = (float)(this.start.zCoord - ParticleLine.interpPosZ);
        final float bx = (float)(this.end.xCoord - ParticleLine.interpPosX);
        final float by = (float)(this.end.yCoord - ParticleLine.interpPosY);
        final float bz = (float)(this.end.zCoord - ParticleLine.interpPosZ);
        tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
        tessellator.addVertexWithUV((double)(bx - rotationX * size - rotationYZ * size), (double)(by - rotationXZ * size), (double)(bz - rotationZ * size - rotationXY * size), (double)u2, (double)v2);
        tessellator.addVertexWithUV((double)(ax - rotationX * size + rotationYZ * size), (double)(ay + rotationXZ * size), (double)(az - rotationZ * size + rotationXY * size), (double)u2, (double)v1);
        tessellator.addVertexWithUV((double)(ax + rotationX * size + rotationYZ * size), (double)(ay + rotationXZ * size), (double)(az + rotationZ * size + rotationXY * size), (double)u1, (double)v1);
        tessellator.addVertexWithUV((double)(bx + rotationX * size - rotationYZ * size), (double)(by - rotationXZ * size), (double)(bz + rotationZ * size - rotationXY * size), (double)u1, (double)v2);
    }
    
    public int getFXLayer() {
        return 1;
    }
}
