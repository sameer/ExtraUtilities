// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.command;

import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.command.CommandBase;

public class CommandKillEntities extends CommandBase
{
    private final String type;
    private final boolean except;
    protected Class<? extends Entity> entityclass;
    protected int numKills;
    
    public CommandKillEntities(final String type, final Class<? extends Entity> entityclass, final boolean except) {
        this.numKills = 0;
        this.type = type;
        this.entityclass = entityclass;
        this.except = except;
    }
    
    public String getCommandName() {
        return "xu_kill" + this.type;
    }
    
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    public String getCommandUsage(final ICommandSender icommandsender) {
        return "/xu_kill" + this.type;
    }
    
    public void processCommand(final ICommandSender icommandsender, final String[] astring) {
        this.numKills = 0;
        for (int j = 0; j < MinecraftServer.getServer().worldServers.length; ++j) {
            this.killEntities((World)MinecraftServer.getServer().worldServers[j]);
        }
        icommandsender.addChatMessage((IChatComponent)new ChatComponentText("Killed " + this.numKills + " of type " + this.entityclass.getName()));
    }
    
    public void killEntities(final World world) {
        for (int i = 0; i < world.loadedEntityList.size(); ++i) {
            if (this.entityclass.isInstance(world.loadedEntityList.get(i)) == this.except) {
                ++this.numKills;
                ((Entity)world.loadedEntityList.get(i)).setDead();
            }
        }
    }
}


