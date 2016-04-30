// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.network.packets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import com.rwtema.extrautils.dynamicgui.DynamicContainer;
import com.rwtema.extrautils.ExtraUtilsMod;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import com.rwtema.extrautils.network.XUPacketBase;

public class PacketGUIWidget extends XUPacketBase
{
    int window_id;
    NBTTagCompound tag;
    
    public PacketGUIWidget() {
    }
    
    public PacketGUIWidget(final int window_id, final NBTTagCompound tag) {
        this.tag = tag;
        this.window_id = window_id;
    }
    
    @Override
    public void writeData(final ByteBuf data) {
        data.writeInt(this.window_id);
        this.writeNBT(data, this.tag);
    }
    
    @Override
    public void readData(final EntityPlayer player, final ByteBuf data) {
        try {
            this.window_id = data.readInt();
            this.tag = this.readNBT(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void doStuffServer(final ChannelHandlerContext ctx) {
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void doStuffClient() {
        if (this.tag != null && this.window_id != 0 && this.window_id == ExtraUtilsMod.proxy.getClientPlayer().openContainer.windowId) {
            try {
                ((DynamicContainer)ExtraUtilsMod.proxy.getClientPlayer().openContainer).recieveDescriptionPacket(this.tag);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public boolean isValidSenderSide(final Side properSenderSide) {
        return properSenderSide == Side.SERVER;
    }
}


