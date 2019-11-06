package com.github.sisyphsu.canoe.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * BeanField represents an field which support getter or setter
 *
 * @author sulin
 * @since 2019-10-29 15:46:00
 */
final class BeanField {

    String name;
    Field  field;
    Method getter;
    Method setter;

    public BeanField(String name) {
        this.name = name;
    }

    public BeanField(Field field, Method getter, Method setter) {
        this.field = field;
        this.getter = getter;
        this.setter = setter;
    }

}
