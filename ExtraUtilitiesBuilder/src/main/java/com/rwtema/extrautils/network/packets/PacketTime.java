// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.network.packets;

import cpw.mods.fml.relauncher.Side;
import com.rwtema.extrautils.CommandTPSTimer;
import com.rwtema.extrautils.ExtraUtilsMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.network.INetHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import com.rwtema.extrautils.network.XUPacketBase;

public class PacketTime extends XUPacketBase
{
    long time;
    int counter;
    
    public PacketTime() {
        this(0L, 0);
    }
    
    public PacketTime(final long time, final int counter) {
        this.time = time;
        this.counter = counter;
    }
    
    @Override
    public void writeData(final ByteBuf data) throws Exception {
        data.writeLong(this.time);
        data.writeByte(this.counter);
    }
    
    @Override
    public void readData(final EntityPlayer player, final ByteBuf data) {
        this.time = data.readLong();
        this.counter = data.readUnsignedByte();
    }
    
    @Override
    public void doStuffServer(final ChannelHandlerContext ctx) {
        final INetHandler netHandler = (INetHandler)ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
        final EntityPlayer player = ExtraUtilsMod.proxy.getPlayerFromNetHandler(netHandler);
        CommandTPSTimer.add(player.getCommandSenderName());
    }
    
    @Override
    public void doStuffClient() {
        CommandTPSTimer.update(this.counter, this.time);
    }
    
    @Override
    public boolean isValidSenderSide(final Side properSenderSide) {
        return true;
    }
}
