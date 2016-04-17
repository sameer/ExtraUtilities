// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.asm;

import org.objectweb.asm.MethodVisitor;
import java.lang.reflect.Method;
import com.google.common.base.Throwables;
import net.minecraftforge.fluids.FluidStack;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class FluidIDGetter
{
    public static final Class<? extends IFluidLegacy> clazz;
    public static final IFluidLegacy fluidLegacy;
    
    static {
        final LaunchClassLoader cl = (LaunchClassLoader)FluidIDGetter.class.getClassLoader();
        Method m_defineClass;
        try {
            m_defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
            m_defineClass.setAccessible(true);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        final String classname = "XU_fluidstack_compat_code";
        final String superName = Type.getInternalName((Class)Object.class);
        final ClassWriter cw = new ClassWriter(0);
        cw.visit(50, 33, classname, (String)null, superName, new String[] { Type.getInternalName((Class)IFluidLegacy.class) });
        cw.visitSource(".dynamic", (String)null);
        final MethodVisitor constructor = cw.visitMethod(1, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]), (String)null, (String[])null);
        constructor.visitCode();
        constructor.visitVarInsn(25, 0);
        constructor.visitMethodInsn(183, superName, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]), false);
        constructor.visitInsn(177);
        constructor.visitMaxs(1, 1);
        constructor.visitEnd();
        final MethodVisitor getData = cw.visitMethod(1, "getID", Type.getMethodDescriptor(Type.INT_TYPE, new Type[] { Type.getType((Class)FluidStack.class) }), (String)null, (String[])null);
        getData.visitCode();
        getData.visitVarInsn(25, 1);
        try {
            FluidStack.class.getDeclaredMethod("getFluidID", (Class<?>[])new Class[0]);
            getData.visitMethodInsn(182, "net/minecraftforge/fluids/FluidStack", "getFluidID", "()I", false);
        }
        catch (NoSuchMethodException e3) {
            getData.visitFieldInsn(180, "net/minecraftforge/fluids/FluidStack", "fluidID", "I");
        }
        getData.visitInsn(172);
        getData.visitMaxs(1, 2);
        getData.visitEnd();
        cw.visitEnd();
        final byte[] b = cw.toByteArray();
        try {
            clazz = (Class)m_defineClass.invoke(cl, classname, b, 0, b.length);
            fluidLegacy = (IFluidLegacy)FluidIDGetter.clazz.newInstance();
        }
        catch (Exception e2) {
            throw Throwables.propagate((Throwable)e2);
        }
    }
    
    public interface IFluidLegacy
    {
        int getID(final FluidStack p0);
    }
}
