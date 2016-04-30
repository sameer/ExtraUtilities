// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.worldgen.Underdark;

import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenBigHole extends WorldGenerator
{
    public boolean generate(final World par1World, final Random par2Random, final int par3, final int par4, final int par5) {
        if (par2Random.nextInt(96) == 0) {
            final int r = 4 + par2Random.nextInt(6);
            final int x = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
            final int z = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);
            for (int y = 0; y < 82 + r; ++y) {
                for (int dx = -r; dx <= r; ++dx) {
                    for (int dz = -r; dz <= r; ++dz) {
                        int ey = y - 82;
                        if (ey < 0) {
                            ey = 0;
                        }
                        if (par2Random.nextInt(1 + ey * ey + dx * dx + dz * dz) < 1 + (r - 1) * (r - 1) / 2) {
                            par1World.setBlock(x + dx, y, z + dz, Blocks.air, 0, 2);
                        }
                    }
                }
            }
        }
        return true;
    }
}


