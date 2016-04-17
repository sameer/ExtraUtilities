// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item.scanner;

import net.minecraftforge.common.IShearable;
import cofh.api.energy.IEnergyHandler;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagList;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.nbt.NBTBase;
import java.util.Collection;
import java.io.IOException;
import java.io.DataOutput;
import net.minecraft.nbt.CompressedStreamTools;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;

public class ScannerRegistry
{
    public static List<IScanner> scanners;
    private static boolean isSorted;
    
    public static void addScanner(final IScanner scan) {
        ScannerRegistry.scanners.add(scan);
        ScannerRegistry.isSorted = false;
    }
    
    public static void sort() {
        Collections.sort(ScannerRegistry.scanners, new SortScanners());
        ScannerRegistry.isSorted = true;
    }
    
    public static List<String> getData(final Object obj, final ForgeDirection side, final EntityPlayer player) {
        final List<String> data = new ArrayList<String>();
        if (!ScannerRegistry.isSorted) {
            sort();
        }
        for (final IScanner scan : ScannerRegistry.scanners) {
            if (scan.getTargetClass().isAssignableFrom(obj.getClass())) {
                scan.addData(obj, data, side, player);
            }
        }
        return data;
    }
    
    static {
        ScannerRegistry.scanners = new ArrayList<IScanner>();
        ScannerRegistry.isSorted = false;
        addScanner(new scanTE());
        addScanner(new scanEntity());
        addScanner(new scanInv());
        addScanner(new scanSidedInv());
        addScanner(new scanTank());
        addScanner(new scanTE3Power());
    }
    
    public static class SortScanners implements Comparator<IScanner>
    {
        @Override
        public int compare(final IScanner arg0, final IScanner arg1) {
            final int a = arg0.getPriority();
            final int b = arg1.getPriority();
            if (a == b) {
                return 0;
            }
            if (a < b) {
                return -1;
            }
            return 1;
        }
    }
    
    public static class scanTE implements IScanner
    {
        @Override
        public Class getTargetClass() {
            return TileEntity.class;
        }
        
        @Override
        public void addData(final Object tile, final List<String> data, final ForgeDirection side, final EntityPlayer player) {
            final NBTTagCompound tags = new NBTTagCompound();
            ((TileEntity)tile).writeToNBT(tags);
            data.add("~~ " + tags.getString("id") + " ~~");
            final ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            final DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
            try {
                try {
                    CompressedStreamTools.write(tags, (DataOutput)dataoutputstream);
                    data.add("Tile Data: " + bytearrayoutputstream.size());
                }
                finally {
                    dataoutputstream.close();
                }
            }
            catch (IOException ex) {}
            if (player.capabilities.isCreativeMode) {
                data.addAll(this.getString(tags));
            }
        }
        
        public List<String> getString(final NBTTagCompound tag) {
            final List<String> v = new ArrayList<String>();
            this.appendStrings(v, (NBTBase)tag, "", "Tags");
            return v;
        }
        
        public void appendStrings(final List<String> strings, final NBTBase nbt, final String prefix, final String name) {
            if (nbt.getId() == XUHelper.NBTIds.NBT.id) {
                final NBTTagCompound tag = (NBTTagCompound)nbt;
                if (tag.func_150296_c().isEmpty()) {
                    strings.add(prefix + name + " = NBT{}");
                }
                else {
                    strings.add(prefix + name + " = NBT{");
                    final ArrayList<String> l = new ArrayList<String>();
                    l.addAll(tag.func_150296_c());
                    Collections.sort(l);
                    for (final String key : l) {
                        this.appendStrings(strings, tag.getTag(key), prefix + "   ", key);
                    }
                    strings.add(prefix + "}");
                }
            }
            else if (nbt.getId() == XUHelper.NBTIds.List.id) {
                final NBTTagList tag2 = (NBTTagList)nbt;
                if (tag2.tagCount() == 0) {
                    strings.add(prefix + name + " = List{}");
                }
                else {
                    strings.add(prefix + name + " = List{");
                    for (int i = 0; i < tag2.tagCount(); ++i) {
                        this.appendStrings(strings, (NBTBase)tag2.getCompoundTagAt(i), prefix + "   ", i + "");
                    }
                    strings.add(prefix + "}");
                }
            }
            else {
                strings.add(prefix + name + " = " + nbt.toString());
            }
        }
        
        @Override
        public int getPriority() {
            return -2147483647;
        }
    }
    
    public static class scanEntity implements IScanner
    {
        @Override
        public Class getTargetClass() {
            return Entity.class;
        }
        
        @Override
        public void addData(final Object tile, final List<String> data, final ForgeDirection side, final EntityPlayer player) {
            final NBTTagCompound tags = new NBTTagCompound();
            if (((Entity)tile).writeToNBTOptional(tags)) {
                data.add("~~ " + tags.getString("id") + " ~~");
                data.add("Entity Data: " + tags.toString().length());
            }
        }
        
        @Override
        public int getPriority() {
            return Integer.MIN_VALUE;
        }
    }
    
    public static class scanEntityLiv implements IScanner
    {
        @Override
        public Class getTargetClass() {
            return EntityLivingBase.class;
        }
        
        @Override
        public void addData(final Object target, final List<String> data, final ForgeDirection side, final EntityPlayer player) {
            final EntityLivingBase e = (EntityLivingBase)target;
            data.add(e.getHealth() + " / " + e.getMaxHealth());
        }
        
        @Override
        public int getPriority() {
            return -110;
        }
    }
    
    public static class scanInv implements IScanner
    {
        @Override
        public Class getTargetClass() {
            return IInventory.class;
        }
        
        @Override
        public void addData(final Object tile, final List<String> data, final ForgeDirection side, final EntityPlayer player) {
            final int n = ((IInventory)tile).getSizeInventory();
            if (n > 0) {
                int k = 0;
                for (int i = 0; i < n; ++i) {
                    if (((IInventory)tile).getStackInSlot(i) != null) {
                        ++k;
                    }
                }
                data.add("Inventory Slots: " + k + " / " + n);
            }
        }
        
        @Override
        public int getPriority() {
            return -100;
        }
    }
    
    public static class scanSidedInv implements IScanner
    {
        @Override
        public Class getTargetClass() {
            return ISidedInventory.class;
        }
        
        @Override
        public void addData(final Object tile, final List<String> data, final ForgeDirection side, final EntityPlayer player) {
            final int[] slots = ((ISidedInventory)tile).getAccessibleSlotsFromSide(side.ordinal());
            int k = 0;
            if (slots.length > 0) {
                for (int i = 0; i < slots.length; ++i) {
                    if (((ISidedInventory)tile).getStackInSlot(i) != null) {
                        ++k;
                    }
                }
                data.add("Inventory Side Slots: " + k + " / " + slots.length);
            }
        }
        
        @Override
        public int getPriority() {
            return -99;
        }
    }
    
    public static class scanTank implements IScanner
    {
        @Override
        public Class getTargetClass() {
            return IFluidHandler.class;
        }
        
        @Override
        public void addData(final Object tile, final List<String> data, final ForgeDirection side, final EntityPlayer player) {
            final FluidTankInfo[] tanks = ((IFluidHandler)tile).getTankInfo(side);
            if (tanks != null) {
                if (tanks.length == 1) {
                    if (tanks[0].fluid != null && tanks[0].fluid.amount > 0) {
                        data.add("Fluid Tank: " + tanks[0].fluid.getFluid().getLocalizedName(tanks[0].fluid) + " - " + tanks[0].fluid.amount + " / " + tanks[0].capacity);
                    }
                    else {
                        data.add("Fluid Tank: Empty - 0 / " + tanks[0].capacity);
                    }
                }
                else {
                    for (int i = 0; i < tanks.length; ++i) {
                        if (tanks[i].fluid != null && tanks[i].fluid.amount > 0) {
                            data.add("Fluid Tank " + i + ": " + tanks[i].fluid.getFluid().getLocalizedName(tanks[i].fluid) + " - " + tanks[i].fluid.amount + " / " + tanks[i].capacity);
                        }
                        else {
                            data.add("Fluid Tank " + i + ": Empty - 0 / " + tanks[i].capacity);
                        }
                    }
                }
            }
        }
        
        @Override
        public int getPriority() {
            return -98;
        }
    }
    
    public static class scanTE3Power implements IScanner
    {
        @Override
        public Class getTargetClass() {
            return IEnergyHandler.class;
        }
        
        @Override
        public void addData(final Object tile, final List<String> data, final ForgeDirection side, final EntityPlayer player) {
            final IEnergyHandler a = (IEnergyHandler)tile;
            data.add(" TE3 Side Energy: " + a.getEnergyStored(side) + " / " + a.getMaxEnergyStored(side));
        }
        
        @Override
        public int getPriority() {
            return 0;
        }
    }
    
    public static class scanShears implements IScanner
    {
        @Override
        public Class getTargetClass() {
            return IShearable.class;
        }
        
        @Override
        public void addData(final Object target, final List<String> data, final ForgeDirection side, final EntityPlayer player) {
            final IShearable a = (IShearable)target;
            data.add("- Shearable");
        }
        
        @Override
        public int getPriority() {
            return 0;
        }
    }
}
