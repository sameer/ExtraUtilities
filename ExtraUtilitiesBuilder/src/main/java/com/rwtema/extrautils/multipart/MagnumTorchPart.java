// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.multipart;

import com.rwtema.extrautils.EventHandlerServer;
import net.minecraft.tileentity.TileEntity;
import com.rwtema.extrautils.tileentity.TileEntityAntiMobTorch;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import java.util.ArrayList;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.Block;
import codechicken.lib.vec.Cuboid6;
import com.rwtema.extrautils.tileentity.IAntiMobTorch;
import codechicken.multipart.IRandomDisplayTick;
import codechicken.multipart.minecraft.McMetaPart;

public class MagnumTorchPart extends McMetaPart implements IRandomDisplayTick, IAntiMobTorch
{
    public Cuboid6 getBounds() {
        return new Cuboid6(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);
    }
    
    public Block getBlock() {
        return ExtraUtils.magnumTorch;
    }
    
    public Iterable<Cuboid6> getCollisionBoxes() {
        final ArrayList t = new ArrayList();
        t.add(this.getBounds());
        return (Iterable<Cuboid6>)t;
    }
    
    public String getType() {
        return "XU|MagnumTorch";
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final Random random) {
        ExtraUtils.magnumTorch.randomDisplayTick(this.tile().getWorldObj(), this.x(), this.y(), this.z(), random);
    }
    
    public void onWorldJoin() {
        final int[] myCoord = TileEntityAntiMobTorch.getCoord((TileEntity)this.tile());
        for (int i = 0; i < EventHandlerServer.magnumTorchRegistry.size(); ++i) {
            final int[] coord = EventHandlerServer.magnumTorchRegistry.get(i);
            if (myCoord[0] == coord[0] && myCoord[1] == coord[1] && myCoord[2] == coord[2] && myCoord[3] == coord[3]) {
                return;
            }
        }
        EventHandlerServer.magnumTorchRegistry.add(myCoord);
    }
    
    public void onWorldSeparate() {
        final int[] myCoord = { this.getWorld().provider.dimensionId, this.x(), this.y(), this.z() };
        EventHandlerServer.magnumTorchRegistry.remove(myCoord);
    }
    
    public float getHorizontalTorchRangeSquared() {
        return 16384.0f;
    }
    
    public float getVerticalTorchRangeSquared() {
        return 1024.0f;
    }
}


