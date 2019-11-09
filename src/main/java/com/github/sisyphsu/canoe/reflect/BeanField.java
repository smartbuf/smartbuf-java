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

    Field  field;
    Method getter;
    Method setter;

    public BeanField(String name, Class type) {
        this.name = name;
        this.type = type;
    }

}
