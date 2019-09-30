package com.github.sisyphsu.nakedata.common;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author sulin
 * @since 2019-09-29 16:36:47
 */
public class ObjectArrayTest {

    @Test
    public void test() {
        ObjectArray<Long> set = new ObjectArray<>(false);
        set.add(1L);
        set.add(2L);
        set.add(3L);
        set.add(4L);

        try {
            set.remove(1);
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }

        assert set.size() == 4;

        assert Arrays.equals(set.data(), new Long[]{1L, 2L, 3L, 4L});

        set.clear();
        assert set.size() == 0;
    }

    @Test
    public void testIndexable() {
        ObjectArray<Long> set = new ObjectArray<>(true);
        set.add(0L);
        set.add(1L);
        set.add(2L);
        set.add(3L);
        set.add(4L);
        assert set.size() == 5;

        try {
            set.add(4L);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        assert set.contains(2L);
        set.remove(2);
        assert set.size() == 4;

        assert !set.contains(2L);
        set.add(2L);
        assert set.size() == 5;

        assert set.offset(3L) == 3;
        assert set.offset(10L) == null;

        set.clear();
        assert set.size() == 0;
    }

}
