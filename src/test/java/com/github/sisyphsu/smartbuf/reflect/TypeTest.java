package com.github.sisyphsu.smartbuf.reflect;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.ref.Reference;
import java.lang.reflect.*;
import java.util.*;

/**
 * Test type
 *
 * @author sulin
 * @since 2019-07-13 17:20:50
 */
public class TypeTest {

    @Test
    public void testRawType() {
        TypeReference ref = new TypeReference<Collection<Queue<Long>>>() {
        };
        Type type = ref.getType();
        System.out.println(type);

        ParameterizedType pt = (ParameterizedType) type;
        System.out.println(pt.getRawType() instanceof Class);
    }

    @Test
    public void testNormalType() throws NoSuchFieldException {
        for (Field field : Model.class.getDeclaredFields()) {
            System.out.println(field.getGenericType());
        }
        GenericArrayType tsType = (GenericArrayType) Model.class.getDeclaredField("ts").getGenericType();
        System.out.println(tsType);
    }

    @Test
    public void testGenericType() throws NoSuchFieldException {
        TypeReference typeReference = new TypeReference<Model<Void, Queue<Long>>>() {
        };
        Type type = typeReference.getType();
        System.out.println(type);

        ParameterizedType pt = (ParameterizedType) type;
        Class clz = (Class) pt.getRawType();
        for (Field field : clz.getDeclaredFields()) {
            Type fType = field.getGenericType();
            System.out.println(fType);
        }

        Type tsType = clz.getDeclaredField("ts").getGenericType();
        GenericArrayType arrayType = (GenericArrayType) tsType;
        TypeVariable genArrType = (TypeVariable) arrayType.getGenericComponentType();
        System.out.println(genArrType.getGenericDeclaration());
        System.out.println(tsType);

        Class decClass = (Class) genArrType.getGenericDeclaration();
        for (TypeVariable parameter : decClass.getTypeParameters()) {
            System.out.println(parameter.getName());
        }
    }

    @Test
    public void testSuper() {
        XType xType = XTypeUtils.toXType(Child.class);
        assert xType.getField("k") != null;
        assert xType.getField("v") != null;
        assert xType.getField("xxx") == null;
    }

    @Data
    public static class Model<X, T> {
        private long                                time;
        private Map                                 objectMap;
        private Map<String, ? extends Date>         strDateMap;
        private Map<? extends Date, ? super String> wildMap;
        private List<Long>                          longs;
        private T[]                                 ts;
        private Reference<T>                        ref;
    }

    @Data
    public static class Bean<T> {
        private T[] name;
    }

    public static class P<T> {
        @Data
        public class Item {
            private T t;
        }
    }

    public static class Parent<K, V> {
        private K k;
        private V v;
    }

    public static class Child<V> extends Parent<String, V> {
        private V v;
    }

}
