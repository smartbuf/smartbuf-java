package com.github.sisyphsu.canoe.benchmark.simple;

import com.github.sisyphsu.canoe.Canoe;
import com.github.sisyphsu.canoe.node.Node;
import com.github.sisyphsu.canoe.reflect.TypeTest;
import lombok.Data;
import net.sf.cglib.beans.BeanMap;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * Benchmark          Mode  Cnt     Score     Error  Units
 * TestBenchmark.bbb  avgt    6  5318.682 ± 115.968  ns/op
 * <p>
 * 3-fields:
 * TestBenchmark.convert  avgt    6  463.863 ± 38.506  ns/op
 * 2-fields:
 * TestBenchmark.convert  avgt    6  391.941 ± 9.256  ns/op
 * 1-fields:
 * TestBenchmark.convert  avgt    6  318.060 ± 19.271  ns/op
 * <p>
 * 70ns/1-fields, 250ns for BeanMap?
 *
 * @author sulin
 * @since 2019-10-28 17:57:59
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class TestBenchmark {

    private static final UserModel USER = UserModel.random();

    static final Person persion = new Person();

    @Benchmark
    public void convert() {
        BeanMap beanMap = BeanMap.create(persion);
        for (Object key : beanMap.keySet()) {
            beanMap.get(key);
        }
    }

    @Data
    public static class Person {
        private int    id   = 100;
        private String name = "hello world";
        private long   time = 1000000L;
    }

}
