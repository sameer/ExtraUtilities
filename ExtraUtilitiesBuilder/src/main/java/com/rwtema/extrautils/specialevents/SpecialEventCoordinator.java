// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.specialevents;

import net.minecraft.entity.player.EntityPlayer;
import com.rwtema.extrautils.LogHelper;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.SharedMonsterAttributes;
import gnu.trove.map.hash.TObjectIntHashMap;
import com.rwtema.extrautils.helper.XURandom;

public class SpecialEventCoordinator
{
    public final String maxHealthName;
    public final int CAP = 2250;
    public final String soulDranTag = "XU_SoulDrain";
    public static final XURandom random;
    public TObjectIntHashMap<ChunkLocation> chunkmap;
    
    public SpecialEventCoordinator() {
        this.maxHealthName = SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName();
        this.chunkmap = (TObjectIntHashMap<ChunkLocation>)new TObjectIntHashMap(10, 0.5f, 0);
    }
    
    public void init() {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void getChunkData(final ChunkDataEvent.Save event) {
        final Chunk chunk = event.getChunk();
        final int i = this.chunkmap.get(new ChunkLocation(chunk.worldObj.provider.dimensionId, chunk.xPosition, chunk.zPosition));
        if (i != 0) {
            event.getData().setInteger("XU_SoulDrain", i);
        }
    }
    
    @SubscribeEvent
    public void getChunkData(final ChunkDataEvent.Load event) {
        final int i = event.getData().getInteger("XU_SoulDrain");
        if (i == 0) {
            return;
        }
        final Chunk chunk = event.getChunk();
        this.chunkmap.put(new ChunkLocation(chunk.worldObj.provider.dimensionId, chunk.xPosition, chunk.zPosition), i);
    }
    
    @SubscribeEvent
    public void playerTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }
        final EntityPlayer player = event.player;
        if (player.worldObj.isRemote) {
            return;
        }
        if (LogHelper.isDeObf) {}
        if ((player.worldObj.getTotalWorldTime() & 0x1FL) > 0L) {
            return;
        }
        final int i = this.chunkmap.adjustOrPutValue(new ChunkLocation(event.player), 1, 1);
        if (i > 2250) {}
    }
    
    static {
        random = XURandom.getInstance();
    }
    
    private final class ChunkLocation
    {
        final int dim;
        final int x;
        final int z;
        
        public ChunkLocation(final EntityPlayer player) {
            this(player.worldObj.provider.dimensionId, (int)player.posX >> 4, (int)player.posZ >> 4);
        }
        
        @Override
        public String toString() {
            return "ChunkLocation{x=" + this.x + ", z=" + this.z + '}';
        }
        
        public ChunkLocation(final int dim, final int x, final int z) {
            this.dim = dim;
            this.x = x;
            this.z = z;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final ChunkLocation that = (ChunkLocation)o;
            return this.dim == that.dim && this.x == that.x && this.z == that.z;
        }
        
        @Override
        public int hashCode() {
            int result = this.dim;
            result = 31 * result + this.x;
            result = 31 * result + this.z;
            return result;
        }
    }
}
