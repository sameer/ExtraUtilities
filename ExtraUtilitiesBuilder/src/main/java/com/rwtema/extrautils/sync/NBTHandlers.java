// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.sync;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class NBTHandlers
{
    public static class NBTHandlerByte
    {
        public static void save(final NBTTagCompound tag, final String fieldName, final byte b) {
            tag.setByte(fieldName, b);
        }
        
        public static byte load(final NBTTagCompound tag, final String fieldName) {
            return tag.getByte(fieldName);
        }
    }
    
    public static class NBTHandlerShort
    {
        public static void save(final NBTTagCompound tag, final String fieldName, final short s) {
            tag.setShort(fieldName, s);
        }
        
        public static short load(final NBTTagCompound tag, final String fieldName) {
            return tag.getShort(fieldName);
        }
    }
    
    public static class NBTHandlerInt
    {
        public static void save(final NBTTagCompound tag, final String fieldName, final int s) {
            tag.setInteger(fieldName, s);
        }
        
        public static int load(final NBTTagCompound tag, final String fieldName) {
            return tag.getInteger(fieldName);
        }
    }
    
    public static class NBTHandlerLong
    {
        public static void save(final NBTTagCompound tag, final String fieldName, final long s) {
            tag.setLong(fieldName, s);
        }
        
        public static long load(final NBTTagCompound tag, final String fieldName) {
            return tag.getLong(fieldName);
        }
    }
    
    public static class NBTHandlerFloat
    {
        public static void save(final NBTTagCompound tag, final String fieldName, final float s) {
            tag.setFloat(fieldName, s);
        }
        
        public static float load(final NBTTagCompound tag, final String fieldName) {
            return tag.getFloat(fieldName);
        }
    }
    
    public static class NBTHandlerDouble
    {
        public static void save(final NBTTagCompound tag, final String fieldName, final double s) {
            tag.setDouble(fieldName, s);
        }
        
        public static double load(final NBTTagCompound tag, final String fieldName) {
            return tag.getDouble(fieldName);
        }
    }
    
    public static class NBTHandlerByteArray
    {
        public static void save(final NBTTagCompound tag, final String fieldName, final byte[] s) {
            tag.setByteArray(fieldName, s);
        }
        
        public static byte[] load(final NBTTagCompound tag, final String fieldName) {
            return tag.getByteArray(fieldName);
        }
    }
    
    public static class NBTHandlerString
    {
        public static void save(final NBTTagCompound tag, final String fieldName, final String s) {
            tag.setString(fieldName, s);
        }
        
        public static String load(final NBTTagCompound tag, final String fieldName) {
            return tag.getString(fieldName);
        }
    }
    
    public static class NBTHandlerNBT
    {
        public static void save(final NBTTagCompound tag, final String fieldName, final NBTTagCompound s) {
            tag.setTag(fieldName, (NBTBase)s);
        }
        
        public static NBTTagCompound load(final NBTTagCompound tag, final String fieldName) {
            return tag.getCompoundTag(fieldName);
        }
    }
    
    public static class NBTHandlerIntArray
    {
        public static void save(final NBTTagCompound tag, final String fieldName, final int[] s) {
            tag.setIntArray(fieldName, s);
        }
        
        public static int[] load(final NBTTagCompound tag, final String fieldName) {
            return tag.getIntArray(fieldName);
        }
    }
    
    public static class NBTHandlerItemStack
    {
        public static void save(final NBTTagCompound tag, final String fieldName, final ItemStack s) {
            if (s != null) {
                tag.setTag(fieldName, (NBTBase)s.writeToNBT(new NBTTagCompound()));
            }
        }
        
        public static ItemStack load(final NBTTagCompound tag, final String fieldName) {
            return ItemStack.loadItemStackFromNBT(tag.getCompoundTag(fieldName));
        }
    }
    
    public static class NBTHandlerBoolean
    {
        public static void save(final NBTTagCompound tag, final String fieldName, final boolean s) {
            tag.setBoolean(fieldName, s);
        }
        
        public static boolean load(final NBTTagCompound tag, final String fieldName) {
            return tag.getBoolean(fieldName);
        }
    }
}


