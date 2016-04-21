// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.enderconstructor;

import com.rwtema.extrautils.helper.XURandom;
import java.util.Iterator;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import com.rwtema.extrautils.ChunkPos;
import java.util.LinkedHashSet;
import java.util.Random;
import net.minecraft.tileentity.TileEntity;

public class TileEnderPillar extends TileEntity
{
    public static final int transmitLimit = 10;
    public static int range;
    public static Random rand;
    public LinkedHashSet<ChunkPos> targets;
    int coolDown;
    boolean init;
    
    public TileEnderPillar() {
        this.targets = new LinkedHashSet<ChunkPos>();
        this.coolDown = 0;
        this.init = false;
    }
    
    public boolean shouldRefresh(final Block oldID, final Block newID, final int oldMeta, final int newMeta, final World world, final int x, final int y, final int z) {
        return oldID != newID || oldMeta > 0 != newMeta > 0;
    }
    
    public void getNearbyTiles() {
        this.init = true;
        this.targets.clear();
        for (int x = this.xCoord - TileEnderPillar.range >> 4 << 4; x < (this.xCoord + TileEnderPillar.range >> 4 << 4) + 16; x += 16) {
            for (int z = this.zCoord - TileEnderPillar.range >> 4 << 4; z < (this.zCoord + TileEnderPillar.range >> 4 << 4) + 16; z += 16) {
                if (this.worldObj.blockExists(x, 100, z)) {
                    final Chunk chunk = this.worldObj.getChunkFromBlockCoords(x, z);
                    for (final Object obj : chunk.chunkTileEntityMap.values()) {
                        final TileEntity tile = (TileEntity)obj;
                        if (Math.abs(tile.xCoord - this.xCoord) + Math.abs(tile.yCoord - this.yCoord) + Math.abs(tile.zCoord - this.zCoord) < TileEnderPillar.range && tile instanceof IEnderFluxHandler) {
                            this.targets.add(new ChunkPos(tile.xCoord, tile.yCoord, tile.zCoord));
                        }
                    }
                }
            }
        }
    }
    
    public void updateEntity() {
        if (!this.init || this.worldObj.getTotalWorldTime() % 40L == 0L) {
            this.getNearbyTiles();
        }
        boolean sent = false;
        if (this.targets.size() > 0 && ((this.worldObj.isRemote && this.getBlockMetadata() == 3) || !this.worldObj.isRemote)) {
            final Iterator<ChunkPos> iterator = this.targets.iterator();
            while (iterator.hasNext()) {
                final ChunkPos c = iterator.next();
                final TileEntity tile;
                if (this.worldObj.blockExists(c.x, c.y, c.z) && (tile = this.worldObj.getTileEntity(c.x, c.y, c.z)) instanceof IEnderFluxHandler) {
                    if (!((IEnderFluxHandler)tile).isActive()) {
                        continue;
                    }
                    if (this.worldObj.isRemote) {
                        final double f = 0.5;
                        for (int i = 0; i < 1; ++i) {
                            final double dx = c.x + f / 2.0 + (1.0 - f) * TileEnderPillar.rand.nextDouble();
                            final double dy = c.y + f / 2.0 + (1.0 - f) * TileEnderPillar.rand.nextDouble();
                            final double dz = c.z + f / 2.0 + (1.0 - f) * TileEnderPillar.rand.nextDouble();
                            final double dx2 = this.xCoord + f / 2.0 + (1.0 - f) * TileEnderPillar.rand.nextDouble();
                            final double dy2 = this.yCoord + f / 2.0 + (1.0 - f) * TileEnderPillar.rand.nextDouble() - 0.5;
                            final double dz2 = this.zCoord + f / 2.0 + (1.0 - f) * TileEnderPillar.rand.nextDouble();
                            this.worldObj.spawnParticle("portal", dx, dy, dz, dx2 - dx, dy2 - dy, dz2 - dz);
                        }
                    }
                    else {
                        final int a = ((IEnderFluxHandler)tile).recieveEnergy(10, Transfer.PERFORM);
                        if (a <= 0) {
                            continue;
                        }
                        sent = true;
                    }
                }
                else {
                    iterator.remove();
                }
            }
        }
        if (!this.worldObj.isRemote) {
            if (sent) {
                this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 3, 2);
                this.coolDown = 20;
            }
            if (this.coolDown > 0) {
                --this.coolDown;
                if (this.coolDown == 0) {
                    this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 2, 2);
                }
            }
        }
    }
    
    static {
        TileEnderPillar.range = 10;
        TileEnderPillar.rand = XURandom.getInstance();
    }
}

