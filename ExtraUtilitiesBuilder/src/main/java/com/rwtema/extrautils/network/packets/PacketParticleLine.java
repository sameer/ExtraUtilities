// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.network.packets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityFX;
import com.rwtema.extrautils.particle.ParticleHelperClient;
import com.rwtema.extrautils.particle.ParticleEndSmoke;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.helper.XUHelper;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.Vec3;
import com.rwtema.extrautils.network.XUPacketBase;

public class PacketParticleLine extends XUPacketBase
{
    Vec3 start;
    Vec3 end;
    
    public PacketParticleLine() {
    }
    
    public PacketParticleLine(final EntityItem item, final TileEntity tile) {
        this.start = Vec3.createVectorHelper(item.posX, item.posY, item.posZ);
        this.end = Vec3.createVectorHelper(tile.xCoord + 0.5, tile.yCoord + 0.8, tile.zCoord + 0.5);
    }
    
    public PacketParticleLine(final Vec3 start, final Vec3 end) {
        this.start = start;
        this.end = end;
    }
    
    @Override
    public void writeData(final ByteBuf data) throws Exception {
        this.writeVec(data, this.start);
        this.writeVec(data, this.end);
    }
    
    @Override
    public void readData(final EntityPlayer player, final ByteBuf data) {
        this.start = this.readVec(data);
        this.end = this.readVec(data);
    }
    
    @Override
    public void doStuffServer(final ChannelHandlerContext ctx) {
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void doStuffClient() {
        for (double v = 0.25 / this.end.subtract(this.start).lengthVector(), h = 0.0; h <= 1.0; h += v) {
            final float f = XUHelper.rand.nextFloat() * 0.6f + 0.4f;
            final double x = this.start.xCoord + (this.end.xCoord - this.start.xCoord) * h;
            final double y = this.start.yCoord + (this.end.yCoord - this.start.yCoord) * h;
            final double z = this.start.zCoord + (this.end.zCoord - this.start.zCoord) * h;
            ParticleHelperClient.addParticle((EntityFX)new ParticleEndSmoke(ExtraUtilsMod.proxy.getClientWorld(), x, y, z, f, f * 0.3f, f * 0.9f));
        }
    }
    
    @Override
    public boolean isValidSenderSide(final Side properSenderSide) {
        return properSenderSide.isServer();
    }
}
