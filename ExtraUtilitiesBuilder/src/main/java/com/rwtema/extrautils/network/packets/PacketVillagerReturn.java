// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.network.packets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import com.rwtema.extrautils.tileentity.TileEntityTradingPost;
import net.minecraft.entity.IMerchant;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayerMP;
import com.rwtema.extrautils.network.XUPacketBase;

public class PacketVillagerReturn extends XUPacketBase
{
    public int merchantId;
    public int world;
    public int x;
    public int y;
    public int z;
    EntityPlayerMP p;
    World theworld;
    
    public PacketVillagerReturn() {
    }
    
    public PacketVillagerReturn(final int merchantId, final int world, final int x, final int y, final int z) {
        this.merchantId = merchantId;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public void writeData(final ByteBuf data) throws Exception {
        data.writeInt(this.merchantId);
        data.writeInt(this.world);
        data.writeInt(this.x);
        data.writeInt(this.y);
        data.writeInt(this.z);
    }
    
    @Override
    public void readData(final EntityPlayer player, final ByteBuf data) {
        this.merchantId = data.readInt();
        this.world = data.readInt();
        this.x = data.readInt();
        this.y = data.readInt();
        this.z = data.readInt();
        this.p = (EntityPlayerMP)player;
        this.theworld = this.p.getEntityWorld();
    }
    
    @Override
    public void doStuffServer(final ChannelHandlerContext ctx) {
        try {
            if (this.world != this.theworld.provider.dimensionId) {
                return;
            }
            if (this.p.getDistance((double)this.x, (double)this.y, (double)this.z) > 6.0) {
                return;
            }
            if (this.theworld.getEntityByID(this.merchantId) instanceof IMerchant) {
                final Entity villager = this.theworld.getEntityByID(this.merchantId);
                final IMerchant trader = (IMerchant)this.theworld.getEntityByID(this.merchantId);
                if (Math.abs(villager.posX - this.x) < TileEntityTradingPost.maxRange & Math.abs(villager.posZ - this.z) < TileEntityTradingPost.maxRange) {
                    if (!villager.isEntityAlive()) {
                        PacketTempChat.sendChat((EntityPlayer)this.p, (IChatComponent)new ChatComponentText("Villager has died"));
                    }
                    else if (trader.getCustomer() != null) {
                        PacketTempChat.sendChat((EntityPlayer)this.p, (IChatComponent)new ChatComponentText("Villager is busy"));
                    }
                    else {
                        this.p.interactWith(villager);
                    }
                }
                else {
                    PacketTempChat.sendChat((EntityPlayer)this.p, (IChatComponent)new ChatComponentText("Villager is no longer in range"));
                }
            }
            else {
                PacketTempChat.sendChat((EntityPlayer)this.p, (IChatComponent)new ChatComponentText("Villager can no longer be found"));
            }
        }
        catch (Exception exception4) {
            exception4.printStackTrace();
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void doStuffClient() {
    }
    
    @Override
    public boolean isValidSenderSide(final Side properSenderSide) {
        return properSenderSide == Side.CLIENT;
    }
}


