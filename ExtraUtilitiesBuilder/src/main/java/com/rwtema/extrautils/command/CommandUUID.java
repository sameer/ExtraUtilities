// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.command;

import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.command.CommandBase;

@SideOnly(Side.CLIENT)
public class CommandUUID extends CommandBase
{
    public String getCommandName() {
        return "uuid";
    }
    
    public String getCommandUsage(final ICommandSender var1) {
        return "/uuid";
    }
    
    public boolean canCommandSenderUseCommand(final ICommandSender par1ICommandSender) {
        return true;
    }
    
    public void processCommand(final ICommandSender var1, final String[] var2) {
        var1.addChatMessage((IChatComponent)new ChatComponentText("Username: " + Minecraft.getMinecraft().getSession().func_148256_e().getName() + " UUID: " + Minecraft.getMinecraft().getSession().func_148256_e().getId()));
    }
}

