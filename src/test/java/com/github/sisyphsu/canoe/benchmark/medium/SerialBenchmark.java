package com.github.sisyphsu.canoe.benchmark.medium;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.canoe.CanoePacket;
import com.github.sisyphsu.canoe.CanoeStream;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * Benchmark                 Mode  Cnt      Score     Error  Units
 * SerialBenchmark.json      avgt    6   4329.166 ± 138.656  ns/op
 * SerialBenchmark.packet    avgt    6  11154.203 ± 456.539  ns/op
 * SerialBenchmark.protobuf  avgt    6    797.809 ±   7.958  ns/op
 * SerialBenchmark.stream    avgt    6   9264.121 ± 392.667  ns/op
 * <p>
 * Array problem !!!
 *
 * @author sulin
 * @since 2019-10-31 20:40:55
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SerialBenchmark {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final UserModel    USER   = UserModel.random();
    private static final CanoeStream  STREAM = new CanoeStream();

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
        CanoePacket.serialize(USER);
    }

    @Benchmark
    public void stream() throws Exception {
        STREAM.serialize(USER);
    }

}
