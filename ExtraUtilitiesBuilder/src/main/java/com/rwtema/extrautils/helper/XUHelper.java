// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.helper;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.chunk.Chunk;
import com.rwtema.extrautils.core.CastIterator;
import java.util.Locale;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import gnu.trove.iterator.TIntIterator;
import java.util.Collection;
import gnu.trove.list.linked.TIntLinkedList;
import com.rwtema.extrautils.tileentity.transfernodes.InvHelper;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import com.rwtema.extrautils.LogHelper;
import net.minecraft.nbt.NBTBase;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraft.init.Blocks;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.world.World;
import java.util.Iterator;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Random;

public class XUHelper
{
    public static boolean deObf;
    public static final Random rand;
    public static final String[] dyes;
    public static final int[] dyeCols;
    private static long timer;
    static final ArrayList<Class<?>> wrenchClazzes;
    private static final UUID temaID;
    public static Random random;
    
    public static boolean isWrench(final ItemStack itemstack) {
        if (itemstack == null) {
            return false;
        }
        final Item item = itemstack.getItem();
        if (item == null) {
            return false;
        }
        if (item == Items.stick) {
            return true;
        }
        for (final Class<?> wrenchClazz : XUHelper.wrenchClazzes) {
            if (wrenchClazz.isAssignableFrom(item.getClass())) {
                return true;
            }
        }
        return false;
    }
    
    public static FluidStack drainBlock(final World world, final int x, final int y, final int z, final boolean doDrain) {
        final Block id = world.getBlock(x, y, z);
        if (id == Blocks.flowing_lava || id == Blocks.lava) {
            FluidStack liquid;
            if (world.getBlockMetadata(x, y, z) == 0) {
                liquid = new FluidStack(FluidRegistry.LAVA, 1000);
            }
            else {
                liquid = new FluidStack(FluidRegistry.LAVA, 0);
            }
            if (doDrain) {
                world.setBlockToAir(x, y, z);
            }
            return liquid;
        }
        if (id == Blocks.flowing_water || id == Blocks.water) {
            FluidStack liquid;
            if (world.getBlockMetadata(x, y, z) == 0) {
                liquid = new FluidStack(FluidRegistry.WATER, 1000);
            }
            else {
                liquid = new FluidStack(FluidRegistry.WATER, 0);
            }
            if (doDrain) {
                world.setBlockToAir(x, y, z);
            }
            return liquid;
        }
        if (id instanceof IFluidBlock) {
            final IFluidBlock fluidBlock = (IFluidBlock)id;
            if (fluidBlock.getFluid() != null && fluidBlock.canDrain(world, x, y, z)) {
                return fluidBlock.drain(world, x, y, z, doDrain);
            }
        }
        return null;
    }
    
    public static String getAnvilName(final ItemStack item) {
        String s = "";
        if (item != null && item.getTagCompound() != null && item.getTagCompound().hasKey("display")) {
            final NBTTagCompound nbttagcompound = item.getTagCompound().getCompoundTag("display");
            if (nbttagcompound.hasKey("Name")) {
                s = nbttagcompound.getString("Name");
            }
        }
        return s;
    }
    
    public static boolean hasPersistantNBT(final Entity entity) {
        return entity.getEntityData().hasKey("PlayerPersisted", 10);
    }
    
    public static NBTTagCompound getPersistantNBT(final Entity entity) {
        final NBTTagCompound t = entity.getEntityData();
        if (!t.hasKey("PlayerPersisted", 10)) {
            final NBTTagCompound tag = new NBTTagCompound();
            t.setTag("PlayerPersisted", (NBTBase)tag);
            return tag;
        }
        return t.getCompoundTag("PlayerPersisted");
    }
    
    public static String getPlayerOwner(final ItemStack item) {
        String s = "";
        if (item != null && item.getTagCompound() != null && item.getTagCompound().hasKey("XU:owner")) {
            s = item.getTagCompound().getString("XU:owner");
        }
        return s;
    }
    
    public static void setPlayerOwner(final ItemStack item, final String s) {
        if (item != null) {
            NBTTagCompound tags = item.getTagCompound();
            if (tags == null) {
                tags = new NBTTagCompound();
            }
            if (s == null || s.equals("")) {
                if (tags.hasKey("XU:owner")) {
                    tags.removeTag("XU:owner");
                    if (tags.hasNoTags()) {
                        item.setTagCompound((NBTTagCompound)null);
                    }
                    else {
                        item.setTagCompound(tags);
                    }
                }
            }
            else {
                tags.setString("XU:owner", s);
                item.setTagCompound(tags);
            }
        }
    }
    
    public static void resetTimer() {
        XUHelper.timer = System.nanoTime();
    }
    
    public static void printTimer(final String t) {
        LogHelper.debug("time:" + t + " - " + (System.nanoTime() - XUHelper.timer) / 1000000.0, new Object[0]);
        XUHelper.timer = System.nanoTime();
    }
    
    public static int getDyeFromItemStack(final ItemStack dye) {
        if (dye == null) {
            return -1;
        }
        if (dye.getItem() == Items.dye) {
            return dye.getItemDamage();
        }
        for (int i = 0; i < 16; ++i) {
            for (final ItemStack target : OreDictionary.getOres(XUHelper.dyes[i])) {
                if (OreDictionary.itemMatches(target, dye, false)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public static int rndInt(final int n) {
        if (n <= 0) {
            return 0;
        }
        return XUHelper.rand.nextInt(n);
    }
    
    public static String getFluidName(final FluidStack fluid) {
        if (fluid == null || fluid.getFluid() == null) {
            return "ERROR";
        }
        String s = fluid.getFluid().getLocalizedName(fluid);
        if (s == null) {
            return "ERROR";
        }
        if (s.equals("")) {
            s = "Unnamed liquid";
        }
        if (s.equals(fluid.getFluid().getUnlocalizedName())) {
            s = fluid.getFluid().getName();
            if (s.equals(s.toLowerCase())) {
                s = s.substring(0, 1).toUpperCase() + s.substring(1, s.length());
            }
        }
        return s;
    }
    
    public static Block safeBlockId(final World world, final int x, final int y, final int z) {
        return safeBlockId(world, x, y, z, Blocks.air);
    }
    
    public static int[] getSlots(final int k) {
        final int[] slots = new int[k];
        for (int i = 0; i < k; ++i) {
            slots[i] = i;
        }
        return slots;
    }
    
    public static TileEntity safegetTileEntity(final World world, final int x, final int y, final int z) {
        if (world.blockExists(x, y, z)) {
            return world.getTileEntity(x, y, z);
        }
        return null;
    }
    
    public static Block safeBlockId(final World world, final int x, final int y, final int z, final Block falseReturnValue) {
        if (world.blockExists(x, y, z)) {
            return world.getBlock(x, y, z);
        }
        return falseReturnValue;
    }
    
    public static boolean canItemsStack(final ItemStack a, final ItemStack b) {
        return canItemsStack(a, b, false, true);
    }
    
    public static ItemStack invInsert(final IInventory inv, ItemStack item, final int side) {
        if (item != null && item.stackSize > 0) {
            final boolean nonSided = !(inv instanceof ISidedInventory);
            int empty = -1;
            int filter = -1;
            final int maxStack = filter = Math.min(item.getMaxStackSize(), inv.getInventoryStackLimit());
            boolean flag = false;
            for (int i : InvHelper.getSlots(inv, side)) {
                final ItemStack dest = inv.getStackInSlot(i);
                if (dest == null) {
                    if (empty == -1 && inv.isItemValidForSlot(i, item) && (nonSided || ((ISidedInventory)inv).canInsertItem(i, item, side))) {
                        empty = i;
                    }
                }
                else if (InvHelper.canStack(item, dest) && inv.isItemValidForSlot(i, item) && (nonSided || ((ISidedInventory)inv).canInsertItem(i, item, side)) && maxStack - dest.stackSize > 0 && filter > 0) {
                    final int l = Math.min(Math.min(item.stackSize, maxStack - dest.stackSize), filter);
                    if (l > 0) {
                        final ItemStack itemStack = dest;
                        itemStack.stackSize += l;
                        final ItemStack itemStack2 = item;
                        itemStack2.stackSize -= l;
                        filter -= l;
                        flag = true;
                        if (item.stackSize <= 0) {
                            item = null;
                            break;
                        }
                        if (filter <= 0) {
                            break;
                        }
                    }
                }
            }
            if (filter > 0 && item != null && empty != -1 && inv.isItemValidForSlot(empty, item) && (nonSided || ((ISidedInventory)inv).canInsertItem(empty, item, side))) {
                if (filter < item.stackSize) {
                    inv.setInventorySlotContents(empty, item.splitStack(filter));
                }
                else {
                    inv.setInventorySlotContents(empty, item);
                    item = null;
                }
                flag = true;
            }
            if (flag) {
                inv.markDirty();
            }
        }
        return item;
    }
    
    public static ItemStack[] simMassInvInsert(final IInventory inv, final ItemStack[] items, final int side) {
        final TIntLinkedList resultInd = new TIntLinkedList();
        final ItemStack[] result = new ItemStack[items.length];
        for (int i = 0; i < result.length; ++i) {
            if (items[i] != null && items[i].stackSize > 0) {
                result[i] = items[i].copy();
                resultInd.add(i);
            }
        }
        return simMassInvInsert_do(inv, side, resultInd, result);
    }
    
    public static ItemStack[] simMassInvInsert(final IInventory inv, final Collection<ItemStack> items, final int side) {
        final TIntLinkedList resultInd = new TIntLinkedList();
        final ItemStack[] result = new ItemStack[items.size()];
        int i = 0;
        for (final ItemStack item : items) {
            if (item != null && item.stackSize > 0) {
                result[i] = item.copy();
                resultInd.add(i);
            }
            ++i;
        }
        return simMassInvInsert_do(inv, side, resultInd, result);
    }
    
    public static ItemStack[] simMassInvInsert_do(final IInventory inv, final int side, final TIntLinkedList resultInd, final ItemStack[] result) {
        if (resultInd.isEmpty()) {
            return null;
        }
        final int[] slots = InvHelper.getSlots(inv, side);
        ISidedInventory invS = null;
        final boolean sided = inv instanceof ISidedInventory;
        if (sided) {
            invS = (ISidedInventory)inv;
        }
        final int maxStack = inv.getInventoryStackLimit();
        final TIntLinkedList emptySlots = new TIntLinkedList();
        for (final int i : slots) {
            final ItemStack curStack = inv.getStackInSlot(i);
            if (curStack == null) {
                emptySlots.add(i);
            }
            else {
                final int m = Math.min(maxStack, curStack.getMaxStackSize()) - curStack.stackSize;
                if (m > 0) {
                    final TIntIterator resultIterator = resultInd.iterator();
                    while (resultIterator.hasNext()) {
                        final int j = resultIterator.next();
                        final ItemStack itemStack = result[j];
                        if (itemStack == null) {
                            continue;
                        }
                        if (!canItemsStack(itemStack, curStack) || !inv.isItemValidForSlot(i, itemStack)) {
                            continue;
                        }
                        if (invS != null && !invS.canInsertItem(i, itemStack, side)) {
                            continue;
                        }
                        final ItemStack itemStack3 = itemStack;
                        itemStack3.stackSize -= m;
                        if (itemStack.stackSize > 0) {
                            continue;
                        }
                        result[j] = null;
                        resultIterator.remove();
                        if (resultInd.isEmpty()) {
                            return null;
                        }
                    }
                }
            }
        }
        if (emptySlots.isEmpty()) {
            return result;
        }
        for (int i = 0; i < emptySlots.size(); i++) {
	    final int k = emptySlots.get(i);
            final TIntIterator resultIterator2 = resultInd.iterator();
            while (resultIterator2.hasNext()) {
                final int l = resultIterator2.next();
                final ItemStack itemStack2 = result[l];
                if (itemStack2 == null) {
                    continue;
                }
                if (!inv.isItemValidForSlot(k, itemStack2)) {
                    continue;
                }
                if (invS != null && !invS.canInsertItem(k, itemStack2, side)) {
                    continue;
                }
                final ItemStack itemStack4 = itemStack2;
                itemStack4.stackSize -= maxStack;
                if (itemStack2.stackSize > 0) {
                    continue;
                }
                result[l] = null;
                resultIterator2.remove();
            }
        }
        if (resultInd.isEmpty()) {
            return null;
        }
        return result;
    }
    
    public static ItemStack simInvInsert(final IInventory inv, ItemStack item, final int side) {
        if (item == null || item.stackSize <= 0) {
            return item;
        }
        item = item.copy();
        final boolean nonSided = !(inv instanceof ISidedInventory);
        int empty = -1;
        int filter = -1;
        final int maxStack = filter = Math.min(item.getMaxStackSize(), inv.getInventoryStackLimit());
        for (final int i : InvHelper.getSlots(inv, side)) {
            final ItemStack dest = inv.getStackInSlot(i);
            if (dest == null) {
                if (empty == -1 && inv.isItemValidForSlot(i, item) && (nonSided || ((ISidedInventory)inv).canInsertItem(i, item, side))) {
                    empty = i;
                }
            }
            else if (InvHelper.canStack(item, dest) && inv.isItemValidForSlot(i, item) && (nonSided || ((ISidedInventory)inv).canInsertItem(i, item, side)) && maxStack - dest.stackSize > 0) {
                if (filter > 0) {
                    final int l = Math.min(Math.min(item.stackSize, maxStack - dest.stackSize), filter);
                    if (l > 0) {
                        final ItemStack itemStack = item;
                        itemStack.stackSize -= l;
                        filter -= l;
                        if (item.stackSize <= 0) {
                            item = null;
                            break;
                        }
                        if (filter <= 0) {
                            break;
                        }
                    }
                }
            }
        }
        if (filter > 0 && item != null && empty != -1 && inv.isItemValidForSlot(empty, item) && (nonSided || ((ISidedInventory)inv).canInsertItem(empty, item, side))) {
            if (filter < item.stackSize) {
                final ItemStack itemStack2 = item;
                itemStack2.stackSize -= filter;
            }
            else {
                item = null;
            }
        }
        return item;
    }
    
    public static int[] getInventorySideSlots(final IInventory inv, final ForgeDirection side) {
        return getInventorySideSlots(inv, side.ordinal());
    }
    
    public static int[] getInventorySideSlots(final IInventory inv, final int side) {
        if (inv instanceof ISidedInventory) {
            return ((ISidedInventory)inv).getAccessibleSlotsFromSide(side);
        }
        final int[] arr = new int[inv.getSizeInventory()];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = i;
        }
        return arr;
    }
    
    public static boolean canItemsStack(final ItemStack a, final ItemStack b, final boolean ignoreDurability, final boolean ignoreStackLimits) {
        return canItemsStack(a, b, ignoreDurability, ignoreStackLimits, false);
    }
    
    public static boolean canItemsStack(final ItemStack a, final ItemStack b, final boolean ignoreDurability, final boolean ignoreStackLimits, final boolean ignoreNBT) {
        if (a == null || b == null) {
            return false;
        }
        if (a.getItem() != b.getItem()) {
            return false;
        }
        if (!ignoreDurability && a.getItemDamage() != b.getItemDamage()) {
            return false;
        }
        if (!ignoreStackLimits) {
            if (!a.isStackable() || a.stackSize >= a.getMaxStackSize()) {
                return false;
            }
            if (!b.isStackable() || b.stackSize >= b.getMaxStackSize()) {
                return false;
            }
        }
        return ignoreNBT || ItemStack.areItemStackTagsEqual(a, b);
    }
    
    public static boolean contains(final ISidedInventory inv, final int side, final ItemStack s) {
        return false;
    }
    
    public static int[] rndSeq(final int n, final Random rand) {
        final int[] rnd = new int[n];
        final int t = -1;
        for (int i = 1; i < n; ++i) {
            final int j = rand.nextInt(i + 1);
            rnd[i] = rnd[j];
            rnd[j] = i;
        }
        return rnd;
    }
    
    public static String s(final int k) {
        return (k == 0) ? "" : "s";
    }
    
    public static boolean isPlayerReal(final EntityPlayer player) {
        return !isPlayerFake(player);
    }
    
    public static boolean isPlayerReal(final EntityPlayerMP player) {
        return !isPlayerFake(player);
    }
    
    public static boolean isPlayerFake(final EntityPlayer player) {
        return player.worldObj == null || (!player.worldObj.isRemote && player.getClass() != EntityPlayerMP.class && !MinecraftServer.getServer().getConfigurationManager().playerEntityList.contains(player));
    }
    
    public static boolean isPlayerFake(final EntityPlayerMP player) {
        if (player.getClass() != EntityPlayerMP.class) {
            return true;
        }
        if (player.playerNetServerHandler == null) {
            return true;
        }
        try {
            player.getPlayerIP().length();
            player.playerNetServerHandler.netManager.getSocketAddress().toString();
        }
        catch (Exception e) {
            return true;
        }
        return !MinecraftServer.getServer().getConfigurationManager().playerEntityList.contains(player);
    }
    
    public static boolean isThisPlayerACheatyBastardOfCheatBastardness(final EntityPlayer player) {
        return !isPlayerFake(player) && isTema(player.getGameProfile());
    }
    
    public static boolean isTema(final GameProfile gameProfile) {
        return isTema(gameProfile.getName(), gameProfile.getId());
    }
    
    private static boolean isTema(final String name, final UUID id) {
        return "RWTema".equals(name) && id.equals(XUHelper.temaID);
    }
    
    public static String addThousandsCommas(final int a) {
        return String.format(Locale.ENGLISH, "%,d", a);
    }
    
    public static TileEntity getNearestTile(final World world, final int x, final int y, final int z, int r, final Class<? extends TileEntity> clazz) {
        TileEntity closestTile = null;
        r *= r;
        double dist = 2.147483647E9;
        for (int cx = x - r >> 4; cx <= x + r >> 4; ++cx) {
            for (int cz = z - r >> 4; cz <= z + r >> 4; ++cz) {
                final Chunk c = world.getChunkFromChunkCoords(cx, cz);
                for (final TileEntity tile : new CastIterator<TileEntity>(c.chunkTileEntityMap.values())) {
                    if (!tile.isInvalid()) {
                        if (!tile.getClass().equals(clazz)) {
                            continue;
                        }
                        final double d = dist2(tile.xCoord - x, tile.yCoord - y, tile.zCoord - z);
                        if (d > r) {
                            continue;
                        }
                        if (d >= dist && closestTile != null) {
                            continue;
                        }
                        dist = d;
                        closestTile = tile;
                    }
                }
            }
        }
        return closestTile;
    }
    
    public static double dist2(final double dx, final double dy, final double dz) {
        return dx * dx + dy * dy + dz * dz;
    }
    
    public static void dropItem(final World world, final int x, final int y, final int z, final ItemStack itemstack) {
        if (itemstack != null) {
            final float dx = XUHelper.random.nextFloat() * 0.8f + 0.1f;
            final float dy = XUHelper.random.nextFloat() * 0.8f + 0.1f;
            final float dz = XUHelper.random.nextFloat() * 0.8f + 0.1f;
            while (itemstack.stackSize > 0) {
                int k1 = XUHelper.random.nextInt(21) + 10;
                if (k1 > itemstack.stackSize) {
                    k1 = itemstack.stackSize;
                }
                itemstack.stackSize -= k1;
                final EntityItem entityitem = new EntityItem(world, (double)(x + dx), (double)(y + dy), (double)(z + dz), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
                final float f3 = 0.05f;
                entityitem.motionX = (float)XUHelper.random.nextGaussian() * f3;
                entityitem.motionY = (float)XUHelper.random.nextGaussian() * f3 + 0.2f;
                entityitem.motionZ = (float)XUHelper.random.nextGaussian() * f3;
                if (itemstack.hasTagCompound()) {
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                }
                world.spawnEntityInWorld((Entity)entityitem);
            }
        }
    }
    
    public static boolean isFluidBlock(final Block b) {
        return b == Blocks.water || b == Blocks.lava || b instanceof IFluidBlock;
    }
    
    public static String niceFormat(final double v) {
        String format;
        if (v == (int)v) {
            format = String.format(Locale.ENGLISH, "%d", (int)v);
        }
        else {
            format = String.format(Locale.ENGLISH, "%s", v);
        }
        return format;
    }
    
    public static ItemStack newRoll() {
        return addLore(addEnchant(new ItemStack(Items.record_13, 1, 101).setStackDisplayName("Rick Astley - Never gonna give you up!"), Enchantment.unbreaking, 1), "Awesome music to exercise to.", "The greatest gift a pretty fairy could ask for.", "Were you expecting something else?");
    }
    
    public static ItemStack addEnchant(final ItemStack stack, final Enchantment enchantment, final int level) {
        stack.addEnchantment(enchantment, level);
        return stack;
    }
    
    public static ItemStack addLore(final ItemStack a, final String... lore) {
        NBTTagCompound tag = a.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        if (!tag.hasKey("display", 10)) {
            tag.setTag("display", (NBTBase)new NBTTagCompound());
        }
        final NBTTagList l = new NBTTagList();
        for (final String s : lore) {
            l.appendTag((NBTBase)new NBTTagString(s));
        }
        tag.getCompoundTag("display").setTag("Lore", (NBTBase)l);
        a.setTagCompound(tag);
        return a;
    }
    
    public static UUID createUUID(final String a, final String b, final String c, final String d) {
        final long u = a.hashCode() | b.hashCode() >> 32;
        final long v = c.hashCode() | d.hashCode() >> 32;
        return new UUID(u, v);
    }
    
    public static NBTTagCompound writeInventoryBasicToNBT(final NBTTagCompound tag, final InventoryBasic inventoryBasic) {
        if (inventoryBasic.hasCustomInventoryName()) {
            tag.setString("CustomName", inventoryBasic.getInventoryName());
        }
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < inventoryBasic.getSizeInventory(); ++i) {
            final ItemStack stackInSlot = inventoryBasic.getStackInSlot(i);
            if (stackInSlot != null) {
                final NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte)i);
                stackInSlot.writeToNBT(itemTag);
                nbttaglist.appendTag((NBTBase)itemTag);
            }
        }
        tag.setTag("Items", (NBTBase)nbttaglist);
        return tag;
    }
    
    public static NBTTagCompound readInventoryBasicFromNBT(final NBTTagCompound tag, final InventoryBasic inventoryBasic) {
        if (tag.hasKey("CustomName", 8)) {
            inventoryBasic.func_110133_a(tag.getString("CustomName"));
        }
        final NBTTagList items = tag.getTagList("Items", 10);
        for (int i = 0; i < items.tagCount(); ++i) {
            final NBTTagCompound itemTag = items.getCompoundTagAt(i);
            final int j = itemTag.getByte("Slot") & 0xFF;
            if (j >= 0 && j < inventoryBasic.getSizeInventory()) {
                inventoryBasic.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(itemTag));
            }
        }
        return tag;
    }
    
    static {
        XUHelper.deObf = false;
        rand = XURandom.getInstance();
        dyes = new String[] { "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" };
        dyeCols = new int[16];
        for (int i = 0; i < 16; ++i) {
            final float[] cols = EntitySheep.fleeceColorTable[i];
            final int r = (int)(cols[0] * 255.0f);
            final int g = (int)(cols[1] * 255.0f);
            final int b = (int)(cols[2] * 255.0f);
            XUHelper.dyeCols[i] = (r << 16 | g << 8 | b);
        }
        XUHelper.timer = 0L;
        final String[] wrenchClassNames = { "buildcraft.api.tools.IToolWrench", "cofh.api.item.IToolHammer", "powercrystals.minefactoryreloaded.api.IMFRHammer", "appeng.api.implementations.items.IAEWrench", "crazypants.enderio.api.tool.ITool" };
        wrenchClazzes = new ArrayList<Class<?>>();
        for (final String wrenchClassName : wrenchClassNames) {
            try {
                XUHelper.wrenchClazzes.add(Class.forName(wrenchClassName));
                LogHelper.fine("Detected wrench class: " + wrenchClassName, new Object[0]);
            }
            catch (ClassNotFoundException ex) {}
        }
        temaID = UUID.fromString("72ddaa05-7bbe-4ae2-9892-2c8d90ea0ad8");
        XUHelper.random = XURandom.getInstance();
    }
    
    public enum NBTIds
    {
        End(0), 
        Byte(1), 
        Short(2), 
        Int(3), 
        Long(4), 
        Float(5), 
        Double(6), 
        ByteArray(7), 
        String(8), 
        List(9), 
        NBT(10), 
        IntArray(12);
        
        public final int id;
        
        private NBTIds(final int i) {
            this.id = i;
        }
    }
}
