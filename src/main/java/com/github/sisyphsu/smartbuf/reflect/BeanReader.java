package com.github.sisyphsu.smartbuf.reflect;

import com.github.sisyphsu.smartbuf.Type;

/**
 * @author sulin
 * @since 2019-11-08 18:01:40
 */
public final class BeanReader {

    static String API_NAME = API.class.getName().replace('.', '/');

    final API         api;
    final BeanField[] fields;
    final String[]    fieldNames;
    final Type[]      fieldTypes;

    BeanReader(API api, BeanField[] fields) {
        this.api = api;
        this.fields = fields;
        this.fieldNames = new String[fields.length];
        this.fieldTypes = new Type[fields.length];
        for (int i = 0; i < fields.length; i++) {
            BeanField field = fields[i];
            this.fieldNames[i] = field.getName();
            this.fieldTypes[i] = field.getType();
        }
    }

    public BeanField[] getFields() {
        return fields;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public Type[] getFieldTypes() {
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
