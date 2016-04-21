// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.generators;

import com.rwtema.extrautils.dynamicgui.WidgetTextData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidTankInfo;
import com.rwtema.extrautils.dynamicgui.IWidget;
import cofh.api.energy.IEnergyHandler;
import com.rwtema.extrautils.dynamicgui.WidgetEnergy;
import com.rwtema.extrautils.dynamicgui.WidgetTank;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;
import com.rwtema.extrautils.dynamicgui.WidgetSlot;
import com.rwtema.extrautils.dynamicgui.WidgetText;
import net.minecraft.inventory.IInventory;
import invtweaks.api.container.InventoryContainer;
import com.rwtema.extrautils.dynamicgui.DynamicContainer;

@InventoryContainer
public class DynamicContainerGenerator extends DynamicContainer
{
    TileEntityGenerator gen;
    public TileEntityGeneratorFurnace genFurnace;
    
    public DynamicContainerGenerator(final IInventory player, final TileEntityGenerator gen) {
        this.genFurnace = null;
        this.gen = gen;
        if (this.gen instanceof TileEntityGeneratorFurnace) {
            this.genFurnace = (TileEntityGeneratorFurnace)this.gen;
        }
        this.widgets.add(new WidgetText(5, 5, BlockGenerator.names[gen.getBlockMetadata()] + " Generator", 162));
        int x = 5;
        final int y = 19;
        if (gen instanceof IInventory) {
            final IInventory inv = (IInventory)gen;
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                final IWidget widg = new WidgetSlot(inv, i, x, y);
                this.widgets.add(widg);
                x += widg.getW() + 5;
            }
        }
        if (gen instanceof IFluidHandler) {
            final FluidTankInfo[] arr$;
            final FluidTankInfo[] tanks = arr$ = gen.getTankInfo(null);
            for (final FluidTankInfo tank : arr$) {
                final IWidget widg2 = new WidgetTank(tank, x, y, 2);
                this.widgets.add(widg2);
                x += widg2.getW() + 5;
            }
        }
        final IWidget w = new WidgetTextCooldown(gen, x, y, 120);
        this.widgets.add(w);
        x += w.getW() + 5;
        this.widgets.add(new WidgetEnergy(gen, ForgeDirection.UP, x, y));
        this.cropAndAddPlayerSlots(player);
        this.validate();
    }
    
    public boolean canInteractWith(final EntityPlayer entityplayer) {
        return true;
    }
    
    public class WidgetTextCooldown extends WidgetTextData
    {
        TileEntityGenerator gen;
        
        public WidgetTextCooldown(final TileEntityGenerator gen, final int x, final int y, final int w) {
            super(x, y, w);
            this.gen = gen;
        }
        
        @Override
        public int getNumParams() {
            return 2;
        }
        
        @Override
        public Object[] getData() {
            return new Object[] { (long)(10.0 * this.gen.coolDown), (long)Math.ceil(10.0 * this.gen.genLevel() * this.gen.getMultiplier()) };
        }
        
        @Override
        public String getConstructedText() {
            if (this.curData == null || this.curData[0] == null) {
                return "";
            }
            double t;
            double t2;
            try {
                t = (Long)this.curData[0] / 200.0;
                t2 = (Long)this.curData[1] / 10.0;
            }
            catch (Exception e) {
                return "";
            }
            return this.gen.getBlurb(t, t2);
        }
    }
}


