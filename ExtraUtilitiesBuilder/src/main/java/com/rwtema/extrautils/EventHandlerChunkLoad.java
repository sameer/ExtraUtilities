// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import com.rwtema.extrautils.tileentity.enderquarry.IChunkLoad;
import net.minecraftforge.event.world.ChunkEvent;

public class EventHandlerChunkLoad
{
    @SubscribeEvent
    public void load(final ChunkEvent.Load event) {
        for (final Object t : event.getChunk().chunkTileEntityMap.values()) {
            if (t instanceof IChunkLoad) {
                ((IChunkLoad)t).onChunkLoad();
            }
        }
    }
    
    @SubscribeEvent
    public void unload(final ChunkEvent.Load event) {
        for (final Object t : event.getChunk().chunkTileEntityMap.values()) {
            if (t instanceof IChunkLoad) {
                ((IChunkLoad)t).onChunkUnload();
            }
        }
    }
}

