// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.network.packets;

import cpw.mods.fml.relauncher.Side;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import com.rwtema.extrautils.network.NetworkHandler;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.inventory.IInventory;
import java.util.Map;
import java.util.TreeSet;
import net.minecraft.world.ChunkPosition;
import java.util.Comparator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import java.util.WeakHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.network.XUPacketBase;

public class PacketNEIPing extends XUPacketBase
{
    private ItemStack itemStack;
    private EntityPlayer player;
    static WeakHashMap<EntityPlayer, Long> timeOutsHandler;
    static final long TIMEOUT = 10L;
    static final int RANGE = 16;
    
    public PacketNEIPing() {
    }
    
    public PacketNEIPing(final ItemStack itemStack) {
        this.itemStack = itemStack;
    }
    
    @Override
    public void writeData(final ByteBuf data) throws Exception {
        this.writeItemStack(data, this.itemStack);
    }
    
    @Override
    public void readData(final EntityPlayer player, final ByteBuf data) {
        this.player = player;
        this.itemStack = this.readItemStack(data);
    }
    
    @Override
    public void doStuffServer(final ChannelHandlerContext ctx) {
        if (this.player == null || this.itemStack == null || this.itemStack.getItem() == null) {
            return;
        }
        final World world = this.player.worldObj;
        final long time = world.getTotalWorldTime();
        final Long aLong = PacketNEIPing.timeOutsHandler.get(this.player);
        if (aLong != null && time - aLong < 10L) {
            return;
        }
        PacketNEIPing.timeOutsHandler.put(this.player, time);
        final int x = (int)Math.round(this.player.posX);
        final int y = (int)Math.round(this.player.posY);
        final int z = (int)Math.round(this.player.posZ);
        final Item trueItem = this.itemStack.getItem();
        final int trueItemDamage = this.itemStack.getItemDamage();
        final TreeSet<ChunkPosition> positions = new TreeSet<ChunkPosition>(new Comparator<ChunkPosition>() {
            @Override
            public int compare(final ChunkPosition o1, final ChunkPosition o2) {
                return Double.compare(PacketNEIPing.this.getRange(x, y, z, o1), PacketNEIPing.this.getRange(x, y, z, o2));
            }
        });
        for (int cx = x - 16; cx <= x + 16; cx += 16) {
            for (int cz = z - 16; cz <= z + 16; cz += 16) {
                if (world.blockExists(cx, y, cz)) {
                    final Chunk chunk = world.getChunkFromBlockCoords(cx, cz);
                    final Set<Map.Entry<ChunkPosition, Object>> entrySet = chunk.chunkTileEntityMap.entrySet();
                    for (final Map.Entry<ChunkPosition, Object> entry : entrySet) {
                        final ChunkPosition e = entry.getKey();
                        final ChunkPosition key = new ChunkPosition(chunk.xPosition * 16 + e.chunkPosX, e.chunkPosY, chunk.zPosition * 16 + e.chunkPosZ);
                        if (!this.inRange(x, y, z, key)) {
                            continue;
                        }
                        final Object value = entry.getValue();
                        if (!(value instanceof IInventory)) {
                            continue;
                        }
                        final IInventory inv = (IInventory)value;
                        for (int i = 0; i < inv.getSizeInventory(); ++i) {
                            final ItemStack stackInSlot = inv.getStackInSlot(i);
                            if (stackInSlot != null) {
                                if (stackInSlot.getItem() == trueItem) {
                                    if (!trueItem.getHasSubtypes() || stackInSlot.getItemDamage() == trueItemDamage) {
                                        positions.add(key);
                                        if (positions.size() >= 20) {
                                            positions.pollLast();
                                            break;
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!positions.isEmpty()) {
            NetworkHandler.sendPacketToPlayer(new PacketNEIPong(new ArrayList<ChunkPosition>(positions)), this.player);
        }
    }
    
    public int getRange(final int x, final int y, final int z, final ChunkPosition pos) {
        return Math.abs(pos.chunkPosX - x) + Math.abs(pos.chunkPosY - y) + Math.abs(pos.chunkPosZ - z);
    }
    
    public boolean inRange(final int x, final int y, final int z, final ChunkPosition pos) {
        return Math.abs(pos.chunkPosX - x) <= 16 && Math.abs(pos.chunkPosY - y) <= 16 && Math.abs(pos.chunkPosZ - z) <= 16;
    }
    
    @Override
    public void doStuffClient() {
    }
    
    @Override
    public boolean isValidSenderSide(final Side properSenderSide) {
        return properSenderSide.isClient();
    }
    
    static {
        PacketNEIPing.timeOutsHandler = new WeakHashMap<EntityPlayer, Long>();
    }
}


