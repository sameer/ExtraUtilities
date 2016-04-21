// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.network.splitter;

import java.util.HashMap;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import com.rwtema.extrautils.network.NetworkHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import java.util.Map;
import io.netty.channel.ChannelHandler;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class PacketRecombiner extends SimpleChannelInboundHandler<XUPacketSplit>
{
    public static Map<Integer, ByteBuf[]> map;
    
    protected void channelRead0(final ChannelHandlerContext ctx, final XUPacketSplit msg) throws Exception {
        ByteBuf[] b = PacketRecombiner.map.get(msg.packetIndex);
        if (b == null || b.length != msg.total) {
            b = new ByteBuf[msg.total];
        }
        b[msg.seq] = msg.data;
        boolean flag = true;
        int s = 0;
        for (int i = 0; i < b.length && flag; ++i) {
            if (b[i] != null) {
                flag = false;
                s += b[i].readableBytes();
            }
        }
        if (flag) {
            final ByteBuf data = Unpooled.buffer(s);
            for (int j = 0; j < b.length; ++j) {
                data.writeBytes(b[j]);
            }
            final FMLProxyPacket proxy = new FMLProxyPacket(data, (String)NetworkHandler.channels.get(Side.CLIENT).attr(NetworkRegistry.FML_CHANNEL).get());
            NetworkHandler.channels.get(FMLCommonHandler.instance().getEffectiveSide()).writeInbound(new Object[] { proxy });
            PacketRecombiner.map.remove(msg.packetIndex);
            if (PacketRecombiner.map.size() > 1024) {
                PacketRecombiner.map.clear();
            }
        }
        else {
            PacketRecombiner.map.put(msg.packetIndex, b);
        }
    }
    
    static {
        PacketRecombiner.map = new HashMap<Integer, ByteBuf[]>();
    }
}

