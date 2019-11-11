package com.github.sisyphsu.smartbuf.benchmark.medium;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.smartbuf.SmartPacket;
import com.github.sisyphsu.smartbuf.SmartStream;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * Benchmark                        Mode  Cnt     Score     Error  Units
 * MediumSerialBenchmark.json       avgt    6  4346.080 ± 123.714  ns/op
 * MediumSerialBenchmark.protobuf   avgt    6   793.625 ±  11.015  ns/op
 * MediumSerialBenchmark.sb_packet  avgt    6  4535.039 ±  74.490  ns/op
 * MediumSerialBenchmark.sb_stream  avgt    6  3882.344 ±  52.287  ns/op
 *
 * @author sulin
 * @since 2019-10-31 20:40:55
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MediumSerialBenchmark {

    static final ObjectMapper MAPPER = new ObjectMapper();
    static final UserModel    USER   = UserModel.random();
    static final SmartStream  STREAM = new SmartStream();

    @Benchmark
    public void json() throws Exception {
        MAPPER.writeValueAsBytes(USER);
    }

    @Benchmark
    public void protobuf() {
        USER.toUser().toBuilder();
    }

    @Benchmark
    public void sb_packet() throws Exception {
        SmartPacket.serialize(USER);
    }

    @Benchmark
    public void sb_stream() throws Exception {
        STREAM.serialize(USER);
    }

}
