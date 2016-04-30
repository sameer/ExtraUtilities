// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.block;

import net.minecraft.tileentity.TileEntity;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.packets.PacketTempChat;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.tileentity.TileEntityTradingPost;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.block.BlockContainer;

public class BlockTradingPost extends BlockContainer
{
    private IIcon[] icons;
    
    public BlockTradingPost() {
        super(Material.wood);
        this.icons = new IIcon[3];
        this.setBlockName("extrautils:trading_post");
        this.setBlockTextureName("extrautils:trading_post");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(1.0f);
        this.setResistance(10.0f).setStepSound(BlockTradingPost.soundTypeWood);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        this.icons[0] = par1IIconRegister.registerIcon("planks");
        this.icons[1] = par1IIconRegister.registerIcon("extrautils:trading_post_top");
        this.icons[2] = par1IIconRegister.registerIcon("extrautils:trading_post_side");
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, final int par2) {
        if (par1 <= 1) {
            return this.icons[par1];
        }
        return this.icons[2];
    }
    
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int par6, final float par7, final float par8, final float par9) {
        if (world.isRemote) {
            return true;
        }
        if (world.getTileEntity(x, y, z) instanceof TileEntityTradingPost) {
            final XUPacketBase packet = ((TileEntityTradingPost)world.getTileEntity(x, y, z)).getTradePacket(player);
            if (packet != null) {
                NetworkHandler.sendPacketToPlayer(packet, player);
            }
            else {
                PacketTempChat.sendChat(player, "No villagers found in range");
            }
            return true;
        }
        return false;
    }
    
    public TileEntity createNewTileEntity(final World var1, final int var2) {
        return new TileEntityTradingPost();
    }
}


