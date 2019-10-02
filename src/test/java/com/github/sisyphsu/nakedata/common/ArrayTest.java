package com.github.sisyphsu.nakedata.common;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author sulin
 * @since 2019-09-29 16:36:47
 */
public class ArrayTest {

    @Test
    public void test() {
        Array<Long> array = new Array<>(false);
        array.add(1L);
        array.add(2L);
        array.add(3L);
        array.add(4L);

        try {
            array.remove(1);
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }

        assert array.size() == 4;

        assert Arrays.equals(array.data(), new Long[]{1L, 2L, 3L, 4L});

        array.clear();
        assert array.size() == 0;

        try {
            array.contains(1L);
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }

        try {
            array.offset(1L);
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }

        array.set(6, 6L);
        assert array.size() == 7;

        array.set(1, 1L);
        assert array.get(1) == 1;

        try {
            array.set(2, null);
            assert false;
        } catch (Exception e) {
            assert e instanceof NullPointerException;
        }
    }

    @Test
    public void testIndexable() {
        Array<Long> array = new Array<>(true);
        array.add(0L);
        array.add(1L);
        array.add(2L);
        array.add(3L);
        array.add(4L);
        assert array.size() == 5;

        try {
            array.add(4L);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        assert array.contains(2L);
        array.remove(2);
        assert array.size() == 4;

        assert !array.contains(2L);
        array.add(2L);
        assert array.size() == 5;

        assert array.offset(3L) == 3;
        assert array.offset(10L) == null;

        array.clear();
        assert array.size() == 0;
    }

}
