package com.github.sisyphsu.smartbuf.benchmark.small;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.smartbuf.SmartPacket;
import com.github.sisyphsu.smartbuf.SmartStream;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark                       Mode  Cnt    Score    Error  Units
 * SmallSerialBenchmark.json       avgt    6  733.705 ± 17.387  ns/op
 * SmallSerialBenchmark.protobuf   avgt    6  197.968 ±  7.379  ns/op
 * SmallSerialBenchmark.sb_packet  avgt    6  630.900 ± 12.533  ns/op
 * SmallSerialBenchmark.sb_stream  avgt    6  368.521 ±  3.235  ns/op
 *
 * @author sulin
 * @since 2019-10-28 17:32:33
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SmallSerialBenchmark {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final UserModel    user          = UserModel.random();
    private static final SmartStream  STREAM        = new SmartStream();

    @Benchmark
    public void json() throws JsonProcessingException {
        OBJECT_MAPPER.writeValueAsString(user);
    }

    @Benchmark
    public void protobuf() {
        user.toPB().toByteArray();
    }

    @Benchmark
    public void sb_packet() throws IOException {
        SmartPacket.serialize(user);
    }

    @Benchmark
    public void sb_stream() throws IOException {
        STREAM.serialize(user);
    }

}
