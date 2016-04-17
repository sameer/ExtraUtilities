// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.EntityLivingBase;
import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import com.rwtema.extrautils.block.BlockSpike;
import java.lang.reflect.Field;
import net.minecraft.item.ItemSword;

public class ItemSwordSpike extends ItemSword
{
    public static final Field mat;
    public final BlockSpike spike;
    
    public ItemSwordSpike(final Block spike) {
        this((BlockSpike)spike);
    }
    
    public static Item.ToolMaterial getMaterial(final BlockSpike spike) {
        final ItemSword item = (ItemSword)spike.swordStack.getItem();
        try {
            return (Item.ToolMaterial)ItemSwordSpike.mat.get(item);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    public ItemSwordSpike(final BlockSpike spike) {
        super(getMaterial(spike));
        this.spike = spike;
        spike.itemSpike = (Item)this;
        this.maxStackSize = 64;
        this.setMaxDamage(0);
    }
    
    public int getSpriteNumber() {
        return 0;
    }
    
    public boolean onItemUse(final ItemStack p_77648_1_, final EntityPlayer p_77648_2_, final World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, final float p_77648_8_, final float p_77648_9_, final float p_77648_10_) {
        final Block block = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);
        if (block == Blocks.snow_layer && (p_77648_3_.getBlockMetadata(p_77648_4_, p_77648_5_, p_77648_6_) & 0x7) < 1) {
            p_77648_7_ = 1;
        }
        else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable((IBlockAccess)p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_)) {
            if (p_77648_7_ == 0) {
                --p_77648_5_;
            }
            if (p_77648_7_ == 1) {
                ++p_77648_5_;
            }
            if (p_77648_7_ == 2) {
                --p_77648_6_;
            }
            if (p_77648_7_ == 3) {
                ++p_77648_6_;
            }
            if (p_77648_7_ == 4) {
                --p_77648_4_;
            }
            if (p_77648_7_ == 5) {
                ++p_77648_4_;
            }
        }
        if (p_77648_1_.stackSize == 0) {
            return false;
        }
        if (!p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_)) {
            return false;
        }
        if (p_77648_5_ == 255 && this.spike.getMaterial().isSolid()) {
            return false;
        }
        if (p_77648_3_.canPlaceEntityOnSide((Block)this.spike, p_77648_4_, p_77648_5_, p_77648_6_, false, p_77648_7_, (Entity)p_77648_2_, p_77648_1_)) {
            final int i1 = this.getMetadata(p_77648_1_.getItemDamage());
            final int j1 = this.spike.onBlockPlaced(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, i1);
            if (this.placeBlockAt(p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, j1)) {
                p_77648_3_.playSoundEffect((double)(p_77648_4_ + 0.5f), (double)(p_77648_5_ + 0.5f), (double)(p_77648_6_ + 0.5f), this.spike.stepSound.func_150496_b(), (this.spike.stepSound.getVolume() + 1.0f) / 2.0f, this.spike.stepSound.getPitch() * 0.8f);
                --p_77648_1_.stackSize;
            }
            return true;
        }
        return false;
    }
    
    public String getUnlocalizedName(final ItemStack p_77667_1_) {
        return this.spike.getUnlocalizedName();
    }
    
    public String getUnlocalizedName() {
        return this.spike.getUnlocalizedName();
    }
    
    @SideOnly(Side.CLIENT)
    public CreativeTabs getCreativeTab() {
        return this.spike.getCreativeTabToDisplayOn();
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item p_150895_1_, final CreativeTabs p_150895_2_, final List p_150895_3_) {
        this.spike.getSubBlocks(p_150895_1_, p_150895_2_, p_150895_3_);
    }
    
    public boolean placeBlockAt(final ItemStack stack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ, final int metadata) {
        if (!world.setBlock(x, y, z, (Block)this.spike, metadata, 3)) {
            return false;
        }
        if (world.getBlock(x, y, z) == this.spike) {
            this.spike.onBlockPlacedBy(world, x, y, z, (EntityLivingBase)player, stack);
            this.spike.onPostBlockPlaced(world, x, y, z, metadata);
        }
        return true;
    }
    
    public String getString() {
        String s = this.spike.getUnlocalizedName().substring("tile.".length());
        s = s.replace("extrautils:", "");
        return s;
    }
    
    public boolean isItemTool(final ItemStack p_77616_1_) {
        return true;
    }
    
    static {
        mat = ReflectionHelper.findField((Class)ItemSword.class, new String[] { "field_150933_b" });
    }
}
