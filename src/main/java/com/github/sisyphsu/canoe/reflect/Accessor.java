package com.github.sisyphsu.canoe.reflect;

public interface Accessor {

    String NAME = Accessor.class.getName().replace('.', '/');

    void getAll(Object o, Object[] values);

    void setAll(Object o, Object[] values);
}
