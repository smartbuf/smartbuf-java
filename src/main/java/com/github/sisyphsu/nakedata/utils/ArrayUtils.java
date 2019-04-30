package com.github.sisyphsu.nakedata.utils;

/**
 * array's util functions
 *
 * @author sulin
 * @since 2019-04-29 17:56:43
 */
public class ArrayUtils {

    /**
     * Fast-Sort arithmetic which support descend sort
     *
     * @param arr  Source array
     * @param from start offset, include
     * @param to   end offset, include
     */
    public static void descSort(int[] arr, int from, int to) {
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
            descSort(arr, from, low - 1);
        }
        if (to - low > 1) {
            descSort(arr, low + 1, to);
        }
    }

    private static void headMax(int[] list, int len, int i) {
        int k = i, root = list[i], index = 2 * k + 1;
        while (index < len) {
            if (index + 1 < len) {
                if (list[index] < list[index + 1]) {
                    index = index + 1;
                }
            }
            if (list[index] > root) {
                list[k] = list[index];
                k = index;
                index = 2 * k + 1;
            } else {
                break;
            }
        }
        list[k] = root;
    }

}
