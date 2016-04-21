// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.network.packets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import com.google.common.base.Throwables;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.network.XUPacketBase;

public class PacketUseItemAlt extends XUPacketBase
{
    private int x;
    private int y;
    private int z;
    private int face;
    private ItemStack item;
    private float hitX;
    private float hitY;
    private float hitZ;
    private EntityPlayerMP player;
    public static final ThreadLocal<Boolean> altPlace;
    
    public PacketUseItemAlt(final int x, final int y, final int z, final int face, final ItemStack item, final float hitX, final float hitY, final float hitZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.face = face;
        this.item = item;
        this.hitX = hitX;
        this.hitY = hitY;
        this.hitZ = hitZ;
    }
    
    public PacketUseItemAlt() {
    }
    
    @Override
    public void writeData(final ByteBuf data) throws Exception {
        data.writeInt(this.x);
        data.writeInt(this.y);
        data.writeInt(this.z);
        data.writeByte(this.face);
        this.writeItemStack(data, this.item);
        data.writeByte((int)(byte)(this.hitX * 16.0f));
        data.writeByte((int)(byte)(this.hitY * 16.0f));
        data.writeByte((int)(byte)(this.hitZ * 16.0f));
    }
    
    @Override
    public void readData(final EntityPlayer player, final ByteBuf data) {
        this.x = data.readInt();
        this.y = data.readInt();
        this.z = data.readInt();
        this.face = data.readByte();
        this.item = this.readItemStack(data);
        this.hitX = data.readByte() * 0.0625f;
        this.hitY = data.readByte() * 0.0625f;
        this.hitZ = data.readByte() * 0.0625f;
        this.player = (EntityPlayerMP)player;
    }
    
    @Override
    public synchronized void doStuffServer(final ChannelHandlerContext ctx) {
        C08PacketPlayerBlockPlacement placement;
        try {
            final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(this.x);
            packetbuffer.writeByte(this.y);
            packetbuffer.writeInt(this.z);
            packetbuffer.writeByte(this.face);
            packetbuffer.writeItemStackToBuffer(this.item);
            packetbuffer.writeByte((int)(this.hitX * 16.0f));
            packetbuffer.writeByte((int)(this.hitY * 16.0f));
            packetbuffer.writeByte((int)(this.hitZ * 16.0f));
            placement = new C08PacketPlayerBlockPlacement();
            placement.readPacketData(packetbuffer);
        }
        catch (IOException e) {
            throw Throwables.propagate((Throwable)e);
        }
        PacketUseItemAlt.altPlace.set(true);
        this.player.playerNetServerHandler.processPlayerBlockPlacement(placement);
        PacketUseItemAlt.altPlace.set(false);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void doStuffClient() {
    }
    
    @Override
    public boolean isValidSenderSide(final Side properSenderSide) {
        return properSenderSide == Side.CLIENT;
    }
    
    static {
        altPlace = new ThreadLocal<Boolean>();
    }
}

