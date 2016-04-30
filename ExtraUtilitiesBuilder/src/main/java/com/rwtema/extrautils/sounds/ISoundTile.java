// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
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


