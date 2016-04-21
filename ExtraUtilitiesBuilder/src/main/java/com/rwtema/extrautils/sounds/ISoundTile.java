// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.sounds;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public interface ISoundTile
{
    boolean shouldSoundPlay();
    
    ResourceLocation getSound();
    
    TileEntity getTile();
}

