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

    BeanWriter(API api, BeanField[] fields) {
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
        for (int i = 0, len = fields.length; i < len; i++) {
            BeanField field = fields[i];
            Object value = values[i];
            if (value != null) {
                continue;
            }
            switch (field.getType()) {
                case Z:
                    values[i] = false;
                    break;
                case B:
                    values[i] = (byte) 0;
                    break;
                case S:
                    values[i] = (short) 0;
                    break;
                case I:
                    values[i] = 0;
                    break;
                case J:
                    values[i] = (long) 0;
                    break;
                case F:
                    values[i] = (float) 0;
                    break;
                case D:
                    values[i] = (double) 0;
                    break;
                case C:
                    values[i] = (char) 0;
                    break;
                default:
                    values[i] = null;
            }
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
