package com.github.smartbuf.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
     * Find the specified field's getter method
     *
     * @param cls   The class to find getter
     * @param field The field to find getter
     * @return The found getter method
     */
    public static Method findGetter(Class<?> cls, Field field) {
        final String fieldName = field.getName();
        final Class fieldType = field.getType();
        final Class[] argTypes = new Class[0];
        Method method = null;
        // isXxx_yyy
        if (fieldType == boolean.class) {
            String isXxx_yyy = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            method = findMethod(cls, isXxx_yyy, argTypes, fieldType);
        }
        // isXxxYyy
        if (method == null && fieldType == boolean.class) {
            String camelCase = fieldNameToCamelCase(fieldName);
            String isXxxYyy = "is" + camelCase.substring(0, 1).toUpperCase() + camelCase.substring(1);
            method = findMethod(cls, isXxxYyy, argTypes, fieldType);
        }
        // getXxx_yyy
        if (method == null) {
            String getXxx_yyy = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            method = findMethod(cls, getXxx_yyy, argTypes, fieldType);
        }
        // getXxxYyy
        if (method == null && fieldName.contains("_")) {
            String camelCase = fieldNameToCamelCase(fieldName);
            String getXxxYyy = "get" + camelCase.substring(0, 1).toUpperCase() + camelCase.substring(1);
            method = findMethod(cls, getXxxYyy, argTypes, fieldType);
        }
        return method;
    }

    /**
     * Find the specified field's setter method
     *
     * @param cls   The class to find setter
     * @param field The field to find setter
     * @return The found setter method
     */
    public static Method findSetter(Class<?> cls, Field field) {
        final String fieldName = field.getName();
        final Class fieldType = field.getType();
        final Class[] argTypes = new Class[]{fieldType};
        // setXxx_yyy
        String setXxx_yyy = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method method = findMethod(cls, setXxx_yyy, argTypes, void.class);
        // setXxxYyy
        if (method == null) {
            String camelCase = fieldNameToCamelCase(fieldName);
            String getXxxYyy = "set" + camelCase.substring(0, 1).toUpperCase() + camelCase.substring(1);
            method = findMethod(cls, getXxxYyy, argTypes, void.class);
        }
        return method;
    }

    /**
     * Find the specified method by its class, name, parameter-types, return-type.
     *
     * @param cls      The class to find method
     * @param name     Method's name
     * @param argTypes Method's argument-types
     * @param retType  Method's return-type
     * @return The found method
     */
    public static Method findMethod(Class<?> cls, String name, Class<?>[] argTypes, Class<?> retType) {
        try {
            Method method = cls.getMethod(name, argTypes);
            if (method.isAnnotationPresent(Deprecated.class) || Modifier.isStatic(method.getModifiers())) {
                return null;
            }
            if (method.getReturnType() != retType) {
                return null;
            }
            return method;
        } catch (NoSuchMethodException ignored) {
            return null;
        }
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

    /**
     * Convert the specified name into camel-case.
     *
     * @param name The field name need to convert
     * @return new camel-case name
     */
    public static String fieldNameToCamelCase(String name) {
        StringBuilder sb = new StringBuilder(name.length());
        for (String str : name.split("_")) {
            if (str.isEmpty()) {
                sb.append('_');
                continue;
            }
            if (sb.length() == 0) {
                sb.append(str);
            } else {
                sb.append(str.substring(0, 1).toUpperCase());
                sb.append(str.substring(1));
            }
        }
        return sb.toString();
    }

}
