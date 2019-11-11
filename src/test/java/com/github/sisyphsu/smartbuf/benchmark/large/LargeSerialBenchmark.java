package com.github.sisyphsu.smartbuf.benchmark.large;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.smartbuf.SmartPacket;
import com.github.sisyphsu.smartbuf.SmartStream;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Benchmark                       Mode  Cnt       Score      Error  Units
 * LargeSerialBenchmark.json       avgt    6  131570.638 ± 2561.824  ns/op
 * LargeSerialBenchmark.protobuf   avgt    6   98832.707 ±  845.220  ns/op
 * LargeSerialBenchmark.sb_packet  avgt    6   81680.392 ± 1086.799  ns/op
 * LargeSerialBenchmark.sb_stream  avgt    6   79006.771 ± 1891.546  ns/op
 *
 * @author sulin
 * @since 2019-11-10 16:12:28
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LargeSerialBenchmark {

    static final ObjectMapper     mapper = new ObjectMapper();
    static final List<TrendModel> trends = LargeTest.trends;
    static final SmartStream      stream = new SmartStream();

    @Benchmark
    public void json() throws Exception {
        mapper.writeValueAsBytes(trends);
    }

    @Benchmark
    public void protobuf() {
        Large.Trends.newBuilder().addAllTrends(trends.stream().map(TrendModel::toPB).collect(Collectors.toList())).build().toByteArray();
    }

    @Benchmark
    public void sb_packet() throws Exception {
        SmartPacket.serialize(trends);
    }

    @Benchmark
    public void sb_stream() throws Exception {
        stream.serialize(trends);
    }

}
