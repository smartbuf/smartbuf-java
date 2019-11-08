package com.github.sisyphsu.canoe.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * BeanField represents an field which support getter or setter
 *
 * @author sulin
 * @since 2019-10-29 15:46:00
 */
public final class BeanField {

    String name;
    Class  type;
    byte   typeCode;

    Field   field;
    Method  getter;
    Method  setter;
    boolean writable;
    boolean readable;

    public BeanField(String name) {
        this.name = name;
    }

    public BeanField(String name, Class type) {
        this.name = name;
        this.type = type;
    }

    public BeanField(Field field, Method getter, Method setter) {
        this.field = field;
        this.getter = getter;
        this.setter = setter;
    }

}
