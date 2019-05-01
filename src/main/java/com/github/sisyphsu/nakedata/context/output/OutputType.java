package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.context.ContextField;

import java.util.List;

/**
 * @author sulin
 * @since 2019-05-01 17:55:19
 */
public class OutputType {

    /**
     * Type's id in context.
     */
    private int id;
    /**
     * Whether is temperary type or not
     */
    private boolean temp;
    /**
     * The fields of this Type, ordered and nullable.
     */
    private List<ContextField> fields;

    public static class Field {
        /**
         * Field's name
         */
        private String name;
        /**
         * Field's type
         */
        private int type;
    }

}
