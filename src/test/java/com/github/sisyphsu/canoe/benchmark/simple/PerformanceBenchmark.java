package com.github.sisyphsu.canoe.benchmark.simple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.canoe.CanoePacket;
import com.github.sisyphsu.canoe.CanoeStream;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark                 Mode  Cnt     Score     Error  Units
 * SimpleBenchmark.json      avgt    6  1474.052 ± 137.155  ns/op
 * SimpleBenchmark.packet    avgt    6  8817.210 ± 325.374  ns/op
 * SimpleBenchmark.protobuf  avgt    6  1271.790 ±  19.230  ns/op
 * SimpleBenchmark.stream    avgt    6  8208.368 ± 243.626  ns/op
 *
 * @author sulin
 * @since 2019-10-28 17:32:33
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class PerformanceBenchmark {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final UserModel USER = UserModel.random();

    private static final CanoeStream STREAM = new CanoeStream();

    @Benchmark
    public void json() throws JsonProcessingException {
        OBJECT_MAPPER.writeValueAsString(USER.toModel());
    }

    @Benchmark
    public void packet() throws IOException {
        CanoePacket.serialize(USER.toModel());
    }

    @Benchmark
    public void stream() throws IOException {
        STREAM.serialize(USER.toModel());
    }

    @Benchmark
    public void protobuf() {
        USER.toPB().toByteArray();
    }

}
