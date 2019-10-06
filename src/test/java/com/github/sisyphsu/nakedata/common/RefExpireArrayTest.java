package com.github.sisyphsu.nakedata.common;

import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * @author sulin
 * @since 2019-10-06 15:51:26
 */
public class RefExpireArrayTest {

    @Test
    public void test() {
        RefExpireArray<String> array = new RefExpireArray<>();
        array.add("hello");
        array.add("world");
        assert array.contains("hello");
        assert array.contains("world");
        assert array.offset("hello") == 0;
        assert array.offset("world") == 1;
        assert array.size() == 2;

        array.unreference(0);
        assert !array.contains("hello");

        array.add("hello");
        array.add("hello");
        array.add("hello");
        array.unreference(0);
        assert array.contains("hello");
        array.unreference(0);
        assert array.contains("hello");
        array.unreference(0);
        assert !array.contains("hello");

        array.add("111");
        array.add("222");
        array.add("333");
        array.add("444");
        array.add("555");
        array.add("666");
        assert array.size() == 7;
        assert Objects.equals(array.get(0), "111");

        assert array.offset("111") == 0;
        assert array.offset("222") == 2;
        assert array.offset("666") == 6;

        array.unreference(3, 4, 5);
        assert !array.contains("333");
        assert !array.contains("444");
        assert !array.contains("555");
        assert array.size() == 4;

        try {
            array.unreference(3);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalStateException;
        }
        try {
            array.add(null);
            assert false;
        } catch (Exception e) {
            assert e instanceof NullPointerException;
        }
    }

}
