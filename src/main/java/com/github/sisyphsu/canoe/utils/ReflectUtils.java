package com.github.sisyphsu.canoe.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sulin
 * @since 2019-11-09 14:19:20
 */
public final class ReflectUtils {

    public static List<Field> findAllFields(Class<?> cls) {
        if (cls == null || cls == Object.class) {
            return Collections.emptyList();
        }
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
        fields.addAll(findAllFields(cls.getSuperclass()));
        return fields;
    }

}
