// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.helper;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;

public class Translate
{
    public static String get(final String id, final Object... objects) {
        String s = StatCollector.translateToLocal(id);
        for (int i = 0; i < objects.length; ++i) {
            s = s.replaceAll("%" + (i + 1), objects[i].toString());
        }
        final ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation(id, objects);
        return chatComponentTranslation.getUnformattedTextForChat();
    }
}


