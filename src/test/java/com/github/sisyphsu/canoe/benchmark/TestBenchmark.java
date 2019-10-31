package com.github.sisyphsu.canoe.benchmark;

import com.github.sisyphsu.canoe.Canoe;
import com.github.sisyphsu.canoe.convertor.ConverterPipeline;
import com.github.sisyphsu.canoe.node.Node;
import com.github.sisyphsu.canoe.node.std.VarintNode;
import com.github.sisyphsu.canoe.transport.Schema;
import org.openjdk.jmh.annotations.*;

import java.util.Date;
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

    static Schema            schema   = new Schema(true);
    static ConverterPipeline pipeline = Canoe.CODEC.getPipeline(Date.class, Node.class);
    static Date              date     = new Date();

    @Benchmark
    public void test() {
//        schema.reset(); // 4ns
//        Canoe.CODEC.toXType(Node.class); // 3ns

//        pipeline.convert(date, Canoe.CODEC.toXType(Node.class)); // 44ns
        VarintNode.valueOf(date.getTime()); // 1.3ns
    }

}
