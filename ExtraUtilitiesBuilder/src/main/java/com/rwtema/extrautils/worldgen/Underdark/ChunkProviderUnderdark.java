// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.worldgen.Underdark;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.chunk.Chunk;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.MapGenCaves;
import java.util.Random;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;

public class ChunkProviderUnderdark extends ChunkProviderFlat implements IChunkProvider
{
    public static final int cavern_height = 55;
    public static boolean denyDecor;
    Random r;
    private Random rand;
    private MapGenCaves caveGenerator;
    private MapGenMineshaft mineshaftGenerator;
    private MapGenRavine ravineGenerator;
    private WorldGenBigHole holes_gen;
    private WorldGenCastle castle_gen;
    private WorldGenBedrockTree tree_gen;
    private World worldObj;
    private Block[] base_array;
    
    public ChunkProviderUnderdark(final World par1World, final long par2, final boolean par4) {
        super(par1World, par2, par4, "2;7,80x1,4,69x0,100x1,7;3;stronghold,dungeon,mineshaft,decoration");
        this.r = new Random();
        this.caveGenerator = new MapGenCaves();
        this.mineshaftGenerator = new MapGenMineshaft();
        this.ravineGenerator = new MapGenRavine();
        this.holes_gen = new WorldGenBigHole();
        this.castle_gen = new WorldGenCastle();
        this.tree_gen = new WorldGenBedrockTree();
        this.base_array = new Block[65536];
        this.worldObj = par1World;
        this.rand = new Random(par2);
        for (int i = 0; i < this.base_array.length; ++i) {
            this.base_array[i] = Blocks.air;
        }
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y <= 83; ++y) {
                    this.base_array[(x * 16 + z) * 128 + y] = Blocks.stone;
                }
            }
        }
    }
    
    public void populate(final IChunkProvider par1IChunkProvider, final int par2, final int par3) {
        super.populate(par1IChunkProvider, par2, par3);
        final int k = par2 * 16;
        final int l = par3 * 16;
        this.mineshaftGenerator.generateStructuresInChunk(this.worldObj, this.r, par2, par3);
        ChunkProviderUnderdark.denyDecor = true;
        this.worldObj.getBiomeGenForCoords(k, l).decorate(this.worldObj, this.rand, k, l);
        GameRegistry.generateWorld(par2, par3, this.worldObj, (IChunkProvider)this, par1IChunkProvider);
        ChunkProviderUnderdark.denyDecor = false;
        this.holes_gen.generate(this.worldObj, this.rand, k + 8, 0, l + 8);
        this.castle_gen.generate(this.worldObj, this.rand, k + 8, 82, l + 8);
        this.tree_gen.generate(this.worldObj, this.rand, k + 8, 82, l + 8);
    }
    
    public Chunk provideChunk(final int par1, final int par2) {
        final Chunk chunk = new Chunk(this.worldObj, par1, par2);
        final Block[] arr = this.base_array.clone();
        this.caveGenerator.func_151539_a((IChunkProvider)this, this.worldObj, par1, par2, arr);
        this.ravineGenerator.func_151539_a((IChunkProvider)this, this.worldObj, par1, par2, arr);
        this.r.setSeed(this.worldObj.getSeed() + (par1 >> 2) * 65535 + (par2 >> 2));
        final int spire_x = (par1 >> 2 << 2) * 16 + 6 + this.r.nextInt(52) - par1 * 16;
        final int spire_z = (par2 >> 2 << 2) * 16 + 6 + this.r.nextInt(52) - par2 * 16;
        this.r.setSeed(this.worldObj.getSeed() + par1 * 65535 + par2);
        final Block stone_type = Blocks.stone;
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                boolean stalegtites = false;
                boolean stalegmites = false;
                final float rs = (spire_x - x) * (spire_x - x) + (spire_z - z) * (spire_z - z);
                for (int y = 0; y < 255; ++y) {
                    final int l = y >> 4;
                    ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[l];
                    if (extendedblockstorage == null) {
                        extendedblockstorage = new ExtendedBlockStorage(y, !this.worldObj.provider.hasNoSky);
                        chunk.getBlockStorageArray()[l] = extendedblockstorage;
                    }
                    Block id;
                    if (y <= 83 && arr[(x * 16 + z) * 128 + y] != Blocks.stone) {
                        id = arr[(x * 16 + z) * 128 + y];
                    }
                    else if (y == 0) {
                        id = Blocks.bedrock;
                    }
                    else if (y < 71) {
                        id = stone_type;
                    }
                    else if (y < 81) {
                        if (stalegmites) {
                            id = Blocks.cobblestone;
                        }
                        else {
                            id = stone_type;
                            stalegmites = (this.r.nextInt(82 - y) == 0);
                        }
                    }
                    else if (y >= 81 && y < 136) {
                        if (!stalegtites && y > 108 && y < 136) {
                            stalegtites = (this.r.nextInt(1 + (136 - y) * (136 - y) * (136 - y)) == 0);
                        }
                        if (stalegtites || y == 81 || (y == 82 && this.r.nextInt(8) == 0) || y == 135 || rs < 4.0f || (rs < 5.5 && this.r.nextBoolean()) || this.r.nextDouble() < 32 - (y - 81) - 8.0 * Math.sqrt(rs) || this.r.nextDouble() < 32 - (136 - y) - 8.0 * Math.sqrt(rs)) {
                            id = Blocks.cobblestone;
                        }
                        else {
                            id = Blocks.air;
                        }
                    }
                    else if (y == 136) {
                        id = Blocks.cobblestone;
                    }
                    else if (y < 254) {
                        id = Blocks.stone;
                    }
                    else {
                        id = Blocks.bedrock;
                    }
                    if (id == null) {
                        id = Blocks.air;
                    }
                    extendedblockstorage.func_150818_a(x, y & 0xF, z, id);
                    extendedblockstorage.setExtBlockMetadata(x, y & 0xF, z, 0);
                }
            }
        }
        chunk.generateSkylightMap();
        final BiomeGenBase[] abiomegenbase = this.worldObj.getWorldChunkManager().loadBlockGeneratorData((BiomeGenBase[])null, par1 * 16, par2 * 16, 16, 16);
        final byte[] abyte = chunk.getBiomeArray();
        for (int k1 = 0; k1 < abyte.length; ++k1) {
            abyte[k1] = (byte)abiomegenbase[k1].biomeID;
        }
        chunk.generateSkylightMap();
        return chunk;
    }
    
    static {
        ChunkProviderUnderdark.denyDecor = false;
    }
}

