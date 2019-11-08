package com.github.sisyphsu.canoe.reflect;

import com.github.sisyphsu.canoe.Const;

import java.util.Collection;

/**
 * @author sulin
 * @since 2019-11-08 18:01:40
 */
public class BeanReader {

    static final String API_NAME = API.class.getName().replace('.', '/');

    private final API         api;
    private final BeanField[] fields;
    private final String[]    fieldNames;
    private final byte[]      fieldTypes;

    public BeanReader(API api, BeanField[] fields) {
        this.api = api;
        this.fields = fields;
        this.fieldNames = new String[fields.length];
        this.fieldTypes = new byte[fields.length];
        for (int i = 0; i < fields.length; i++) {
            BeanField field = fields[i];
            this.fieldNames[i] = field.name;
            byte type = Const.TYPE_UNKNOWN;
            Class<?> cls = field.type;
            if (cls == Boolean.class || cls == boolean.class) {
                type = Const.TYPE_CONST;
            } else if (cls == Float.class || cls == float.class) {
                type = Const.TYPE_FLOAT;
            } else if (cls == Double.class || cls == double.class) {
                type = Const.TYPE_DOUBLE;
            } else if (cls == Byte.class || cls == Short.class || cls == Integer.class || cls == Long.class) {
                type = Const.TYPE_VARINT;
            } else if (cls == byte.class || cls == short.class || cls == int.class || cls == long.class) {
                type = Const.TYPE_VARINT;
            } else if (CharSequence.class.isAssignableFrom(cls)) {
                type = Const.TYPE_STRING;
            } else if (Enum.class.isAssignableFrom(cls)) {
                type = Const.TYPE_SYMBOL;
            } else if (Collection.class.isAssignableFrom(cls)) {
                type = Const.TYPE_ARRAY;
            } else if (cls.isArray()) {
                if (cls == boolean[].class) {
                    type = Const.TYPE_NARRAY_BOOL;
                } else if (cls == byte[].class) {
                    type = Const.TYPE_NARRAY_BYTE;
                } else if (cls == short[].class) {
                    type = Const.TYPE_NARRAY_SHORT;
                } else if (cls == int[].class) {
                    type = Const.TYPE_NARRAY_INT;
                } else if (cls == long[].class) {
                    type = Const.TYPE_NARRAY_LONG;
                } else if (cls == float[].class) {
                    type = Const.TYPE_NARRAY_FLOAT;
                } else if (cls == double[].class) {
                    type = Const.TYPE_NARRAY_DOUBLE;
                } else {
                    type = Const.TYPE_ARRAY;
                }
            }
            this.fieldTypes[i] = type;
        }
    }

    public BeanField[] getFields() {
        return fields;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public byte[] getFieldTypes() {
        return fieldTypes;
    }

    /**
     * Get all values of the specified object's properties.
     *
     * @param t The specified object to get values
     * @return t's all values
     */
    public Object[] getValues(Object t) {
        Object[] result = new Object[fieldNames.length];
        api.getAll(t, result);
        return result;
    }

    public interface API {
        /**
         * Fetch all properties of the specified object by predefined fields and their order
         *
         * @param o      The Object to access, its getter will be called in predefined order
         * @param values The array to accept getter's result
         */
        void getAll(Object o, Object[] values);
    }

}
