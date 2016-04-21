// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.command;

import com.rwtema.extrautils.EventHandlerClient;
import net.minecraft.command.ICommandSender;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.command.CommandBase;

@SideOnly(Side.CLIENT)
public class CommandHologram extends CommandBase
{
    public String getCommandName() {
        return "xu_holo";
    }
    
    public boolean canCommandSenderUseCommand(final ICommandSender par1ICommandSender) {
        return true;
    }
    
    public String getCommandUsage(final ICommandSender icommandsender) {
        return "/xu_holo <playername>";
    }
    
    public void processCommand(final ICommandSender icommandsender, final String[] astring) {
        if (EventHandlerClient.holograms.contains(astring[0])) {
            EventHandlerClient.holograms.remove(astring[0]);
        }
        else {
            EventHandlerClient.holograms.add(astring[0]);
        }
    }
}

