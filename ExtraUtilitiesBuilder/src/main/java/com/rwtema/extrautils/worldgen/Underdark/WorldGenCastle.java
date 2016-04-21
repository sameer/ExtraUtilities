// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.worldgen.Underdark;

import com.rwtema.extrautils.helper.XURandom;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.block.Block;
import net.minecraft.item.ItemDoor;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import com.rwtema.extrautils.block.BlockColorData;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.LogHelper;
import net.minecraftforge.common.DungeonHooks;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import com.rwtema.extrautils.ChunkPos;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenCastle extends WorldGenerator
{
    public static final int rad = 17;
    private int[][] block;
    public static final int[] dx;
    public static final int[] dy;
    public static final int d = 9;
    public static String[] dungeons;
    public static Random staticRand;
    public long timer;
    private int[] block_allocations;
    private boolean colorbricks;
    private boolean colorWoods;
    private boolean lightGen;
    private ArrayList<ChunkPos> torchPos;
    
    public WorldGenCastle() {
        this.block = new int[17][17];
        this.timer = 0L;
        this.block_allocations = new int[16];
        this.torchPos = new ArrayList<ChunkPos>();
    }
    
    public static void setMobSpawner(final World world, final int x, final int y, final int z, final Random rand) {
        world.setBlock(x, y, z, Blocks.mob_spawner, 0, 2);
        final TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)world.getTileEntity(x, y, z);
        if (tileentitymobspawner != null) {
            tileentitymobspawner.func_145881_a().setEntityName(DungeonHooks.getRandomDungeonMob(rand));
        }
        else {
            LogHelper.error("Failed to fetch mob spawner entity at (" + x + ", " + y + ", " + z + ")", new Object[0]);
        }
    }
    
    public int castleX(final long seed, final int x, final int z) {
        WorldGenCastle.staticRand.setSeed(seed + (x >> 9) + 65535 * (z >> 9));
        return (x >> 9 << 9) + WorldGenCastle.staticRand.nextInt(390) + 61;
    }
    
    public int castleZ(final long seed, final int x, final int z) {
        WorldGenCastle.staticRand.setSeed(seed + (x >> 9) + 65535 * (z >> 9));
        WorldGenCastle.staticRand.nextInt(390);
        return (z >> 9 << 9) + WorldGenCastle.staticRand.nextInt(390) + 61;
    }
    
    public boolean generate(final World world, final Random rand, int x, final int y, int z) {
        final int cx = this.castleX(world.getSeed(), x, z);
        final int cz = this.castleZ(world.getSeed(), x, z);
        if (cx >> 4 != x >> 4 || cz >> 4 != z >> 4) {
            return false;
        }
        for (int ax = 0; ax < 17; ++ax) {
            for (int ay = 0; ay < 17; ++ay) {
                this.s(ax, ay, -1);
            }
        }
        final ArrayList<Vec2> initBlocks = new ArrayList<Vec2>();
        initBlocks.add(new Vec2(8, 8));
        for (int i = 0; i < initBlocks.size() && initBlocks.size() < 68.0; ++i) {
            final int ax2 = initBlocks.get(i).x;
            final int ay2 = initBlocks.get(i).y;
            for (int dj = rand.nextInt(4), j = (dj + 1) % 4; j != dj; j = (j + 1) % 4) {
                if (this.isValid(ax2 + WorldGenCastle.dx[j], ay2 + WorldGenCastle.dy[j])) {
                    final Vec2 t = new Vec2(ax2 + WorldGenCastle.dx[j], ay2 + WorldGenCastle.dy[j]);
                    if (!initBlocks.contains(t)) {
                        if (initBlocks.size() - 2 - i > 0) {
                            initBlocks.add(i + 1 + rand.nextInt(initBlocks.size() - 2 - i), t);
                        }
                        else {
                            initBlocks.add(t);
                        }
                        this.s(ax2 + WorldGenCastle.dx[j], ay2 + WorldGenCastle.dy[j], 0);
                        this.s(16 - (ax2 + WorldGenCastle.dx[j]), 16 - (ay2 + WorldGenCastle.dy[j]), 0);
                        this.s(ax2 + WorldGenCastle.dx[j], 16 - (ay2 + WorldGenCastle.dy[j]), 0);
                        this.s(16 - (ax2 + WorldGenCastle.dx[j]), ay2 + WorldGenCastle.dy[j], 0);
                    }
                }
            }
        }
        x = cx - 24;
        z = cz - 24;
        float r = 1.0f;
        float g = 1.0f;
        float b = 1.0f;
        this.colorbricks = (ExtraUtils.colorBlockBrick != null);
        this.colorWoods = (ExtraUtils.coloredWood != null);
        if (this.colorbricks) {
            for (int k = 0; k < 16; ++k) {
                final int j = rand.nextInt(1 + k);
                if (k != j) {
                    this.block_allocations[k] = this.block_allocations[j];
                }
                this.block_allocations[j] = k;
            }
            final float[][] cols = new float[16][3];
            g = (r = (b = 0.4f + 0.6f * rand.nextFloat()));
            r *= (2.0f + rand.nextFloat()) / 3.0f;
            g *= (2.0f + rand.nextFloat()) / 3.0f;
            b *= (2.0f + rand.nextFloat()) / 3.0f;
            for (int l = 0; l < 16; ++l) {
                final float br = (1.0f + rand.nextFloat()) / 2.0f;
                cols[l][0] = r * br * (5.0f + rand.nextFloat()) / 6.0f;
                cols[l][1] = g * br * (5.0f + rand.nextFloat()) / 6.0f;
                cols[l][2] = b * br * (5.0f + rand.nextFloat()) / 6.0f;
            }
            for (int dx = x - 16; dx <= x + 51 + 16; dx += 16) {
                for (int dz = z - 16; dz <= z + 51 + 16; dz += 16) {
                    for (int m = 0; m < 16; ++m) {
                        BlockColorData.changeColorData(world, dx, y, dz, this.block_allocations[m], cols[m][0], cols[m][1], cols[m][2]);
                    }
                }
            }
        }
        this.torchPos.clear();
        for (int k = 0; k < 5; ++k) {
            this.genLevel(world, rand, x, y + k * 5, z, k);
        }
        for (final ChunkPos torchPo1 : this.torchPos) {
            world.getChunkFromBlockCoords(torchPo1.x, torchPo1.z).func_150807_a(torchPo1.x & 0xF, torchPo1.y, torchPo1.z & 0xF, Blocks.torch, 5);
        }
        for (final ChunkPos torchPo2 : this.torchPos) {
            world.func_147451_t(torchPo2.x, torchPo2.y, torchPo2.z);
        }
        return true;
    }
    
    public void genLevel(final World world, final Random rand, final int x, final int y, final int z, final int level) {
        for (int ax = 0; ax < 17; ++ax) {
            for (int ay = 0; ay < 17; ++ay) {
                if (this.block[ax][ay] == -1 || ax <= level || 16 - ax <= level || ay <= level || 16 - ay <= level) {
                    this.block[ax][ay] = -1;
                }
                else {
                    this.block[ax][ay] = 0;
                }
            }
        }
        final ArrayList<Vec2> list = new ArrayList<Vec2>();
        final ArrayList<Vec2> corridors = new ArrayList<Vec2>();
        this.block[8][8] = 1;
        corridors.add(new Vec2(8, 8));
        if (level == 0) {
            for (int ax2 = 1; ax2 < 17; ++ax2) {
                this.block[ax2][8] = 1;
                corridors.add(new Vec2(ax2, 8));
            }
            this.block[0][8] = -1;
        }
        int a = 0;
        while (a < 289) {
            ++a;
            final Vec2 t = corridors.get(rand.nextInt(corridors.size()));
            final int k = 1 + rand.nextInt(8);
            final int d = rand.nextInt(4);
            boolean canAdd = true;
            for (int i = 1; i <= k && canAdd; ++i) {
                if (!this.isValid(t.x + WorldGenCastle.dx[d] * i, t.y + WorldGenCastle.dy[d] * i) || this.g(t.x + WorldGenCastle.dx[d] * i, t.y + WorldGenCastle.dy[d] * i) != 0) {
                    canAdd = false;
                }
                else {
                    int c_n = 0;
                    for (int j = 0; j < 4; ++j) {
                        if (this.g(t.x + WorldGenCastle.dx[d] * i + WorldGenCastle.dx[j], t.y + WorldGenCastle.dy[d] * i + WorldGenCastle.dy[j]) != 0) {
                            ++c_n;
                        }
                    }
                    if (c_n >= 2) {
                        canAdd = false;
                    }
                    else {
                        c_n = 0;
                        for (int ddx = -2; ddx <= 2; ++ddx) {
                            for (int ddy = -2; ddy <= 2; ++ddy) {
                                if (this.g(t.x + WorldGenCastle.dx[d] * i + ddx, t.y + WorldGenCastle.dy[d] * i + ddy) != 0) {
                                    ++c_n;
                                }
                            }
                        }
                        if (c_n >= 8) {
                            canAdd = false;
                        }
                        else {
                            this.s(t.x + WorldGenCastle.dx[d] * i, t.y + WorldGenCastle.dy[d] * i, 1);
                            corridors.add(new Vec2(t.x + WorldGenCastle.dx[d] * i, t.y + WorldGenCastle.dy[d] * i));
                            a += 12;
                        }
                    }
                }
            }
        }
        Collections.shuffle(corridors);
        for (int l = 0; l < corridors.size() && l < 10; ++l) {
            list.add(corridors.get(l));
        }
        int numDoors = 0;
        final ArrayList<Integer> doorDirections = new ArrayList<Integer>();
        doorDirections.add(0);
        for (int m = 0; m < list.size(); ++m) {
            final int ax3 = list.get(m).x;
            final int ay2 = list.get(m).y;
            boolean added = false;
            if (this.g(ax3, ay2) == 0) {
                for (int j = 0; j < 4; ++j) {
                    if (this.g(ax3 + WorldGenCastle.dx[j], ay2 + WorldGenCastle.dy[j]) >> 1 > 0 && this.g(ax3 + WorldGenCastle.dx[j], ay2 + WorldGenCastle.dy[j]) >> 1 == this.g(ax3 + WorldGenCastle.dx[(j + 1) % 4], ay2 + WorldGenCastle.dy[(j + 1) % 4]) >> 1) {
                        added = true;
                        this.s(ax3, ay2, (this.g(ax3 + WorldGenCastle.dx[j], ay2 + WorldGenCastle.dy[j]) >> 1) * 2 + 1);
                    }
                }
                if (!added) {
                    final int k2 = rand.nextInt(4);
                    for (int j2 = 0; j2 < 4; ++j2) {
                        if (this.g(ax3 + WorldGenCastle.dx[(j2 + k2) % 4], ay2 + WorldGenCastle.dy[(j2 + k2) % 4]) >> 1 > 0) {
                            added = true;
                            this.s(ax3, ay2, (this.g(ax3 + WorldGenCastle.dx[(j2 + k2) % 4], ay2 + WorldGenCastle.dy[(j2 + k2) % 4]) >> 1) * 2 + 1);
                        }
                    }
                }
                if (!added) {
                    final int k2 = rand.nextInt(4);
                    for (int j2 = 0; j2 < 4; ++j2) {
                        if (this.g(ax3 + WorldGenCastle.dx[(j2 + k2) % 4], ay2 + WorldGenCastle.dy[(j2 + k2) % 4]) == 1) {
                            added = true;
                            ++numDoors;
                            doorDirections.add(j2);
                            this.s(ax3, ay2, numDoors * 2);
                        }
                    }
                }
            }
            for (int j = 0; j < 4; ++j) {
                if (this.isValid(ax3 + WorldGenCastle.dx[j], ay2 + WorldGenCastle.dy[j]) && !list.contains(new Vec2(ax3 + WorldGenCastle.dx[j], ay2 + WorldGenCastle.dy[j]))) {
                    if (list.size() - m >= 1) {
                        list.add(m + rand.nextInt(list.size() - m), new Vec2(ax3 + WorldGenCastle.dx[j], ay2 + WorldGenCastle.dy[j]));
                    }
                    else {
                        list.add(new Vec2(ax3 + WorldGenCastle.dx[j], ay2 + WorldGenCastle.dy[j]));
                    }
                }
            }
        }
        int doorDir = 0;
        for (int ax3 = 0; ax3 < 17; ++ax3) {
            for (int ay2 = 0; ay2 < 17; ++ay2) {
                final int d2 = this.g(ax3, ay2);
                if (d2 >= 0) {
                    for (int dax = -1; dax <= 1; ++dax) {
                        for (int day = -1; day <= 1; ++day) {
                            this.setBrick(world, x + 1 + ax3 * 3 + dax, y, z + 1 + ay2 * 3 + day, 0);
                            Block id = Blocks.air;
                            if (dax == 0 && day == 0) {
                                id = Blocks.air;
                                if (d2 > 1) {
                                    this.setWood(world, x + 1 + ax3 * 3 + dax, y, z + 1 + ay2 * 3 + day, (d2 >> 1) % 16);
                                }
                            }
                            else if (dax == 0 || day == 0) {
                                doorDir = ((dax == 0) ? ((day == 1) ? 3 : 1) : ((dax == 1) ? 2 : 0));
                                if (this.g(ax3 + dax, ay2 + day) >> 1 == d2 >> 1 || (d2 == 1 && this.g(ax3 + dax, ay2 + day) <= 0) || (d2 == 1 && this.g(ax3 + dax, ay2 + day) % 2 == 0) || (d2 % 2 == 0 && this.g(ax3 + dax, ay2 + day) == 1)) {
                                    if (d2 % 2 == 0 && this.g(ax3 + dax, ay2 + day) == 1) {
                                        id = Blocks.planks;
                                    }
                                    else {
                                        id = Blocks.air;
                                        if (d2 > 1) {
                                            this.setWood(world, x + 1 + ax3 * 3 + dax, y, z + 1 + ay2 * 3 + day, this.woodPattern(d2, ax3 * 3 + dax, ay2 * 3 + day));
                                        }
                                    }
                                }
                                else {
                                    id = Blocks.stonebrick;
                                }
                            }
                            else if (d2 > 1 && this.g(ax3 + dax, ay2) >> 1 == d2 >> 1 && this.g(ax3, ay2 + day) >> 1 == d2 >> 1) {
                                id = Blocks.air;
                                this.setWood(world, x + 1 + ax3 * 3 + dax, y, z + 1 + ay2 * 3 + day, this.woodPattern(d2, ax3 * 3 + dax, ay2 * 3 + day));
                            }
                            else {
                                id = Blocks.stonebrick;
                            }
                            if (id == Blocks.planks) {
                                ItemDoor.placeDoorBlock(world, x + 1 + ax3 * 3 + dax, y + 1, z + 1 + ay2 * 3 + day, doorDir, Blocks.wooden_door);
                                this.setBrick(world, x + 1 + ax3 * 3 + dax, y + 3, z + 1 + ay2 * 3 + day, 2);
                                this.setBrick(world, x + 1 + ax3 * 3 + dax, y + 4, z + 1 + ay2 * 3 + day, 1);
                            }
                            else {
                                for (int dh = 0; dh <= 3; ++dh) {
                                    if (id == Blocks.stonebrick) {
                                        this.setBrick(world, x + 1 + ax3 * 3 + dax, y + 1 + dh, z + 1 + ay2 * 3 + day, (dh == 2) ? 2 : 1);
                                    }
                                    else {
                                        world.setBlock(x + 1 + ax3 * 3 + dax, y + 1 + dh, z + 1 + ay2 * 3 + day, id, 0, 2);
                                    }
                                }
                            }
                            if (d2 == 1) {
                                this.setBrick(world, x + 1 + ax3 * 3 + dax, y + 3, z + 1 + ay2 * 3 + day, 1);
                            }
                            this.setBrick(world, x + 1 + ax3 * 3 + dax, y + 4, z + 1 + ay2 * 3 + day, 3);
                        }
                    }
                }
                else {
                    for (int dax = -1; dax <= 1; ++dax) {
                        for (int day = -1; day <= 1; ++day) {
                            if (this.g(ax3 + dax, ay2 + day) >= 0 || this.g(ax3 + dax, ay2) >= 0 || this.g(ax3, ay2 + day) >= 0) {
                                this.setBrick(world, x + 1 + ax3 * 3 + dax, y, z + 1 + ay2 * 3 + day, 0);
                                this.setBrick(world, x + 1 + ax3 * 3 + dax, y + 4, z + 1 + ay2 * 3 + day, 4);
                                if ((ax3 + dax + ay2 + day) % 2 == 0) {
                                    this.setBrick(world, x + 1 + ax3 * 3 + dax, y + 5, z + 1 + ay2 * 3 + day, 5);
                                    this.torchPos.add(new ChunkPos(x + 1 + ax3 * 3 + dax, y + 6, z + 1 + ay2 * 3 + day));
                                }
                            }
                        }
                    }
                }
            }
        }
        int ax3 = 8;
        int ay2 = 8;
        for (int h = 0; h <= 4; ++h) {
            for (int dax = -2; dax <= 2; ++dax) {
                for (int day = -2; day <= 2; ++day) {
                    if (Math.abs(dax) < 2 || Math.abs(day) < 2) {
                        if (h > 0 && h < 4) {
                            world.setBlock(x + 1 + ax3 * 3 + dax, y + h, z + 1 + ay2 * 3 + day, Blocks.air, 0, 2);
                        }
                        else {
                            this.setBrick(world, x + 1 + ax3 * 3 + dax, y + h, z + 1 + ay2 * 3 + day, 3);
                        }
                    }
                }
            }
            this.setBrick(world, x + 1 + ax3 * 3 + 1, y + h, z + 1 + ay2 * 3, 0);
            world.setBlock(x + 1 + ax3 * 3, y + h, z + 1 + ay2 * 3, Blocks.ladder, 4, 2);
        }
        if (level == 0) {
            for (int dax2 = -1; dax2 <= 1; ++dax2) {
                for (int day2 = -1; day2 <= 1; ++day2) {
                    this.setBrick(world, x + 1 + ax3 * 3 + dax2, y, z + 1 + ay2 * 3 + day2, 1);
                }
            }
        }
        final int numChests = (17 - 2 * level) * (17 - 2 * level) / 49;
        final ArrayList<Vec2> chestPos = new ArrayList<Vec2>();
        for (int i2 = list.size() - 1; chestPos.size() < numChests && i2 >= 0; --i2) {
            final Vec2 v = list.get(i2);
            boolean add = this.g(v.x, v.y) > 1;
            if (add) {
                for (int j3 = 0; add && j3 < chestPos.size(); ++j3) {
                    if (chestPos.get(j3).distFrom(v) < 8.0) {
                        add = false;
                    }
                }
            }
            if (add) {
                chestPos.add(v);
            }
        }
        for (final Vec2 chestPo : chestPos) {
            world.setBlock(x + 1 + 3 * chestPo.x, y + 1, z + 1 + 3 * chestPo.y, (Block)Blocks.chest);
            final TileEntityChest tile = (TileEntityChest)world.getTileEntity(x + 1 + 3 * chestPo.x, y + 1, z + 1 + 3 * chestPo.y);
            if (tile != null) {
                final ChestGenHooks info = ChestGenHooks.getInfo(WorldGenCastle.dungeons[rand.nextInt(WorldGenCastle.dungeons.length)]);
                WeightedRandomChestContent.generateChestContents(rand, info.getItems(rand), (IInventory)tile, info.getCount(rand));
            }
            setMobSpawner(world, x + 1 + 3 * chestPo.x, y + 2, z + 1 + 3 * chestPo.y, rand);
        }
    }
    
    public void setBrick(final World world, final int x, final int y, final int z, final int type) {
        if (this.colorbricks) {
            world.setBlock(x, y, z, (Block)ExtraUtils.colorBlockBrick, this.block_allocations[type], 2);
        }
        else {
            world.setBlock(x, y, z, Blocks.stonebrick);
        }
    }
    
    public void setWood(final World world, final int x, final int y, final int z, final int type) {
        if (this.colorWoods) {
            world.setBlock(x, y, z, (Block)ExtraUtils.coloredWood, this.block_allocations[type], 2);
        }
        else {
            world.setBlock(x, y, z, Blocks.planks, type % 4, 2);
        }
    }
    
    public int g(final int x, final int y) {
        return this.isValid(x, y) ? this.block[x][y] : -1;
    }
    
    public void s(final int x, final int y, final int i) {
        if (this.isValid(x, y)) {
            this.block[x][y] = i;
        }
    }
    
    public boolean isValid(final int x, final int y) {
        return x >= 0 && y >= 0 && (x < 17 & y < 17);
    }
    
    public int woodPattern(int d, final int x, final int y) {
        d = (d >> 1) % 16;
        switch (d) {
            case 0: {
                if (x % 2 == y % 2) {
                    ++d;
                    break;
                }
                break;
            }
            case 3: {
                if (x % 2 == 0) {
                    ++d;
                    break;
                }
                break;
            }
            case 4: {
                if (y % 2 == 0) {
                    ++d;
                    break;
                }
                break;
            }
            case 6: {
                if (x % 2 * (y % 2) == 0) {
                    ++d;
                    break;
                }
                break;
            }
            case 8: {
                if (x % 3 * (y % 3) == 0) {
                    ++d;
                    break;
                }
                break;
            }
            case 9: {
                if (x % 4 + y % 4 == 0) {
                    ++d;
                    break;
                }
                break;
            }
            case 11: {
                if (x % 4 + y % 2 == 0) {
                    ++d;
                    break;
                }
                break;
            }
            case 12: {
                if (x % 3 == 0) {
                    ++d;
                    break;
                }
                break;
            }
            case 13: {
                if (x % 6 == 0) {
                    ++d;
                    break;
                }
                if (x % 2 == 0) {
                    d += 2;
                    break;
                }
                break;
            }
        }
        return d % 16;
    }
    
    static {
        dx = new int[] { -1, 0, 1, 0 };
        dy = new int[] { 0, -1, 0, 1 };
        WorldGenCastle.dungeons = new String[] { "dungeonChest", "strongholdCorridor", "strongholdLibrary", "pyramidDesertyChest", "pyramidJungleChest", "mineshaftCorridor", "villageBlacksmith", "strongholdCrossing", "dungeonChest", "dungeonChest", "dungeonChest", "dungeonChest" };
        WorldGenCastle.staticRand = XURandom.getInstance();
    }
    
    public static class Vec2
    {
        int x;
        int y;
        
        public Vec2(final int x, final int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Vec2 && ((Vec2)o).x == this.x && ((Vec2)o).y == this.y;
        }
        
        public double distFrom(final Vec2 other) {
            return Math.sqrt((this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y));
        }
    }
}


