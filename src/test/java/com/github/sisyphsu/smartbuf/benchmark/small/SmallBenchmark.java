package com.github.sisyphsu.smartbuf.benchmark.small;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.smartbuf.SmartPacket;
import com.github.sisyphsu.smartbuf.SmartStream;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark                Mode  Cnt    Score    Error  Units
 * SmallBenchmark.json      avgt    6  721.521 ± 51.067  ns/op
 * SmallBenchmark.packet    avgt    6  599.017 ±  3.766  ns/op
 * SmallBenchmark.protobuf  avgt    6  202.389 ±  3.532  ns/op
 * SmallBenchmark.stream    avgt    6  354.649 ± 16.291  ns/op
 *
 * @author sulin
 * @since 2019-10-28 17:32:33
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SmallBenchmark {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final UserModel    user          = UserModel.random();
    private static final SmartStream  STREAM        = new SmartStream();

    static {
        try {
            STREAM.serialize(user);
            STREAM.serialize(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void json() throws JsonProcessingException {
        OBJECT_MAPPER.writeValueAsString(user);
    }

    @Benchmark
    public void packet() throws IOException {
        SmartPacket.serialize(user);
    }

    @Benchmark
    public void stream() throws IOException {
        STREAM.serialize(user);
    }

    @Benchmark
    public void protobuf() {
        user.toPB().toByteArray();
    }

}
