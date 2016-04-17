// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.network.packets;

import com.rwtema.extrautils.helper.XURandom;
import net.minecraft.client.particle.EntityFX;
import com.rwtema.extrautils.particle.ParticleTransferNodes;
import net.minecraft.client.Minecraft;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import java.util.Random;
import com.rwtema.extrautils.network.XUPacketBase;

public class PacketParticleEvent extends XUPacketBase
{
    int x;
    int y;
    int z;
    byte i;
    private static Random rand;
    public static final int[] dx;
    public static final int[] dy;
    public static final int[] dz;
    public static final double w = 0.2;
    public static final double w1 = 0.3;
    public static final double w2 = 0.4;
    
    public PacketParticleEvent() {
    }
    
    public PacketParticleEvent(final int x, final int y, final int z, final byte i) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.i = i;
    }
    
    @Override
    public void writeData(final ByteBuf data) throws Exception {
        data.writeInt(this.x);
        data.writeInt(this.y);
        data.writeInt(this.z);
        data.writeByte((int)this.i);
    }
    
    @Override
    public void readData(final EntityPlayer player, final ByteBuf data) {
        this.x = data.readInt();
        this.y = data.readInt();
        this.z = data.readInt();
        this.i = data.readByte();
    }
    
    @Override
    public void doStuffServer(final ChannelHandlerContext ctx) {
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void doStuffClient() {
        this.doParticleEvent(this.i, this.x, this.y, this.z);
    }
    
    @SideOnly(Side.CLIENT)
    private void doParticleEvent(final int type, final int x, final int y, final int z) {
        switch (type) {
            case 0: {
                for (int dy = 1, y2 = y + dy; y2 < 256; y2 += dy) {
                    ExtraUtilsMod.proxy.getClientWorld().spawnParticle("portal", x + 0.5, y2 + 0.5, z + 0.5, PacketParticleEvent.rand.nextGaussian() * 0.1, -dy + PacketParticleEvent.rand.nextGaussian() * 0.1, PacketParticleEvent.rand.nextGaussian() * 0.1);
                    dy = PacketParticleEvent.rand.nextInt(8);
                }
                break;
            }
            case 1: {
                ExtraUtilsMod.proxy.getClientWorld().spawnParticle("reddust", x + 0.5, y + 0.5, z + 0.5, 1.0, 0.3, 0.9);
                break;
            }
            case 2: {
                for (int dy = 1, y2 = y + dy; y2 < 256; y2 += dy) {
                    ExtraUtilsMod.proxy.getClientWorld().spawnParticle("portal", x + 0.5, y2 + 0.5, z + 0.5, PacketParticleEvent.rand.nextGaussian() * 0.1, dy + PacketParticleEvent.rand.nextGaussian() * 0.1, PacketParticleEvent.rand.nextGaussian() * 0.1);
                    dy = PacketParticleEvent.rand.nextInt(8);
                }
                break;
            }
            case 3: {
                this.spawnNodeParticles(x, y, z, 1.0f, 0.0f, 0.0f);
                break;
            }
            case 4: {
                this.spawnNodeParticles(x, y, z, 0.0f, 0.0f, 1.0f);
                break;
            }
            case 5: {
                this.spawnNodeParticles(x, y, z, 0.0f, 1.0f, 0.0f);
                break;
            }
            case 6: {
                this.spawnNodeParticles(x, y, z, 0.0f, 1.0f, 1.0f);
                break;
            }
            case 7: {
                this.spawnNodeParticles(x, y, z, 1.0f, 1.0f, 0.0f);
                break;
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    private void spawnNodeParticles(final int x, final int y, final int z, final float pr, final float pg, final float pb) {
        if (ExtraUtils.disableNodeParticles) {
            return;
        }
        Minecraft.getMinecraft().effectRenderer.addEffect((EntityFX)new ParticleTransferNodes(ExtraUtilsMod.proxy.getClientWorld(), x + 0.5, y + 0.5, z + 0.5, pr, pg, pb));
    }
    
    @Override
    public boolean isValidSenderSide(final Side properSenderSide) {
        return properSenderSide == Side.SERVER;
    }
    
    static {
        PacketParticleEvent.rand = XURandom.getInstance();
        dx = new int[] { 0, 1, 0, 1, 0, 1, 0, 1 };
        dy = new int[] { 0, 0, 1, 1, 0, 0, 1, 1 };
        dz = new int[] { 0, 0, 0, 0, 1, 1, 1, 1 };
    }
}
