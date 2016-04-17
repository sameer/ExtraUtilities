// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils;

import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockLeaves;
import com.rwtema.extrautils.block.BlockCursedEarth;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class ActivationRitual
{
    public static int max_range;
    public static int num_light;
    public static int required_dirt;
    public static int time_window;
    
    public static boolean redstoneCirclePresent(final World world, final int x, final int y, final int z) {
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dz = -1; dz <= 1; ++dz) {
                if ((dx != 0 | dz != 0) && world.getBlock(x + dx, y, z + dz) != Blocks.redstone_wire) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean altarInDarkness_Client(final World world, final int x, final int y, final int z) {
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dz = -1; dz <= 1; ++dz) {
                if (world.getSkyBlockTypeBrightness(EnumSkyBlock.Block, x + dx, y, z + dz) + world.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, x + dx, y, z + dz) - world.calculateSkylightSubtracted(1.0f) > 9) {
                    return false;
                }
            }
        }
        return world.getSkyBlockTypeBrightness(EnumSkyBlock.Block, x, y + 1, z) + world.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, x, y + 1, z) - world.calculateSkylightSubtracted(1.0f) <= 9;
    }
    
    public static boolean altarInDarkness(final World world, final int x, final int y, final int z) {
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dz = -1; dz <= 1; ++dz) {
                if (world.getBlockLightValue(x + dx, y, z + dz) > 9) {
                    return false;
                }
            }
        }
        return world.getBlockLightValue(x, y + 1, z) <= 9;
    }
    
    public static boolean altarCanSeeMoon(final World world, final int x, final int y, final int z) {
        return world.canBlockSeeTheSky(x, y, z);
    }
    
    public static boolean altarOnEarth(final World world, final int x, final int y, final int z) {
        final boolean hasDirt = false;
        for (int dx = -1; dx <= 1 && !hasDirt; ++dx) {
            for (int dz = -1; dz <= 1 && !hasDirt; ++dz) {
                if (world.getBlock(x + dx, y - 1, z + dz) != Blocks.dirt && world.getBlock(x + dx, y - 1, z + dz) != Blocks.grass) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static int checkTime(long time) {
        time %= 24000L;
        if (time < 18000 - ActivationRitual.time_window) {
            return -1;
        }
        if (time > 18000 + ActivationRitual.time_window) {
            return 1;
        }
        return 0;
    }
    
    public static boolean naturalEarth(final World world, final int x, final int y, final int z) {
        int num_dirt = 0;
        for (int dx = -ActivationRitual.max_range; dx <= ActivationRitual.max_range; ++dx) {
            for (int dz = -ActivationRitual.max_range; dz <= ActivationRitual.max_range; ++dz) {
                if (dx * dx + dz * dz < ActivationRitual.max_range * ActivationRitual.max_range) {
                    for (int dy = Math.min(3 + world.getTopSolidOrLiquidBlock(x + dx, z + dz) - y, ActivationRitual.max_range); dy >= -ActivationRitual.max_range; --dy) {
                        if (dx * dx + dy * dy + dz * dz <= ActivationRitual.max_range * ActivationRitual.max_range) {
                            final Block id = world.getBlock(x + dx, y + dy, z + dz);
                            if (id == Blocks.dirt || id == Blocks.grass) {
                                if (canShift(world.getBlock(x + dx, y + dy + 1, z + dz)) && ++num_dirt > ActivationRitual.required_dirt) {
                                    return true;
                                }
                                break;
                            }
                            else if (id.isOpaqueCube()) {
                                break;
                            }
                        }
                        else if (dy < 0) {
                            break;
                        }
                    }
                }
                else if (dz > 0) {
                    break;
                }
            }
        }
        return false;
    }
    
    public static boolean canShift(final Block id) {
        return id == Blocks.air || (id != null && id.getMaterial() != Material.water && id.getMobilityFlag() == 1);
    }
    
    public static void startRitual(final World world, final int x, final int y, final int z, final EntityPlayer player) {
        world.addWeatherEffect((Entity)new EntityLightningBolt(world, (double)x, (double)y, (double)z));
        if (ExtraUtils.cursedEarth != null) {
            BlockCursedEarth.powered = 16;
            for (int dx = -ActivationRitual.max_range; dx <= ActivationRitual.max_range; ++dx) {
                for (int dz = -ActivationRitual.max_range; dz <= ActivationRitual.max_range; ++dz) {
                    if (dx * dx + dz * dz < ActivationRitual.max_range * ActivationRitual.max_range) {
                        for (int dy = ActivationRitual.max_range; dy > -ActivationRitual.max_range; --dy) {
                            if (dx * dx + dy * dy + dz * dz <= ActivationRitual.max_range * ActivationRitual.max_range) {
                                final Block id = world.getBlock(x + dx, y + dy, z + dz);
                                if (id != Blocks.air) {
                                    if (id == Blocks.dirt || id == Blocks.grass) {
                                        world.setBlock(x + dx, y + dy, z + dz, ExtraUtils.cursedEarth, 0, 3);
                                        break;
                                    }
                                    if (id instanceof BlockLeaves) {
                                        id.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                                        world.setBlock(x + dx, y + dy, z + dz, Blocks.air);
                                    }
                                    else if (id instanceof BlockSnow) {
                                        world.setBlock(x + dx, y + dy, z + dz, Blocks.air);
                                    }
                                    else if (id.getMobilityFlag() == 1 && id != Blocks.redstone_wire) {
                                        world.func_147480_a(x + dx, y + dy, z + dz, true);
                                    }
                                    else if (id.isOpaqueCube()) {
                                        break;
                                    }
                                }
                            }
                            else if (dy < 0) {
                                break;
                            }
                        }
                    }
                    else if (dz > 0) {
                        break;
                    }
                }
            }
            BlockCursedEarth.powered = 0;
        }
    }
    
    static {
        ActivationRitual.max_range = 8;
        ActivationRitual.num_light = 0;
        ActivationRitual.required_dirt = 20;
        ActivationRitual.time_window = 500;
    }
}
