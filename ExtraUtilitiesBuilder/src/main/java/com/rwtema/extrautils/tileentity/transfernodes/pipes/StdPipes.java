// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import net.minecraftforge.common.util.ForgeDirection;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import net.minecraft.world.IBlockAccess;

public class StdPipes
{
    public static IPipe[] pipe;
    
    public static IPipe getPipeType(final int type) {
        if (type < 0 || type >= StdPipes.pipe.length) {
            return null;
        }
        return StdPipes.pipe[type];
    }
    
    public static int getNextPipeType(final IBlockAccess world, final int x, final int y, final int z, int metadata) {
        if ((metadata >= 8 && metadata < 15) || metadata > 15) {
            return metadata;
        }
        if (metadata == 7) {
            return 0;
        }
        if (metadata == 15) {
            return 7;
        }
        if (metadata == 6) {
            return 15;
        }
        metadata = (metadata + 1) % 8;
        final IPipe pipe = TNHelper.getPipe(world, x, y, z);
        if (pipe == null) {
            return metadata;
        }
        int numNeighbors = 0;
        for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (TNHelper.canInput(world, x, y, z, dir) && TNHelper.canInput(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite())) {
                ++numNeighbors;
            }
        }
        if (numNeighbors <= 1) {
            if (metadata >= 1 && metadata <= 6) {
                return 15;
            }
        }
        else {
            for (ForgeDirection dir2 = ForgeDirection.getOrientation(metadata - 1); metadata >= 1 && metadata <= 6 && (!TNHelper.canInput(world, x, y, z, dir2) || !TNHelper.canInput(world, x + dir2.offsetX, y + dir2.offsetY, z + dir2.offsetZ, dir2.getOpposite())); dir2 = ForgeDirection.getOrientation(++metadata - 1)) {}
            if (metadata == 7) {
                return 15;
            }
        }
        return metadata;
    }
    
    static {
        (StdPipes.pipe = new IPipe[17])[0] = new PipeStandard();
        for (int i = 0; i < 6; ++i) {
            StdPipes.pipe[i + 1] = new PipeDirectional(ForgeDirection.getOrientation(i));
        }
        StdPipes.pipe[7] = new PipeNonInserting();
        StdPipes.pipe[8] = new PipeSorting();
        StdPipes.pipe[9] = new PipeFilter();
        StdPipes.pipe[10] = new PipeRationing();
        StdPipes.pipe[11] = new PipeEnergy();
        StdPipes.pipe[12] = new PipeCrossOver();
        StdPipes.pipe[13] = new PipeModSorting();
        StdPipes.pipe[14] = new PipeEnergyExtract();
        StdPipes.pipe[15] = new PipeEOF();
        StdPipes.pipe[16] = new PipeHyperRationing();
    }
}


