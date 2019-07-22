package com.github.sisyphsu.nakedata.convertor.reflect;

import com.alibaba.fastjson.TypeReference;
import org.junit.Test;

import java.awt.*;
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
    public void testGenModel() {
        Type type = new TypeReference<GenericModel<?, Rectangle, String>>() {
        }.getType();

        XType xType = XTypeUtils.toXType(type);
        System.out.println(xType);
    }

    @Test
    public void testType() {
        Type prev = null;
        for (int i = 0; i < 10; i++) {
            Type type = new TypeReference<GenericModel<?, Rectangle, String>>() {
            }.getType();
            System.out.println(type == prev);
            prev = type;
        }
    }

    public static class TypeVariableBean<T extends Date> {

        private T t;
        private List<? super Date> list;

        public <X extends Number> X hh() {
            return null;
        }
    }

}