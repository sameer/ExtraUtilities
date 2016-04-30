// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import java.util.Locale;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import java.util.Iterator;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.packets.PacketTime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLCommonHandler;
import java.util.HashSet;
import net.minecraft.command.CommandBase;

public class CommandTPSTimer extends CommandBase
{
    public static final CommandTPSTimer INSTANCE;
    public static HashSet<String> playerSet;
    private static long[] clientTimer;
    private static int clientCounter;
    private static String displayString;
    public boolean display;
    
    public CommandTPSTimer() {
        this.display = false;
    }
    
    public static void init() {
        FMLCommonHandler.instance().bus().register((Object)CommandTPSTimer.INSTANCE);
    }
    
    public static void add(final String commandSenderName) {
        EntityPlayer playerInstance = null;
        for (final Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            final EntityPlayer player = (EntityPlayer)o;
            if (commandSenderName.equals(player.getCommandSenderName())) {
                playerInstance = player;
            }
        }
        if (playerInstance == null) {
            return;
        }
        if (CommandTPSTimer.playerSet.contains(commandSenderName)) {
            CommandTPSTimer.playerSet.remove(commandSenderName);
            NetworkHandler.sendPacketToPlayer(new PacketTime(0L, 255), playerInstance);
        }
        else {
            CommandTPSTimer.playerSet.add(commandSenderName);
        }
    }
    
    public static void update(final int counter, final long time) {
        if (counter >= 100 || counter < 0) {
            CommandTPSTimer.INSTANCE.display = false;
            return;
        }
        CommandTPSTimer.INSTANCE.display = true;
        while (CommandTPSTimer.clientCounter != counter) {
            ++CommandTPSTimer.clientCounter;
            if (CommandTPSTimer.clientCounter >= 100) {
                CommandTPSTimer.clientCounter = 0;
            }
            CommandTPSTimer.clientTimer[CommandTPSTimer.clientCounter] = time;
        }
        CommandTPSTimer.displayString = getDisplayString();
    }
    
    @SubscribeEvent
    public void onServerTick(final TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || CommandTPSTimer.playerSet.isEmpty()) {
            return;
        }
        final MinecraftServer server = MinecraftServer.getServer();
        final int counter = server.getTickCounter() % 100;
        final long[] longs = server.tickTimeArray;
        if (longs == null) {
            return;
        }
        for (final Object o : server.getConfigurationManager().playerEntityList) {
            final EntityPlayer player = (EntityPlayer)o;
            if (CommandTPSTimer.playerSet.contains(player.getCommandSenderName())) {
                NetworkHandler.sendPacketToPlayer(new PacketTime(longs[counter], counter), player);
            }
        }
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onDraw(final TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !this.display || CommandTPSTimer.displayString.length() == 0) {
            return;
        }
        final Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.theWorld == null) {
            CommandTPSTimer.displayString = "";
            return;
        }
        final FontRenderer fontrenderer = minecraft.fontRenderer;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(3042);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(770, 771);
        RenderHelper.disableStandardItemLighting();
        fontrenderer.drawString(CommandTPSTimer.displayString, 0, 0, -1, true);
    }
    
    public static String getDisplayString() {
        long sum = 0L;
        long max = 0L;
        for (final long l : CommandTPSTimer.clientTimer) {
            sum += l;
            max = Math.max(max, l);
        }
        return "TPS: " + formatTimer(CommandTPSTimer.clientTimer[CommandTPSTimer.clientCounter]) + ", " + formatTimer(sum / CommandTPSTimer.clientTimer.length) + ", " + formatTimer(max);
    }
    
    public static String formatTimer(final double time) {
        final double tps = Math.min(1000.0 / (time * 1.0E-6), 20.0);
        final boolean tpsDown = tps != 20.0;
        return String.format(Locale.ENGLISH, "%5.2f", time * 1.0E-6) + "(" + (tpsDown ? EnumChatFormatting.RED : "") + String.format(Locale.ENGLISH, "%5.2f", tps) + (tpsDown ? EnumChatFormatting.RESET : "") + ")";
    }
    
    public boolean canCommandSenderUseCommand(final ICommandSender p_71519_1_) {
        return true;
    }
    
    public String getCommandName() {
        return "xu_tps";
    }
    
    public String getCommandUsage(final ICommandSender p_71518_1_) {
        return "xu_tps";
    }
    
    public void processCommand(final ICommandSender p_71515_1_, final String[] p_71515_2_) {
        CommandTPSTimer.displayString = "";
        NetworkHandler.sendPacketToServer(new PacketTime());
    }
    
    static {
        INSTANCE = new CommandTPSTimer();
        CommandTPSTimer.playerSet = new HashSet<String>();
        CommandTPSTimer.clientTimer = new long[100];
        CommandTPSTimer.clientCounter = 0;
        CommandTPSTimer.displayString = "";
    }
}


