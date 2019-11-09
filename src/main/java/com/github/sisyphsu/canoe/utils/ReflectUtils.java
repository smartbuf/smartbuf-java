package com.github.sisyphsu.canoe.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sulin
 * @since 2019-11-09 14:19:20
 */
public final class ReflectUtils {

    private ReflectUtils() {
    }

    public static List<Field> findAllValidFields(Class<?> cls) {
        List<Field> fields = new ArrayList<>();
        while (cls != null && cls != Object.class) {
            fields.addAll(findValidFields(cls));
            cls = cls.getSuperclass();
        }
        return fields;
    }

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
