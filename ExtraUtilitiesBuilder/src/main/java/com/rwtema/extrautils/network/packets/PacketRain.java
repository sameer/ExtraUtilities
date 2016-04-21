// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.network.packets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTBase;
import com.rwtema.extrautils.ExtraUtilsMod;
import net.minecraft.nbt.NBTTagCompound;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import com.rwtema.extrautils.network.XUPacketBase;

public class PacketRain extends XUPacketBase
{
    private boolean shouldRain;
    
    public PacketRain() {
    }
    
    public PacketRain(final boolean shouldRain) {
        this.shouldRain = shouldRain;
    }
    
    @Override
    public void writeData(final ByteBuf data) {
        data.writeBoolean(this.shouldRain);
    }
    
    @Override
    public void readData(final EntityPlayer player, final ByteBuf data) {
        this.shouldRain = data.readBoolean();
    }
    
    @Override
    public void doStuffServer(final ChannelHandlerContext ctx) {
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void doStuffClient() {
        NBTTagCompound t = new NBTTagCompound();
        final EntityPlayer player = ExtraUtilsMod.proxy.getClientPlayer();
        if (player.getEntityData().hasKey("PlayerPersisted")) {
            t = player.getEntityData().getCompoundTag("PlayerPersisted");
        }
        else {
            player.getEntityData().setTag("PlayerPersisted", (NBTBase)t);
        }
        t.setBoolean("ExtraUtilities|Rain", this.shouldRain);
    }
    
    @Override
    public boolean isValidSenderSide(final Side properSenderSide) {
        return properSenderSide == Side.SERVER;
    }
}


