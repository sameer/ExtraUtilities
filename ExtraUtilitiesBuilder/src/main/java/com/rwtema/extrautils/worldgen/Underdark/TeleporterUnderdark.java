// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.worldgen.Underdark;

import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import java.util.Iterator;
import net.minecraft.world.chunk.Chunk;
import com.rwtema.extrautils.tileentity.TileEntityPortal;
import net.minecraft.tileentity.TileEntity;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import net.minecraft.world.Teleporter;

public class TeleporterUnderdark extends Teleporter
{
    private final WorldServer worldServerInstance;
    
    public TeleporterUnderdark(final WorldServer par1WorldServer) {
        super(par1WorldServer);
        this.worldServerInstance = par1WorldServer;
    }
    
    public void placeInPortal(final Entity entity, final double x, double y, final double z, final float r) {
        if (!this.placeInExistingPortal(entity, x, y, z, r)) {
            if (this.worldServerInstance.provider.dimensionId != ExtraUtils.underdarkDimID) {
                y = this.worldServerInstance.getTopSolidOrLiquidBlock((int)x, (int)z);
                entity.setLocationAndAngles(x, y, z, entity.rotationYaw, 0.0f);
            }
            else {
                this.makePortal(entity);
            }
        }
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
    
    public boolean placeInExistingPortal(final Entity entity, final double x, final double y, final double z, final float r) {
        TileEntity destPortal = null;
        for (int s = 0; s <= 5 && destPortal == null; ++s) {
            for (int dx = -s; dx <= s; ++dx) {
                for (int dz = -s; dz <= s; ++dz) {
                    if (destPortal == null) {
                        destPortal = this.findPortalInChunk(x + dx * 16, z + dz * 16);
                    }
                }
            }
        }
        if (destPortal != null) {
            entity.setLocationAndAngles(destPortal.xCoord + 0.5, (double)(destPortal.yCoord + 1), destPortal.zCoord + 0.5, entity.rotationYaw, entity.rotationPitch);
            final double motionX = 0.0;
            entity.motionZ = motionX;
            entity.motionY = motionX;
            entity.motionX = motionX;
            return true;
        }
        return false;
    }
    
    public boolean makePortal(final Entity entity) {
        final int ex = MathHelper.floor_double(entity.posX);
        int ey = MathHelper.floor_double(entity.posY) - 1;
        final int ez = MathHelper.floor_double(entity.posZ);
        ey /= 5;
        ey += 139;
        if (ey > 247) {
            ey = 247;
        }
        for (int x = -3; x <= 3; ++x) {
            for (int z = -3; z <= 3; ++z) {
                for (int y = -2; y <= 4; ++y) {
                    if (x == 0 && y == -1 && z == 0) {
                        this.worldServerInstance.setBlock(ex + x, ey + y, ez + z, ExtraUtils.portal, 1, 2);
                        this.worldServerInstance.scheduleBlockUpdate(ex + x, ey + y, ez + z, ExtraUtils.portal, 1);
                    }
                    else if (x == -3 || x == 3 || y <= -1 || y == 4 || z == -3 || z == 3) {
                        this.worldServerInstance.setBlock(ex + x, ey + y, ez + z, Blocks.cobblestone);
                    }
                    else if (y == 0 && (x == 2 || x == -2 || z == 2 || z == -2)) {
                        this.worldServerInstance.setBlock(ex + x, ey + y, ez + z, Blocks.torch, 5, 3);
                    }
                    else {
                        this.worldServerInstance.setBlock(ex + x, ey + y, ez + z, Blocks.air);
                    }
                }
            }
        }
        entity.setLocationAndAngles(ex + 0.5, (double)ey, ez + 0.5, entity.rotationYaw, 0.0f);
        final double motionX = 0.0;
        entity.motionZ = motionX;
        entity.motionY = motionX;
        entity.motionX = motionX;
        return true;
    }
    
    public void removeStalePortalLocations(final long par1) {
    }
}


