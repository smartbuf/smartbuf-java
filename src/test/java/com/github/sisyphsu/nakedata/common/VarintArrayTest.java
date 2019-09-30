package com.github.sisyphsu.nakedata.common;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author sulin
 * @since 2019-09-29 16:36:47
 */
public class VarintArrayTest {

    @Test
    public void test() {
        VarintArray set = new VarintArray(false);
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);

        try {
            set.remove(1);
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }

        assert set.size() == 4;

        assert Arrays.equals(set.data(), new long[]{1L, 2L, 3L, 4L});

        set.clear();
        assert set.size() == 0;
    }

    @Test
    public void testIndexable() {
        VarintArray set = new VarintArray(true);
        set.add(0);
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        assert set.size() == 5;

        try {
            set.add(4);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        assert set.contains(2);
        set.remove(2);
        assert set.size() == 4;

        assert !set.contains(2);
        set.add(2);
        assert set.size() == 5;

        System.out.println(set.offset(3));
        assert set.offset(3) == 3;
        assert set.offset(10) == null;

        set.clear();
        assert set.size() == 0;
    }

}
