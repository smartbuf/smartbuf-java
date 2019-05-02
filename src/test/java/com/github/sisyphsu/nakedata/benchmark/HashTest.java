package com.github.sisyphsu.nakedata.benchmark;

import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 测试Integer与String作为Map的key时的性能表现，预期后者的hash运算可能会有额外的性能损耗。
 * <p>
 * 执行put操作然后remove
 * Benchmark             Mode  Cnt  Score    Error  Units
 * HashTest.testInt      avgt    6  0.011 ±  0.001  us/op
 * HashTest.testInteger  avgt    6  0.011 ±  0.001  us/op
 * HashTest.testString   avgt    6  0.010 ±  0.001  us/op
 * <p>
 * 执行重复put操作
 * Benchmark             Mode  Cnt  Score    Error  Units
 * HashTest.testInt      avgt    6  0.007 ±  0.002  us/op
 * HashTest.testInteger  avgt    6  0.006 ±  0.001  us/op
 * HashTest.testString   avgt    6  0.004 ±  0.001  us/op
 * <p>
 * 怀疑是String常量存在编译器优化，采用运行期随机字符串进行重复put操作
 * Benchmark             Mode  Cnt  Score    Error  Units
 * HashTest.testInt      avgt    6  0.002 ±  0.001  us/op
 * HashTest.testInteger  avgt    6  0.002 ±  0.001  us/op
 * HashTest.testString   avgt    6  0.004 ±  0.001  us/op
 * <p>
 * 随机常量，重复put然后delete
 * Benchmark             Mode  Cnt  Score    Error  Units
 * HashTest.testInt      avgt    6  0.010 ±  0.001  us/op
 * HashTest.testInteger  avgt    6  0.010 ±  0.001  us/op
 * HashTest.testString   avgt    6  0.011 ±  0.001  us/op
 * <p>
 * 随机常量，扩容后重复put、get、remove
 * Benchmark             Mode  Cnt  Score    Error  Units
 * HashTest.testInt      avgt    6  0.012 ±  0.003  us/op
 * HashTest.testInteger  avgt    6  0.012 ±  0.004  us/op
 * HashTest.testString   avgt    6  0.012 ±  0.001  us/op
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

    private static String strKey = RandomStringUtils.randomNumeric(6);
    private static Integer intKey = Integer.parseInt(strKey);

    static {
        for (int i = 0; i < 15; i++) {
            intMap.put(i, i);
            strMap.put(String.valueOf(i), i);
        }
    }

    @Benchmark
    public void testInt() {
        intMap.put(intKey, null);
        intMap.get(intKey);
        intMap.remove(intKey);
    }

    @Benchmark
    public void testInteger() {
        Integer i = intKey;
        intMap.put(i, null);
        intMap.get(i);
        intMap.remove(i);
    }

    @Benchmark
    public void testString() {
        strMap.put(strKey, null);
        strMap.get(strKey);
        strMap.remove(strKey);
    }

}
