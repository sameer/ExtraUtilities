// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.sounds;

import java.util.Map;
import net.minecraft.client.audio.SoundHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import com.rwtema.extrautils.tileentity.generators.TileEntityGenerator;
import com.rwtema.extrautils.ExtraUtilsMod;
import java.lang.reflect.Field;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Sounds
{
    private static Field playingSounds;
    private static Field soundMgr;
    
    public static void registerSoundTile(final ISoundTile soundTile) {
        if (ExtraUtilsMod.proxy.isClientSideAvailable()) {
            registerSoundTileDo(soundTile);
        }
    }
    
    public static void addGenerator(final TileEntityGenerator gen) {
        if (ExtraUtilsMod.proxy.isClientSideAvailable()) {
            XUSoundTileGenerators.addGenerator(gen);
        }
    }
    
    public static void refresh() {
        if (ExtraUtilsMod.proxy.isClientSideAvailable()) {
            XUSoundTileGenerators.refresh();
        }
    }
    
    private static void registerSoundTileDo(final ISoundTile soundTile) {
        tryAddSound((ISound)new XUSoundTile(soundTile));
    }
    
    public static void tryAddSound(final ISound sound) {
        if (canAddSound(sound)) {
            Minecraft.getMinecraft().getSoundHandler().playSound(sound);
        }
    }
    
    public static boolean canAddSound(final ISound sound) {
        if (Sounds.playingSounds == null) {
            Sounds.playingSounds = ReflectionHelper.findField((Class)SoundManager.class, new String[] { "playingSounds", "field_148629_h" });
            Sounds.soundMgr = ReflectionHelper.findField((Class)SoundHandler.class, new String[] { "sndManager", "field_147694_f" });
        }
        try {
            final SoundManager manager = (SoundManager)Sounds.soundMgr.get(Minecraft.getMinecraft().getSoundHandler());
            final Map map = (Map)Sounds.playingSounds.get(manager);
            return !map.containsValue(sound);
        }
        catch (IllegalAccessException e) {
            return false;
        }
    }
    
    static {
        Sounds.playingSounds = null;
    }
}


