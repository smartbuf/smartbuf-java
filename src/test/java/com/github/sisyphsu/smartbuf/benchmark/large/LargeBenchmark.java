package com.github.sisyphsu.smartbuf.benchmark.large;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.smartbuf.SmartPacket;
import com.github.sisyphsu.smartbuf.SmartStream;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Benchmark                Mode  Cnt       Score       Error  Units
 * LargeBenchmark.json      avgt    6  147872.403 ± 15552.341  ns/op
 * LargeBenchmark.protobuf  avgt    6  115657.352 ±  2803.250  ns/op
 * LargeBenchmark.packet    avgt    6   90870.999 ±   788.830  ns/op
 * LargeBenchmark.stream    avgt    6   88805.906 ±  1576.276  ns/op
 *
 * @author sulin
 * @since 2019-11-10 16:12:28
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LargeBenchmark {

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
    public void packet() throws Exception {
        SmartPacket.serialize(trends);
    }

    @Benchmark
    public void stream() throws Exception {
        stream.serialize(trends);
    }

}
