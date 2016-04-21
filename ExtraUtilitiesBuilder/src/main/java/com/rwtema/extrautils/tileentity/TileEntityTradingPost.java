// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity;

import com.rwtema.extrautils.network.packets.PacketVillager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import com.rwtema.extrautils.network.XUPacketBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.entity.EntityLiving;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.entity.IMerchant;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTradingPost extends TileEntity
{
    public static int maxRange;
    public int[] ids;
    public MerchantRecipeList[] data;
    
    public TileEntityTradingPost() {
        this.ids = null;
        this.data = null;
    }
    
    public boolean canUpdate() {
        return false;
    }
    
    public AxisAlignedBB getAABB() {
        return AxisAlignedBB.getBoundingBox(this.xCoord + 0.5 - TileEntityTradingPost.maxRange, 0.0, this.zCoord + 0.5 - TileEntityTradingPost.maxRange, this.xCoord + 0.5 + TileEntityTradingPost.maxRange, 255.0, this.zCoord + 0.5 + TileEntityTradingPost.maxRange);
    }
    
    public List<IMerchant> getVillagers() {
        final List t = this.worldObj.getEntitiesWithinAABB((Class)IMerchant.class, this.getAABB());
        final List<IMerchant> traders = new ArrayList<IMerchant>();
        for (final Object aT : t) {
            if (this.isValidVillager((IMerchant)aT, true)) {
                traders.add((IMerchant)aT);
            }
        }
        return traders;
    }
    
    public boolean isValidVillager(final IMerchant villager, final boolean locationAlreadyChecked) {
        return villager instanceof EntityLiving && !((EntityLiving)villager).isChild() && (locationAlreadyChecked || this.getAABB().isVecInside(Vec3.createVectorHelper(((EntityLiving)villager).posX, ((EntityLiving)villager).posY, ((EntityLiving)villager).posZ)));
    }
    
    public XUPacketBase getTradePacket(final EntityPlayer player) {
        final List<IMerchant> traders = this.getVillagers();
        if (traders == null || traders.size() == 0) {
            return null;
        }
        final NBTTagCompound pkt = new NBTTagCompound();
        int n = 0;
        pkt.setInteger("player_id", player.getEntityId());
        for (int i = 0; i < traders.size(); ++i) {
            final IMerchant v = traders.get(i);
            pkt.setInteger("i" + i, ((EntityLiving)v).getEntityId());
            pkt.setTag("t" + i, (NBTBase)v.getRecipes((EntityPlayer)null).getRecipiesAsTags());
            ++n;
        }
        if (n == 0) {
            return null;
        }
        pkt.setInteger("n", n);
        return new PacketVillager(this.xCoord, this.yCoord, this.zCoord, pkt);
    }
    
    public double distSq(double x, double y, double z) {
        x -= this.xCoord + 0.5;
        y -= this.yCoord + 0.5;
        z -= this.zCoord + 0.5;
        return x * x + y * y + z * z;
    }
    
    public int toInt(final Object x) {
        if (x instanceof Double) {
            return (int)Math.floor((Double)x);
        }
        if (x instanceof Float) {
            return (int)Math.floor((Float)x);
        }
        if (x instanceof Integer) {
            return (Integer)x;
        }
        if (x instanceof String) {
            return Integer.parseInt((String)x);
        }
        return 0;
    }
    
    static {
        TileEntityTradingPost.maxRange = 32;
    }
}


