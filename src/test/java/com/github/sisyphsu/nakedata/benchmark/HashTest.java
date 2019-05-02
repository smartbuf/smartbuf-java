package com.github.sisyphsu.nakedata.benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 测试Integer与String作为Map的key时的性能表现，预期后者的hash运算可能会有额外的性能损耗。
 * <p>
 * Benchmark             Mode  Cnt  Score    Error  Units
 * HashTest.testInt      avgt    6  0.011 ±  0.001  us/op
 * HashTest.testInteger  avgt    6  0.011 ±  0.001  us/op
 * HashTest.testString   avgt    6  0.010 ±  0.001  us/op
 * <p>
 * 字符串的hash函数好像并没有影响到Map性能。
 *
 * @author sulin
 * @since 2019-05-02 18:55:33
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class HashTest {

    private static final Map<Integer, Object> intMap = new HashMap<>();
    private static final Map<String, Object> strMap = new HashMap<>();

    @Benchmark
    public void testInt() {
        intMap.put(123456, null);
        intMap.remove(123456);
    }

    @Benchmark
    public void testInteger() {
        Integer i = 123456;
        intMap.put(i, null);
        intMap.remove(i);
    }

    @Benchmark
    public void testString() {
        strMap.put("123456", null);
        strMap.remove("123456");
    }

}
