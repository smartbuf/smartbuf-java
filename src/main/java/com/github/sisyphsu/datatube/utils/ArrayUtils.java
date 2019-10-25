package com.github.sisyphsu.datatube.utils;

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
     * Adjust the specified heap from root, make sure that heap[root] is the largest item at [root, last).
     *
     * @param heap Array that represents heap
     * @param root Root offset of heap
     * @param last Last offset
     */
    public static void maxHeapAdjust(final long[] heap, final int root, final int last) {
        final long rootVal = heap[root];
        int parent = root;
        int child1, child2;
        while (true) {
            child1 = 2 * parent + 1;
            child2 = child1 + 1;
            if (child1 >= last) {
                break; // no children
            }
            if (child2 < last && heap[child2] > heap[child1]) {
                child1 = child2; // choice the bigger child
            }
            if (heap[child1] <= rootVal) {
                break; // this branch is smaller
            }
            heap[parent] = heap[child1]; // switch root to smaller position
            parent = child1;
        }
        heap[parent] = rootVal;
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
