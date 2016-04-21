// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.enderconstructor;

import com.rwtema.extrautils.dynamicgui.WidgetTextData;
import com.rwtema.extrautils.dynamicgui.WidgetProgressArrow;
import com.rwtema.extrautils.dynamicgui.WidgetSlotGhost;
import net.minecraft.inventory.ISidedInventory;
import com.rwtema.extrautils.dynamicgui.WidgetSlotRespectsInsertExtract;
import com.rwtema.extrautils.dynamicgui.WidgetSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import invtweaks.api.container.InventoryContainer;
import com.rwtema.extrautils.dynamicgui.DynamicContainer;

@InventoryContainer
public class DynamicContainerEnderConstructor extends DynamicContainer
{
    public TileEnderConstructor tile;
    public IInventory player;
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par2) {
        return super.transferStackInSlot(par1EntityPlayer, par2);
    }
    
    public DynamicContainerEnderConstructor(final IInventory player, final TileEnderConstructor tile) {
        this.tile = tile;
        this.player = player;
        for (int j = 0; j < 3; ++j) {
            for (int i = 0; i < 3; ++i) {
                this.widgets.add(new WidgetSlot((IInventory)tile.inv, i + j * 3, 30 + i * 18, 17 + j * 18));
            }
        }
        this.widgets.add(new WidgetSlotRespectsInsertExtract((ISidedInventory)tile, 9, 124, 35));
        this.widgets.add(new Arrow(tile, 90, 35));
        this.widgets.add(new WidgetSlotGhost((IInventory)tile.inv, 9, 92, 13));
        this.widgets.add(new WidgetEFText(tile, 9, 75, 124));
        this.cropAndAddPlayerSlots(player);
        this.validate();
    }
    
    public boolean canInteractWith(final EntityPlayer entityplayer) {
        return true;
    }
    
    public static class Arrow extends WidgetProgressArrow
    {
        TileEnderConstructor tile;
        
        public Arrow(final TileEnderConstructor tile, final int x, final int y) {
            super(x, y);
            this.tile = tile;
        }
        
        @Override
        public int getWidth() {
            return this.tile.getDisplayProgress();
        }
    }
    
    public static class WidgetEFText extends WidgetTextData
    {
        IEnderFluxHandler tile;
        
        public WidgetEFText(final IEnderFluxHandler tile, final int x, final int y, final int w) {
            super(x, y, w);
            this.tile = tile;
        }
        
        @Override
        public int getNumParams() {
            return 1;
        }
        
        @Override
        public Object[] getData() {
            return new Object[] { this.tile.getAmountRequested(), (byte)(this.tile.isActive() ? 1 : 0) };
        }
        
        @Override
        public String getConstructedText() {
            if (this.curData == null || this.curData.length != 2 || !(this.curData[0] instanceof Float) || !(this.curData[1] instanceof Boolean)) {
                return "";
            }
            if ((Byte)this.curData[1] == 1) {
                return "Ender-Flux: " + (Float)this.curData[0] / 1000.0f + " EF";
            }
            return "";
        }
    }
}


