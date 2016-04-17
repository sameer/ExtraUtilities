// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.particle;

import net.minecraft.world.World;
import net.minecraft.client.particle.EntityReddustFX;

public class ParticleEndSmoke extends EntityReddustFX
{
    public final int textureIndex = 7;
    
    public ParticleEndSmoke(final World p_i1223_1_, final double p_i1223_2_, final double p_i1223_4_, final double p_i1223_6_, final float p_i1223_8_, final float p_i1223_9_, final float p_i1223_10_) {
        super(p_i1223_1_, p_i1223_2_, p_i1223_4_, p_i1223_6_, p_i1223_8_, p_i1223_9_, p_i1223_10_);
        this.setParticleTextureIndex(this.textureIndex);
    }
    
    public ParticleEndSmoke(final World p_i1224_1_, final double p_i1224_2_, final double p_i1224_4_, final double p_i1224_6_, final float p_i1224_8_, final float p_i1224_9_, final float p_i1224_10_, final float p_i1224_11_) {
        super(p_i1224_1_, p_i1224_2_, p_i1224_4_, p_i1224_6_, p_i1224_8_, p_i1224_9_, p_i1224_10_, p_i1224_11_);
        this.setParticleTextureIndex(this.textureIndex);
    }
    
    public void onUpdate() {
        super.onUpdate();
        this.setParticleTextureIndex(7);
    }
    
    public int getBrightnessForRender(final float p_70070_1_) {
        final int i = super.getBrightnessForRender(p_70070_1_);
        float f1 = this.particleAge / this.particleMaxAge;
        f1 *= f1;
        f1 *= f1;
        final int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        k += (int)(f1 * 15.0f * 16.0f);
        if (k > 240) {
            k = 240;
        }
        return j | k << 16;
    }
    
    public float getBrightness(final float p_70013_1_) {
        final float f1 = super.getBrightness(p_70013_1_);
        float f2 = this.particleAge / this.particleMaxAge;
        f2 *= f2 * f2 * f2;
        return f1 * (1.0f - f2) + f2;
    }
}
