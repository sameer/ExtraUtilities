// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EffectRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.IResourceManagerReloadListener;

@SideOnly(Side.CLIENT)
public class ParticleHelperClient implements IResourceManagerReloadListener
{
    private static EffectRenderer effectRenderer;
    
    @SideOnly(Side.CLIENT)
    public static void addParticle(final EntityFX particle) {
        ParticleHelperClient.effectRenderer.addEffect(particle);
    }
    
    public void onResourceManagerReload(final IResourceManager manager) {
        ParticleHelperClient.effectRenderer = Minecraft.getMinecraft().effectRenderer;
        registerTextures(manager);
    }
    
    private static void registerTextures(final IResourceManager manager) {
    }
}

