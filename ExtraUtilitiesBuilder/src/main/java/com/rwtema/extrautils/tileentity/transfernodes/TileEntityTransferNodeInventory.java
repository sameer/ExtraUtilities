// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes;

import com.rwtema.extrautils.block.Box;
import com.rwtema.extrautils.block.BoxModel;
import com.rwtema.extrautils.item.ItemNodeUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.tileentity.TileEntity;
import com.rwtema.extrautils.inventory.LiquidInventory;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraft.init.Blocks;
import java.util.Iterator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.Facing;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.ItemBuffer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.inventory.ISidedInventory;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeInventory;

public class TileEntityTransferNodeInventory extends TileEntityTransferNode implements INodeInventory, ISidedInventory
{
    private static final int[] contents;
    private static final int[] nullcontents;
    private static InvCrafting crafting;
    private static ForgeDirection[] orthY;
    private static ForgeDirection[] orthX;
    private boolean hasCStoneGen;
    private int genCStoneCounter;
    private long checkTimer;
    private IRecipe cachedRecipe;
    private int prevStack;
    private boolean delay;
    private boolean isDirty;
    
    public TileEntityTransferNodeInventory() {
        super("Inv", new ItemBuffer());
        this.hasCStoneGen = false;
        this.genCStoneCounter = 0;
        this.checkTimer = 0L;
        this.pr = 1.0f;
        this.pg = 0.0f;
        this.pb = 0.0f;
        this.cachedRecipe = null;
        this.prevStack = 0;
        this.delay = false;
        this.isDirty = false;
    }
    
    public TileEntityTransferNodeInventory(final String txt, final INodeBuffer buffer) {
        super(txt, buffer);
        this.hasCStoneGen = false;
        this.genCStoneCounter = 0;
        this.checkTimer = 0L;
        this.pr = 1.0f;
        this.pg = 0.0f;
        this.pb = 0.0f;
        this.cachedRecipe = null;
        this.prevStack = 0;
        this.delay = false;
        this.isDirty = false;
    }
    
    public static IRecipe findMatchingRecipe(final InventoryCrafting inv, final World world) {
        for (int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); ++i) {
            final IRecipe recipe = (IRecipe)CraftingManager.getInstance().getRecipeList().get(i);
            if (recipe.matches(inv, world)) {
                return recipe;
            }
        }
        return null;
    }
    
    private static int getFirstExtractableItemStackSlot(final IInventory inv, final int side) {
        for (final int i : XUHelper.getInventorySideSlots(inv, side)) {
            final ItemStack item = inv.getStackInSlot(i);
            if (item != null && item.stackSize > 0 && (!(inv instanceof ISidedInventory) || ((ISidedInventory)inv).canExtractItem(i, item, side))) {
                if (!item.getItem().hasContainerItem(item)) {
                    return i;
                }
                final ItemStack t = item.getItem().getContainerItem(item);
                for (final int j : XUHelper.getInventorySideSlots(inv, side)) {
                    if (((j != i && inv.getStackInSlot(j) == null) || (j == i && item.stackSize == 1)) && inv.isItemValidForSlot(j, t) && (!(inv instanceof ISidedInventory) || ((ISidedInventory)inv).canInsertItem(i, t, side))) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);
    }
    
    @Override
    public void processBuffer() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            if (this.coolDown > 0) {
                this.coolDown -= this.stepCoolDown;
            }
            if (this.checkRedstone()) {
                return;
            }
            this.startDelayMarkDirty();
            while (this.coolDown <= 0) {
                this.coolDown += TileEntityTransferNodeInventory.baseMaxCoolDown;
                if (this.handleInventories()) {
                    this.advPipeSearch();
                }
                this.loadbuffer();
            }
            this.finishMarkDirty();
        }
    }
    
    public void loadbuffer() {
        if (this.buffer.getBuffer() != null && ((ItemStack)this.buffer.getBuffer()).stackSize >= ((ItemStack)this.buffer.getBuffer()).getMaxStackSize()) {
            return;
        }
        int dir = this.getBlockMetadata() % 6;
        final IInventory inv = TNHelper.getInventory(this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[dir], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir]));
        if (inv != null) {
            if (inv instanceof ISidedInventory) {
                dir = Facing.oppositeSide[dir];
                final ISidedInventory invs = (ISidedInventory)inv;
                final int[] aint = invs.getAccessibleSlotsFromSide(dir);
                for (int i = 0; i < aint.length && (this.buffer.getBuffer() == null || ((ItemStack)this.buffer.getBuffer()).stackSize < ((ItemStack)this.buffer.getBuffer()).getMaxStackSize()); ++i) {
                    final ItemStack itemstack = invs.getStackInSlot(aint[i]);
                    if (itemstack != null && itemstack.stackSize > 0 && (this.buffer.getBuffer() == null || XUHelper.canItemsStack((ItemStack)this.buffer.getBuffer(), itemstack, false, true)) && invs.canExtractItem(aint[i], itemstack, dir)) {
                        final ItemStack itemstack2 = itemstack.copy();
                        ItemStack itemstack3;
                        if (this.upgradeNo(3) == 0) {
                            itemstack3 = XUHelper.invInsert((IInventory)this, invs.decrStackSize(aint[i], 1), -1);
                        }
                        else {
                            itemstack3 = XUHelper.invInsert((IInventory)this, invs.getStackInSlot(aint[i]), -1);
                        }
                        if (this.upgradeNo(3) == 0) {
                            if (itemstack3 == null) {
                                inv.markDirty();
                                return;
                            }
                            inv.setInventorySlotContents(aint[i], itemstack2);
                        }
                        else {
                            inv.setInventorySlotContents(aint[i], itemstack3);
                        }
                        inv.markDirty();
                    }
                }
            }
            else {
                for (int j = inv.getSizeInventory(), k = 0; k < j && (this.buffer.getBuffer() == null || ((ItemStack)this.buffer.getBuffer()).stackSize < ((ItemStack)this.buffer.getBuffer()).getMaxStackSize()); ++k) {
                    final ItemStack itemstack4 = inv.getStackInSlot(k);
                    if (itemstack4 != null && (this.buffer.getBuffer() == null || XUHelper.canItemsStack((ItemStack)this.buffer.getBuffer(), itemstack4, false, true))) {
                        final ItemStack itemstack5 = itemstack4.copy();
                        ItemStack itemstack6;
                        if (this.upgradeNo(3) == 0) {
                            itemstack6 = XUHelper.invInsert((IInventory)this, inv.decrStackSize(k, 1), -1);
                        }
                        else {
                            itemstack6 = XUHelper.invInsert((IInventory)this, inv.getStackInSlot(k), -1);
                        }
                        if (itemstack6 != null && itemstack6.stackSize == 0) {
                            itemstack6 = null;
                        }
                        if (this.upgradeNo(3) == 0) {
                            if (itemstack6 == null) {
                                inv.markDirty();
                                return;
                            }
                            inv.setInventorySlotContents(k, itemstack5);
                        }
                        else {
                            inv.setInventorySlotContents(k, itemstack6);
                        }
                        inv.markDirty();
                    }
                }
            }
        }
        else if (this.upgradeNo(2) > 0) {
            if (this.genCobble()) {
                return;
            }
            if (this.doCraft()) {
                return;
            }
            this.suckItems();
        }
    }
    
    public void startDelayMarkDirty() {
        if (this.delay) {
            throw new RuntimeException("Tile Entity to be marked for delayMarkDirty is already marked as such");
        }
        this.delay = true;
        this.isDirty = false;
    }
    
    public void finishMarkDirty() {
        if (this.isDirty) {
            super.markDirty();
        }
        this.delay = false;
        this.isDirty = false;
    }
    
    @Override
    public void markDirty() {
        if (!this.delay) {
            this.isDirty = false;
            super.markDirty();
        }
        else {
            this.isDirty = true;
        }
    }
    
    private void suckItems() {
        if (this.buffer.getBuffer() == null || ((ItemStack)this.buffer.getBuffer()).stackSize < ((ItemStack)this.buffer.getBuffer()).getMaxStackSize()) {
            final ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() % 6);
            double r = Math.log(this.upgradeNo(2)) / Math.log(2.0);
            if (r > 3.5) {
                r = 3.5;
            }
            for (final Object o : this.worldObj.getEntitiesWithinAABB((Class)EntityItem.class, AxisAlignedBB.getBoundingBox((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1)).offset(dir.offsetX * (1.0 + r), dir.offsetY * (1.0 + r), dir.offsetZ * (1.0 + r)).expand(r, r, r))) {
                final EntityItem item = (EntityItem)o;
                final ItemStack itemstack = item.getEntityItem();
                if (item.isEntityAlive() && itemstack != null && (this.buffer.getBuffer() == null || XUHelper.canItemsStack((ItemStack)this.buffer.getBuffer(), itemstack, false, true))) {
                    ItemStack itemstack2 = itemstack.copy();
                    if (this.upgradeNo(3) == 0) {
                        itemstack2.stackSize = 1;
                    }
                    int n = itemstack2.stackSize;
                    itemstack2 = XUHelper.invInsert((IInventory)this, itemstack2, -1);
                    if (itemstack2 != null) {
                        n -= itemstack2.stackSize;
                    }
                    if (n <= 0) {
                        continue;
                    }
                    final ItemStack itemStack = itemstack;
                    itemStack.stackSize -= n;
                    if (itemstack.stackSize > 0) {
                        item.setEntityItemStack(itemstack);
                    }
                    else {
                        item.setDead();
                    }
                    if (this.upgradeNo(3) == 0) {
                        return;
                    }
                    continue;
                }
            }
        }
    }
    
    private boolean doCraft() {
        if (this.buffer.getBuffer() == null || ((ItemStack)this.buffer.getBuffer()).stackSize < ((ItemStack)this.buffer.getBuffer()).getMaxStackSize()) {
            final ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata() % 6);
            final boolean craft = this.worldObj.getBlock(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ) == Blocks.crafting_table;
            if (!craft) {
                return false;
            }
            final ForgeDirection dirX = TileEntityTransferNodeInventory.orthX[dir.ordinal()];
            final ForgeDirection dirY = TileEntityTransferNodeInventory.orthY[dir.ordinal()];
            final int[] slots = new int[9];
            final IInventory[] inventories = new IInventory[9];
            boolean isEmpty = true;
            for (int dx = -1; dx <= 1; ++dx) {
                for (int dy = -1; dy <= 1; ++dy) {
                    final TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX * 2 + dirX.offsetX * dx + dirY.offsetX * dy, this.yCoord + dir.offsetY * 2 + dirX.offsetY * dx + dirY.offsetY * dy, this.zCoord + dir.offsetZ * 2 + dirX.offsetZ * dx + dirY.offsetZ * dy);
                    final int j = dx + 1 + 3 * (-dy + 1);
                    final boolean a = tile instanceof IInventory;
                    final boolean b = a || tile instanceof IFluidHandler;
                    if (b) {
                        if (a) {
                            inventories[j] = (IInventory)tile;
                        }
                        else {
                            inventories[j] = (IInventory)new LiquidInventory((IFluidHandler)tile, dir.getOpposite());
                        }
                        final int i = getFirstExtractableItemStackSlot(inventories[j], dir.getOpposite().ordinal());
                        slots[j] = i;
                        if (i >= 0) {
                            final ItemStack item = inventories[j].getStackInSlot(i);
                            TileEntityTransferNodeInventory.crafting.setInventorySlotContents(j, item.copy());
                            isEmpty = false;
                        }
                        else {
                            TileEntityTransferNodeInventory.crafting.setInventorySlotContents(j, (ItemStack)null);
                        }
                    }
                    else {
                        inventories[j] = null;
                        TileEntityTransferNodeInventory.crafting.setInventorySlotContents(j, (ItemStack)null);
                    }
                }
            }
            if (isEmpty) {
                return true;
            }
            if (this.cachedRecipe == null || !this.cachedRecipe.matches((InventoryCrafting)TileEntityTransferNodeInventory.crafting, this.worldObj) || this.cachedRecipe.getCraftingResult((InventoryCrafting)TileEntityTransferNodeInventory.crafting) == null) {
                final int p = TileEntityTransferNodeInventory.crafting.hashCode();
                if (p == this.prevStack && this.prevStack != 0 && this.rand.nextInt(10) > 0) {
                    return true;
                }
                this.prevStack = p;
                final IRecipe r = findMatchingRecipe(TileEntityTransferNodeInventory.crafting, this.worldObj);
                if (r == null || r.getCraftingResult((InventoryCrafting)TileEntityTransferNodeInventory.crafting) == null || !this.isItemValidForSlot(0, r.getCraftingResult((InventoryCrafting)TileEntityTransferNodeInventory.crafting))) {
                    return true;
                }
                this.cachedRecipe = r;
            }
            final ItemStack stack = this.cachedRecipe.getCraftingResult((InventoryCrafting)TileEntityTransferNodeInventory.crafting);
            this.prevStack = 0;
            if (this.buffer.getBuffer() != null) {
                if (!XUHelper.canItemsStack(stack, (ItemStack)this.buffer.getBuffer(), false, true, false)) {
                    return true;
                }
                if (stack.stackSize + ((ItemStack)this.buffer.getBuffer()).stackSize > stack.getMaxStackSize()) {
                    return true;
                }
            }
            if (!this.isItemValidForSlot(0, stack)) {
                return true;
            }
            final ItemStack[] items = new ItemStack[9];
            for (int k = 0; k < 9; ++k) {
                if (inventories[k] != null && slots[k] >= 0) {
                    final ItemStack c = inventories[k].getStackInSlot(slots[k]);
                    boolean flag = false;
                    if (c == null || !XUHelper.canItemsStack(TileEntityTransferNodeInventory.crafting.getStackInSlot(k), c)) {
                        flag = true;
                    }
                    if (!flag) {
                        items[k] = inventories[k].decrStackSize(slots[k], 1);
                        if (items[k] != null && items[k].stackSize != 1) {
                            flag = true;
                        }
                    }
                    if (flag) {
                        for (int l = 0; l <= k; ++l) {
                            if (items[l] != null && inventories[l] != null) {
                                items[l] = XUHelper.invInsert(inventories[l], items[l], dir.getOpposite().ordinal());
                                if (items[l] != null) {
                                    XUHelper.dropItem(this.getWorldObj(), this.getNodeX(), this.getNodeY(), this.getNodeZ(), items[l]);
                                }
                            }
                        }
                        return true;
                    }
                    if (c.getItem().hasContainerItem(c)) {
                        final ItemStack t = c.getItem().getContainerItem(c);
                        if (t != null && (!t.isItemStackDamageable() || t.getItemDamage() <= t.getMaxDamage())) {
                            XUHelper.invInsert(inventories[k], t, dir.getOpposite().ordinal());
                        }
                    }
                }
            }
            XUHelper.invInsert((IInventory)this, stack, -1);
        }
        return true;
    }
    
    private boolean genCobble() {
        if (ExtraUtils.disableCobblegen) {
            return false;
        }
        if (this.buffer.getBuffer() == null || (((ItemStack)this.buffer.getBuffer()).getItem() == Item.getItemFromBlock(Blocks.cobblestone) && ((ItemStack)this.buffer.getBuffer()).stackSize < 64)) {
            final int dir = this.getBlockMetadata() % 6;
            this.genCStoneCounter = (this.genCStoneCounter + 1) % (1 + this.upgradeNo(0));
            if (this.genCStoneCounter != 0) {
                return false;
            }
            if (this.worldObj.getTotalWorldTime() - this.checkTimer > 100L) {
                this.checkTimer = this.worldObj.getTotalWorldTime();
                this.hasCStoneGen = false;
                if (this.worldObj.getBlock(this.xCoord + Facing.offsetsXForSide[dir], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir]) == Blocks.cobblestone) {
                    boolean hasLava = false;
                    boolean hasWater = false;
                    for (int i = 2; (!hasWater || !hasLava) && i < 6; hasWater |= (this.worldObj.getBlock(this.xCoord + Facing.offsetsXForSide[dir] + Facing.offsetsXForSide[i], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir] + Facing.offsetsZForSide[i]).getMaterial() == Material.water), hasLava |= (this.worldObj.getBlock(this.xCoord + Facing.offsetsXForSide[dir] + Facing.offsetsXForSide[i], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir] + Facing.offsetsZForSide[i]).getMaterial() == Material.lava), ++i) {}
                    if (hasWater && hasLava) {
                        this.hasCStoneGen = true;
                    }
                }
            }
            if (this.hasCStoneGen) {
                if (this.buffer.getBuffer() == null) {
                    this.buffer.setBuffer(new ItemStack(Blocks.cobblestone, this.upgradeNo(2)));
                }
                else {
                    final ItemStack itemStack = (ItemStack)this.buffer.getBuffer();
                    itemStack.stackSize += 1 + this.upgradeNo(2);
                    if (((ItemStack)this.buffer.getBuffer()).stackSize > 64) {
                        ((ItemStack)this.buffer.getBuffer()).stackSize = 64;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public int getSizeInventory() {
        return 1;
    }
    
    public ItemStack getStackInSlot(final int i) {
        if (i == 0) {
            return (ItemStack)this.buffer.getBuffer();
        }
        return null;
    }
    
    public ItemStack decrStackSize(final int i, final int j) {
        if (i != 0) {
            return null;
        }
        if (this.buffer.getBuffer() == null) {
            return null;
        }
        if (((ItemStack)this.buffer.getBuffer()).stackSize <= j) {
            final ItemStack itemstack = (ItemStack)this.buffer.getBuffer();
            this.buffer.setBuffer(null);
            this.markDirty();
            return itemstack;
        }
        final ItemStack itemstack = ((ItemStack)this.buffer.getBuffer()).splitStack(j);
        if (((ItemStack)this.buffer.getBuffer()).stackSize == 0) {
            this.buffer.setBuffer(null);
        }
        this.markDirty();
        return itemstack;
    }
    
    public ItemStack getStackInSlotOnClosing(final int i) {
        if (i != 0) {
            return null;
        }
        if (this.buffer.getBuffer() != null) {
            final ItemStack itemstack = (ItemStack)this.buffer.getBuffer();
            this.buffer.setBuffer(null);
            return itemstack;
        }
        return null;
    }
    
    public void setInventorySlotContents(final int i, final ItemStack itemstack) {
        this.buffer.setBuffer(itemstack);
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }
    
    public String getInventoryName() {
        return "gui.transferNode";
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer par1EntityPlayer) {
        return par1EntityPlayer.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 64.0;
    }
    
    public void openInventory() {
    }
    
    public void closeInventory() {
    }
    
    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        for (int j = 0; j < this.upgrades.getSizeInventory(); ++j) {
            final ItemStack upgrade = this.upgrades.getStackInSlot(j);
            if (upgrade != null && ItemNodeUpgrade.isFilter(upgrade) && !ItemNodeUpgrade.matchesFilterItem(itemstack, upgrade)) {
                return false;
            }
        }
        return true;
    }
    
    public int[] getAccessibleSlotsFromSide(final int j) {
        if (j < 0 || j >= 6 || j == this.getBlockMetadata() % 6) {
            return TileEntityTransferNodeInventory.contents;
        }
        return TileEntityTransferNodeInventory.nullcontents;
    }
    
    public boolean canInsertItem(final int i, final ItemStack itemstack, final int j) {
        return (j < 0 || j >= 6 || j == this.getBlockMetadata() % 6) && this.isItemValidForSlot(i, itemstack);
    }
    
    public boolean canExtractItem(final int i, final ItemStack itemstack, final int j) {
        return false;
    }
    
    @Override
    public TileEntityTransferNodeInventory getNode() {
        return this;
    }
    
    public BoxModel getModel(final ForgeDirection dir) {
        final BoxModel boxes = new BoxModel();
        boxes.add(new Box(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.0625f, 0.9375f).rotateToSide(dir).setTextureSides(dir.ordinal(), BlockTransferNode.nodeBase));
        boxes.add(new Box(0.1875f, 0.0625f, 0.1875f, 0.8125f, 0.25f, 0.8125f).rotateToSide(dir));
        boxes.add(new Box(0.3125f, 0.25f, 0.3125f, 0.6875f, 0.375f, 0.6875f).rotateToSide(dir));
        boxes.add(new Box(0.375f, 0.25f, 0.375f, 0.625f, 0.375f, 0.625f).rotateToSide(dir).setTexture(BlockTransferNode.nodeBase).setAllSideInvisible().setSideInvisible(dir.getOpposite().ordinal(), false));
        return boxes;
    }
    
    static {
        contents = new int[] { 0 };
        nullcontents = new int[0];
        TileEntityTransferNodeInventory.crafting = new InvCrafting(3, 3);
        TileEntityTransferNodeInventory.orthY = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.NORTH, ForgeDirection.UP, ForgeDirection.UP, ForgeDirection.UP, ForgeDirection.UP, ForgeDirection.UNKNOWN };
        TileEntityTransferNodeInventory.orthX = new ForgeDirection[] { ForgeDirection.WEST, ForgeDirection.WEST, ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.UNKNOWN };
    }
}

