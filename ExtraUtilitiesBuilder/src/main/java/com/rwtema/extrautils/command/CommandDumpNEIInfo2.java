// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.command;

import net.minecraft.item.ItemStack;
import java.util.Iterator;
import com.rwtema.extrautils.LogHelper;
import com.google.common.io.Files;
import com.google.common.base.Charsets;
import java.util.Comparator;
import java.util.Collections;
import com.rwtema.extrautils.nei.InfoData;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.command.CommandBase;

@SideOnly(Side.CLIENT)
public class CommandDumpNEIInfo2 extends CommandBase
{
    public String getCommandName() {
        return "dumpneidocs2";
    }
    
    public String getCommandUsage(final ICommandSender icommandsender) {
        return "/dumpneidocs2";
    }
    
    public void processCommand(final ICommandSender icommandsender, final String[] astring) {
        final File f = new File(Minecraft.getMinecraft().mcDataDir, "en_US_doc.lang");
        String t = "";
        Collections.sort(InfoData.data, cmpData.instance);
        for (final InfoData data : InfoData.data) {
            String id;
            if (data.precise) {
                id = data.item.getUnlocalizedName();
            }
            else {
                id = data.item.getItem().getUnlocalizedName();
            }
            t = t + "doc." + id + ".name=" + data.name + "\n";
            if (data.info.length == 1) {
                t = t + "doc." + id + ".info=" + data.info[0].replace('\n', ' ') + "\n";
            }
            else {
                for (int i = 0; i < data.info.length; ++i) {
                    t = t + "doc." + id + ".info." + i + "=" + data.info[i].replace('\n', ' ') + "\n";
                }
            }
        }
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
            return arg0.item.getUnlocalizedName().compareTo(arg1.item.getUnlocalizedName());
        }
        
        static {
            cmpData.instance = new cmpData();
        }
    }
}
