package com.github.sisyphsu.nakedata;

import com.github.sisyphsu.nakedata.meta.TypeMeta;

import java.util.List;

/**
 * Result
 *
 * @author sulin
 * @since 2019-04-24 21:58:32
 */
public class DataTrunk {

    /**
     * Temporary names, could be the keys of map/object.
     */
    private List<String> names;
    /**
     * Temporary object type, could be map.
     */
    private List<TypeMeta> types;
    /**
     * Variable name, should be cached by otherside.
     * must match the specified variable role.
     */
    private List<String> varNames;
    /**
     * Variable data type, should be cached by otherside.
     */
    private List<TypeMeta> varTypes;

    /**
     * Data body
     */
    private byte[] body;

}
