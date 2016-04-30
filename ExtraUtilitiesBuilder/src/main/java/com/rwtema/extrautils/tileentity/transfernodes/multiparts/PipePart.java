// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import net.minecraft.util.IIcon;
import codechicken.multipart.TMultiPart;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import net.minecraftforge.common.util.ForgeDirection;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.Block;
import com.rwtema.extrautils.block.BoxModel;
import java.util.ArrayList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderGlobal;
import codechicken.lib.vec.Vector3;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import java.util.Iterator;
import codechicken.lib.raytracer.IndexedCuboid6;
import java.util.HashSet;
import com.rwtema.extrautils.block.Box;
import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import net.minecraft.world.IBlockAccess;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.StdPipes;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.entity.player.EntityPlayer;
import codechicken.lib.vec.Cuboid6;
import net.minecraft.util.MovingObjectPosition;
import java.util.Arrays;
import net.minecraft.item.ItemStack;
import codechicken.multipart.TSlottedPart;
import codechicken.multipart.INeighborTileChange;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipeCosmetic;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import codechicken.microblock.ISidedHollowConnect;

public class PipePart extends MCMetaTilePart implements ISidedHollowConnect, IPipe, IPipeCosmetic, INeighborTileChange, TSlottedPart
{
    public static DummyPipePart[] dummyPipes;
    public int blockMasks;
    public byte[] flagmasks;
    
    public PipePart(final int meta) {
        super(meta);
        this.blockMasks = -1;
        this.flagmasks = new byte[] { 1, 2, 4, 8, 16, 32 };
    }
    
    public PipePart() {
        this.blockMasks = -1;
        this.flagmasks = new byte[] { 1, 2, 4, 8, 16, 32 };
    }
    
    public Iterable<ItemStack> getDrops() {
        return Arrays.asList(new ItemStack(this.getBlock(), 1, this.getBlock().damageDropped(this.getMetadata())));
    }
    
    public ItemStack pickItem(final MovingObjectPosition hit) {
        return new ItemStack(this.getBlock(), 1, this.getBlock().damageDropped(this.getMetadata()));
    }
    
    public int getMetadata() {
        return this.meta % 16;
    }
    
    public Iterable<Cuboid6> getOcclusionBoxes() {
        return Arrays.asList(new Cuboid6((double)(0.5f - this.baseSize()), (double)(0.5f - this.baseSize()), (double)(0.5f - this.baseSize()), (double)(0.5f + this.baseSize()), (double)(0.5f + this.baseSize()), (double)(0.5f + this.baseSize())));
    }
    
    public boolean activate(final EntityPlayer player, final MovingObjectPosition part, final ItemStack item) {
        if (this.getWorld().isRemote) {
            return true;
        }
        if (XUHelper.isWrench(item)) {
            final int newmetadata = StdPipes.getNextPipeType((IBlockAccess)this.getWorld(), part.blockX, part.blockY, part.blockZ, this.meta);
            this.meta = (byte)newmetadata;
            this.sendDescUpdate();
            return true;
        }
        return false;
    }
    
    public final Cuboid6 getBounds() {
        final Box bounds = ((BlockTransferPipe)this.getBlock()).getWorldModel((IBlockAccess)this.getWorld(), this.x(), this.y(), this.z()).boundingBox();
        return new Cuboid6((double)bounds.minX, (double)bounds.minY, (double)bounds.minZ, (double)bounds.maxX, (double)bounds.maxY, (double)bounds.maxZ);
    }
    
    public final HashSet<IndexedCuboid6> getSubParts() {
        final HashSet<IndexedCuboid6> boxes = new HashSet<IndexedCuboid6>();
        for (final Box bounds : ((BlockTransferPipe)this.getBlock()).getWorldModel((IBlockAccess)this.getWorld(), this.x(), this.y(), this.z())) {
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
    
    public Iterable<Cuboid6> getCollisionBoxes() {
        final ArrayList<Cuboid6> t2 = new ArrayList<Cuboid6>();
        final BoxModel model = ((BlockTransferPipe)this.getBlock()).getWorldModel((IBlockAccess)this.world(), this.x(), this.y(), this.z());
        for (final Box box : model) {
            t2.add(new Cuboid6((double)box.minX, (double)box.minY, (double)box.minZ, (double)box.maxX, (double)box.maxY, (double)box.maxZ));
        }
        return t2;
    }
    
    public Block getBlock() {
        return (this.meta < 16) ? ExtraUtils.transferPipe : ExtraUtils.transferPipe2;
    }
    
    public String getType() {
        return "extrautils:transfer_pipe";
    }
    
    public ArrayList<ForgeDirection> getOutputDirections(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir, final INodeBuffer buffer) {
        return StdPipes.getPipeType(this.meta).getOutputDirections(world, x, y, z, dir, buffer);
    }
    
    public boolean transferItems(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir, final INodeBuffer buffer) {
        return StdPipes.getPipeType(this.meta).transferItems(world, x, y, z, dir, buffer);
    }
    
    public boolean canInput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return !this.isBlocked(dir) && StdPipes.getPipeType(this.meta).canInput(world, x, y, z, dir);
    }
    
    public boolean canOutput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return !this.isBlocked(dir) && StdPipes.getPipeType(this.meta).canOutput((IBlockAccess)this.getWorld(), x, y, z, dir);
    }
    
    public int limitTransfer(final TileEntity dest, final ForgeDirection side, final INodeBuffer buffer) {
        return StdPipes.getPipeType(this.meta).limitTransfer(dest, side, buffer);
    }
    
    public IInventory getFilterInventory(final IBlockAccess world, final int x, final int y, final int z) {
        return null;
    }
    
    public boolean shouldConnectToTile(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return !this.isBlocked(dir) && StdPipes.getPipeType(this.meta).shouldConnectToTile(world, x, y, z, dir);
    }
    
    public void reloadBlockMasks() {
        this.blockMasks = 0;
        for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            PipePart.dummyPipes[dir.ordinal()].h = 0.5f - this.baseSize();
            if (!this.tile().canAddPart((TMultiPart)PipePart.dummyPipes[dir.ordinal()])) {
                this.blockMasks |= this.flagmasks[dir.ordinal()];
            }
        }
    }
    
    public void onPartChanged(final TMultiPart part) {
        this.reloadBlockMasks();
    }
    
    public void onNeighborChanged() {
        this.reloadBlockMasks();
    }
    
    public boolean isBlocked(final ForgeDirection dir) {
        if (this.blockMasks < 0) {
            this.reloadBlockMasks();
        }
        return (this.blockMasks & this.flagmasks[dir.ordinal()]) == this.flagmasks[dir.ordinal()];
    }
    
    public IIcon baseTexture() {
        return ((IPipeCosmetic)StdPipes.getPipeType(this.meta)).baseTexture();
    }
    
    public IIcon socketTexture(final ForgeDirection dir) {
        return ((IPipeCosmetic)StdPipes.getPipeType(this.meta)).socketTexture(dir);
    }
    
    public IIcon pipeTexture(final ForgeDirection dir, final boolean blocked) {
        return ((IPipeCosmetic)StdPipes.getPipeType(this.meta)).pipeTexture(dir, blocked);
    }
    
    public IIcon invPipeTexture(final ForgeDirection dir) {
        return ((IPipeCosmetic)StdPipes.getPipeType(this.meta)).invPipeTexture(dir);
    }
    
    public String getPipeType() {
        return StdPipes.getPipeType(this.meta).getPipeType();
    }
    
    public float baseSize() {
        return ((IPipeCosmetic)StdPipes.getPipeType(this.meta)).baseSize();
    }
    
    public boolean occlusionTest(final TMultiPart npart) {
        return DummyPipePart.class.equals(npart.getClass()) || super.occlusionTest(npart);
    }
    
    public void onNeighborTileChanged(final int arg0, final boolean arg1) {
        this.reloadBlockMasks();
    }
    
    public boolean weakTileChanges() {
        return true;
    }
    
    public int getSlotMask() {
        return 64;
    }
    
    public int getHollowSize(final int side) {
        return 6;
    }
    
    @Override
    public TileEntity getBlockTile() {
        return (TileEntity)this.tile();
    }
    
    static {
        PipePart.dummyPipes = new DummyPipePart[6];
        for (int i = 0; i < 6; ++i) {
            PipePart.dummyPipes[i] = new DummyPipePart(i, 0.375f);
        }
    }
}


