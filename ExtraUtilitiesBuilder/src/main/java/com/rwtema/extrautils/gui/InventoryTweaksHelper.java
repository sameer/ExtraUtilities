// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.gui;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.InventoryPlayer;
import java.util.HashMap;
import net.minecraft.inventory.Slot;
import java.util.List;
import invtweaks.api.container.ContainerSection;
import java.util.Map;
import net.minecraft.inventory.Container;

public class InventoryTweaksHelper
{
    public static Map<ContainerSection, List<Slot>> getSlots(final Container inventory) {
        return getSlots(inventory, false);
    }
    
    public static Map<ContainerSection, List<Slot>> getSlots(final Container inventory, final boolean playerInvOnly) {
        final Map<ContainerSection, List<Slot>> map = new HashMap<ContainerSection, List<Slot>>();
        for (final Object o : inventory.inventorySlots) {
	    Slot s = (Slot) o;
            final ContainerSection c = null;
            if (s.inventory instanceof InventoryPlayer) {
                putSlot(map, s, ContainerSection.INVENTORY);
                if (s.slotNumber < 9) {
                    putSlot(map, s, ContainerSection.INVENTORY_HOTBAR);
                }
                else if (s.slotNumber < 36) {
                    putSlot(map, s, ContainerSection.INVENTORY_NOT_HOTBAR);
                }
                else {
                    putSlot(map, s, ContainerSection.ARMOR);
                }
            }
            else {
                if (playerInvOnly) {
                    continue;
                }
                putSlot(map, s, ContainerSection.CHEST);
            }
        }
        return map;
    }
    
    private static void putSlot(final Map<ContainerSection, List<Slot>> map, final Slot s, final ContainerSection c) {
        List<Slot> list = map.get(c);
        if (list == null) {
            list = new ArrayList<Slot>();
        }
        list.add(s);
        map.put(c, list);
    }
}


