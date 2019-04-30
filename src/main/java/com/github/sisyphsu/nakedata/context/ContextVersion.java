package com.github.sisyphsu.nakedata.context;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * An version of Context
 *
 * @author sulin
 * @since 2019-04-29 13:47:26
 */
@Data
public class ContextVersion {

    /**
     * The new version's number
     */
    private int version;
    /**
     * The expired name's context ids.
     */
    private List<Integer> nameExpired = new ArrayList<>();
    /**
     * The expired type's context ids.
     */
    private List<Integer> typeExpired = new ArrayList<>();
    /**
     * The new added names.
     */
    private List<String> nameAdded = new ArrayList<>();
    /**
     * The new added types.
     */
    private List<ContextType> typeAdded = new ArrayList<>();

}
