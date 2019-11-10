package com.github.sisyphsu.smartbuf.reflect;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Date;
import java.util.List;

/**
 * @author sulin
 * @since 2019-07-22 14:59:45
 */
public class XTypeUtilsTest {

    @Test
    public void testTypeVariable() throws Exception {
        Field field = TypeVariableBean.class.getDeclaredField("t");
        TypeVariable typeVariable = (TypeVariable) field.getGenericType();
        Type[] bounds = typeVariable.getBounds();
        System.out.println(bounds);

        Method method = TypeVariableBean.class.getDeclaredMethod("hh");
        TypeVariable returnType = (TypeVariable) method.getGenericReturnType();
        Type[] returnBounds = returnType.getBounds();
        System.out.println(returnBounds);
    }

    @Test
    public void testReference() {
        XType type = XTypeUtils.toXType(new TypeRef<Reference<Long>>() {
        }.getType());
        System.out.println(type);
    }

    @Test
    public void testGenModel() {
        Type type = new TypeRef<GenericModel<?, Rectangle, String>>() {
        }.getType();

        XType xType = XTypeUtils.toXType(type);
        System.out.println(xType);
    }

    @Test
    public void testCache() {
        XType prevXT = null;
        for (int i = 0; i < 10; i++) {
            Type type = new TypeRef<GenericModel<?, Rectangle, String>>() {
            }.getType();
            XType xt = XTypeUtils.toXType(type);
            assert prevXT == null || prevXT == xt;
            prevXT = xt;
            System.out.println(xt);
        }
    }

    @Test
    public void testVariable() {
        Type type = new TypeRef<TypeVariableBean<?>>() {
        }.getType();

        XType<?> xt = XTypeUtils.toXType(type);

        assert xt.getRawType() == TypeVariableBean.class;

        XField<?> tField = xt.getField("t");
        XField<?> listField = xt.getField("list");
        assert tField != null && listField != null;

        assert tField.getType().getRawType() == Date.class;

        assert listField.getType().getParameterizedType("E").getRawType() == Date.class;
    }

    public static class TypeVariableBean<T extends Date> {

        private T                  t;
        private List<? super Date> list;

        public <X extends Number> X hh() {
            return null;
        }
    }

}
