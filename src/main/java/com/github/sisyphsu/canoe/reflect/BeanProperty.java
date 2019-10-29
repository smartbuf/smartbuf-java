package com.github.sisyphsu.canoe.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author sulin
 * @since 2019-10-29 15:46:00
 */
public class BeanProperty {

    String name;
    Field  field;
    Method getter;
    Method setter;

    public BeanProperty(String name) {
        this.name = name;
    }

    public BeanProperty(Field field, Method getter, Method setter) {
        this.field = field;
        this.getter = getter;
        this.setter = setter;
    }

}
