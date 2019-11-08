package com.github.sisyphsu.canoe.reflect;

/**
 * @author sulin
 * @since 2019-11-08 17:52:09
 */
public abstract class BeanWriter {

    static final String NAME = API.class.getName().replace('.', '/');

    private final API         api;
    private final BeanField[] fields;
    private final String[]    fieldNames;

    public BeanWriter(API api, BeanField[] fields) {
        this.api = api;
        this.fields = fields;
        this.fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            this.fieldNames[i] = fields[i].name;
        }
    }

    /**
     * Set all values of the specified object' properties
     *
     * @param t    The specified object to set values
     * @param vals all values to setup
     */
    public void setValues(Object t, Object[] vals) {
        if (vals == null || vals.length != fields.length) {
            throw new IllegalArgumentException("invalid values");
        }
        api.setAll(t, vals);
    }

    public interface API {
        /**
         * Setup all properties of the specified object by predefined fields and their order
         *
         * @param o      The Object to access, its setter will be called in predefined order
         * @param values The values which need to set into object
         */
        void setAll(Object o, Object[] values);
    }

}
