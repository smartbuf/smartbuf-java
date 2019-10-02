package com.github.sisyphsu.nakedata.common;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author sulin
 * @since 2019-10-02 11:10:47
 */
public class RecycleArrayTest {

    @Test
    public void test() throws InterruptedException {
        RecycleArray<String> array = new RecycleArray<>();

        Thread.sleep(10);
        array.add("1");
        Thread.sleep(10);
        array.add("2");
        Thread.sleep(10);
        array.add("3");
        Thread.sleep(10);
        array.add("4");
        Thread.sleep(10);
        array.add("5");
        Thread.sleep(10);
        array.add("2"); // reactive "2"

        assertEquals(array.size(), 5);

        assertEquals(array.offset("4"), 3);
        assertEquals(array.get(3), "4");

        long[] items = array.release(2);
        System.out.println("release: " + Arrays.toString(items));
        assert items.length == 2;
        assertEquals((int) items[0], 2);
        assertEquals((int) items[1], 0);

        assertEquals(array.size(), 3);

        assert !array.contains("1");
        assert !array.contains("3");

        assert array.contains("2");
        assert array.contains("4");
        assert array.contains("5");

        try {
            array.add(null);
            assert false;
        } catch (Exception e) {
            assert e instanceof NullPointerException;
        }

        items = array.release(1);
        System.out.println("release: " + Arrays.toString(items));
        assert items.length == 1;
        assert ((int) items[0]) == 3;
    }

}
