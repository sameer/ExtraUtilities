// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import java.util.LinkedList;

public class EventHandlerEntityItemStealer
{
    private static LinkedList<ItemStack> items;
    private static LinkedList<EntityItem> entityItems;
    private static boolean capturing;
    private static boolean killExperience;
    public static final HashMap<Class, Boolean> clazztypes;
    
    public static void startCapture(final boolean killExp) {
        EventHandlerEntityItemStealer.killExperience = killExp;
        startCapture();
    }
    
    public static void startCapture() {
        if (EventHandlerEntityItemStealer.capturing) {
            throw new IllegalStateException("Capturing item drops twice");
        }
        EventHandlerEntityItemStealer.capturing = true;
    }
    
    public static void stopCapture() {
        EventHandlerEntityItemStealer.capturing = false;
        EventHandlerEntityItemStealer.killExperience = false;
    }
    
    public static List<EntityItem> getCapturedEntities() {
        EventHandlerEntityItemStealer.capturing = false;
        final List<EntityItem> i = new ArrayList<EntityItem>();
        i.addAll(EventHandlerEntityItemStealer.entityItems);
        EventHandlerEntityItemStealer.entityItems.clear();
        EventHandlerEntityItemStealer.items.clear();
        return i;
    }
    
    public static List<ItemStack> getCapturedItemStacks() {
        EventHandlerEntityItemStealer.capturing = false;
        final List<ItemStack> i = new ArrayList<ItemStack>();
        i.addAll(EventHandlerEntityItemStealer.items);
        EventHandlerEntityItemStealer.entityItems.clear();
        EventHandlerEntityItemStealer.items.clear();
        return i;
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEntityJoinWorld(final EntityJoinWorldEvent event) {
        if (!EventHandlerEntityItemStealer.capturing || event.entity.worldObj.isRemote) {
            return;
        }
        final Entity entity = event.entity;
        if (entity.getClass() == EntitySilverfish.class) {
            ((EntitySilverfish)entity).onDeath(DamageSource.cactus);
            entity.setDead();
            event.setCanceled(true);
            return;
        }
        if (EventHandlerEntityItemStealer.killExperience && entity.getClass() == EntityXPOrb.class) {
            entity.setDead();
            event.setCanceled(true);
            return;
        }
        if (isEntityItem(entity.getClass())) {
            final ItemStack stack = entity.getDataWatcher().getWatchableObjectItemStack(10);
            if (stack == null) {
                return;
            }
            EventHandlerEntityItemStealer.items.add(stack);
            EventHandlerEntityItemStealer.entityItems.add((EntityItem)entity);
            entity.setDead();
            event.setCanceled(true);
        }
    }
    
    public static boolean isEntityItem(final Class clazz) {
        Boolean aBoolean = EventHandlerEntityItemStealer.clazztypes.get(clazz);
        if (aBoolean == null) {
            aBoolean = EntityItem.class.isAssignableFrom(clazz);
            EventHandlerEntityItemStealer.clazztypes.put(clazz, aBoolean);
        }
        return aBoolean;
    }
    
    static {
        EventHandlerEntityItemStealer.items = new LinkedList<ItemStack>();
        EventHandlerEntityItemStealer.entityItems = new LinkedList<EntityItem>();
        EventHandlerEntityItemStealer.capturing = false;
        EventHandlerEntityItemStealer.killExperience = false;
        clazztypes = new HashMap<Class, Boolean>();
    }
}


