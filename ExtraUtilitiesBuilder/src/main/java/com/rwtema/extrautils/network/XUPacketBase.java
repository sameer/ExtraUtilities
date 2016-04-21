// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.network;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTSizeTracker;
import java.io.IOException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import java.nio.charset.Charset;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import io.netty.buffer.ByteBuf;

public abstract class XUPacketBase
{
    public abstract void writeData(final ByteBuf p0) throws Exception;
    
    public void writeVec(final ByteBuf data, final Vec3 vec3) {
        data.writeFloat((float)vec3.xCoord);
        data.writeFloat((float)vec3.yCoord);
        data.writeFloat((float)vec3.zCoord);
    }
    
    public void writeChatComponent(final ByteBuf data, final IChatComponent chatComponent) {
        this.writeString(data, IChatComponent.Serializer.func_150696_a(chatComponent));
    }
    
    public IChatComponent readChatComponent(final ByteBuf data) {
        return IChatComponent.Serializer.func_150699_a(this.readString(data));
    }
    
    public abstract void readData(final EntityPlayer p0, final ByteBuf p1);
    
    public Vec3 readVec(final ByteBuf data) {
        return Vec3.createVectorHelper((double)data.readFloat(), (double)data.readFloat(), (double)data.readFloat());
    }
    
    public abstract void doStuffServer(final ChannelHandlerContext p0);
    
    @SideOnly(Side.CLIENT)
    public abstract void doStuffClient();
    
    public abstract boolean isValidSenderSide(final Side p0);
    
    public void writeString(final ByteBuf data, final String string) {
        final byte[] stringData = string.getBytes(Charset.forName("UTF-8"));
        data.writeShort(stringData.length);
        data.writeBytes(stringData);
    }
    
    public String readString(final ByteBuf data) {
        final short length = data.readShort();
        final byte[] bytes = new byte[length];
        data.readBytes(bytes);
        return new String(bytes, Charset.forName("UTF-8"));
    }
    
    public void writeNBT(final ByteBuf data, final NBTTagCompound tag) {
        if (tag == null) {
            data.writeShort(-1);
            return;
        }
        try {
            final byte[] compressed = CompressedStreamTools.compress(tag);
            data.writeShort(compressed.length);
            data.writeBytes(compressed);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public NBTTagCompound readNBT(final ByteBuf data) {
        final short length = data.readShort();
        if (length <= 0) {
            return null;
        }
        final byte[] bytes = new byte[length];
        data.readBytes(bytes);
        try {
            return CompressedStreamTools.func_152457_a(bytes, NBTSizeTracker.field_152451_a);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void writeItemStack(final ByteBuf data, final ItemStack item) {
        if (item == null) {
            data.writeShort(-1);
        }
        else {
            data.writeShort(Item.getIdFromItem(item.getItem()));
            data.writeByte(item.stackSize);
            data.writeShort(item.getItemDamage());
            NBTTagCompound nbttagcompound = null;
            if (item.getItem().isDamageable() || item.getItem().getShareTag()) {
                nbttagcompound = item.stackTagCompound;
            }
            this.writeNBT(data, nbttagcompound);
        }
    }
    
    public ItemStack readItemStack(final ByteBuf data) {
        ItemStack itemstack = null;
        final short id = data.readShort();
        if (id >= 0) {
            final byte stackSize = data.readByte();
            final short metadata = data.readShort();
            itemstack = new ItemStack(Item.getItemById((int)id), (int)stackSize, (int)metadata);
            itemstack.stackTagCompound = this.readNBT(data);
        }
        return itemstack;
    }
}

