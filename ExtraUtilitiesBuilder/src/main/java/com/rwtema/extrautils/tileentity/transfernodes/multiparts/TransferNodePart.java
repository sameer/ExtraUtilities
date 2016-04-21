// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import codechicken.lib.raytracer.IndexedCuboid6;
import java.util.HashSet;
import com.rwtema.extrautils.block.Box;
import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferNode;
import net.minecraft.util.IIcon;
import codechicken.multipart.RedstoneInteractions;
import codechicken.multipart.scalatraits.TRedstoneTile;
import codechicken.multipart.TMultiPart;
import net.minecraft.inventory.IInventory;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import com.rwtema.extrautils.block.BoxModel;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.block.Block;
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Iterator;
import codechicken.lib.vec.Vector3;
import net.minecraft.entity.Entity;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.util.AxisAlignedBB;
import codechicken.lib.vec.Cuboid6;
import java.util.List;
import java.util.ArrayList;
import com.rwtema.extrautils.ExtraUtilsMod;
import net.minecraft.world.IBlockAccess;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.StdPipes;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import java.util.Arrays;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import scala.util.Random;
import codechicken.multipart.TFacePart;
import codechicken.multipart.TSlottedPart;
import codechicken.microblock.ISidedHollowConnect;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipeCosmetic;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INode;

public abstract class TransferNodePart extends MCMetaTilePart implements INode, IPipeCosmetic, ISidedHollowConnect, TSlottedPart, TFacePart
{
    public static Random rand;
    private static DummyPipePart[] dummyPipes;
    public TileEntityTransferNode node;
    public boolean valid;
    public boolean init;
    public int blockMasks;
    public byte[] flagmasks;
    int id;
    
    public TransferNodePart(final int meta, final TileEntityTransferNode node) {
        super(meta);
        this.valid = true;
        this.init = false;
        this.blockMasks = -1;
        this.flagmasks = new byte[] { 1, 2, 4, 8, 16, 32 };
        this.id = TransferNodePart.rand.nextInt();
        this.node = node;
        node.blockMetadata = meta;
    }
    
    public TransferNodePart(final TileEntityTransferNode node) {
        this.valid = true;
        this.init = false;
        this.blockMasks = -1;
        this.flagmasks = new byte[] { 1, 2, 4, 8, 16, 32 };
        this.id = TransferNodePart.rand.nextInt();
        this.node = node;
        node.blockMetadata = this.meta;
    }
    
    @Override
    public TileEntity getBlockTile() {
        return (TileEntity)this.tile();
    }
    
    public int getHollowSize(final int side) {
        return 6;
    }
    
    public Iterable<ItemStack> getDrops() {
        return Arrays.asList(new ItemStack(this.getBlock(), 1, this.getBlock().damageDropped((int)this.meta)));
    }
    
    public ItemStack pickItem(final MovingObjectPosition hit) {
        return new ItemStack(this.getBlock(), 1, this.getBlock().damageDropped(this.getMetadata()));
    }
    
    @Override
    public void bufferChanged() {
        this.getNode().bufferChanged();
    }
    
    public boolean activate(final EntityPlayer player, final MovingObjectPosition part, final ItemStack item) {
        if (this.getWorld().isRemote) {
            return true;
        }
        if (XUHelper.isWrench(item)) {
            final int newmetadata = StdPipes.getNextPipeType((IBlockAccess)this.getWorld(), part.blockX, part.blockY, part.blockZ, this.getNode().pipe_type);
            this.getNode().pipe_type = (byte)newmetadata;
            this.sendDescUpdate();
            return true;
        }
        player.openGui((Object)ExtraUtilsMod.instance, 0, this.getWorld(), this.x(), this.y(), this.z());
        return true;
    }
    
    public void onRemoved() {
        if (!this.getWorld().isRemote) {
            final List<ItemStack> drops = new ArrayList<ItemStack>();
            for (int i = 0; i < this.node.upgrades.getSizeInventory(); ++i) {
                if (this.node.upgrades.getStackInSlot(i) != null) {
                    drops.add(this.node.upgrades.getStackInSlot(i));
                }
            }
            this.tile().dropItems((Iterable)drops);
        }
    }
    
    public void onWorldJoin() {
        if (this.getWorld() != null) {
            this.node.setWorldObj(this.getWorld());
        }
        this.node.xCoord = this.x();
        this.node.yCoord = this.y();
        this.node.zCoord = this.z();
        this.node.onWorldJoin();
        this.reloadBlockMasks();
    }
    
    public void onWorldSeparate() {
        this.node.invalidate();
    }
    
    public Iterable<Cuboid6> getCollisionBoxes() {
        final ArrayList<AxisAlignedBB> t = new ArrayList<AxisAlignedBB>();
        final ArrayList<Cuboid6> t2 = new ArrayList<Cuboid6>();
        ExtraUtils.transferNode.addCollisionBoxesToList(this.getWorld(), this.x(), this.y(), this.z(), AxisAlignedBB.getBoundingBox((double)this.x(), (double)this.y(), (double)this.z(), (double)(this.x() + 1), (double)(this.y() + 1), (double)(this.z() + 1)), (List)t, (Entity)null);
        for (final AxisAlignedBB aT : t) {
            t2.add(new Cuboid6(aT.minX, aT.minY, aT.minZ, aT.maxX, aT.maxY, aT.maxZ).sub(new Vector3((double)this.x(), (double)this.y(), (double)this.z())));
        }
        return t2;
    }
    
    public void save(final NBTTagCompound tag) {
        super.save(tag);
        final NBTTagCompound subtag = new NBTTagCompound();
        this.node.writeToNBT(subtag);
        tag.setTag("node", (NBTBase)subtag);
    }
    
    public void load(final NBTTagCompound tag) {
        super.load(tag);
        this.node.readFromNBT(tag.getCompoundTag("node"));
    }
    
    public boolean doesTick() {
        return true;
    }
    
    public void update() {
        if (this.node != null && !this.world().isRemote) {
            this.node.blockMetadata = this.meta;
            if (this.getWorld().getTileEntity(this.x(), this.y(), this.z()) == this.tile()) {
                if (this.node.getWorldObj() == null) {
                    this.onWorldJoin();
                }
                this.node.updateEntity();
            }
        }
    }
    
    public void writeDesc(final MCDataOutput packet) {
        packet.writeByte((int)this.meta);
        packet.writeByte(this.node.pipe_type);
    }
    
    public void readDesc(final MCDataInput packet) {
        this.meta = packet.readByte();
        this.node.pipe_type = packet.readByte();
    }
    
    public Block getBlock() {
        return ExtraUtils.transferNode;
    }
    
    @Override
    public int getNodeX() {
        return this.node.getNodeX();
    }
    
    @Override
    public int getNodeY() {
        return this.node.getNodeY();
    }
    
    @Override
    public int getNodeZ() {
        return this.node.getNodeZ();
    }
    
    @Override
    public ForgeDirection getNodeDir() {
        this.node.blockMetadata = this.meta;
        return this.node.getNodeDir();
    }
    
    @Override
    public int getPipeX() {
        return this.node.getPipeX();
    }
    
    @Override
    public int getPipeY() {
        return this.node.getPipeY();
    }
    
    @Override
    public int getPipeZ() {
        return this.node.getPipeZ();
    }
    
    @Override
    public int getPipeDir() {
        return this.node.getPipeDir();
    }
    
    @Override
    public List<ItemStack> getUpgrades() {
        return this.node.getUpgrades();
    }
    
    @Override
    public boolean checkRedstone() {
        return this.node.checkRedstone();
    }
    
    @Override
    public BoxModel getModel(final ForgeDirection dir) {
        return this.node.getModel(dir);
    }
    
    @Override
    public String getNodeType() {
        return this.node.getNodeType();
    }
    
    public boolean transferItems(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir, final INodeBuffer buffer) {
        return this.getNode().transferItems(world, x, y, z, dir, buffer);
    }
    
    public boolean canInput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return !this.isBlocked(dir) && this.getNode().canInput(world, x, y, z, dir);
    }
    
    public ArrayList<ForgeDirection> getOutputDirections(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir, final INodeBuffer buffer) {
        return this.getNode().getOutputDirections(world, x, y, z, dir, buffer);
    }
    
    public boolean canOutput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return !this.isBlocked(dir) && this.getNode().canOutput(world, x, y, z, dir);
    }
    
    public int limitTransfer(final TileEntity dest, final ForgeDirection side, final INodeBuffer buffer) {
        return this.getNode().limitTransfer(dest, side, buffer);
    }
    
    public IInventory getFilterInventory(final IBlockAccess world, final int x, final int y, final int z) {
        return this.getNode().getFilterInventory(world, x, y, z);
    }
    
    public boolean shouldConnectToTile(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return !this.isBlocked(dir) && this.getNode().shouldConnectToTile(world, x, y, z, dir);
    }
    
    public void reloadBlockMasks() {
        this.blockMasks = 0;
        for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            TransferNodePart.dummyPipes[dir.ordinal()].h = 0.5f + this.baseSize();
            if (dir == this.getNodeDir()) {
                this.blockMasks |= this.flagmasks[dir.ordinal()];
            }
            else if (!this.tile().canAddPart((TMultiPart)TransferNodePart.dummyPipes[dir.ordinal()])) {
                this.blockMasks |= this.flagmasks[dir.ordinal()];
            }
        }
    }
    
    public void onPartChanged(final TMultiPart part) {
        this.reloadBlockMasks();
    }
    
    public void onNeighborChanged() {
        this.node.updateRedstone();
        this.reloadBlockMasks();
    }
    
    @Override
    public boolean isPowered() {
        return this.node.isPowered();
    }
    
    @Override
    public boolean recalcRedstone() {
        if (this.tile() instanceof TRedstoneTile) {
            final TRedstoneTile rsT = (TRedstoneTile)this.tile();
            for (int side = 0; side < 6; ++side) {
                if (rsT.weakPowerLevel(side) > 0) {
                    return true;
                }
            }
        }
        for (int side2 = 0; side2 < 6; ++side2) {
            if (RedstoneInteractions.getPowerTo(this.world(), this.x(), this.y(), this.z(), side2, 31) > 0) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isBlocked(final ForgeDirection dir) {
        if (this.node.getWorldObj() == null) {
            this.onWorldJoin();
        }
        if (this.blockMasks < 0) {
            this.reloadBlockMasks();
        }
        return (this.blockMasks & this.flagmasks[dir.ordinal()]) == this.flagmasks[dir.ordinal()];
    }
    
    @Override
    public IIcon baseTexture() {
        return this.getNode().baseTexture();
    }
    
    @Override
    public IIcon pipeTexture(final ForgeDirection dir, final boolean blocked) {
        return this.getNode().pipeTexture(dir, blocked);
    }
    
    @Override
    public IIcon invPipeTexture(final ForgeDirection dir) {
        return this.getNode().invPipeTexture(dir);
    }
    
    @Override
    public IIcon socketTexture(final ForgeDirection dir) {
        return this.getNode().socketTexture(dir);
    }
    
    public String getPipeType() {
        return this.getNode().getPipeType();
    }
    
    @Override
    public float baseSize() {
        return this.getNode().baseSize();
    }
    
    public boolean occlusionTest(final TMultiPart npart) {
        return npart instanceof DummyPipePart || super.occlusionTest(npart);
    }
    
    public final Cuboid6 getBounds() {
        final Box bounds = ((BlockTransferNode)this.getBlock()).getWorldModel((IBlockAccess)this.getWorld(), this.x(), this.y(), this.z()).boundingBox();
        return new Cuboid6((double)bounds.minX, (double)bounds.minY, (double)bounds.minZ, (double)bounds.maxX, (double)bounds.maxY, (double)bounds.maxZ);
    }
    
    public final HashSet<IndexedCuboid6> getSubParts() {
        final HashSet<IndexedCuboid6> boxes = new HashSet<IndexedCuboid6>();
        for (final Box bounds : ((BlockTransferNode)this.getBlock()).getWorldModel((IBlockAccess)this.getWorld(), this.x(), this.y(), this.z())) {
            boxes.add(new IndexedCuboid6((Object)0, new Cuboid6((double)bounds.minX, (double)bounds.minY, (double)bounds.minZ, (double)bounds.maxX, (double)bounds.maxY, (double)bounds.maxZ)));
        }
        return boxes;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean drawHighlight(final MovingObjectPosition hit, final EntityPlayer player, final float frame) {
        GL11.glEnable(3042);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.4f);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDepthMask(false);
        final float f1 = 0.002f;
        final double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * frame;
        final double d2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * frame;
        final double d3 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * frame;
        RenderGlobal.drawOutlinedBoundingBox(this.getBounds().add(new Vector3((double)this.x(), (double)this.y(), (double)this.z())).toAABB().expand((double)f1, (double)f1, (double)f1).getOffsetBoundingBox(-d0, -d2, -d3), -1);
        GL11.glDepthMask(true);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        return true;
    }
    
    public int getSlotMask() {
        if (this.getNode().getNodeDir() == ForgeDirection.UNKNOWN) {
            return 64;
        }
        return 0x40 | 1 << this.getNode().getNodeDir().ordinal();
    }
    
    public int redstoneConductionMap() {
        return 0;
    }
    
    public boolean solid(final int arg0) {
        return false;
    }
    
    static {
        TransferNodePart.rand = new Random();
        TransferNodePart.dummyPipes = new DummyPipePart[6];
        for (int i = 0; i < 6; ++i) {
            TransferNodePart.dummyPipes[i] = new DummyPipePart(i, 0.625f);
        }
    }
}

