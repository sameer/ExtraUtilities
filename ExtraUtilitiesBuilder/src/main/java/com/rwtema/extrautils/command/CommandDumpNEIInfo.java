// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.command;

import net.minecraft.item.ItemStack;
import java.util.Iterator;
import com.google.common.io.Files;
import com.google.common.base.Charsets;
import java.util.Comparator;
import java.util.Collections;
import com.rwtema.extrautils.nei.InfoData;
import java.io.File;
import com.rwtema.extrautils.LogHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.command.CommandBase;

@SideOnly(Side.CLIENT)
public class CommandDumpNEIInfo extends CommandBase
{
    public String getCommandName() {
        return "dumpneidocs";
    }
    
    public String getCommandUsage(final ICommandSender icommandsender) {
        return "/dumpneidocs";
    }
    
    public void processCommand(final ICommandSender icommandsender, final String[] astring) {
        final TextureMap tex = Minecraft.getMinecraft().getTextureMapBlocks();
        final Map map = (Map)ReflectionHelper.getPrivateValue((Class)TextureMap.class, (Object)tex, new String[] { "mapRegisteredSprites" });
        for (final Object o : map.values()) {
            final TextureAtlasSprite sprite = (TextureAtlasSprite)o;
            if (sprite.getIconWidth() < 16 || sprite.getIconHeight() < 16) {
                LogHelper.debug(sprite.getIconName(), new Object[0]);
            }
        }
        final File f = new File(Minecraft.getMinecraft().mcDataDir, "extrautilitiesnei.txt");
        String t = "";
        Collections.sort(InfoData.data, cmpData.instance);
        boolean blocks = true;
        t = "[spoiler='New Blocks']\n";
        for (final InfoData data : InfoData.data) {
            if (blocks && !data.isBlock) {
                blocks = false;
                t += "[/spoiler]\n\n";
                t += "[spoiler='New Items']\n";
            }
            t = t + "[spoiler='" + data.name + "']\n";
            if (data.url != null) {
                t = t + "[center][img]" + data.url + "[/img][/center]\n";
            }
            boolean extraSpoilerTag = false;
            for (final String s : data.info) {
                if (s.startsWith("Spoilers:")) {
                    extraSpoilerTag = true;
                    t += "[spoiler='Spoilers!']\n";
                }
                else {
                    t = t + s + "\n";
                }
            }
            if (extraSpoilerTag) {
                t += "[/spoiler]\n";
            }
            t += "[/spoiler]\n\n";
        }
        t += "[/spoiler]\n";
        try {
            Files.write((CharSequence)t, f, Charsets.UTF_8);
            LogHelper.info("Dumped Extra Utilities NEI info data to " + f.getAbsolutePath(), new Object[0]);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public static class cmpData implements Comparator<InfoData>
    {
        public static cmpData instance;
        
        public static String getItem(final ItemStack i) {
            final ItemStack t = new ItemStack(i.getItem(), 1, 0);
            return t.getDisplayName();
        }
        
        @Override
        public int compare(final InfoData arg0, final InfoData arg1) {
            if (arg0.isBlock && !arg1.isBlock) {
                return -1;
            }
            if (arg1.isBlock && !arg0.isBlock) {
                return 1;
            }
            return arg0.name.compareTo(arg1.name);
        }
        
        static {
            cmpData.instance = new cmpData();
        }
    }
}

