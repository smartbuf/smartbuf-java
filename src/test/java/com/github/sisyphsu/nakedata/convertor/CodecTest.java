package com.github.sisyphsu.nakedata.convertor;

import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-05-19 14:11:03
 */
public class CodecTest {

    @Test
    public void testArray() {
        Object intArr = new Integer[0];
        System.out.println(intArr.getClass());
        System.out.println(intArr instanceof Object[]);
        System.out.println(intArr.getClass().getComponentType());

        Object[] objArr = new Object[]{1, 2};
        System.out.println(objArr instanceof Integer[]);
    }

}