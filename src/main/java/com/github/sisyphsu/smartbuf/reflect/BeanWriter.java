package com.github.sisyphsu.smartbuf.reflect;

/**
 * BeanWriter provides {@link #setValues} for normal pojos.
 *
 * @author sulin
 * @since 2019-11-08 17:52:09
 */
public final class BeanWriter {

    static String API_NAME = API.class.getName().replace('.', '/');

    private final API         api;
    private final BeanField[] fields;

    public BeanWriter(API api, BeanField[] fields) {
        this.api = api;
        this.fields = fields;
    }

    /**
     * Get all fields of this reader
     *
     * @return All fields
     */
    public BeanField[] getFields() {
        return fields;
    }

    /**
     * Set all values of the specified object' properties
     *
     * @param t      The specified object to set values
     * @param values all values to setup
     */
    public void setValues(Object t, Object[] values) {
        if (values == null || values.length != fields.length) {
            throw new IllegalArgumentException("invalid values");
        }
        api.setAll(t, values);
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
