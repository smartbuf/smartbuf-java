package com.github.sisyphsu.nakedata.test;

import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
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
        Boolean[] arr = new Boolean[1];
        System.out.println(arr.getClass());
        System.out.println(arr.getClass().getSuperclass());

        boolean[] arr2 = new boolean[1];
        System.out.println(arr2.getClass());
        System.out.println(arr2.getClass().getSuperclass());
    }

    @Test
    public void testObjArray() {
        Object[] arr = new Object[3];
        arr[0] = null;
        arr[1] = true;
        arr[2] = false;
        Boolean[] arr2 = (Boolean[]) arr;

        System.out.println(arr2);
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

    @Test
    public void testType2() {
        Class t1 = boolean[].class;
        System.out.println(t1);
        System.out.println(t1.getComponentType());
        Object arr1 = Array.newInstance(t1.getComponentType(), 2);
        Array.set(arr1, 0, false);
        System.out.println(arr1);

        Class t2 = Boolean[].class;
        System.out.println(t2);
        System.out.println(t2.getComponentType());
        Object arr2 = Array.newInstance(t2.getComponentType(), 2);
        Array.set(arr2, 0, null);
        System.out.println(arr2);
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
