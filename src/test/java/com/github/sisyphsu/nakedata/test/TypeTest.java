package com.github.sisyphsu.nakedata.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sulin
 * @since 2019-05-14 20:26:09
 */
public class TypeTest {

    @Test
    public void test() {
        System.out.println(Object[].class);
        System.out.println(Boolean[].class);
        System.out.println(boolean[].class);
        System.out.println(byte[].class);
        System.out.println(int[].class);

        Class cls1 = Boolean[].class;
        System.out.println(cls1.isArray());
        System.out.println(cls1.getComponentType());
        System.out.println(cls1.getSuperclass());

        Class cls2 = boolean[].class;
        System.out.println(cls2.isArray());
        System.out.println(cls2.getComponentType());
    }

    @Test
    public void testArray() {
        Boolean[] arr = new Boolean[0];
        Object[] os = arr;
        System.out.println(arr.getClass());
        System.out.println(arr.getClass());
    }

    @Test
    public void testTypeTree() {
        List<Integer> list = new ArrayList<>();
        Class listType = list.getClass();
        for (Class clz = listType; clz != null; clz = clz.getSuperclass()) {
            System.out.println(clz.getGenericSuperclass());
        }
        printInterfaces(listType);
    }

    @Test
    public void testEnum() {
        System.out.println(Gender.GIRL instanceof Enum);
        System.out.println(Gender.BOY.getClass());

        for (Class clz = Gender.class; clz != null; clz = clz.getSuperclass()) {
            System.out.println(clz.getGenericSuperclass());
        }
    }

    private void printInterfaces(Class clz) {
        for (Class it : clz.getInterfaces()) {
            System.out.println(it);
            printInterfaces(it);
        }
    }

    public enum Gender {
        GIRL,
        BOY
    }

}
