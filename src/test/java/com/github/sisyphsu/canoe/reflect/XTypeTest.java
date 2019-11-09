package com.github.sisyphsu.canoe.reflect;

import org.junit.jupiter.api.Test;

import java.lang.ref.Reference;
import java.util.*;

/**
 * Test for XType
 *
 * @author sulin
 * @since 2019-09-20 14:56:05
 */
@SuppressWarnings("ConstantConditions")
public class XTypeTest<T> {

    private T[] ts;

    @Test
    public void test() {
        try {
            new XType<>(Long.class, null, null);
            assert false;
        } catch (Exception e) {
            assert e instanceof NullPointerException;
        }

        try {
            new XType<>(Long.class, new String[1], new XType[2]);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        XType xType = XTypeUtils.toXType(byte[].class);
        assert xType.getField("test") == null;
        assert xType.getParameterizedType("V") == null;

        try {
            xType.getParameterizedType();
            assert false;
        } catch (Exception e) {
            assert e instanceof RuntimeException;
        }

        xType = XTypeUtils.toXType(new TypeRef<Map<String, String>>() {
        }.getType());
        assert xType.getParameterizedType("xxx") == null;
        assert xType.getParameterizedType("V") != null;

        try {
            xType.getParameterizedType();
            assert false;
        } catch (Exception e) {
            assert e instanceof RuntimeException;
        }

        xType = XTypeUtils.toXType(XTypeTest.class);
        assert xType.getField("ts") != null;
    }

    @Test
    public void testPure() {
        XType xType = XTypeUtils.toXType(byte[].class);
        assert xType.isPure();

        xType = XTypeUtils.toXType(new TypeRef<Date[]>() {
        }.getType());
        assert xType.isPure();

        xType = XTypeUtils.toXType(new TypeRef<Map<String, String>>() {
        }.getType());
        assert !xType.isPure();

        xType = XTypeUtils.toXType(XTypeTest.class);
        assert !xType.isPure();
        assert !xType.getField("ts").getType().isPure();
    }

    @Test
    public void testToString() {
        XType xType = XTypeUtils.toXType(byte[].class);
        System.out.println(xType);

        xType = XTypeUtils.toXType(Object[].class);
        System.out.println(xType);

        xType = XTypeUtils.toXType(Random.class);
        System.out.println(xType);

        xType = XTypeUtils.toXType(Reference.class);
        System.out.println(xType);

        xType = XTypeUtils.toXType(new TypeRef<Reference<Long>>() {
        }.getType());
        System.out.println(xType);

        xType = XTypeUtils.toXType(new TypeRef<AbstractList<Long>>() {
        }.getType());
        System.out.println(xType);

        xType = XTypeUtils.toXType(new TypeRef<Optional<AbstractList<Long>>>() {
        }.getType());
        System.out.println(xType);

        xType = XTypeUtils.toXType(new TypeRef<Map<Reference<Long>, List<Optional<Date>>>>() {
        }.getType());
        System.out.println(xType);

        xType = XTypeUtils.toXType(new TypeRef<Boom<Queue<Double>>>() {
        }.getType());
        System.out.println(xType);
        assert xType.getField("ts") != null;
        assert xType.getField("ts").getField() != null;
        System.out.println(xType.getField("ts").getType());
    }

    public static class Boom<T> {
        private T[] ts;
    }

}
