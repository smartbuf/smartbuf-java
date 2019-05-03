package com.github.sisyphsu.nakedata.benchmark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 测试Tree结构与复杂Key的性能对比
 * <p>
 * Benchmark                   Mode  Cnt  Score    Error  Units
 * HashTreeTest.hashBenchmark  avgt    6  0.001 ±  0.001  us/op
 * HashTreeTest.mapBenchmark   avgt    6  0.018 ±  0.001  us/op
 * HashTreeTest.treeBenchmark  avgt    6  0.097 ±  0.009  us/op
 * <p>
 * 树形结构检索速度太慢，直接采用map就可以获得稳定且高效的性能表现。
 *
 * @author sulin
 * @since 2019-05-03 14:21:26
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class HashTreeTest {

    private static final Map<Key, Object> complexMap = new HashMap<>();
    private static final Node root = new Node();
    private static final int[] keys = new int[]{1, 2, 3, 4, 5, 6, 7, 8};

    static {
        for (int i = 0; i < 16; i++) {
            int[] types = new int[8];
            for (int j = 0; j < types.length; j++) {
                types[j] = RandomUtils.nextInt(100000, 999999);
            }
            complexMap.put(new Key(types), null);
            root.add(types, 0, null);
        }
    }

    @Test
    public void testNode() {
        root.add(keys, 0, "hello");
        System.out.println(root.get(keys, 0));

        complexMap.put(new Key(new int[]{1, 2, 3, 4, 5, 6, 7, 8}), "hello2");
        System.out.println(complexMap.get(new Key(new int[]{1, 2, 3, 4, 5, 6, 7, 8})));
    }

    @Benchmark
    public void mapBenchmark() {
        Key key = new Key(keys);
        complexMap.put(key, null);
        complexMap.get(key);
    }

    @Benchmark
    public void treeBenchmark() {
        root.add(keys, 0, null);
        root.get(keys, 0);
    }

    @Benchmark
    public void hashBenchmark() {
        Key key = new Key(keys);
        key.hashCode();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key {
        private int[] types;
    }

    @Data
    public static class Node {
        private Object value;
        private Map<Integer, Node> nodes = new HashMap<>();

        public void add(int[] ids, int off, Object val) {
            if (ids.length <= off) {
                this.value = val;
                return;
            }
            int id = ids[off];
            Node subNode = nodes.get(id);
            if (subNode == null) {
                subNode = new Node();
                nodes.put(id, subNode);
            }
            subNode.add(ids, off + 1, val);
        }

        public Object get(int[] ids, int off) {
            if (ids.length <= off) {
                return this.value;
            }
            int id = ids[off];
            Node subNode = nodes.get(id);
            if (subNode == null) {
                return null;
            }
            return subNode.get(ids, off + 1);
        }

    }

}
