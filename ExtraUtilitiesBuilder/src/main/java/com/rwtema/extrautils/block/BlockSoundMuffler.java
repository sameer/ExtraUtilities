// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.block;

import com.rwtema.extrautils.network.packets.PacketTempChat;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.entity.player.EntityPlayer;
import com.rwtema.extrautils.tileentity.TileEntitySoundMuffler;
import com.rwtema.extrautils.tileentity.TileEntityRainMuffler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.block.Block;

public class BlockSoundMuffler extends Block
{
    private IIcon rainIcon;
    private IIcon rainOnIcon;
    
    public BlockSoundMuffler() {
        super(Material.cloth);
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(0.8f);
        this.setStepSound(BlockSoundMuffler.soundTypeCloth);
        this.setBlockName("extrautils:sound_muffler");
        this.setBlockTextureName("extrautils:sound_muffler");
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        this.blockIcon = par1IIconRegister.registerIcon("extrautils:sound_muffler");
        this.rainIcon = par1IIconRegister.registerIcon("extrautils:rain_muffler");
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, final int par2) {
        if (par2 == 1) {
            return this.rainIcon;
        }
        return this.blockIcon;
    }
    
    public int damageDropped(final int par1) {
        return par1;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }
    
    public boolean hasTileEntity(final int metadata) {
        return true;
    }
    
    public TileEntity createTileEntity(final World world, final int metadata) {
        if (metadata == 1) {
            return new TileEntityRainMuffler();
        }
        return new TileEntitySoundMuffler();
    }
    
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int par6, final float par7, final float par8, final float par9) {
        if (world.getBlockMetadata(x, y, z) == 1 && !XUHelper.isPlayerFake(player)) {
            NBTTagCompound tags = new NBTTagCompound();
            if (player.getEntityData().hasKey("PlayerPersisted")) {
                tags = player.getEntityData().getCompoundTag("PlayerPersisted");
            }
            else {
                player.getEntityData().setTag("PlayerPersisted", (NBTBase)tags);
            }
            if (tags.hasKey("ExtraUtilities|Rain")) {
                if (tags.getBoolean("ExtraUtilities|Rain")) {
                    tags.setBoolean("ExtraUtilities|Rain", false);
                    if (world.isRemote) {
                        PacketTempChat.sendChat(player, "You remove the magic wool from your ears");
                    }
                }
                else {
                    tags.setBoolean("ExtraUtilities|Rain", true);
                    if (world.isRemote) {
                        PacketTempChat.sendChat(player, "You place some magic wool in your ears");
                    }
                }
            }
            else {
                tags.setBoolean("ExtraUtilities|Rain", true);
                if (world.isRemote) {
                    PacketTempChat.sendChat(player, "You place some magic wool in your ears");
                }
            }
            return true;
        }
        return false;
    }
}


