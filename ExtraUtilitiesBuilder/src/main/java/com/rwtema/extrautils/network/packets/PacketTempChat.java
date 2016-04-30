// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.network.packets;

import net.minecraft.client.Minecraft;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ChatComponentText;
import com.rwtema.extrautils.network.NetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import com.rwtema.extrautils.IClientCode;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import com.rwtema.extrautils.network.XUPacketBase;

public class PacketTempChat extends XUPacketBase
{
    IChatComponent chatComponent;
    
    public static void sendChat(final EntityPlayer player, final IChatComponent s) {
        if (XUHelper.isPlayerFake(player)) {
            return;
        }
        if (player.worldObj.isRemote) {
            ExtraUtilsMod.proxy.exectuteClientCode(new IClientCode() {
                @SideOnly(Side.CLIENT)
                @Override
                public void exectuteClientCode() {
                    addChat(s);
                }
            });
        }
        else {
            NetworkHandler.sendPacketToPlayer(new PacketTempChat(s), player);
        }
    }
    
    public static void sendChat(final EntityPlayer player, final String s) {
        sendChat(player, (IChatComponent)new ChatComponentText(s));
    }
    
    public PacketTempChat() {
    }
    
    public PacketTempChat(final String s) {
        this((IChatComponent)new ChatComponentText(s));
    }
    
    public PacketTempChat(final IChatComponent chatComponent) {
        this.chatComponent = chatComponent;
    }
    
    @Override
    public void writeData(final ByteBuf data) throws Exception {
        this.writeChatComponent(data, this.chatComponent);
    }
    
    @Override
    public void readData(final EntityPlayer player, final ByteBuf data) {
        this.chatComponent = this.readChatComponent(data);
    }
    
    @Override
    public void doStuffServer(final ChannelHandlerContext ctx) {
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void doStuffClient() {
        addChat(this.chatComponent);
    }
    
    @SideOnly(Side.CLIENT)
    private static void addChat(final IChatComponent chat) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(chat, 7706071);
    }
    
    @Override
    public boolean isValidSenderSide(final Side properSenderSide) {
        return properSenderSide == Side.SERVER;
    }
}


