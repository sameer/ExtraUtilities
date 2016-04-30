// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.FMLCommonHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<XUPacketBase>
{
    protected void channelRead0(final ChannelHandlerContext ctx, final XUPacketBase msg) throws Exception {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            msg.doStuffServer(ctx);
        }
    }
}


