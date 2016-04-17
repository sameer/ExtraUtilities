// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.network.packets;

import java.util.ArrayList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import com.rwtema.extrautils.ExtraUtilsMod;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import java.util.List;
import com.rwtema.extrautils.network.XUPacketBase;

public class PacketParticle extends XUPacketBase
{
    public static List<String> particleNames;
    public String p;
    public double x;
    public double y;
    public double z;
    public double vx;
    public double vy;
    public double vz;
    
    public PacketParticle() {
    }
    
    public PacketParticle(final String p, final double x, final double y, final double z, final double vx, final double vy, final double vz) {
        this.p = p;
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }
    
    @Override
    public void writeData(final ByteBuf dataoutputstream) throws Exception {
        if (!PacketParticle.particleNames.contains(this.p)) {
            dataoutputstream.writeByte(0);
        }
        else {
            dataoutputstream.writeByte(PacketParticle.particleNames.indexOf(this.p));
        }
        dataoutputstream.writeFloat((float)this.x);
        dataoutputstream.writeFloat((float)this.y);
        dataoutputstream.writeFloat((float)this.z);
        dataoutputstream.writeFloat((float)this.vx);
        dataoutputstream.writeFloat((float)this.vy);
        dataoutputstream.writeFloat((float)this.vz);
    }
    
    @Override
    public void readData(final EntityPlayer player, final ByteBuf datainputstream) {
        this.p = PacketParticle.particleNames.get(datainputstream.readUnsignedByte());
        this.x = datainputstream.readFloat();
        this.y = datainputstream.readFloat();
        this.z = datainputstream.readFloat();
        this.vx = datainputstream.readFloat();
        this.vy = datainputstream.readFloat();
        this.vz = datainputstream.readFloat();
    }
    
    @Override
    public void doStuffServer(final ChannelHandlerContext ctx) {
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void doStuffClient() {
        ExtraUtilsMod.proxy.getClientWorld().spawnParticle(this.p, this.x, this.y, this.z, this.vx, this.vy, this.vz);
    }
    
    @Override
    public boolean isValidSenderSide(final Side properSenderSide) {
        return properSenderSide == Side.SERVER;
    }
    
    static {
        (PacketParticle.particleNames = new ArrayList<String>()).add("bubble");
        PacketParticle.particleNames.add("suspended");
        PacketParticle.particleNames.add("depthsuspend");
        PacketParticle.particleNames.add("townaura");
        PacketParticle.particleNames.add("crit");
        PacketParticle.particleNames.add("magicCrit");
        PacketParticle.particleNames.add("smoke");
        PacketParticle.particleNames.add("mobSpell");
        PacketParticle.particleNames.add("mobSpellAmbient");
        PacketParticle.particleNames.add("spell");
        PacketParticle.particleNames.add("instantSpell");
        PacketParticle.particleNames.add("witchMagic");
        PacketParticle.particleNames.add("note");
        PacketParticle.particleNames.add("portal");
        PacketParticle.particleNames.add("enchantmenttable");
        PacketParticle.particleNames.add("explode");
        PacketParticle.particleNames.add("flame");
        PacketParticle.particleNames.add("lava");
        PacketParticle.particleNames.add("footstep");
        PacketParticle.particleNames.add("splash");
        PacketParticle.particleNames.add("largesmoke");
        PacketParticle.particleNames.add("cloud");
        PacketParticle.particleNames.add("reddust");
        PacketParticle.particleNames.add("snowballpoof");
        PacketParticle.particleNames.add("dripWater");
        PacketParticle.particleNames.add("dripLava");
        PacketParticle.particleNames.add("snowshovel");
        PacketParticle.particleNames.add("slime");
        PacketParticle.particleNames.add("heart");
        PacketParticle.particleNames.add("angryVillager");
        PacketParticle.particleNames.add("happyVillager");
    }
}
