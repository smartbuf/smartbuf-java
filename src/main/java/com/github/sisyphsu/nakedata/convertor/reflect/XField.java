package com.github.sisyphsu.nakedata.convertor.reflect;

import lombok.Data;

import java.lang.reflect.Field;

/**
 * XField represent a field of class, with clear generic type.
 *
 * @author sulin
 * @since 2019-07-15 20:42:17
 */
@Data
public class XField<T> {

    /**
     * Field's name
     */
    private String name;
    /**
     * Java's reflect field
     */
    private Field field;
    /**
     * Field's clear type
     */
    private XType<T> type;

}
