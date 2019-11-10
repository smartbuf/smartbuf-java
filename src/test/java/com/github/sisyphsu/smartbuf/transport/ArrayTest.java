package com.github.sisyphsu.smartbuf.transport;

import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-09-29 16:36:47
 */
public class ArrayTest {

    @Test
    public void test() {
        Array<Long> array = new Array<>();
        assert array.cap() == 0;
        assert array.size() == 0;

        array.add(1L);
        array.add(2L);
        array.add(3L);
        array.add(4L);
        array.add(5L);

        assert array.size() == 5;

        array.clear();
        assert array.size() == 0;
        assert array.cap() > 0;
    }

    @Test
    public void testEquals() {
        Array<Long> array1 = new Array<>();
        Array<Long> array2 = new Array<>();

        assert !array1.equals(new Object());
        assert array1.equals(array2);

        array1.add(1L);
        array1.add(2L);
        assert !array1.equals(array2);

        array2.add(1L);
        assert !array1.equals(array2);

        array2.add(2L);
        assert array1.equals(array2);

        array1.add(3L);
        array2.add(4L);
        assert !array1.equals(array2);
    }

}
