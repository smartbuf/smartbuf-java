package com.github.sisyphsu.nakedata.field;

import com.github.sisyphsu.nakedata.meta.TypeMeta;

/**
 * @author sulin
 * @since 2019-04-24 21:06:59
 */
public class FieldNode {

    /**
     * Field's unique id
     */
    private int fieldID;
    /**
     * Field's type code
     */
    private int fieldType;

    /**
     * Object Metadata
     */
    private TypeMeta metadata;

}
