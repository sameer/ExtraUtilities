// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.endercollector;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.Packet;
import net.minecraft.util.IChatComponent;
import com.rwtema.extrautils.network.packets.PacketTempChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.entity.player.EntityPlayer;
import com.rwtema.extrautils.network.packets.PacketParticleLine;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.Block;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.packets.PacketParticleCurve;
import net.minecraftforge.common.util.ForgeDirection;
import com.rwtema.extrautils.item.ItemNodeUpgrade;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.List;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.Facing;
import net.minecraft.entity.item.EntityItem;
import com.rwtema.extrautils.helper.XURandom;
import java.util.Random;
import net.minecraft.util.Vec3;
import net.minecraft.item.ItemStack;
import java.util.LinkedList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.tileentity.TileEntity;

public class TileEnderCollector extends TileEntity
{
    int range;
    AxisAlignedBB bounds;
    boolean isStuffed;
    boolean init;
    LinkedList<ItemStack> items;
    private static final Vec3[] sides;
    ItemStack filter;
    private Random rand;
    
    public TileEnderCollector() {
        this.range = 8;
        this.isStuffed = false;
        this.init = false;
        this.items = new LinkedList<ItemStack>();
        this.rand = XURandom.getInstance();
    }
    
    public void setRange(final int r) {
        this.range = r;
        this.bounds = AxisAlignedBB.getBoundingBox((double)(this.xCoord - r / 2.0f), (double)(this.yCoord - r / 2.0f), (double)(this.zCoord - r / 2.0f), (double)(this.xCoord + r / 2.0f + 1.0f), (double)(this.yCoord + r / 2.0f + 1.0f), (double)(this.zCoord + r / 2.0f + 1.0f));
    }
    
    public void updateEntity() {
        if (this.worldObj.isRemote) {
            return;
        }
        this.init = true;
        if (!this.isStuffed && this.worldObj.getTotalWorldTime() % 40L == 0L) {
            if (this.bounds == null) {
                this.setRange(this.range);
            }
            final List<EntityItem> entitiesWithinAABB = (List<EntityItem>)this.worldObj.getEntitiesWithinAABB((Class)EntityItem.class, this.bounds);
            for (final EntityItem entityItem : entitiesWithinAABB) {
                this.grabEntity(entityItem);
            }
        }
        if (!this.items.isEmpty()) {
            if (this.isStuffed && this.worldObj.getTotalWorldTime() % 10L != 0L) {
                return;
            }
            final int side = this.getBlockMetadata() % 6;
            final ListIterator<ItemStack> iter = this.items.listIterator();
            final TileEntity tileEntity = this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[side], this.yCoord + Facing.offsetsYForSide[side], this.zCoord + Facing.offsetsZForSide[side]);
            if (tileEntity instanceof IInventory) {
                final IInventory inventory = (IInventory)tileEntity;
                while (iter.hasNext()) {
                    ItemStack next = iter.next();
                    next = XUHelper.invInsert(inventory, next.copy(), side ^ 0x1);
                    if (next != null) {
                        iter.set(next);
                    }
                    else {
                        iter.remove();
                    }
                }
            }
        }
        this.isStuffed = !this.items.isEmpty();
        this.updateMeta();
    }
    
    public void updateMeta() {
        final int oldMeta = this.getBlockMetadata();
        final int newMeta = oldMeta % 6 + (this.isStuffed ? 6 : 0);
        if (newMeta != oldMeta) {
            this.changeMeta(newMeta);
        }
    }
    
    public void changeMeta(final int newMeta) {
        this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, newMeta, 2);
    }
    
    public boolean inRange(final Entity entity) {
        if (this.isStuffed || !this.init || this.tileEntityInvalid) {
            return false;
        }
        if (this.bounds == null) {
            this.setRange(this.range);
        }
        return this.bounds.intersectsWith(entity.boundingBox);
    }
    
    public void invalidate() {
        super.invalidate();
        CollectorHandler.unregister(this);
    }
    
    public void onChunkUnload() {
        CollectorHandler.unregister(this);
    }
    
    public void setWorldObj(final World p_145834_1_) {
        super.setWorldObj(p_145834_1_);
        if (!this.worldObj.isRemote) {
            CollectorHandler.register(this);
        }
    }
    
    public void grabEntity(final EntityItem entity) {
        if (entity.isDead) {
            return;
        }
        final int side = this.getBlockMetadata() % 6;
        if (this.worldObj.isAirBlock(this.xCoord + Facing.offsetsXForSide[side], this.yCoord + Facing.offsetsYForSide[side], this.zCoord + Facing.offsetsZForSide[side])) {
            return;
        }
        final TileEntity tileEntity = this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[side], this.yCoord + Facing.offsetsYForSide[side], this.zCoord + Facing.offsetsZForSide[side]);
        if (!(tileEntity instanceof IInventory)) {
            return;
        }
        final IInventory inv = (IInventory)tileEntity;
        final ItemStack stack = entity.getDataWatcher().getWatchableObjectItemStack(10);
        if (stack == null) {
            return;
        }
        if (this.filter != null && !ItemNodeUpgrade.matchesFilterItem(stack, this.filter)) {
            return;
        }
        final ItemStack itemStack = XUHelper.simInvInsert(inv, stack, side ^ 0x1);
        if (itemStack != null && itemStack.stackSize == stack.stackSize) {
            return;
        }
        this.items.add(stack);
        this.signalChange(entity);
        entity.setEntityItemStack((ItemStack)null);
        entity.setDead();
    }
    
    public void signalChange(final EntityItem item) {
        final int side = this.getBlockMetadata() % 6;
        NetworkHandler.sendToAllAround(new PacketParticleCurve(item, Vec3.createVectorHelper(this.xCoord + 0.5 - ForgeDirection.getOrientation(side).offsetX * 0.4, this.yCoord + 0.5 - ForgeDirection.getOrientation(side).offsetY * 0.4, this.zCoord + 0.5 - ForgeDirection.getOrientation(side).offsetZ * 0.4), TileEnderCollector.sides[side]), this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 32.0);
    }
    
    public boolean shouldRefresh(final Block oldBlock, final Block newBlock, final int oldMeta, final int newMeta, final World world, final int x, final int y, final int z) {
        return newBlock != oldBlock;
    }
    
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.range = tag.getByte("Range");
        final NBTTagList tagList = tag.getTagList("Items", XUHelper.NBTIds.NBT.id);
        for (int i = 0; i < tagList.tagCount(); ++i) {
            final ItemStack itemStack = ItemStack.loadItemStackFromNBT(tagList.getCompoundTagAt(i));
            if (itemStack != null) {
                this.items.add(itemStack);
            }
        }
        if (tag.hasKey("Filter")) {
            this.filter = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Filter"));
        }
    }
    
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);
        final NBTTagList list = new NBTTagList();
        for (final ItemStack item : this.items) {
            list.appendTag((NBTBase)item.writeToNBT(new NBTTagCompound()));
        }
        tag.setTag("Items", (NBTBase)list);
        tag.setByte("Range", (byte)this.range);
        if (this.filter != null) {
            tag.setTag("Filter", (NBTBase)this.filter.writeToNBT(new NBTTagCompound()));
        }
    }
    
    public void onNeighbourChange() {
    }
    
    public void drawLine(final Vec3 a, final Vec3 b) {
        NetworkHandler.sendToAllAround(new PacketParticleLine(a, b), this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 32.0);
    }
    
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX, final float hitY, final float hitZ) {
        if (world.isRemote) {
            return true;
        }
        if (this.isStuffed) {
            this.dropItems();
            return true;
        }
        final ItemStack heldItem = player.getHeldItem();
        if (heldItem != null) {
            if (this.filter == null && ItemNodeUpgrade.isFilter(heldItem)) {
                this.filter = heldItem.copy();
                heldItem.stackSize = 0;
                this.worldObj.markBlockForUpdate(x, y, z);
                return true;
            }
            if (this.filter != null && XUHelper.isWrench(heldItem)) {
                try {
                    CollectorHandler.dontCollect = true;
                    this.dropItem(this.filter.copy());
                }
                finally {
                    CollectorHandler.dontCollect = false;
                }
                this.filter = null;
                this.worldObj.markBlockForUpdate(x, y, z);
                return true;
            }
        }
        if (!player.isSneaking()) {
            ++this.range;
            if (this.range > 8) {
                this.range = 1;
            }
        }
        else {
            --this.range;
            if (this.range < 1) {
                this.range = 8;
            }
        }
        this.setRange(this.range);
        this.drawCube(this.bounds);
        PacketTempChat.sendChat(player, (IChatComponent)new ChatComponentText(String.format("Range: %s  (%s, %s, %s -> %s, %s, %s)", this.range / 2.0f, this.bounds.minX, this.bounds.minY, this.bounds.minZ, this.bounds.maxX, this.bounds.maxY, this.bounds.maxZ)));
        return true;
    }
    
    public void drawCube(final AxisAlignedBB b) {
        final double x0 = b.minX;
        final double x2 = b.maxX;
        final double y0 = b.minY;
        final double y2 = b.maxY;
        final double z0 = b.minZ;
        final double z2 = b.maxZ;
        final Vec3 p000 = Vec3.createVectorHelper(x0, y0, z0);
        final Vec3 p2 = Vec3.createVectorHelper(x0, y0, z2);
        final Vec3 p3 = Vec3.createVectorHelper(x0, y2, z0);
        final Vec3 p4 = Vec3.createVectorHelper(x0, y2, z2);
        final Vec3 p5 = Vec3.createVectorHelper(x2, y0, z0);
        final Vec3 p6 = Vec3.createVectorHelper(x2, y0, z2);
        final Vec3 p7 = Vec3.createVectorHelper(x2, y2, z0);
        final Vec3 p8 = Vec3.createVectorHelper(x2, y2, z2);
        final Vec3 center = Vec3.createVectorHelper((x0 + x2) / 2.0, (y0 + y2) / 2.0, (z0 + z2) / 2.0);
        this.drawLine(p000, p2);
        this.drawLine(p000, p3);
        this.drawLine(p000, p5);
        this.drawLine(p2, p4);
        this.drawLine(p2, p6);
        this.drawLine(p3, p4);
        this.drawLine(p3, p7);
        this.drawLine(p5, p6);
        this.drawLine(p5, p7);
        this.drawLine(p4, p8);
        this.drawLine(p6, p8);
        this.drawLine(p7, p8);
        this.drawLine(center, p000);
        this.drawLine(center, p2);
        this.drawLine(center, p3);
        this.drawLine(center, p4);
        this.drawLine(center, p5);
        this.drawLine(center, p6);
        this.drawLine(center, p7);
        this.drawLine(center, p8);
    }
    
    public void dropItems() {
        try {
            CollectorHandler.dontCollect = true;
            for (final ItemStack itemstack : this.items) {
                this.dropItem(itemstack);
            }
        }
        finally {
            CollectorHandler.dontCollect = false;
        }
        this.items.clear();
        this.isStuffed = false;
    }
    
    public void dropItem(final ItemStack itemstack) {
        final float dx = this.rand.nextFloat() * 0.8f + 0.1f;
        final float dy = this.rand.nextFloat() * 0.8f + 0.1f;
        final float dz = this.rand.nextFloat() * 0.8f + 0.1f;
        while (itemstack.stackSize > 0) {
            int j1 = this.rand.nextInt(21) + 10;
            if (j1 > itemstack.stackSize) {
                j1 = itemstack.stackSize;
            }
            itemstack.stackSize -= j1;
            final EntityItem entityitem = new EntityItem(this.worldObj, (double)(this.xCoord + dx), (double)(this.yCoord + dy), (double)(this.zCoord + dz), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
            if (itemstack.hasTagCompound()) {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
            }
            entityitem.motionX = this.rand.nextGaussian() * 0.05000000074505806;
            entityitem.motionY = this.rand.nextGaussian() * 0.05000000074505806 + 0.20000000298023224;
            entityitem.motionZ = this.rand.nextGaussian() * 0.05000000074505806;
            this.worldObj.spawnEntityInWorld((Entity)entityitem);
        }
    }
    
    public Packet getDescriptionPacket() {
        final NBTTagCompound t = new NBTTagCompound();
        if (this.filter != null) {
            t.setTag("Filter", (NBTBase)this.filter.writeToNBT(new NBTTagCompound()));
        }
        return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 4, t);
    }
    
    public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        final NBTTagCompound tag = pkt.func_148857_g();
        if (tag.hasKey("Filter")) {
            this.filter = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Filter"));
        }
        else {
            this.filter = null;
        }
    }
    
    static {
        sides = new Vec3[] { Vec3.createVectorHelper(0.0, -1.0, 0.0), Vec3.createVectorHelper(0.0, 1.0, 0.0), Vec3.createVectorHelper(0.0, 0.0, -1.0), Vec3.createVectorHelper(0.0, 0.0, 1.0), Vec3.createVectorHelper(-1.0, 0.0, 0.0), Vec3.createVectorHelper(1.0, 0.0, 0.0) };
    }
}
