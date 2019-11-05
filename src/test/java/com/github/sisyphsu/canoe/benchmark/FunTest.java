package com.github.sisyphsu.canoe.benchmark;

import com.github.sisyphsu.canoe.node.basic.ObjectNode;
import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-11-05 14:40:00
 */
public class FunTest {

    @Test
    public void test() {
        System.out.println(char[].class.getName());
        System.out.println(boolean[].class.getName());
        System.out.println(byte[].class.getName());
        System.out.println(short[].class.getName());
        System.out.println(int[].class.getName());
        System.out.println(long[].class.getName());
        System.out.println(float[].class.getName());
        System.out.println(double[].class.getName());
        System.out.println(Object[].class.getName());

        System.out.println();

        System.out.println(ObjectNode.class.getName());
        System.out.println(Boolean.class.getName());
        System.out.println(Float.class.getName());
        System.out.println(Double.class.getName());
        System.out.println(Byte.class.getName());
        System.out.println(Short.class.getName());
        System.out.println(Integer.class.getName());
        System.out.println(Long.class.getName());
        System.out.println(Character.class.getName());

        System.out.println();

        System.out.println(String.class.getName());
        System.out.println(StringBuilder.class.getName());
        System.out.println(StringBuffer.class.getName());
        System.out.println(CharSequence.class.getName());
    }
}
