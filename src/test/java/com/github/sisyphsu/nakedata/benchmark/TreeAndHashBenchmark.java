package com.github.sisyphsu.nakedata.benchmark;

import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 测试TreeMap与HashMap的性能对比
 * <p>
 * 第一轮测试 - 容量128
 * Benchmark                           Mode  Cnt  Score    Error  Units
 * TreeAndHashBenchmark.hashBenchmark  avgt    9  0.014 ±  0.001  us/op
 * TreeAndHashBenchmark.treeBenchmark  avgt    9  0.014 ±  0.002  us/op
 * <p>
 * 第二轮测试 - 容量1k
 * Benchmark                           Mode  Cnt  Score   Error  Units
 * TreeAndHashBenchmark.hashBenchmark  avgt    9  0.019 ± 0.002  us/op
 * TreeAndHashBenchmark.treeBenchmark  avgt    9  0.019 ± 0.003  us/op
 * <p>
 * 第三轮测试 - 容量64k
 * Benchmark                           Mode  Cnt  Score   Error  Units
 * TreeAndHashBenchmark.hashBenchmark  avgt    9  0.020 ± 0.002  us/op
 * TreeAndHashBenchmark.treeBenchmark  avgt    9  0.022 ± 0.006  us/op
 *
 * @author sulin
 * @since 2019-05-06 10:49:47
 */
@Warmup(iterations = 2, time = 2)
@BenchmarkMode(Mode.AverageTime)
@Fork(3)
@Measurement(iterations = 3, time = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class TreeAndHashBenchmark {

    private static final Map<String, Object> HASH_MAP = new HashMap<>();
    private static final Map<String, Object> TREE_MAP = new HashMap<>();
    private static final String KEY = RandomStringUtils.randomAlphanumeric(16);
    private static final int INIT_SIZE = 65536;

    static {
        for (int i = 0; i < INIT_SIZE; i++) {
            String key = RandomStringUtils.randomAlphanumeric(16);
            HASH_MAP.put(key, key);
            TREE_MAP.put(key, key);
        }
    }

    @Benchmark
    public void hashBenchmark() {
        HASH_MAP.put(KEY, KEY);
        HASH_MAP.get(KEY);
        HASH_MAP.remove(KEY);
    }

    @Benchmark
    public void treeBenchmark() {
        TREE_MAP.put(KEY, KEY);
        TREE_MAP.get(KEY);
        TREE_MAP.remove(KEY);
    }

}
