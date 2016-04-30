// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class SlotGhostItemContainer extends SlotItemContainer
{
    public SlotGhostItemContainer(final IInventory par1iInventory, final int slot, final int x, final int y, final int filterIndex) {
        super(par1iInventory, slot, x, y, filterIndex);
    }
    
    public boolean canTakeStack(final EntityPlayer par1EntityPlayer) {
        return false;
    }
}


