// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.worldgen;

import java.util.Iterator;
import net.minecraft.world.chunk.Chunk;
import com.rwtema.extrautils.tileentity.TileEntityPortal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraft.world.Teleporter;

public class TeleporterBase extends Teleporter
{
    protected final WorldServer worldServerInstance;
    
    public TeleporterBase(final WorldServer p_i1963_1_) {
        super(p_i1963_1_);
        this.worldServerInstance = p_i1963_1_;
    }
    
    public TileEntity findPortalInChunk(final double x, final double z) {
        final Chunk chunk = this.worldServerInstance.getChunkFromBlockCoords((int)x, (int)z);
        for (final Object tile : chunk.chunkTileEntityMap.values()) {
            if (tile instanceof TileEntityPortal) {
                return (TileEntity)tile;
            }
        }
        return null;
    }
}


