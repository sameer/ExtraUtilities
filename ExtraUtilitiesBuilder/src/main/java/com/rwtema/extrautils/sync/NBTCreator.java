// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.sync;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.objectweb.asm.MethodVisitor;
import java.util.Iterator;
import java.util.List;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import java.util.ArrayList;
import org.objectweb.asm.Type;
import java.util.HashMap;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class NBTCreator
{
    public static LaunchClassLoader cl;
    public static String type;
    private static final HashMap<Type, Class<?>> handlers;
    
    public static AutoNBT createAutoNBT(final Class<?> clazz) {
        final String targetName = clazz.getName();
        final String targetType = Type.getDescriptor((Class)clazz);
        byte[] bytes;
        try {
            bytes = NBTCreator.cl.getClassBytes(targetName);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        final List<FieldNode> fields = new ArrayList<FieldNode>();
        final ClassNode classNode = new ClassNode();
        final ClassReader classReader = new ClassReader(bytes);
        classReader.accept((ClassVisitor)classNode, 1);
        for (final FieldNode field : classNode.fields) {
            for (final AnnotationNode ann : field.visibleAnnotations) {
                if (NBTCreator.type.equals(ann.desc)) {
                    fields.add(field);
                    break;
                }
            }
        }
        final ClassWriter cw = new ClassWriter(1);
        final String name = "FLM_ItemWrench";
        final String superName = Type.getInternalName((Class)AutoNBT.class);
        cw.visit(50, 33, name, (String)null, superName, new String[0]);
        cw.visitSource(".dynamic", (String)null);
        MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", (String)null, (String[])null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        mv.visitMethodInsn(183, superName, "<init>", "()V", false);
        mv.visitInsn(177);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        mv = cw.visitMethod(1, "writeToNBT", "(Lnet/minecraft/nbt/NBTTagCompound;Ljava/lang/Object;)V", (String)null, (String[])null);
        mv.visitCode();
        mv.visitVarInsn(25, 2);
        mv.visitTypeInsn(192, targetType);
        mv.visitVarInsn(58, 3);
        for (final FieldNode field2 : fields) {
            mv.visitVarInsn(25, 1);
            mv.visitLdcInsn((Object)field2.name);
            mv.visitVarInsn(25, 3);
            mv.visitFieldInsn(180, targetType, field2.name, field2.desc);
            mv.visitMethodInsn(184, getTargetForField(field2), "save", "(Lnet/minecraft/nbt/NBTTagCompound;Ljava/lang/String;" + field2.desc + ")V", false);
        }
        mv.visitInsn(177);
        mv.visitEnd();
        mv = cw.visitMethod(1, "readFromNBT", "(Lnet/minecraft/nbt/NBTTagCompound;Ljava/lang/Object;)V", (String)null, (String[])null);
        mv.visitCode();
        mv.visitVarInsn(25, 2);
        mv.visitTypeInsn(192, targetType);
        mv.visitVarInsn(58, 3);
        for (final FieldNode field2 : fields) {
            mv.visitVarInsn(25, 1);
            mv.visitLdcInsn((Object)field2.name);
            mv.visitVarInsn(25, 3);
            mv.visitFieldInsn(180, targetType, field2.name, field2.desc);
            mv.visitMethodInsn(184, getTargetForField(field2), "save", "(Lnet/minecraft/nbt/NBTTagCompound;Ljava/lang/String;" + field2.desc + ")V", false);
        }
        mv.visitInsn(177);
        mv.visitEnd();
        cw.visitEnd();
        return null;
    }
    
    public static String getTargetForField(final FieldNode field) {
        return Type.getDescriptor((Class)NBTCreator.handlers.get(Type.getType(field.desc)));
    }
    
    static {
        NBTCreator.cl = (LaunchClassLoader)NBTCreator.class.getClassLoader();
        NBTCreator.type = Type.getDescriptor((Class)Sync.class);
        (handlers = new HashMap<Type, Class<?>>()).put(Type.BOOLEAN_TYPE, NBTHandlers.NBTHandlerBoolean.class);
        NBTCreator.handlers.put(Type.BYTE_TYPE, NBTHandlers.NBTHandlerByte.class);
        NBTCreator.handlers.put(Type.SHORT_TYPE, NBTHandlers.NBTHandlerShort.class);
        NBTCreator.handlers.put(Type.INT_TYPE, NBTHandlers.NBTHandlerInt.class);
        NBTCreator.handlers.put(Type.LONG_TYPE, NBTHandlers.NBTHandlerLong.class);
        NBTCreator.handlers.put(Type.FLOAT_TYPE, NBTHandlers.NBTHandlerFloat.class);
        NBTCreator.handlers.put(Type.DOUBLE_TYPE, NBTHandlers.NBTHandlerDouble.class);
        NBTCreator.handlers.put(Type.getType((Class)byte[].class), NBTHandlers.NBTHandlerByteArray.class);
        NBTCreator.handlers.put(Type.getType((Class)String.class), NBTHandlers.NBTHandlerString.class);
        NBTCreator.handlers.put(Type.getType((Class)NBTTagCompound.class), NBTHandlers.NBTHandlerNBT.class);
        NBTCreator.handlers.put(Type.getType((Class)int[].class), NBTHandlers.NBTHandlerIntArray.class);
        NBTCreator.handlers.put(Type.getType((Class)ItemStack.class), NBTHandlers.NBTHandlerItemStack.class);
    }
}


