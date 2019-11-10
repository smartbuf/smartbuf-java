package com.github.sisyphsu.smartbuf.utils;

import java.lang.reflect.Array;

/**
 * Some utils for array, meanly sort
 *
 * @author sulin
 * @since 2019-10-02 15:16:34
 */
public final class ArrayUtils {

    private ArrayUtils() {
    }

    /**
     * Fast-Sort arithmetic which support descend sort for int[]
     *
     * @param arr  Source array
     * @param from start offset, include
     * @param to   end offset, include
     */
    public static void descFastSort(final int[] arr, final int from, final int to) {
        if (to - from < 1) {
            return;
        }
        int low = from;
        int high = to;
        int baseVal = arr[low];

        while (low < high) {
            for (; high > low; high--) {
                if (arr[high] > baseVal) {
                    arr[low] = arr[high];
                    low++;
                    break;
                }
            }
            for (; high > low; low++) {
                if (arr[low] < baseVal) {
                    arr[high] = arr[low];
                    high--;
                    break;
                }
            }
        }

        arr[low] = baseVal;
        if (low - from > 1) {
            descFastSort(arr, from, low - 1);
        }
        if (to - low > 1) {
            descFastSort(arr, low + 1, to);
        }
    }

    /**
     * Fast-Sort arithmetic which support descend sort for long[]
     *
     * @param arr  Source array
     * @param from start offset, include
     * @param to   end offset, include
     */
    public static void descFastSort(final long[] arr, final int from, final int to) {
        if (to - from < 1) {
            return;
        }
        int low = from;
        int high = to;
        long baseVal = arr[low];

        while (low < high) {
            for (; high > low; high--) {
                if (arr[high] > baseVal) {
                    arr[low] = arr[high];
                    low++;
                    break;
                }
            }
            for (; high > low; low++) {
                if (arr[low] < baseVal) {
                    arr[high] = arr[low];
                    high--;
                    break;
                }
            }
        }

        arr[low] = baseVal;
        if (low - from > 1) {
            descFastSort(arr, from, low - 1);
        }
        if (to - low > 1) {
            descFastSort(arr, low + 1, to);
        }
    }

    /**
     * put value into array's specified position, execute expansion automatically.
     *
     * @param arr Source array
     * @param pos Put position
     * @param val New item
     * @param <T> T
     * @return Put result
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] put(T[] arr, int pos, T val) {
        if (arr == null) {
            arr = (T[]) new Object[4];
        }
        if (arr.length <= pos) {
            T[] newArr = (T[]) Array.newInstance(arr.getClass().getComponentType(), arr.length * 2);
            System.arraycopy(arr, 0, newArr, 0, arr.length);
            arr = newArr;
        }
        arr[pos] = val;
        return arr;
    }

}
