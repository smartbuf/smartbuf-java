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

        Class cls1 = Boolean[].class;
        System.out.println(cls1.isArray());
        System.out.println(cls1.getComponentType());

        Class cls2 = boolean[].class;
        System.out.println(cls2.isArray());
        System.out.println(cls2.getComponentType());
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

    private void printInterfaces(Class clz) {
        for (Class it : clz.getInterfaces()) {
            System.out.println(it);
            printInterfaces(it);
        }
    }

}
