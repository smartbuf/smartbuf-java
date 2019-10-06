package com.github.sisyphsu.nakedata.common;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author sulin
 * @since 2019-09-29 16:36:47
 */
public class LongArrayTest {

    @Test
    public void test() {
        LongArray array = new LongArray(false);
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(4);

        try {
            array.remove(1);
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }

        assert array.size() == 4;

        assert Arrays.equals(array.data(), new long[]{1L, 2L, 3L, 4L});

        array.clear();
        assert array.size() == 0;

        try {
            array.contains(1);
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }

        try {
            array.offset(1);
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }

        array.set(6, 6);
        assert array.size() == 7;

        array.set(1, 1);
        assert array.get(1) == 1;
    }

    @Test
    public void testIndexable() {
        LongArray array = new LongArray(true);
        array.add(0);
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(4);
        assert array.size() == 5;

        try {
            array.add(4);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        assert array.contains(2);
        array.remove(2);
        assert array.size() == 4;

        assert !array.contains(2);
        array.add(2);
        assert array.size() == 5;

        System.out.println(array.offset(3));
        assert array.offset(3) == 3;
        assert array.offset(10) == null;

        array.clear();
        assert array.size() == 0;
    }

}
