// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.network.packets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import com.rwtema.extrautils.EventHandlerClient;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import com.rwtema.extrautils.network.XUPacketBase;

public class PacketAngelRingNotifier extends XUPacketBase
{
    String username;
    int wingType;
    
    public PacketAngelRingNotifier() {
    }
    
    public PacketAngelRingNotifier(final String player, final int wing) {
        this.username = player;
        this.wingType = wing;
    }
    
    @Override
    public void writeData(final ByteBuf data) throws Exception {
        this.writeString(data, this.username);
        data.writeByte(this.wingType);
    }
    
    @Override
    public void readData(final EntityPlayer player, final ByteBuf data) {
        this.username = this.readString(data);
        this.wingType = data.readByte();
    }
    
    @Override
    public void doStuffServer(final ChannelHandlerContext ctx) {
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void doStuffClient() {
        if (this.wingType > 0) {
            EventHandlerClient.flyingPlayers.put(this.username, this.wingType);
        }
        else {
            EventHandlerClient.flyingPlayers.remove(this.username);
        }
    }
    
    @Override
    public boolean isValidSenderSide(final Side properSenderSide) {
        return properSenderSide == Side.SERVER;
    }
}
