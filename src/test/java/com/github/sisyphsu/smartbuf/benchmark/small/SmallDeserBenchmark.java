package com.github.sisyphsu.smartbuf.benchmark.small;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.smartbuf.SmartPacket;
import com.github.sisyphsu.smartbuf.SmartStream;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark                      Mode  Cnt    Score    Error  Units
 * SmallDeserBenchmark.json       avgt    6  824.563 ± 15.484  ns/op
 * SmallDeserBenchmark.protobuf   avgt    6  137.648 ±  5.369  ns/op
 * SmallDeserBenchmark.sb_packet  avgt    6  721.921 ±  5.272  ns/op
 * SmallDeserBenchmark.sb_stream  avgt    6  451.134 ± 13.717  ns/op
 *
 * @author sulin
 * @since 2019-11-11 10:38:04
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SmallDeserBenchmark {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final UserModel    user   = UserModel.random();
    private static final SmartStream  stream = new SmartStream();

    static byte[] jsonBytes;
    static byte[] pbBytes;
    static byte[] packetBytes;
    static byte[] streamBytes;

    static {
        try {
            jsonBytes = mapper.writeValueAsBytes(user);
            pbBytes = user.toPB().toByteArray();
            packetBytes = SmartPacket.serialize(user);
            streamBytes = stream.serialize(user);

            // smartbuf's stream-mode need warm up to avoid sequence error
            stream.deserialize(streamBytes, UserModel.class);

            jsonBytes = mapper.writeValueAsBytes(user);
            pbBytes = user.toPB().toByteArray();
            packetBytes = SmartPacket.serialize(user);
            streamBytes = stream.serialize(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public void json() throws Exception {
        mapper.readValue(jsonBytes, UserModel.class);
    }

    @Benchmark
    public void protobuf() throws Exception {
        Small.User.parseFrom(pbBytes);
    }

    @Benchmark
    public void sb_packet() throws Exception {
        SmartPacket.deserialize(packetBytes, UserModel.class);
    }

    @Benchmark
    public void sb_stream() throws Exception {
        stream.deserialize(streamBytes, UserModel.class);
    }

}
