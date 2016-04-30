// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.util.Facing;
import com.rwtema.extrautils.block.BlockBUD;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBUD extends TileEntity
{
    public static final int maxCountDown = 8;
    public int countDown;
    public boolean powered;
    int[] metadata;
    int[] hashCode;
    boolean init;
    private Block[] ids;
    private boolean ready;
    
    public TileEntityBUD() {
        this.countDown = 0;
        this.powered = false;
        this.metadata = new int[6];
        this.hashCode = new int[6];
        this.init = false;
        this.ids = new Block[6];
    }
    
    public boolean shouldRefresh(final Block oldBlock, final Block newBlock, final int oldMeta, final int newMeta, final World world, final int x, final int y, final int z) {
        return oldBlock != newBlock || oldMeta >= 3 != newMeta >= 3;
    }
    
    public Packet getDescriptionPacket() {
        this.powered = (this.countDown >= 6);
        final NBTTagCompound t = new NBTTagCompound();
        t.setBoolean("powered", this.powered);
        return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 4, t);
    }
    
    @SideOnly(Side.CLIENT)
    public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
        if (this.worldObj.isRemote) {
            if (pkt.func_148857_g().hasKey("powered")) {
                this.powered = pkt.func_148857_g().getBoolean("powered");
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
        }
    }
    
    public void updateEntity() {
        if (this.worldObj.isRemote) {
            return;
        }
        this.powered = (this.countDown >= 7);
        if (this.countDown > 0) {
            --this.countDown;
            if (this.countDown == 0 || this.countDown == 7 || this.countDown == 5) {
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                ((BlockBUD)this.getBlockType()).updateRedstone(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
            }
        }
        this.ready = (this.countDown == 0);
        if (!this.init) {
            this.init = true;
            this.ready = false;
        }
        for (int i = 0; i < 6; ++i) {
            this.checkDir(i);
        }
    }
    
    public void checkDir(final int i) {
        final int dx = this.xCoord + Facing.offsetsXForSide[i];
        final int dy = this.yCoord + Facing.offsetsYForSide[i];
        final int dz = this.zCoord + Facing.offsetsZForSide[i];
        if (this.worldObj.blockExists(dx, dy, dz)) {
            final Block id = this.worldObj.getBlock(dx, dy, dz);
            if (id != this.getBlockType()) {
                if (this.worldObj.isAirBlock(dx, dy, dz)) {
                    if (this.ids[i] != Blocks.air) {
                        if (this.ready) {
                            this.countDown = 8;
                        }
                        this.ids[i] = Blocks.air;
                        this.metadata[i] = 0;
                        this.hashCode[i] = 0;
                    }
                }
                else {
                    boolean idChange = false;
                    if (id != this.ids[i]) {
                        if (this.ready) {
                            this.countDown = 8;
                        }
                        idChange = true;
                        this.ids[i] = id;
                    }
                    final int md = this.worldObj.getBlockMetadata(dx, dy, dz);
                    if (md != this.metadata[i]) {
                        if (this.ready) {
                            this.countDown = 8;
                        }
                        idChange = true;
                        this.metadata[i] = md;
                    }
                    final TileEntity tile = this.worldObj.getTileEntity(dx, dy, dz);
                    if (tile != null) {
                        final NBTTagCompound t = new NBTTagCompound();
                        tile.writeToNBT(t);
                        final int h = t.hashCode();
                        if (h != this.hashCode[i]) {
                            if (this.ready || !idChange) {
                                this.countDown = 8;
                            }
                            this.hashCode[i] = h;
                        }
                    }
                }
            }
        }
    }
    
    public boolean getPowered() {
        return this.worldObj.isRemote ? this.powered : (this.countDown >= 6);
    }
}


