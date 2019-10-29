package com.github.sisyphsu.canoe.reflect;

public interface Accessor {

    String NAME = Accessor.class.getName().replace('.', '/');

    void getAll(Object[] arr);

    void setAll(Object[] arr);
}
