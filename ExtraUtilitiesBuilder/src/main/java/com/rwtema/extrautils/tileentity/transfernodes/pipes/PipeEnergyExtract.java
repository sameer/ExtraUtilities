// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import net.minecraftforge.common.util.ForgeDirection;
import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import net.minecraft.util.IIcon;

public class PipeEnergyExtract extends PipeEnergy
{
    public static final String name = "Energy_Extract";
    
    public PipeEnergyExtract() {
        super("Energy_Extract");
    }
    
    @Override
    public IIcon baseTexture() {
        return BlockTransferPipe.pipes_energy_extract;
    }
    
    @Override
    public IIcon pipeTexture(final ForgeDirection dir, final boolean blocked) {
        return BlockTransferPipe.pipes_energy_extract;
    }
    
    @Override
    public IIcon invPipeTexture(final ForgeDirection dir) {
        return BlockTransferPipe.pipes_energy_extract;
    }
    
    @Override
    public IIcon socketTexture(final ForgeDirection dir) {
        return BlockTransferPipe.pipes_nozzle_energy_extract;
    }
}


