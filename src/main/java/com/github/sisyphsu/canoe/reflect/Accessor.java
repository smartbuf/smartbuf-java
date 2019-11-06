package com.github.sisyphsu.canoe.reflect;

/**
 * Accessor defines the specification of bean accessor
 *
 * @author sulin
 * @since 2019-10-29 15:42:49
 */
public interface Accessor {

    String NAME = Accessor.class.getName().replace('.', '/');

    /**
     * Fetch all properties of the specified object by predefined fields and their order
     *
     * @param o      The Object to access, its getter will be called in predefined order
     * @param values The array to accept getter's result
     */
    void getAll(Object o, Object[] values);

    /**
     * Setup all properties of the specified object by predefined fields and their order
     *
     * @param o      The Object to access, its setter will be called in predefined order
     * @param values The values which need to set into object
     */
    void setAll(Object o, Object[] values);
}
