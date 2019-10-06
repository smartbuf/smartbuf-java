package com.github.sisyphsu.nakedata.context.common;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author sulin
 * @since 2019-09-29 16:36:47
 */
public class DoubleArrayTest {

    @Test
    public void test() {
        DoubleArray array = new DoubleArray(false);
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

        assert Arrays.equals(array.data(), new double[]{1.0, 2.0, 3.0, 4.0});

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
        DoubleArray array = new DoubleArray(true);
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

        assert array.offset(3) == 3;
        assert array.offset(10) == null;

        array.clear();
        assert array.size() == 0;
    }

}
