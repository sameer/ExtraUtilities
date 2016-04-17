// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.core;

import com.rwtema.extrautils.ExtraUtils;
import java.util.Objects;
import com.google.common.base.Throwables;
import java.lang.reflect.Field;
import java.util.HashMap;

public class NSafe
{
    static HashMap<Tuple<Class<?>, String>, Field> cache;
    
    public static Field getField(final Class<?> clazz, final String fieldName) {
        final Tuple<Class<?>, String> key = new Tuple<Class<?>, String>(clazz, fieldName);
        Field val = NSafe.cache.get(key);
        if (val == null) {
            try {
                final Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                val = f;
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            NSafe.cache.put(key, val);
        }
        return val;
    }
    
    public static <K> K get(final Object object, final String fieldName) {
        if (object == null) {
            return null;
        }
        final Field field = getField(object.getClass(), fieldName);
        if (field == null) {
            return null;
        }
        K result = null;
        try {
            result = (K)field.get(object);
        }
        catch (IllegalAccessException e) {
            throw Throwables.propagate((Throwable)e);
        }
        return result;
    }
    
    public static <T> T set(final T object, final String value, final Objects... param) {
        final String s = get(ExtraUtils.wateringCan, "iconString");
        return object;
    }
    
    static {
        NSafe.cache = new HashMap<Tuple<Class<?>, String>, Field>();
    }
}
