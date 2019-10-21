package com.github.sisyphsu.datube.reflect;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Map;

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

}
