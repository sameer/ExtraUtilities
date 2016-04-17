// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.network;

import cpw.mods.fml.common.FMLCommonHandler;
import io.netty.channel.ChannelHandlerContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.channel.ChannelHandler;

@ChannelHandler.Sharable
@SideOnly(Side.CLIENT)
public class PacketHandlerClient extends PacketHandler
{
    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final XUPacketBase msg) throws Exception {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            msg.doStuffServer(ctx);
        }
        else {
            msg.doStuffClient();
        }
    }
}
