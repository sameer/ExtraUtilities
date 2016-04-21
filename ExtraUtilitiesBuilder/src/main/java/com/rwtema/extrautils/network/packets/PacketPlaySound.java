// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.network.packets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.ISound;
import com.rwtema.extrautils.sounds.Sounds;
import net.minecraft.client.audio.PositionedSoundRecord;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;
import com.rwtema.extrautils.network.XUPacketBase;

public class PacketPlaySound extends XUPacketBase
{
    public short sound_id;
    public float x;
    public float y;
    public float z;
    ResourceLocation[] sounds;
    
    public PacketPlaySound() {
        this.sounds = new ResourceLocation[] { new ResourceLocation("extrautils", "hostile.creepy_laugh") };
        this.sound_id = -1;
    }
    
    public PacketPlaySound(final short sound_id, final float x, final float y, final float z) {
        this.sounds = new ResourceLocation[] { new ResourceLocation("extrautils", "hostile.creepy_laugh") };
        this.sound_id = sound_id;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public void writeData(final ByteBuf data) throws Exception {
        data.writeByte((int)this.sound_id);
        data.writeFloat(this.x);
        data.writeFloat(this.y);
        data.writeFloat(this.z);
    }
    
    @Override
    public void readData(final EntityPlayer player, final ByteBuf data) {
        this.sound_id = data.readUnsignedByte();
        this.x = data.readFloat();
        this.y = data.readFloat();
        this.z = data.readFloat();
    }
    
    @Override
    public void doStuffServer(final ChannelHandlerContext ctx) {
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void doStuffClient() {
        if (this.sound_id < 0 || this.sound_id >= this.sounds.length) {
            return;
        }
        Sounds.tryAddSound((ISound)PositionedSoundRecord.func_147675_a(this.sounds[this.sound_id], this.x, this.y, this.z));
    }
    
    @Override
    public boolean isValidSenderSide(final Side properSenderSide) {
        return properSenderSide == Side.SERVER;
    }
}


