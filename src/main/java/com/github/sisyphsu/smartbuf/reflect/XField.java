package com.github.sisyphsu.smartbuf.reflect;

import java.lang.reflect.Field;

/**
 * XField represents a field of class, with clear generic type.
 *
 * @author sulin
 * @since 2019-07-15 20:42:17
 */
public final class XField<T> {

    /**
     * Field's name
     */
    private String   name;
    /**
     * Java's reflect field
     */
    private Field    field;
    /**
     * Field's clear type
     */
    private XType<T> type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public XType<T> getType() {
        return type;
    }

    public void setType(XType<T> type) {
        this.type = type;
    }
}
