// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import io.netty.buffer.ByteBuf;
import java.lang.reflect.Field;

public abstract class XUAutoPacket extends XUPacketBase
{
    boolean init;
    
    public XUAutoPacket() {
        this.init = false;
    }
    
    public void getReflectData() {
        this.getClass().getDeclaredFields();
    }
    
    public void writeField(final Field f, final ByteBuf data) throws IllegalAccessException {
        final Class type = f.getType();
        if (String.class.equals(type)) {
            this.writeString(data, (String)f.get(this));
        }
        else if (Byte.TYPE.equals(type)) {
            data.writeByte((int)(byte)f.get(this));
        }
        else if (Short.TYPE.equals(type)) {
            data.writeShort((int)(short)f.get(this));
        }
        else if (Integer.TYPE.equals(type)) {
            data.writeInt((int)f.get(this));
        }
        else if (Long.TYPE.equals(type)) {
            data.writeDouble((double)(long)f.get(this));
        }
        else if (Float.TYPE.equals(type)) {
            data.writeFloat((float)f.get(this));
        }
        else if (Double.TYPE.equals(type)) {
            data.writeDouble((double)f.get(this));
        }
        else if (NBTTagCompound.class.equals(type)) {
            this.writeNBT(data, (NBTTagCompound)f.get(this));
        }
    }
    
    public void readField(final Field f, final ByteBuf data) throws IllegalAccessException {
        final Class type = f.getType();
        if (String.class.equals(type)) {
            f.set(this, this.readString(data));
        }
        else if (Byte.TYPE.equals(type)) {
            f.set(this, data.readByte());
        }
        else if (Short.TYPE.equals(type)) {
            f.set(this, data.readShort());
        }
        else if (Integer.TYPE.equals(type)) {
            f.set(this, data.readInt());
        }
        else if (Long.TYPE.equals(type)) {
            f.set(this, data.readLong());
        }
        else if (Float.TYPE.equals(type)) {
            f.set(this, data.readFloat());
        }
        else if (Double.TYPE.equals(type)) {
            f.set(this, data.readDouble());
        }
        else if (NBTTagCompound.class.equals(type)) {
            f.set(this, this.readNBT(data));
        }
    }
    
    @Override
    public void writeData(final ByteBuf data) throws Exception {
    }
    
    @Override
    public void readData(final EntityPlayer player, final ByteBuf data) {
    }
}
