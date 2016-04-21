// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeLiquid;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeInventory;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeHyperEnergy;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeEnergy;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeLiquid;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeInventory;
import codechicken.multipart.TMultiPart;
import codechicken.lib.vec.BlockCoord;
import net.minecraft.world.World;
import java.util.Set;
import com.rwtema.extrautils.ExtraUtils;
import java.util.HashSet;
import net.minecraft.block.Block;
import codechicken.multipart.MultiPartRegistry;

public class RegisterTransferNodeParts implements MultiPartRegistry.IPartFactory, MultiPartRegistry.IPartConverter
{
    public void init() {
        MultiPartRegistry.registerConverter((MultiPartRegistry.IPartConverter)this);
        MultiPartRegistry.registerParts((MultiPartRegistry.IPartFactory)this, new String[] { "extrautils:transfer_node_inv", "extrautils:transfer_node_liquid", "extrautils:transfer_node_energy", "extrautils:transfer_node_inv_remote", "extrautils:transfer_node_liquid_remote", "extrautils:transfer_node_energy_hyper" });
    }
    
    public Iterable<Block> blockTypes() {
        final Set<Block> s = new HashSet<Block>();
        s.add(ExtraUtils.transferNode);
        s.add(ExtraUtils.transferNodeRemote);
        return s;
    }
    
    public TMultiPart convert(final World world, final BlockCoord pos) {
        final Block id = world.getBlock(pos.x, pos.y, pos.z);
        final int meta = world.getBlockMetadata(pos.x, pos.y, pos.z);
        if (id == ExtraUtils.transferNode) {
            if (meta < 6) {
                if (world.getTileEntity(pos.x, pos.y, pos.z) instanceof TileEntityTransferNodeInventory) {
                    return (TMultiPart)new TransferNodePartInventory(meta, (TileEntityTransferNodeInventory)world.getTileEntity(pos.x, pos.y, pos.z));
                }
            }
            else if (meta < 12) {
                if (world.getTileEntity(pos.x, pos.y, pos.z) instanceof TileEntityTransferNodeLiquid) {
                    return (TMultiPart)new TransferNodePartLiquid(meta, (TileEntityTransferNodeLiquid)world.getTileEntity(pos.x, pos.y, pos.z));
                }
            }
            else if (meta == 12) {
                if (world.getTileEntity(pos.x, pos.y, pos.z) instanceof TileEntityTransferNodeEnergy) {
                    return (TMultiPart)new TransferNodePartEnergy(meta, (TileEntityTransferNodeEnergy)world.getTileEntity(pos.x, pos.y, pos.z));
                }
            }
            else if (meta == 13 && world.getTileEntity(pos.x, pos.y, pos.z) instanceof TileEntityTransferNodeHyperEnergy) {
                return (TMultiPart)new TransferNodePartHyperEnergy(meta, (TileEntityTransferNodeHyperEnergy)world.getTileEntity(pos.x, pos.y, pos.z));
            }
        }
        if (id == ExtraUtils.transferNodeRemote) {
            if (meta < 6) {
                if (world.getTileEntity(pos.x, pos.y, pos.z) instanceof TileEntityRetrievalNodeInventory) {
                    return (TMultiPart)new TransferNodePartInventoryRemote(meta, (TileEntityRetrievalNodeInventory)world.getTileEntity(pos.x, pos.y, pos.z));
                }
            }
            else if (meta < 12) {
                if (world.getTileEntity(pos.x, pos.y, pos.z) instanceof TileEntityRetrievalNodeLiquid) {
                    return (TMultiPart)new TransferNodePartLiquidRemote(meta, (TileEntityRetrievalNodeLiquid)world.getTileEntity(pos.x, pos.y, pos.z));
                }
            }
            else if (meta == 12 && world.getTileEntity(pos.x, pos.y, pos.z) instanceof TileEntityTransferNodeEnergy) {
                return (TMultiPart)new TransferNodePartEnergy(meta, (TileEntityTransferNodeEnergy)world.getTileEntity(pos.x, pos.y, pos.z));
            }
        }
        return null;
    }
    
    public TMultiPart createPart(final String name, final boolean client) {
        if (name.equals("extrautils:transfer_node_inv")) {
            return (TMultiPart)new TransferNodePartInventory();
        }
        if (name.equals("extrautils:transfer_node_liquid")) {
            return (TMultiPart)new TransferNodePartLiquid();
        }
        if (name.equals("extrautils:transfer_node_energy")) {
            return (TMultiPart)new TransferNodePartEnergy();
        }
        if (name.equals("extrautils:transfer_node_inv_remote")) {
            return (TMultiPart)new TransferNodePartInventoryRemote();
        }
        if (name.equals("extrautils:transfer_node_liquid_remote")) {
            return (TMultiPart)new TransferNodePartLiquidRemote();
        }
        if (name.equals("extrautils:transfer_node_energy_hyper")) {
            return (TMultiPart)new TransferNodePartHyperEnergy();
        }
        return null;
    }
}

