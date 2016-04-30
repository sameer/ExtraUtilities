// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.helper;

import java.util.Locale;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class XUHelperClient
{
    public static EntityPlayer clientPlayer() {
        return (EntityPlayer)Minecraft.getMinecraft().thePlayer;
    }
    
    public static World clientWorld() {
        return (World)Minecraft.getMinecraft().theWorld;
    }
    
    public static IIcon registerCustomIcon(final String texture, final IIconRegister register, final TextureAtlasSprite sprite) {
        IIcon result = (IIcon)((TextureMap)register).getTextureExtry(texture);
        if (result == null) {
            result = (IIcon)sprite;
            ((TextureMap)register).setTextureEntry(texture, sprite);
        }
        return result;
    }
    
    public static String commaDelimited(final int n) {
        return String.format(Locale.ENGLISH, "%,d", n);
    }
}


