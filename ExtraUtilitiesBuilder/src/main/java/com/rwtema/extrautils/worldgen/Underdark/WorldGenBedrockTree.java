// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.worldgen.Underdark;

import net.minecraft.util.Facing;
import java.util.Iterator;
import net.minecraft.inventory.IInventory;
import net.minecraft.block.Block;
import com.rwtema.extrautils.helper.XUHelper;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import com.rwtema.extrautils.ChunkPos;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenBedrockTree extends WorldGenerator
{
    public ItemStack[] items;
    private ArrayList<ChunkPos> torchPos;
    private ChunkPos chestPos;
    
    public WorldGenBedrockTree() {
        this.items = new ItemStack[] { new ItemStack(Items.gold_ingot), new ItemStack(Items.iron_ingot), new ItemStack(Items.diamond), new ItemStack(Items.emerald), new ItemStack(Blocks.stone), new ItemStack(Blocks.cobblestone), new ItemStack(Items.coal), new ItemStack(Items.coal), new ItemStack(Blocks.stone), new ItemStack(Blocks.stone), new ItemStack(Blocks.stone), new ItemStack(Blocks.stone), new ItemStack(Blocks.mossy_cobblestone), new ItemStack(Blocks.mossy_cobblestone), new ItemStack(Blocks.cobblestone), new ItemStack(Blocks.torch), new ItemStack(Blocks.torch), new ItemStack(Items.redstone) };
        this.torchPos = new ArrayList<ChunkPos>();
    }
    
    public boolean generate(final World par1World, final Random par2Random, final int par3, final int par4, final int par5) {
        if (par2Random.nextInt(960) == 0) {
            XUHelper.resetTimer();
            final int r = 4 + par2Random.nextInt(6);
            final int x = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
            final int z = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);
            this.torchPos.clear();
            this.chestPos = new ChunkPos(x, 80, z);
            this.genTreePart(par1World, x, 80, z, 1, 10, par2Random);
            par1World.setBlock(this.chestPos.x, this.chestPos.y, this.chestPos.z, (Block)Blocks.chest);
            if (par1World.getTileEntity(this.chestPos.x, this.chestPos.y, this.chestPos.z) instanceof IInventory) {
                final IInventory inv = (IInventory)par1World.getTileEntity(this.chestPos.x, this.chestPos.y, this.chestPos.z);
                for (int i = 0; i < inv.getSizeInventory(); ++i) {
                    final ItemStack t = this.items[par2Random.nextInt(this.items.length)].copy();
                    t.stackSize = 1 + par2Random.nextInt(1 + par2Random.nextInt(1 + par2Random.nextInt(1 + par2Random.nextInt(1 + par2Random.nextInt(64)))));
                    inv.setInventorySlotContents(i, t);
                }
            }
            for (final ChunkPos torchPo1 : this.torchPos) {
                if (par1World.getBlock(torchPo1.x, torchPo1.y, torchPo1.z) == Blocks.air) {
                    par1World.getChunkFromBlockCoords(torchPo1.x, torchPo1.z).func_150807_a(torchPo1.x & 0xF, torchPo1.y, torchPo1.z & 0xF, Blocks.torch, 5);
                }
            }
            for (final ChunkPos torchPo2 : this.torchPos) {
                par1World.func_147451_t(torchPo2.x, torchPo2.y, torchPo2.z);
            }
        }
        return true;
    }
    
    public void genTreePart(final World world, final int x, final int y, final int z, final int dir, final int dist, final Random rand) {
        if (dist < 1) {
            return;
        }
        for (int i = 1; i < dist + 1; ++i) {
            if (world.getBlock(x + Facing.offsetsXForSide[dir] * i, y + Facing.offsetsYForSide[dir] * i, z + Facing.offsetsZForSide[dir] * i) == Blocks.bedrock) {
                return;
            }
            if (y + Facing.offsetsYForSide[dir] * i + 1 > this.chestPos.y) {
                this.chestPos = new ChunkPos(x + Facing.offsetsXForSide[dir] * i, y + Facing.offsetsYForSide[dir] * i + 1, z + Facing.offsetsZForSide[dir] * i);
            }
            world.setBlock(x + Facing.offsetsXForSide[dir] * i, y + Facing.offsetsYForSide[dir] * i, z + Facing.offsetsZForSide[dir] * i, Blocks.bedrock);
            if (y + Facing.offsetsYForSide[dir] * i < 80) {
                return;
            }
            if (y + Facing.offsetsYForSide[dir] * i > 120) {
                return;
            }
            if (rand.nextInt(5) == 0) {
                this.torchPos.add(new ChunkPos(x, y + 1, z));
            }
        }
        if (dist <= 1) {
            return;
        }
        for (int d = 0; d < 6; ++d) {
            if (d != Facing.oppositeSide[dir]) {
                this.genTreePart(world, x + Facing.offsetsXForSide[dir] * dist, y + Facing.offsetsYForSide[dir] * dist, z + Facing.offsetsZForSide[dir] * dist, d, (int)Math.floor(rand.nextDouble() * dist), rand);
            }
        }
    }
}

