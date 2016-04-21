// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.sounds;

import net.minecraft.client.entity.EntityClientPlayerMP;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.util.ResourceLocation;
import com.rwtema.extrautils.tileentity.generators.TileEntityGenerator;
import java.util.WeakHashMap;
import java.lang.ref.WeakReference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.ITickableSound;

@SideOnly(Side.CLIENT)
public class XUSoundTileGenerators implements ITickableSound
{
    private static WeakReference<XUSoundTileGenerators> instance;
    private WeakHashMap<TileEntityGenerator, Void> tiles;
    private boolean donePlaying;
    private float zPosF;
    private float xPosF;
    private float yPosF;
    private float volume;
    private ResourceLocation location;
    
    public XUSoundTileGenerators() {
        this.tiles = new WeakHashMap<TileEntityGenerator, Void>();
        this.donePlaying = false;
        this.zPosF = 0.0f;
        this.xPosF = 0.0f;
        this.yPosF = 0.0f;
        this.volume = 1.0E-6f;
        this.location = TileEntityGenerator.hum;
    }
    
    private static XUSoundTileGenerators getInstance() {
        XUSoundTileGenerators sound = (XUSoundTileGenerators.instance != null) ? XUSoundTileGenerators.instance.get() : null;
        if (sound == null) {
            sound = new XUSoundTileGenerators();
            XUSoundTileGenerators.instance = new WeakReference<XUSoundTileGenerators>(sound);
        }
        return sound;
    }
    
    private static void clearInstance() {
        XUSoundTileGenerators.instance = null;
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
    
    @SideOnly(Side.CLIENT)
    public static void addGenerator(final TileEntityGenerator generator) {
        final XUSoundTileGenerators instance1 = getInstance();
        removeOldValues();
        if (!instance1.tiles.containsKey(generator)) {
            instance1.tiles.put(generator, null);
            if (instance1.tiles.size() == 1) {
                refresh();
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public static void refresh() {
        if (XUSoundTileGenerators.instance == null || XUSoundTileGenerators.instance.get() == null) {
            return;
        }
        final XUSoundTileGenerators sound = getInstance();
        if (!sound.tiles.isEmpty()) {
            removeOldValues();
            if (!sound.tiles.isEmpty() && Sounds.canAddSound((ISound)sound)) {
                sound.setDonePlaying(false);
                sound.volume = 1.0E-7f;
                Sounds.tryAddSound((ISound)sound);
            }
        }
    }
    
    private static void removeOldValues() {
        final WeakHashMap<TileEntityGenerator, Void> weakHashMap = getInstance().tiles;
        final Iterator<TileEntityGenerator> iter = weakHashMap.keySet().iterator();
        while (iter.hasNext()) {
            final TileEntityGenerator gen = iter.next();
            if (gen == null || gen.isInvalid() || gen.getWorldObj() != Minecraft.getMinecraft().theWorld) {
                iter.remove();
            }
        }
    }
    
    public synchronized void update() {
        final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            this.setDonePlaying(true);
            return;
        }
        removeOldValues();
        if (this.tiles.size() == 0) {
            if (this.volume > 5.0E-4) {
                this.volume *= 0.9f;
            }
            else {
                this.setDonePlaying(true);
            }
        }
        else {
            boolean shouldPlay = false;
            if (this.tiles.size() == 1) {
                for (final TileEntityGenerator gen : this.tiles.keySet()) {
                    if (gen.shouldSoundPlay()) {
                        shouldPlay = true;
                        this.moveTorwards(gen.x() + 0.5f, gen.y() + 0.5f, gen.z() + 0.5f, 0.05f);
                    }
                }
            }
            else {
                float x = 0.0f;
                float y = 0.0f;
                float z = 0.0f;
                float total_weight = 0.0f;
                for (final TileEntityGenerator gen2 : this.tiles.keySet()) {
                    if (gen2 != null && gen2.shouldSoundPlay()) {
                        shouldPlay = true;
                        final float w = weight(gen2.getDistanceFrom(player.posX, player.posY, player.posZ));
                        x += w * gen2.x();
                        y += w * gen2.y();
                        z += w * gen2.z();
                        total_weight += w;
                    }
                }
                if (shouldPlay && total_weight > 0.0f) {
                    this.moveTorwards(x / total_weight + 0.5f, y / total_weight + 0.5f, z / total_weight + 0.5f, 0.05f);
                }
                else {
                    this.volume *= 0.9f;
                }
            }
            if (shouldPlay) {
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
    
    private void moveTorwards(final float x, final float y, final float z, float speed) {
        if (this.volume == 0.0f) {
            speed = 1.0f;
        }
        this.xPosF = this.xPosF * (1.0f - speed) + x * speed;
        this.yPosF = this.yPosF * (1.0f - speed) + y * speed;
        this.zPosF = this.zPosF * (1.0f - speed) + z * speed;
    }
    
    private static float weight(final double d) {
        if (d < 1.0) {
            return 1.0f;
        }
        return (float)(1.0 / d);
    }
    
    public void setDonePlaying(final boolean donePlaying) {
        this.donePlaying = donePlaying;
        if (donePlaying) {
            clearInstance();
        }
    }
    
    static {
        XUSoundTileGenerators.instance = null;
    }
}


