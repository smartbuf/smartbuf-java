package com.github.sisyphsu.nakedata.node;

import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * @author sulin
 * @since 2019-10-21 18:48:29
 */
@SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
public class SubListTest {

    @Test
    public void test() {
        Object[] arr = new Object[]{1, 2L, 3.0f, 4.0, null, "hello"};

        SubList<Object> list = new SubList<>(0, arr.length, arr);
        assert list.toArray() == arr;
        list = new SubList<>(0, arr.length - 1, arr);
        assert list.toArray() != arr;

        list = new SubList<>(1, 5, arr);
        assert list.size() == 4;
        assert Objects.equals(list.get(0), arr[1]);
        assert Objects.equals(list.get(1), arr[2]);

        assert list.toArray().length == list.size();

        Object[] cp1 = list.toArray(new Object[0]);
        Object[] cp2 = list.toArray(new Object[list.size()]);
        assert Objects.deepEquals(cp1, cp2);

        list.set(2, 5);
        assert Objects.equals(arr[3], 5);

        assert list.indexOf(-1) == -1;
        assert list.indexOf("hello") == -1;
        assert list.indexOf(3.0f) == 1;
        assert list.contains(null);
        assert !list.contains(1);

        list.forEach(o -> {
        });

        try {
            list.replaceAll(null);
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }

        try {
            list.sort(null);
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }
    }

}
