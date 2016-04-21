// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import com.rwtema.extrautils.helper.XUHelper;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import com.rwtema.extrautils.ChunkPos;
import com.rwtema.extrautils.ExtraUtilsMod;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.entity.EntityLiving;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.util.IIcon;
import net.minecraft.item.Item;

public class ItemBuildersWand extends Item implements IItemMultiTransparency
{
    public int maxBlocks;
    private IIcon[] icons;
    
    public ItemBuildersWand(final int par2) {
        this.maxBlocks = 9;
        this.maxStackSize = 1;
        this.setUnlocalizedName("extrautils:builderswand");
        this.maxBlocks = par2;
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    }
    
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(final ItemStack par1ItemStack, final int pass) {
        return false;
    }
    
    public boolean hitEntity(final ItemStack par1ItemStack, final EntityLiving par2EntityLiving, final EntityLiving par3EntityLiving) {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }
    
    public EnumAction getItemUseAction(final ItemStack par1ItemStack) {
        return EnumAction.none;
    }
    
    public boolean onItemUseFirst(final ItemStack stack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ) {
        this.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        if (world.isRemote) {
            ExtraUtilsMod.proxy.sendUsePacket(x, y, z, side, stack, hitX, hitY, hitZ);
        }
        return true;
    }
    
    public boolean onItemUse(final ItemStack wand, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ) {
        if (world.isRemote) {
            return true;
        }
        if (!player.capabilities.allowEdit) {
            return false;
        }
        final List<ChunkPos> blocks = this.getPotentialBlocks(player, world, x, y, z, side);
        if (blocks.size() == 0) {
            return false;
        }
        final Block blockId = world.getBlock(x, y, z);
        if (world.isAirBlock(x, y, z)) {
            return false;
        }
        int data = -1;
        final Item item1 = Item.getItemFromBlock(blockId);
        if (item1.getHasSubtypes()) {
            data = blockId.getDamageValue(world, x, y, z);
        }
        if (blocks.size() > 0) {
            int slot = 0;
            for (final ChunkPos temp : blocks) {
                for (slot = 0; slot < player.inventory.getSizeInventory() && (player.inventory.getStackInSlot(slot) == null || player.inventory.getStackInSlot(slot).getItem() != item1 || !(data == -1 | data == player.inventory.getStackInSlot(slot).getItemDamage())); ++slot) {}
                if (slot >= player.inventory.getSizeInventory()) {
                    break;
                }
                ItemStack item2 = player.inventory.getStackInSlot(slot);
                if (player.capabilities.isCreativeMode) {
                    item2 = item2.copy();
                }
                if (!item2.tryPlaceItemIntoWorld(player, world, temp.x, temp.y, temp.z, side, hitX, hitY, hitZ) || player.capabilities.isCreativeMode || item2.stackSize != 0) {
                    continue;
                }
                player.inventory.setInventorySlotContents(slot, (ItemStack)null);
            }
            player.inventory.markDirty();
            if (player instanceof EntityPlayerMP) {
                ((EntityPlayerMP)player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)player);
            }
        }
        return true;
    }
    
    public List<ChunkPos> getPotentialBlocks(final EntityPlayer player, final World world, final int x, final int y, final int z, final int face) {
        final List<ChunkPos> blocks = new ArrayList<ChunkPos>();
        if (world == null) {
            return blocks;
        }
        final Block blockId = world.getBlock(x, y, z);
        if (world.isAirBlock(x, y, z)) {
            return blocks;
        }
        if (player == null || XUHelper.isPlayerFake(player)) {
            return blocks;
        }
        int data = -1;
        if (Item.getItemFromBlock(blockId) == null) {
            return blocks;
        }
        if (Item.getItemFromBlock(blockId).getHasSubtypes()) {
            data = blockId.getDamageValue(world, x, y, z);
        }
        int numBlocks = 0;
        ItemStack genericStack = null;
        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            if (player.inventory.getStackInSlot(i) != null) {
                if (player.inventory.getStackInSlot(i).getItem() == Item.getItemFromBlock(blockId) && (data == -1 || data == player.inventory.getStackInSlot(i).getItemDamage())) {
                    genericStack = player.inventory.getStackInSlot(i);
                    if (player.capabilities.isCreativeMode) {
                        numBlocks = this.maxBlocks;
                        break;
                    }
                    numBlocks += player.inventory.getStackInSlot(i).stackSize;
                }
                if (numBlocks >= this.maxBlocks) {
                    numBlocks = this.maxBlocks;
                    break;
                }
            }
        }
        final int dx = Facing.offsetsXForSide[face];
        final int dy = Facing.offsetsYForSide[face];
        final int dz = Facing.offsetsZForSide[face];
        int mx = (dx == 0) ? 1 : 0;
        int my = (dy == 0) ? 1 : 0;
        int mz = (dz == 0) ? 1 : 0;
        boolean hollow = false;
        if (ExtraUtilsMod.proxy.isAltSneaking()) {
            if (player.isSneaking()) {
                hollow = true;
            }
            else {
                if (face <= 1) {
                    return blocks;
                }
                mx = 0;
                mz = 0;
                my = 1;
            }
        }
        else if (player.isSneaking()) {
            if (face <= 1) {
                return blocks;
            }
            my = 0;
        }
        final AxisAlignedBB var11 = blockId.getCollisionBoundingBoxFromPool(world, x, y, z);
        if (numBlocks <= 0 || !(blockId.canPlaceBlockOnSide(world, x + dx, y + dy, z + dz, face) & y + dy < 255)) {
            return blocks;
        }
        if (!this.checkAAB(world, var11, dx, dy, dz)) {
            return blocks;
        }
        blocks.add(new ChunkPos(x + dx, y + dy, z + dz));
        for (int j = 0; j < blocks.size() & blocks.size() < numBlocks; ++j) {
            for (int ax = -mx; ax <= mx; ++ax) {
                for (int ay = -my; ay <= my; ++ay) {
                    for (int az = -mz; az <= mz; ++az) {
                        final ChunkPos temp = new ChunkPos(blocks.get(j).x + ax, blocks.get(j).y + ay, blocks.get(j).z + az);
                        if (blocks.size() < numBlocks && player.canPlayerEdit(temp.x, temp.y, temp.z, face, genericStack) && !blocks.contains(temp) && world.getBlock(temp.x - dx, temp.y - dy, temp.z - dz) == blockId && blockId.canPlaceBlockOnSide(world, temp.x, temp.y, temp.z, face) && (data == -1 || data == blockId.getDamageValue(world, temp.x - dx, temp.y - dy, temp.z - dz)) && this.checkAAB(world, var11, temp.x - x, temp.y - y, temp.z - z) && !blocks.contains(temp)) {
                            blocks.add(temp);
                        }
                    }
                }
            }
        }
        return blocks;
    }
    
    public boolean checkAAB(final World world, final AxisAlignedBB bounds, final int dx, final int dy, final int dz) {
        return bounds == null || world.checkNoEntityCollision(bounds.getOffsetBoundingBox((double)dx, (double)dy, (double)dz));
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IIconRegister) {
        this.icons = new IIcon[2];
        final IIcon[] icons = this.icons;
        final int n = 0;
        final IIcon registerIcon = par1IIconRegister.registerIcon(this.getUnlocalizedName().substring(5));
        icons[n] = registerIcon;
        this.itemIcon = registerIcon;
        this.icons[1] = par1IIconRegister.registerIcon(this.getUnlocalizedName().substring(5) + "1");
    }
    
    public int numIcons(final ItemStack item) {
        return 2;
    }
    
    public IIcon getIconForTransparentRender(final ItemStack item, final int pass) {
        return this.icons[pass];
    }
    
    public float getIconTransparency(final ItemStack item, final int pass) {
        if (pass == 1) {
            return 0.5f;
        }
        return 1.0f;
    }
}

