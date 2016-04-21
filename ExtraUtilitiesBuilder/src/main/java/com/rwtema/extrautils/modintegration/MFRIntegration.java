// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.modintegration;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import powercrystals.minefactoryreloaded.api.FactoryRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.ExtraUtils;

public class MFRIntegration
{
    private static MFRIntegration instance;
    
    public static void registerMFRIntegration() {
        if (MFRIntegration.instance != null) {
            throw new IllegalStateException("Already registered");
        }
        if (!ExtraUtils.enderLilyEnabled) {
            return;
        }
        MFRIntegration.instance = new MFRIntegration();
        FactoryRegistry.sendMessage("registerHarvestable_Crop", (Object)new ItemStack((Block)ExtraUtils.enderLily, 1, 7));
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setString("seed", Item.itemRegistry.getNameForObject((Object)Item.getItemFromBlock((Block)ExtraUtils.enderLily)));
        tag.setString("crop", Block.blockRegistry.getNameForObject((Object)ExtraUtils.enderLily));
        FactoryRegistry.sendMessage("registerPlantable_Standard", (Object)tag);
    }
    
    static {
        MFRIntegration.instance = null;
    }
}


