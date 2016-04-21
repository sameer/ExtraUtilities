// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.network.packets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.village.MerchantRecipeList;
import com.rwtema.extrautils.tileentity.TileEntityTradingPost;
import com.rwtema.extrautils.ExtraUtilsMod;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import com.rwtema.extrautils.network.XUPacketBase;

public class PacketVillager extends XUPacketBase
{
    public int x;
    public int y;
    public int z;
    public NBTTagCompound tag;
    
    public PacketVillager() {
    }
    
    public PacketVillager(final int x, final int y, final int z, final NBTTagCompound tag) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tag = tag;
    }
    
    @Override
    public void writeData(final ByteBuf data) throws Exception {
        data.writeInt(this.x);
        data.writeInt(this.y);
        data.writeInt(this.z);
        this.writeNBT(data, this.tag);
    }
    
    @Override
    public void readData(final EntityPlayer player, final ByteBuf data) {
        this.x = data.readInt();
        this.y = data.readInt();
        this.z = data.readInt();
        this.tag = this.readNBT(data);
    }
    
    @Override
    public void doStuffServer(final ChannelHandlerContext ctx) {
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void doStuffClient() {
        if (this.tag == null) {
            return;
        }
        if (!this.tag.hasKey("player_id")) {
            return;
        }
        if (!this.tag.hasKey("n")) {
            return;
        }
        final TileEntity tile = ExtraUtilsMod.proxy.getClientWorld().getTileEntity(this.x, this.y, this.z);
        if (!(tile instanceof TileEntityTradingPost)) {
            return;
        }
        final TileEntityTradingPost t2 = (TileEntityTradingPost)tile;
        final int n = this.tag.getInteger("n");
        t2.ids = new int[n];
        t2.data = new MerchantRecipeList[n];
        for (int i = 0; i < n; ++i) {
            t2.ids[i] = this.tag.getInteger("i" + i);
            t2.data[i] = new MerchantRecipeList(this.tag.getCompoundTag("t" + i));
        }
        ExtraUtilsMod.proxy.getClientPlayer().openGui((Object)ExtraUtilsMod.instance, 0, ExtraUtilsMod.proxy.getClientWorld(), this.x, this.y, this.z);
    }
    
    @Override
    public boolean isValidSenderSide(final Side properSenderSide) {
        return properSenderSide == Side.SERVER;
    }
}


