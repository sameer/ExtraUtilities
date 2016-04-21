// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.worldgen.endoftime;

import net.minecraft.world.ChunkPosition;
import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import java.util.Arrays;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderEndOfTime implements IChunkProvider
{
    World worldObj;
    
    public ChunkProviderEndOfTime(final World worldObj, final long seed) {
        this.worldObj = worldObj;
    }
    
    public boolean chunkExists(final int p_73149_1_, final int p_73149_2_) {
        return true;
    }
    
    public Chunk provideChunk(final int x, final int z) {
        final Chunk chunk = new Chunk(this.worldObj, x, z);
        Arrays.fill(chunk.getBiomeArray(), (byte)BiomeGenBase.plains.biomeID);
        chunk.generateSkylightMap();
        chunk.isTerrainPopulated = true;
        chunk.isLightPopulated = true;
        chunk.isModified = true;
        return chunk;
    }
    
    public Chunk loadChunk(final int p_73158_1_, final int p_73158_2_) {
        return this.provideChunk(p_73158_1_, p_73158_2_);
    }
    
    public void populate(final IChunkProvider provider, final int x, final int z) {
    }
    
    public boolean saveChunks(final boolean p_73151_1_, final IProgressUpdate p_73151_2_) {
        return true;
    }
    
    public boolean unloadQueuedChunks() {
        return false;
    }
    
    public boolean canSave() {
        return true;
    }
    
    public String makeString() {
        return "EoTLevelSource";
    }
    
    public List getPossibleCreatures(final EnumCreatureType p_73155_1_, final int p_73155_2_, final int p_73155_3_, final int p_73155_4_) {
        final BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(p_73155_2_, p_73155_4_);
        return biomegenbase.getSpawnableList(p_73155_1_);
    }
    
    public ChunkPosition func_147416_a(final World p_147416_1_, final String p_147416_2_, final int p_147416_3_, final int p_147416_4_, final int p_147416_5_) {
        return null;
    }
    
    public int getLoadedChunkCount() {
        return 0;
    }
    
    public void recreateStructures(final int p_82695_1_, final int p_82695_2_) {
    }
    
    public void saveExtraData() {
    }
}


