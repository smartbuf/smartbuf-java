package com.github.sisyphsu.canoe.reflect;

import com.github.sisyphsu.canoe.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * BeanField represents an field which support getter or setter
 *
 * @author sulin
 * @since 2019-10-29 15:46:00
 */
public final class BeanField {

    final String name;
    final Class  cls;
    final Type   type;

    Field  field;
    Method getter;
    Method setter;

    public BeanField(String name, Class cls) {
        this.name = name;
        this.cls = cls;
        this.type = Type.valueOf(cls);
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

}
