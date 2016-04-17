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
import com.rwtema.extrautils.helper.SplineHelper;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.Vec3;
import com.rwtema.extrautils.network.XUPacketBase;

public class PacketParticleCurve extends XUPacketBase
{
    Vec3 startPos;
    Vec3 endPos;
    Vec3 startVel;
    Vec3 endVel;
    
    public PacketParticleCurve() {
    }
    
    public PacketParticleCurve(final Vec3 startPos, final Vec3 endPos, final Vec3 startVel, final Vec3 endVel) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.startVel = startVel;
        this.endVel = endVel;
    }
    
    public static Vec3 matchSpeed(final EntityItem item, final Vec3 v) {
        final double s = 5.0;
        return Vec3.createVectorHelper(v.xCoord * s, v.yCoord * s, v.zCoord * s);
    }
    
    public PacketParticleCurve(final EntityItem item, final Vec3 dest, final Vec3 vec) {
        this(Vec3.createVectorHelper(item.posX, item.posY, item.posZ), dest, Vec3.createVectorHelper(item.motionX * 5.0, item.motionY * 5.0, item.motionZ * 5.0), matchSpeed(item, vec));
    }
    
    @Override
    public void writeData(final ByteBuf data) throws Exception {
        this.writeVec(data, this.startPos);
        this.writeVec(data, this.endPos);
        this.writeVec(data, this.startVel);
        this.writeVec(data, this.endVel);
    }
    
    @Override
    public void readData(final EntityPlayer player, final ByteBuf data) {
        this.startPos = this.readVec(data);
        this.endPos = this.readVec(data);
        this.startVel = this.readVec(data);
        this.endVel = this.readVec(data);
    }
    
    @Override
    public void doStuffServer(final ChannelHandlerContext ctx) {
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void doStuffClient() {
        final double v = 0.15 / this.endPos.subtract(this.startPos).lengthVector();
        final double[] xParam = SplineHelper.splineParams(this.startPos.xCoord, this.endPos.xCoord, this.startVel.xCoord, this.endVel.xCoord);
        final double[] yParam = SplineHelper.splineParams(this.startPos.yCoord, this.endPos.yCoord, this.startVel.yCoord, this.endVel.yCoord);
        final double[] zParam = SplineHelper.splineParams(this.startPos.zCoord, this.endPos.zCoord, this.startVel.zCoord, this.endVel.zCoord);
        final float f = XUHelper.rand.nextFloat() * 0.6f + 0.4f;
        for (double h = v; h <= 1.0; h += v) {
            final double x = SplineHelper.evalSpline(h, xParam);
            final double y = SplineHelper.evalSpline(h, yParam);
            final double z = SplineHelper.evalSpline(h, zParam);
            ParticleHelperClient.addParticle((EntityFX)new ParticleEndSmoke(ExtraUtilsMod.proxy.getClientWorld(), x, y, z, f, f * 0.3f, f * 0.9f));
        }
    }
    
    @Override
    public boolean isValidSenderSide(final Side properSenderSide) {
        return properSenderSide.isServer();
    }
}
