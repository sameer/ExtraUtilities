// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.endercollector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.rwtema.extrautils.block.BoxModel;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import com.rwtema.extrautils.block.BlockMultiBlock;

public class BlockEnderCollector extends BlockMultiBlock
{
    IIcon side;
    IIcon bottom;
    IIcon top1;
    IIcon top2;
    IIcon side_disabled;
    IIcon top2_disabled;
    
    public BlockEnderCollector() {
        super(Material.rock);
        this.setBlockName("extrautils:enderCollector");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(1.5f).setStepSound(BlockEnderCollector.soundTypeStone);
    }
    
    public void prepareForRender(final String label) {
    }
    
    public BoxModel getWorldModel(final IBlockAccess world, final int x, final int y, final int z) {
        return this.getInventoryModel(world.getBlockMetadata(x, y, z));
    }
    
    public BoxModel getInventoryModel(final int metadata) {
        final boolean disabled = metadata >= 6;
        final BoxModel boxes = new BoxModel();
        final IIcon sideIcon = disabled ? this.side_disabled : this.side;
        boxes.addBoxI(1, 0, 4, 15, 2, 12).setTexture(sideIcon).setTextureSides(this.bottom, this.top1);
        boxes.addBoxI(4, 0, 1, 12, 2, 15).setTexture(sideIcon).setTextureSides(this.bottom, this.top1);
        boxes.addBoxI(4, 2, 4, 12, 4, 12).setTexture(sideIcon).setTextureSides(this.bottom, this.top1);
        boxes.addBoxI(5, 4, 5, 11, 6, 11).setTexture(sideIcon).setTextureSides(this.bottom, this.top1);
        boxes.addBoxI(6, 6, 6, 10, 16, 10).setTexture(sideIcon).setTextureSides(this.bottom, this.top1);
        final IIcon top2Icon = disabled ? this.top2_disabled : this.top2;
        boxes.addBoxI(6, 10, 1, 10, 14, 15).setTexture(sideIcon).setTextureSides(top2Icon, top2Icon);
        boxes.addBoxI(1, 10, 6, 15, 14, 10).setTexture(sideIcon).setTextureSides(top2Icon, top2Icon);
        boxes.rotateToSideTex(ForgeDirection.getOrientation(metadata % 6));
        return boxes;
    }
    
    public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block block) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEnderCollector) {
            ((TileEnderCollector)tileEntity).onNeighbourChange();
        }
    }
    
    public void registerBlockIcons(final IIconRegister register) {
        this.side = register.registerIcon("extrautils:enderCollectorSide");
        this.side_disabled = register.registerIcon("extrautils:enderCollectorSide_disabled");
        final IIcon registerIcon = register.registerIcon("extrautils:enderCollectorBottom");
        this.bottom = registerIcon;
        this.blockIcon = registerIcon;
        this.top1 = register.registerIcon("extrautils:enderCollectorTop1");
        this.top2 = register.registerIcon("extrautils:enderCollectorTop2");
        this.top2_disabled = register.registerIcon("extrautils:enderCollectorTop2_disabled");
    }
    
    public boolean hasTileEntity(final int metadata) {
        return true;
    }
    
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileEnderCollector();
    }
    
    public int onBlockPlaced(final World world, final int x, final int y, final int z, final int side, final float p_149660_6_, final float p_149660_7_, final float p_149660_8_, final int meta) {
        return side ^ 0x1;
    }
    
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX, final float hitY, final float hitZ) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEnderCollector) {
            return ((TileEnderCollector)tileEntity).onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
        }
        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }
    
    public void breakBlock(final World world, final int x, final int y, final int z, final Block p_149749_5_, final int p_149749_6_) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEnderCollector) {
            ((TileEnderCollector)tileEntity).dropItems();
        }
        super.breakBlock(world, x, y, z, p_149749_5_, p_149749_6_);
    }
}

