package com.github.sisyphsu.smartbuf.benchmark.medium;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.smartbuf.SmartPacket;
import com.github.sisyphsu.smartbuf.SmartStream;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * Benchmark                 Mode  Cnt     Score    Error  Units
 * MediumBenchmark.json      avgt    6  4326.088 ± 91.941  ns/op
 * MediumBenchmark.packet    avgt    6  4492.260 ± 75.072  ns/op
 * MediumBenchmark.protobuf  avgt    6   802.377 ±  6.276  ns/op
 * MediumBenchmark.stream    avgt    6  3829.154 ± 77.204  ns/op
 *
 * @author sulin
 * @since 2019-10-31 20:40:55
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MediumBenchmark {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final UserModel    USER   = UserModel.random();
    private static final SmartStream  STREAM = new SmartStream();

    @Benchmark
    public void json() throws Exception {
        MAPPER.writeValueAsBytes(USER);
    }

    @Benchmark
    public void protobuf() {
        USER.toUser().toBuilder();
    }

    @Benchmark
    public void packet() throws Exception {
        SmartPacket.serialize(USER);
    }

    @Benchmark
    public void stream() throws Exception {
        STREAM.serialize(USER);
    }

}
