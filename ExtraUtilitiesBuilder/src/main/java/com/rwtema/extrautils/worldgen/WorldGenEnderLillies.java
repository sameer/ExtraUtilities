// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.worldgen;

import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.World;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.world.chunk.Chunk;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenEnderLillies implements IWorldGenerator
{
    public static String retrogen;
    
    public WorldGenEnderLillies() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    public static boolean isAirBlock(final Block id) {
        return id == Blocks.air || id == null;
    }
    
    public static void gen(final Random random, final Chunk chunk) {
        for (int i = 0; i < 32; ++i) {
            final int x = random.nextInt(16);
            final int y = 10 + random.nextInt(65);
            final int z = random.nextInt(16);
            if (chunk.getBlock(x, y, z) == Blocks.end_stone && isAirBlock(chunk.getBlock(x, y + 1, z))) {
                chunk.func_150807_a(x, y + 1, z, (Block)ExtraUtils.enderLily, 7);
                if (random.nextDouble() < 0.2) {
                    return;
                }
            }
        }
    }
    
    public void generate(final Random random, final int chunkX, final int chunkZ, final World world, final IChunkProvider chunkGenerator, final IChunkProvider chunkProvider) {
        if (world.provider.dimensionId == 1) {
            gen(random, world.getChunkFromChunkCoords(chunkX, chunkZ));
        }
    }
    
    public void saveData(final ChunkDataEvent.Save event) {
        if (event.world.provider.dimensionId == 1) {
            event.getData().setInteger(WorldGenEnderLillies.retrogen, ExtraUtils.enderLilyRetrogenId);
        }
    }
    
    public void loadData(final ChunkDataEvent.Load event) {
        if (ExtraUtils.enderLilyRetrogenId > 0 && event.world.provider.dimensionId == 1 && event.world instanceof WorldServer && event.getData().getInteger(WorldGenEnderLillies.retrogen) != ExtraUtils.enderLilyRetrogenId) {
            final long worldSeed = event.world.getSeed();
            final Random random = new Random(worldSeed);
            final long xSeed = random.nextLong() >> 3;
            final long zSeed = random.nextLong() >> 3;
            final long chunkSeed = xSeed * event.getChunk().xPosition + zSeed * event.getChunk().zPosition ^ worldSeed;
            random.setSeed(chunkSeed);
            gen(random, event.getChunk());
        }
    }
    
    static {
        WorldGenEnderLillies.retrogen = "XU:EnderLilyRetrogen";
    }
}
