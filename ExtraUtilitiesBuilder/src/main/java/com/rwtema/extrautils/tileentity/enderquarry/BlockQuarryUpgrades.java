// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.enderquarry;

import com.rwtema.extrautils.helper.Translate;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import com.rwtema.extrautils.block.Box;
import com.rwtema.extrautils.block.BoxModel;
import net.minecraft.world.IBlockAccess;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import com.rwtema.extrautils.block.IBlockTooltip;
import com.rwtema.extrautils.block.BlockMultiBlockSelection;

public class BlockQuarryUpgrades extends BlockMultiBlockSelection implements IBlockTooltip
{
    int[] powerDrain;
    IIcon[] icons;
    IIcon[] iconsFlipped;
    IIcon arms;
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister p_149651_1_) {
        for (int i = 0; i < 10; ++i) {
            this.icons[i] = p_149651_1_.registerIcon("extrautils:quarry_upgrades/quarryUpgrade" + i);
            this.iconsFlipped[i] = (IIcon)new IconFlipped(this.icons[i], true, false);
        }
        final IIcon registerIcon = p_149651_1_.registerIcon("extrautils:quarry_upgrades/quarryUpgradeArm");
        this.arms = registerIcon;
        this.blockIcon = registerIcon;
    }
    
    public BlockQuarryUpgrades() {
        super(Material.rock);
        this.powerDrain = new int[16];
        this.icons = new IIcon[16];
        this.iconsFlipped = new IIcon[16];
        this.arms = null;
        this.setBlockName("extrautils:enderQuarryUpgrade");
        this.setBlockTextureName("extrautils:enderQuarryUpgrade");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(1.0f);
        this.setStepSound(BlockQuarryUpgrades.soundTypeStone);
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item p_149666_1_, final CreativeTabs p_149666_2_, final List p_149666_3_) {
        for (int i = 0; i < 10; ++i) {
            p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
        }
    }
    
    public void prepareForRender(final String label) {
    }
    
    public BoxModel getWorldModel(final IBlockAccess world, final int x, final int y, final int z) {
        final int metadata = world.getBlockMetadata(x, y, z);
        final BoxModel model = this.getInventoryModel(metadata);
        model.get(0).textureSide[2] = this.iconsFlipped[metadata];
        model.get(0).textureSide[3] = null;
        for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == ExtraUtils.enderQuarry) {
                final Box b = new Box(0.125f, 0.0f, 0.125f, 0.875f, 0.0625f, 0.875f);
                b.rotateToSide(dir);
                b.texture = this.arms;
                model.add(b);
            }
        }
        return model;
    }
    
    public BoxModel getInventoryModel(final int metadata) {
        final BoxModel b = new BoxModel();
        b.addBoxI(1, 1, 1, 15, 15, 15);
        b.get(0).texture = this.icons[metadata];
        b.get(0).textureSide[0] = this.iconsFlipped[metadata];
        b.get(0).textureSide[3] = this.iconsFlipped[metadata];
        b.get(0).textureSide[5] = this.iconsFlipped[metadata];
        return b;
    }
    
    public int damageDropped(final int p_149692_1_) {
        return p_149692_1_;
    }
    
    @Override
    public void addInformation(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final List par3List, final boolean par4) {
        final int meta = par1ItemStack.getItemDamage() & 0xF;
        final double v = TileEntityEnderQuarry.powerMultipliers[meta];
        final String format = XUHelper.niceFormat(v);
        par3List.add(Translate.get("power.drain", format));
    }
}

