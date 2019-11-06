package com.github.sisyphsu.canoe.utils;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-10-02 15:18:19
 */
public class ArrayUtilsTest {

    @Test
    public void testDescFastSort() {
        ArrayUtils.descFastSort(new int[0], 0, 0);

        int[] arr = {1};
        ArrayUtils.descFastSort(arr, 0, 0);
        assert arr[0] == 1;

        arr = new int[]{1, 2};
        ArrayUtils.descFastSort(arr, 0, 1);
        assert arr[0] == 2;
        assert arr[1] == 1;

        arr = new int[]{3, 2, 4, 1, 7};
        ArrayUtils.descFastSort(arr, 0, arr.length - 1);
        assert arr[0] == 7;
        assert arr[1] == 4;
        assert arr[2] == 3;
        assert arr[3] == 2;
        assert arr[4] == 1;

        arr = new int[100];
        for (int i = 0; i < 100; i++) {
            arr[i] = RandomUtils.nextInt(1, 10000000);
        }
        ArrayUtils.descFastSort(arr, 0, arr.length - 1);
        for (int i = 0; i < 99; i++) {
            assert arr[i] >= arr[i + 1];
        }
    }

    @Test
    public void testLongArray() {
        ArrayUtils.descFastSort(new long[0], 0, 0);

        long[] arr = {1};
        ArrayUtils.descFastSort(arr, 0, 0);
        assert arr[0] == 1;

        arr = new long[]{1, 2};
        ArrayUtils.descFastSort(arr, 0, 1);
        assert arr[0] == 2;
        assert arr[1] == 1;

        arr = new long[]{3, 2, 4, 1, 7};
        ArrayUtils.descFastSort(arr, 0, arr.length - 1);
        assert arr[0] == 7;
        assert arr[1] == 4;
        assert arr[2] == 3;
        assert arr[3] == 2;
        assert arr[4] == 1;

        arr = new long[100];
        for (int i = 0; i < 100; i++) {
            arr[i] = RandomUtils.nextInt(1, 10000000);
        }
        ArrayUtils.descFastSort(arr, 0, arr.length - 1);
        for (int i = 0; i < 99; i++) {
            assert arr[i] >= arr[i + 1];
        }
    }

    @Test
    public void testAr() {
        Object[] arr = new Object[4];
        arr[0] = "1";
        assert arr[0] == "1";

        arr = ArrayUtils.put(null, 0, "0");
        arr = ArrayUtils.put(arr, 1, "1");
        arr = ArrayUtils.put(arr, 2, "2");
        arr = ArrayUtils.put(arr, 3, "3");
        arr = ArrayUtils.put(arr, 4, "4");

        assert arr[0] == "0";
    }

}
