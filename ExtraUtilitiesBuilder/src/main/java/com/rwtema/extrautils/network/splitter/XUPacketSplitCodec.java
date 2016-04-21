// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.network.splitter;

import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;

@ChannelHandler.Sharable
public class XUPacketSplitCodec extends FMLIndexedMessageToMessageCodec<XUPacketSplit>
{
    public XUPacketSplitCodec() {
        this.addDiscriminator(0, (Class)XUPacketSplit.class);
    }
    
    public void encodeInto(final ChannelHandlerContext ctx, final XUPacketSplit msg, final ByteBuf target) throws Exception {
        target.writeInt(msg.packetIndex);
        target.writeInt(msg.seq);
        target.writeInt(msg.total);
        target.writeBytes(msg.data);
    }
    
    public void decodeInto(final ChannelHandlerContext ctx, final ByteBuf source, final XUPacketSplit msg) {
        msg.packetIndex = source.readInt();
        msg.seq = source.readInt();
        msg.total = source.readInt();
        final ByteBuf buffer = Unpooled.buffer(source.readableBytes());
        source.readBytes(buffer);
        msg.data = buffer;
    }
}


