package com.github.sisyphsu.nakedata.context.output;

import lombok.Data;

/**
 * @author sulin
 * @since 2019-05-01 18:03:00
 */
@Data
public class TypeField {

    /**
     * Field's name
     */
    private String name;
    /**
     * Field's type
     */
    private int type;

}
