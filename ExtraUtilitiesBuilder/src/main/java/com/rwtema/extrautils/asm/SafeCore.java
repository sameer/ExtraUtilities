// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.asm;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.FieldNode;
import java.util.Iterator;
import org.objectweb.asm.Type;
import cpw.mods.fml.relauncher.SideOnly;
import org.objectweb.asm.tree.AnnotationNode;
import java.util.List;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import java.io.IOException;
import net.minecraft.launchwrapper.LaunchClassLoader;
import java.util.HashMap;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.launchwrapper.IClassTransformer;

public class SafeCore implements IClassTransformer
{
    private static Side INVALID_SIDE;
    private static String SIDE_SERVER;
    private static String SIDE_CLIENT;
    private static HashMap<String, Side> classSideHashMap;
    LaunchClassLoader cl;
    public String[] clientPrefixes;
    
    public SafeCore() {
        this.cl = (LaunchClassLoader)SafeCore.class.getClassLoader();
        this.clientPrefixes = new String[] { "net.minecraft.", "net.minecraftforge.", "cpw.mods.fml", "com.rwtema.extrautils." };
    }
    
    public Side getSide(final String clazz) {
        if (SafeCore.classSideHashMap.containsKey(clazz)) {
            return SafeCore.classSideHashMap.get(clazz);
        }
        final Side side = this.getSide_do(clazz);
        SafeCore.classSideHashMap.put(clazz, side);
        return side;
    }
    
    public Side getSide_do(final String clazz) {
        if (clazz.indexOf(46) == -1) {
            return null;
        }
        byte[] bytes;
        try {
            bytes = this.cl.getClassBytes(clazz);
        }
        catch (IOException e) {
            return SafeCore.INVALID_SIDE;
        }
        if (bytes == null) {
            return SafeCore.INVALID_SIDE;
        }
        boolean flag = true;
        for (final String clientPrefix : this.clientPrefixes) {
            if (clazz.startsWith(clientPrefix)) {
                flag = false;
                break;
            }
        }
        if (flag) {
            return null;
        }
        final ClassNode classNode = new ClassNode();
        final ClassReader classReader = new ClassReader(bytes);
        classReader.accept((ClassVisitor)classNode, 0);
        return this.getSide(classNode.visibleAnnotations);
    }
    
    public Side getSide(final List<AnnotationNode> anns) {
        if (anns == null) {
            return null;
        }
        for (final AnnotationNode ann : anns) {
            if (ann.desc.equals(Type.getDescriptor((Class)SideOnly.class)) && ann.values != null) {
                for (int x = 0; x < ann.values.size() - 1; x += 2) {
                    final Object key = ann.values.get(x);
                    final Object value = ann.values.get(x + 1);
                    if (key instanceof String && key.equals("value") && value instanceof String[]) {
                        final String s = ((String[])value)[1];
                        if (s.equals(SafeCore.SIDE_SERVER)) {
                            return Side.SERVER;
                        }
                        if (s.equals(SafeCore.SIDE_CLIENT)) {
                            return Side.CLIENT;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public boolean isInvalid(final String clazz) {
        return this.getSide(clazz) == SafeCore.INVALID_SIDE;
    }
    
    public boolean isInvalid(Type type) {
        while (type.getSort() == 9) {
            type = type.getElementType();
        }
        return type.getSort() != 11 && this.isInvalid(type.getClassName());
    }
    
    public boolean isInvalid(final Type[] types) {
        for (final Type type : types) {
            if (this.isInvalid(type)) {
                return true;
            }
        }
        return false;
    }
    
    public byte[] transform(final String s, final String s2, final byte[] bytes) {
        if (!s.startsWith("com.rwtema.extrautils")) {
            return bytes;
        }
        final ClassNode classNode = new ClassNode();
        final ClassReader reader = new ClassReader(bytes);
        reader.accept((ClassVisitor)classNode, 0);
        final Side side = this.getSide(classNode.visibleAnnotations);
        SafeCore.classSideHashMap.put(s, side);
        this.stripInvalidAnnotations(classNode.visibleAnnotations);
        final Iterator<FieldNode> iterator = classNode.fields.iterator();
        while (iterator.hasNext()) {
            final FieldNode field = iterator.next();
            if (this.isInvalid(Type.getType(field.desc))) {
                iterator.remove();
            }
            else {
                this.stripInvalidAnnotations(field.visibleAnnotations);
            }
        }
        final Iterator<MethodNode> iterator2 = classNode.methods.iterator();
        while (iterator2.hasNext()) {
            final MethodNode method = iterator2.next();
            if (this.invalidMethod(method)) {
                iterator2.remove();
            }
            else {
                this.stripInvalidAnnotations(method.visibleAnnotations);
            }
        }
        final ClassWriter writer = new ClassWriter(1);
        classNode.accept((ClassVisitor)writer);
        return writer.toByteArray();
    }
    
    public void stripInvalidAnnotations(final List<AnnotationNode> visibleAnnotations) {
        if (visibleAnnotations == null) {
            return;
        }
        final Iterator<AnnotationNode> iterator = visibleAnnotations.iterator();
        while (iterator.hasNext()) {
            final AnnotationNode visibleAnnotation = iterator.next();
            if (this.isInvalid(Type.getType(visibleAnnotation.desc))) {
                iterator.remove();
            }
        }
    }
    
    public boolean invalidMethod(final MethodNode method) {
        if (this.isInvalid(Type.getReturnType(method.desc))) {
            return true;
        }
        for (final Type type : Type.getArgumentTypes(method.desc)) {
            if (this.isInvalid(type)) {
                return true;
            }
        }
        return false;
    }
    
    static {
        SafeCore.INVALID_SIDE = ((FMLLaunchHandler.side() == Side.CLIENT) ? Side.SERVER : Side.CLIENT);
        SafeCore.SIDE_SERVER = Side.SERVER.name();
        SafeCore.SIDE_CLIENT = Side.CLIENT.name();
        (SafeCore.classSideHashMap = new HashMap<String, Side>()).put(Boolean.TYPE.getName(), null);
        SafeCore.classSideHashMap.put(Byte.TYPE.getName(), null);
        SafeCore.classSideHashMap.put(Character.TYPE.getName(), null);
        SafeCore.classSideHashMap.put(Double.TYPE.getName(), null);
        SafeCore.classSideHashMap.put(Float.TYPE.getName(), null);
        SafeCore.classSideHashMap.put(Integer.TYPE.getName(), null);
        SafeCore.classSideHashMap.put(Long.TYPE.getName(), null);
        SafeCore.classSideHashMap.put(Short.TYPE.getName(), null);
        SafeCore.classSideHashMap.put(Void.TYPE.getName(), null);
    }
}
