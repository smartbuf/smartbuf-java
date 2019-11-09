package com.github.sisyphsu.canoe;

import java.util.Collection;

public enum Type {

    Z,
    BOOL,
    B,
    BYTE,
    S,
    SHORT,
    I,
    INTEGER,
    J,
    LONG,
    F,
    FLOAT,
    D,
    DOUBLE,
    C,
    CHAR,
    STRING,
    ENUM,
    ARRAY,
    ARRAY_BOOL,
    ARRAY_BYTE,
    ARRAY_SHORT,
    ARRAY_INT,
    ARRAY_LONG,
    ARRAY_FLOAT,
    ARRAY_DOUBLE,
    ARRAY_CHAR,
    COLLECTION,
    UNKNOWN;

    public static Type valueOf(Class<?> cls) {
        if (cls == boolean.class) {
            return Z;
        } else if (cls == Boolean.class) {
            return BOOL;
        } else if (cls == float.class) {
            return F;
        } else if (cls == Float.class) {
            return FLOAT;
        } else if (cls == double.class) {
            return D;
        } else if (cls == Double.class) {
            return DOUBLE;
        } else if (cls == byte.class) {
            return B;
        } else if (cls == Byte.class) {
            return BYTE;
        } else if (cls == short.class) {
            return S;
        } else if (cls == Short.class) {
            return SHORT;
        } else if (cls == int.class) {
            return I;
        } else if (cls == Integer.class) {
            return INTEGER;
        } else if (cls == long.class) {
            return J;
        } else if (cls == Long.class) {
            return LONG;
        } else if (cls == char.class) {
            return C;
        } else if (cls == Character.class) {
            return CHAR;
        } else if (CharSequence.class.isAssignableFrom(cls)) {
            return STRING;
        } else if (Enum.class.isAssignableFrom(cls)) {
            return ENUM;
        } else if (Collection.class.isAssignableFrom(cls)) {
            return COLLECTION;
        } else if (cls.isArray()) {
            if (cls == boolean[].class) {
                return ARRAY_BOOL;
            } else if (cls == byte[].class) {
                return ARRAY_BYTE;
            } else if (cls == short[].class) {
                return ARRAY_SHORT;
            } else if (cls == int[].class) {
                return ARRAY_INT;
            } else if (cls == long[].class) {
                return ARRAY_LONG;
            } else if (cls == float[].class) {
                return ARRAY_FLOAT;
            } else if (cls == double[].class) {
                return ARRAY_DOUBLE;
            } else if (cls == char[].class) {
                return ARRAY_CHAR;
            } else {
                return ARRAY;
            }
        } else {
            return UNKNOWN;
        }
    }

}
