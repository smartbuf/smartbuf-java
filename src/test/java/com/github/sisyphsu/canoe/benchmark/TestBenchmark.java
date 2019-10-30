package com.github.sisyphsu.canoe.benchmark;

import com.github.sisyphsu.canoe.transport.Schema;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * @author sulin
 * @since 2019-10-30 20:21:38
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class TestBenchmark {

    static Schema schema = new Schema(true);

    @Benchmark
    public void test() {
        schema.reset(); // 4ns
    }

}
