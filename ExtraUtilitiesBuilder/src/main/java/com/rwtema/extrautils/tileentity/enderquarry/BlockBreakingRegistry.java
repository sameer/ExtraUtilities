// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.enderquarry;

import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import java.util.Iterator;
import java.lang.reflect.Method;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockFence;
import com.rwtema.extrautils.LogHelper;
import net.minecraft.init.Blocks;
import java.util.HashSet;
import net.minecraft.launchwrapper.LaunchClassLoader;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import java.util.HashMap;

public class BlockBreakingRegistry
{
    public static BlockBreakingRegistry instance;
    public static HashMap<Block, entry> entries;
    public static Set<String> methodNames;
    public static Map<String, Boolean> names;
    public static LaunchClassLoader cl;
    
    public static boolean blackList(final Block id) {
        return BlockBreakingRegistry.entries.get(id).blackList;
    }
    
    public static boolean isSpecial(final Block id) {
        return BlockBreakingRegistry.entries.get(id).isSpecial;
    }
    
    public static boolean isFence(final Block id) {
        return BlockBreakingRegistry.entries.get(id).isFence;
    }
    
    public static boolean isFluid(final Block id) {
        return BlockBreakingRegistry.entries.get(id).isFluid;
    }
    
    public void setupBreaking() {
        if (BlockBreakingRegistry.methodNames == null) {
            BlockBreakingRegistry.methodNames = new HashSet<String>();
            for (final Method m : BlockDummy.class.getDeclaredMethods()) {
                BlockBreakingRegistry.methodNames.add(m.getName());
            }
            for (final Object aBlockRegistry : Block.blockRegistry) {
                BlockBreakingRegistry.entries.put((Block)aBlockRegistry, new entry());
            }
            BlockBreakingRegistry.entries.get(Blocks.torch).blackList = true;
            for (final Object aBlockRegistry : Block.blockRegistry) {
                final Block block = (Block)aBlockRegistry;
                final entry e = BlockBreakingRegistry.entries.get(block);
                String name = block.getClass().getName();
                if (block.getUnlocalizedName() != null) {
                    name = block.getUnlocalizedName();
                }
                try {
                    name = Block.blockRegistry.getNameForObject((Object)block);
                }
                catch (Exception err) {
                    LogHelper.error("Error getting name for block " + name, new Object[0]);
                    err.printStackTrace();
                }
                e.isFence = false;
                try {
                    e.isFence = (block.getRenderType() == 11);
                }
                catch (Exception err) {
                    LogHelper.error("Error checking block class code: Exception calling getRenderType() on block " + name, new Object[0]);
                    err.printStackTrace();
                }
                catch (NoClassDefFoundError err2) {
                    throw new RuntimeException("Serious error calling getRenderType() on block " + name + " : Likely cause is client-side code is being called server-side", err2);
                }
                catch (Throwable err3) {
                    throw new RuntimeException("Serious error calling getRenderType() on block " + name, err3);
                }
                e.isFence = (e.isFence || block instanceof BlockFence);
                if (block instanceof BlockLiquid || block instanceof IFluidBlock) {
                    e.blackList = true;
                    e.isFluid = true;
                }
                e.isSpecial = this.hasSpecialBreaking(block.getClass());
            }
        }
    }
    
    public boolean hasSpecialBreaking(final Class clazz) {
        if (clazz == null || clazz.equals(Block.class)) {
            return false;
        }
        if (BlockBreakingRegistry.names.containsKey(clazz.getName())) {
            return BlockBreakingRegistry.names.get(clazz.getName());
        }
        LogHelper.fine("Checking class for special block breaking code: " + clazz.getName(), new Object[0]);
        try {
            byte[] bytes;
            if (clazz.getClassLoader() instanceof LaunchClassLoader) {
                bytes = ((LaunchClassLoader)clazz.getClassLoader()).getClassBytes(clazz.getName());
            }
            else {
                bytes = BlockBreakingRegistry.cl.getClassBytes(clazz.getName());
            }
            final ClassNode classNode = new ClassNode();
            final ClassReader classReader = new ClassReader(bytes);
            classReader.accept((ClassVisitor)classNode, 0);
            for (final MethodNode method : classNode.methods) {
                if (BlockBreakingRegistry.methodNames.contains(method.name)) {
                    LogHelper.fine("Detected special block breaking code in class: " + clazz.getName(), new Object[0]);
                    BlockBreakingRegistry.names.put(clazz.getName(), true);
                    return true;
                }
            }
        }
        catch (Throwable e) {
            try {
                for (final Method m : clazz.getDeclaredMethods()) {
                    if (BlockBreakingRegistry.methodNames.contains(m.getName())) {
                        LogHelper.fine("Detected special block breaking code in class: " + clazz.getName(), new Object[0]);
                        BlockBreakingRegistry.names.put(clazz.getName(), true);
                        return true;
                    }
                }
            }
            catch (Throwable e2) {
                LogHelper.error("Error checking block class code: " + clazz.getName(), new Object[0]);
                e.printStackTrace();
                e2.printStackTrace();
                BlockBreakingRegistry.names.put(clazz.getName(), true);
                return true;
            }
        }
        final boolean result = this.hasSpecialBreaking(clazz.getSuperclass());
        BlockBreakingRegistry.names.put(clazz.getName(), result);
        return result;
    }
    
    static {
        BlockBreakingRegistry.instance = new BlockBreakingRegistry();
        BlockBreakingRegistry.entries = new HashMap<Block, entry>();
        BlockBreakingRegistry.methodNames = null;
        BlockBreakingRegistry.names = new HashMap<String, Boolean>();
        BlockBreakingRegistry.cl = (LaunchClassLoader)BlockBreakingRegistry.class.getClassLoader();
    }
    
    public static class entry
    {
        public boolean isSpecial;
        public boolean blackList;
        public boolean isFence;
        public boolean isFluid;
        
        public entry() {
            this.isSpecial = false;
            this.blackList = false;
            this.isFence = false;
            this.isFluid = false;
        }
    }
}
