// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.sounds;

import net.minecraft.client.audio.ISound;
import net.minecraft.util.ResourceLocation;
import java.lang.ref.WeakReference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.ITickableSound;

@SideOnly(Side.CLIENT)
public class XUSoundTile implements ITickableSound
{
    WeakReference<ISoundTile> sound;
    private boolean donePlaying;
    private float zPosF;
    private float xPosF;
    private float yPosF;
    private float volume;
    private ResourceLocation location;
    
    public XUSoundTile(final ISoundTile sound) {
        this.donePlaying = false;
        this.zPosF = 0.0f;
        this.xPosF = 0.0f;
        this.yPosF = 0.0f;
        this.volume = 1.0E-6f;
        this.sound = new WeakReference<ISoundTile>(sound);
        this.location = sound.getSound();
    }
    
    public boolean isDonePlaying() {
        return this.donePlaying;
    }
    
    public ResourceLocation getPositionedSoundLocation() {
        return this.location;
    }
    
    public boolean canRepeat() {
        return true;
    }
    
    public int getRepeatDelay() {
        return 0;
    }
    
    public float getVolume() {
        return this.volume;
    }
    
    public float getPitch() {
        return 1.0f;
    }
    
    public float getXPosF() {
        return this.xPosF;
    }
    
    public float getYPosF() {
        return this.yPosF;
    }
    
    public float getZPosF() {
        return this.zPosF;
    }
    
    public ISound.AttenuationType getAttenuationType() {
        return ISound.AttenuationType.LINEAR;
    }
    
    public void update() {
        final ISoundTile sound = (this.sound != null) ? this.sound.get() : null;
        if (sound == null || sound.getTile().isInvalid()) {
            this.sound = null;
            if (this.volume > 5.0E-4) {
                this.volume *= 0.9f;
            }
            else {
                this.donePlaying = true;
            }
        }
        else {
            this.xPosF = sound.getTile().xCoord + 0.5f;
            this.yPosF = sound.getTile().yCoord + 0.5f;
            this.zPosF = sound.getTile().zCoord + 0.5f;
            if (sound.shouldSoundPlay()) {
                if (this.volume < 0.9995) {
                    this.volume = 1.0f - (1.0f - this.volume) * 0.9f;
                }
                else {
                    this.volume = 1.0f;
                }
            }
            else if (this.volume > 5.0E-4) {
                this.volume *= 0.9f;
            }
            else {
                this.volume = 0.0f;
            }
        }
    }
}


