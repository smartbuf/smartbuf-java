package com.github.sisyphsu.nakedata.benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 测试Hash表与Array的读性能测试
 * <p>
 * 第一轮测试，读3次
 * Benchmark                  Mode  Cnt  Score    Error  Units
 * HashArrayReadTest.testArr  avgt    6  0.011 ±  0.001  us/op
 * HashArrayReadTest.testMap  avgt    6  0.005 ±  0.001  us/op
 * <p>
 * 第二轮测试，读1次
 * Benchmark                  Mode  Cnt  Score    Error  Units
 * HashArrayReadTest.testArr  avgt    6  0.004 ±  0.001  us/op
 * HashArrayReadTest.testMap  avgt    6  0.002 ±  0.001  us/op
 *
 * @author sulin
 * @since 2019-05-02 19:54:57
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class HashArrayReadTest {

    private static final Map<Integer, Integer> map = new HashMap<>();
    private static final int[] arr = new int[16];

    static {
        for (int i = 0; i < 16; i++) {
            map.put(i, i);
            arr[i] = i;
        }
        Arrays.sort(arr);
    }

    @Benchmark
    public void testMap() {
        Integer i = map.get(8);
    }

    @Benchmark
    public void testArr() {
        int i = arr[Arrays.binarySearch(arr, 8)];
    }

}
