package com.github.sisyphsu.smartbuf.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * ReflectUtils wraps some useful java reflection features
 *
 * @author sulin
 * @since 2019-11-09 14:19:20
 */
public final class ReflectUtils {

    private ReflectUtils() {
    }

    /**
     * Find all valid fields the owned by the specified class, includes its super class.
     *
     * @param cls The class to find fields
     * @return All valid fields owned by cls
     */
    public static List<Field> findAllValidFields(Class<?> cls) {
        List<Field> fields = new ArrayList<>();
        while (cls != null && cls != Object.class) {
            fields.addAll(findValidFields(cls));
            cls = cls.getSuperclass();
        }
        return fields;
    }

    /**
     * Find all valid fields that declared by the specified class,
     * the 'valid' means not static, not transient, not deprecated.
     *
     * @param cls The class to find fields
     * @return All valid fields declared by cls
     */
    public static List<Field> findValidFields(Class<?> cls) {
        List<Field> fields = new ArrayList<>();
        for (Field f : cls.getDeclaredFields()) {
            int mod = f.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isTransient(mod) || f.isAnnotationPresent(Deprecated.class)) {
                continue; // ignore non-public and transient and deprecated
            }
            if (f.getType() == Void.class) {
                continue;
            }
            fields.add(f);
        }
        return fields;
    }

}
