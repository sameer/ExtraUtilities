// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.multipart;

import codechicken.lib.vec.BlockCoord;
import net.minecraft.world.World;
import java.util.HashSet;
import codechicken.multipart.TMultiPart;
import net.minecraft.block.Block;
import java.util.Set;
import codechicken.multipart.MultiPartRegistry;

public class RegisterBlockPart implements MultiPartRegistry.IPartConverter, MultiPartRegistry.IPartFactory
{
    public Set<Block> t;
    Block block;
    Class<? extends TMultiPart> part;
    String name;
    
    public RegisterBlockPart(final Block block, final Class<? extends TMultiPart> part) {
        this.t = null;
        this.block = null;
        this.part = null;
        this.name = "";
        try {
            this.name = ((TMultiPart)part.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0])).getType();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public RegisterBlockPart(final Block block, final Class<? extends TMultiPart> part, final String name) {
        this.t = null;
        this.block = null;
        this.part = null;
        this.name = "";
        this.block = block;
        this.part = part;
        this.name = name;
    }
    
    public TMultiPart createPart(final String name, final boolean client) {
        if (name.equals(name)) {
            try {
                return (TMultiPart)this.part.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
    
    public void init() {
        if (this.name.equals("") || this.block == null || this.part == null) {
            return;
        }
        MultiPartRegistry.registerConverter((MultiPartRegistry.IPartConverter)this);
        MultiPartRegistry.registerParts((MultiPartRegistry.IPartFactory)this, new String[] { this.name });
    }
    
    public Iterable<Block> blockTypes() {
        if (this.t == null) {
            (this.t = new HashSet<Block>()).add(this.block);
        }
        return this.t;
    }
    
    public TMultiPart convert(final World world, final BlockCoord pos) {
        final Block id = world.getBlock(pos.x, pos.y, pos.z);
        if (id == this.block) {
            try {
                return (TMultiPart)this.part.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}


