// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.command;

import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.nei.InfoData;
import java.util.Comparator;
import java.util.Iterator;
import com.rwtema.extrautils.LogHelper;
import com.google.common.io.Files;
import com.google.common.base.Charsets;
import java.util.Properties;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.Map;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandDumpLocalization extends CommandBase
{
    public String getCommandName() {
        return "dumplocalization";
    }
    
    public String getCommandUsage(final ICommandSender icommandsender) {
        return "/dumplocalization";
    }
    
    public void processCommand(final ICommandSender icommandsender, final String[] astring) {
        final File f = new File(Minecraft.getMinecraft().mcDataDir, "extrautilities_localization.txt");
        final Map<String, Properties> k = (Map<String, Properties>)ReflectionHelper.getPrivateValue((Class)LanguageRegistry.class, (Object)LanguageRegistry.instance(), new String[] { "modLanguageData" });
        final String lang = FMLCommonHandler.instance().getCurrentLanguage();
        final Properties p = k.get(lang);
        String t = "";
        for (final Map.Entry<Object, Object> entry : p.entrySet()) {
            t = t + entry.getKey() + "=" + entry.getValue() + "\n";
        }
        try {
            Files.write((CharSequence)t, f, Charsets.UTF_8);
            LogHelper.info("Dumped Language data to " + f.getAbsolutePath(), new Object[0]);
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


