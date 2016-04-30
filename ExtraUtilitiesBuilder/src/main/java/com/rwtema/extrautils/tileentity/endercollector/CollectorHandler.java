// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.endercollector;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import java.util.List;
import java.util.Collections;
import net.minecraft.entity.item.EntityItem;
import com.rwtema.extrautils.EventHandlerEntityItemStealer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import java.util.ArrayList;
import net.minecraft.world.World;
import java.util.WeakHashMap;

public class CollectorHandler
{
    public static final WeakHashMap<World, WeakHashMap<TileEnderCollector, Object>> map;
    public static CollectorHandler INSTANCE;
    public static boolean dontCollect;
    private static final ArrayList<TileEnderCollector> collectors;
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEntityJoinWorld(final EntityJoinWorldEvent event) {
        if (event.entity.worldObj.isRemote) {
            return;
        }
        if (CollectorHandler.dontCollect) {
            return;
        }
        if (CollectorHandler.map.isEmpty()) {
            return;
        }
        final WeakHashMap<TileEnderCollector, Object> map = CollectorHandler.map.get(event.entity.worldObj);
        if (map == null || map.isEmpty()) {
            return;
        }
        final Entity entity = event.entity;
        if (!EventHandlerEntityItemStealer.isEntityItem(entity.getClass())) {
            return;
        }
        final ItemStack stack = entity.getDataWatcher().getWatchableObjectItemStack(10);
        if (stack == null) {
            return;
        }
        CollectorHandler.collectors.clear();
        for (final TileEnderCollector tileEnderCollector : map.keySet()) {
            if (tileEnderCollector.inRange(entity)) {
                CollectorHandler.collectors.add(tileEnderCollector);
            }
        }
        if (CollectorHandler.collectors.isEmpty()) {
            return;
        }
        if (CollectorHandler.collectors.size() == 1) {
            CollectorHandler.collectors.get(0).grabEntity((EntityItem)entity);
        }
        else {
            Collections.shuffle(CollectorHandler.collectors);
            final EntityItem entityItem = (EntityItem)entity;
            for (final TileEnderCollector collector : CollectorHandler.collectors) {
                collector.grabEntity(entityItem);
                if (entity.isDead) {
                    break;
                }
            }
        }
        CollectorHandler.collectors.clear();
        if (entity.isDead) {
            event.setCanceled(true);
        }
    }
    
    public static void register(final TileEnderCollector tile) {
        final World worldObj = tile.getWorldObj();
        if (worldObj == null || worldObj.isRemote) {
            return;
        }
        getWorldMap(tile).put(tile, null);
    }
    
    public static void unregister(final TileEnderCollector tile) {
        final World worldObj = tile.getWorldObj();
        if (worldObj == null || worldObj.isRemote) {
            return;
        }
        getWorldMap(tile).remove(tile);
    }
    
    public static WeakHashMap<TileEnderCollector, Object> getWorldMap(final TileEnderCollector tile) {
        WeakHashMap<TileEnderCollector, Object> worldMap = CollectorHandler.map.get(tile.getWorldObj());
        if (worldMap == null) {
            worldMap = new WeakHashMap<TileEnderCollector, Object>();
            CollectorHandler.map.put(tile.getWorldObj(), worldMap);
        }
        return worldMap;
    }
    
    static {
        map = new WeakHashMap<World, WeakHashMap<TileEnderCollector, Object>>();
        CollectorHandler.INSTANCE = new CollectorHandler();
        MinecraftForge.EVENT_BUS.register((Object)CollectorHandler.INSTANCE);
        collectors = new ArrayList<TileEnderCollector>(1);
    }
}


