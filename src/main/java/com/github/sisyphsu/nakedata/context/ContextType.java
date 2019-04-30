package com.github.sisyphsu.nakedata.context;

import lombok.Getter;

import java.util.List;

/**
 * @author sulin
 * @since 2019-04-29 13:10:56
 */
@Getter
public class ContextType {

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

    public ContextType(int id, List<ContextField> fields) {
        this.id = id;
        this.fields = fields;
    }

}
