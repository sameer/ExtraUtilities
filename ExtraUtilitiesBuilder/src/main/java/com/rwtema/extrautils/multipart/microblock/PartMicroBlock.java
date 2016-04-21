// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.multipart.microblock;

import java.util.Iterator;
import java.util.ArrayList;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.multipart.TileMultipart;
import net.minecraft.tileentity.TileEntity;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.IconHitEffects;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.init.Blocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.render.Vertex5;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import java.util.Arrays;
import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.NormalOcclusionTest;
import codechicken.multipart.TMultiPart;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.multipart.JNormalOcclusion;
import codechicken.microblock.IMicroMaterialRender;
import codechicken.lib.render.IFaceRenderer;
import codechicken.multipart.JIconHitEffects;
import codechicken.multipart.JCuboidPart;

public abstract class PartMicroBlock extends JCuboidPart implements JIconHitEffects, IFaceRenderer, IMicroMaterialRender, JNormalOcclusion, IFence, IMicroBlock
{
    public MicroMaterialRegistry.IMicroMaterial mat;
    int material;
    boolean overEthereal;
    
    public PartMicroBlock() {
        this.mat = null;
        this.overEthereal = false;
    }
    
    public PartMicroBlock(final int material) {
        this.mat = null;
        this.overEthereal = false;
        this.material = material;
    }
    
    public boolean isEthereal() {
        return false;
    }
    
    public boolean hideCreativeTab() {
        return false;
    }
    
    public boolean occlusionTest(final TMultiPart npart) {
        return NormalOcclusionTest.apply((JNormalOcclusion)this, npart);
    }
    
    public Iterable<Cuboid6> getOcclusionBoxes() {
        return Arrays.asList(this.getBounds());
    }
    
    public void harvest(final MovingObjectPosition hit, final EntityPlayer player) {
        super.harvest(hit, player);
    }
    
    public void renderFace(final Vertex5[] face, final int side) {
    }
    
    public void writeDesc(final MCDataOutput packet) {
        packet.writeInt(this.material);
    }
    
    public void readDesc(final MCDataInput packet) {
        this.material = packet.readInt();
    }
    
    public void save(final NBTTagCompound tag) {
        super.save(tag);
        tag.setString("mat", MicroMaterialRegistry.materialName(this.material));
    }
    
    public void load(final NBTTagCompound tag) {
        super.load(tag);
        this.material = MicroMaterialRegistry.materialID(tag.getString("mat"));
    }
    
    public abstract Iterable<Cuboid6> getCollisionBoxes();
    
    public ItemStack getItemDrop() {
        final ItemStack item = new ItemStack((Item)ItemMicroBlock.instance, 1, this.getMetadata());
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setString("mat", MicroMaterialRegistry.materialName(this.material));
        item.setTagCompound(tag);
        return item;
    }
    
    public Iterable<ItemStack> getDrops() {
        return Arrays.asList(this.getItemDrop());
    }
    
    public ItemStack pickItem(final MovingObjectPosition hit) {
        return this.getItemDrop();
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getBreakingIcon(final Object subPart, final int side) {
        return this.getBrokenIcon(side);
    }
    
    public MicroMaterialRegistry.IMicroMaterial getMaterial() {
        if (this.mat == null) {
            this.mat = MicroMaterialRegistry.getMaterial(this.material);
        }
        return this.mat;
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getBrokenIcon(final int side) {
        if (this.mat != null) {
            return this.mat.getBreakingIcon(side);
        }
        return Blocks.stone.getIcon(0, 0);
    }
    
    @SideOnly(Side.CLIENT)
    public void addHitEffects(final MovingObjectPosition hit, final EffectRenderer effectRenderer) {
        IconHitEffects.addHitEffects((JIconHitEffects)this, hit, effectRenderer);
    }
    
    @SideOnly(Side.CLIENT)
    public void addDestroyEffects(final MovingObjectPosition hit, final EffectRenderer effectRenderer) {
        IconHitEffects.addDestroyEffects((JIconHitEffects)this, effectRenderer, false);
    }
    
    public Cuboid6 getRenderBounds() {
        return this.getBounds();
    }
    
    public int getLightValue() {
        return MicroMaterialRegistry.getMaterial(this.material).getLightValue();
    }
    
    public void onNeighborChanged() {
        this.reloadShape();
    }
    
    public void drop() {
        TileMultipart.dropItem(this.getItemDrop(), this.world(), Vector3.fromTileEntityCenter((TileEntity)this.tile()));
        this.tile().remPart((TMultiPart)this);
    }
    
    public void onPartChanged(final TMultiPart part) {
        this.reloadShape();
    }
    
    public Iterable<IndexedCuboid6> getSubParts() {
        final ArrayList<IndexedCuboid6> boxes = new ArrayList<IndexedCuboid6>();
        this.overEthereal = true;
        for (final Cuboid6 cuboid6 : this.getCollisionBoxes()) {
            boxes.add(new IndexedCuboid6((Object)0, cuboid6));
        }
        this.overEthereal = false;
        return boxes;
    }
    
    public void onWorldJoin() {
        this.reloadShape();
        super.onWorldJoin();
    }
    
    public abstract void reloadShape();
    
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(final Vector3 pos, final int pass) {
        this.reloadShape();
        if (this.mat == null) {
            this.mat = MicroMaterialRegistry.getMaterial(this.material);
        }
        if (this.mat != null && this.mat.canRenderInPass(pass)) {
            this.render(pos, pass);
            return true;
        }
        return false;
    }
    
    public abstract void render(final Vector3 p0, final int p1);
    
    public float getStrength(final MovingObjectPosition hit, final EntityPlayer player) {
        return this.getMaterial().getStrength(player);
    }
}

