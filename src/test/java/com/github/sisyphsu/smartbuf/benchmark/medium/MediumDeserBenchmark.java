package com.github.sisyphsu.smartbuf.benchmark.medium;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.smartbuf.SmartPacket;
import com.github.sisyphsu.smartbuf.SmartStream;
import com.github.sisyphsu.smartbuf.transport.Input;
import com.github.sisyphsu.smartbuf.utils.CodecUtils;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark                       Mode  Cnt     Score     Error  Units
 * MediumDeserBenchmark.json       avgt    6  6736.829 ± 157.811  ns/op
 * MediumDeserBenchmark.protobuf   avgt    6  2553.560 ±  50.666  ns/op
 * MediumDeserBenchmark.sb_packet  avgt    6  6340.070 ±  99.095  ns/op
 * MediumDeserBenchmark.sb_stream  avgt    6  5413.430 ± 222.224  ns/op
 *
 * @author sulin
 * @since 2019-11-11 10:53:43
 */
@Warmup(iterations = 2, time = 2)
@Fork(3)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MediumDeserBenchmark {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final UserModel    user   = UserModel.random();
    private static final SmartStream  stream = new SmartStream();

    static Input input = new Input(false);

    static Object inputObj;
    static byte[] jsonBytes;
    static byte[] pbBytes;
    static byte[] packetBytes;
    static byte[] streamBytes;

    static {
        try {
            jsonBytes = mapper.writeValueAsBytes(user);
            pbBytes = user.toUser().toByteArray();
            packetBytes = SmartPacket.serialize(user);
            streamBytes = stream.serialize(user);

            // smartbuf's stream-mode need warm up to avoid sequence error
            stream.deserialize(streamBytes, UserModel.class);

            jsonBytes = mapper.writeValueAsBytes(user);
            pbBytes = user.toUser().toByteArray();
            packetBytes = SmartPacket.serialize(user);
            streamBytes = stream.serialize(user);

            inputObj = input.read(packetBytes);
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
        Medium.User.parseFrom(pbBytes);
    }

    @Benchmark
    public void sb_packet() throws Exception {
        SmartPacket.deserialize(packetBytes, UserModel.class);
    }

    @Benchmark
    public void sb_stream() throws Exception {
        stream.deserialize(streamBytes, UserModel.class);
    }

    //    @Benchmark
    public void test() throws IOException {
//        input.read(packetBytes); // 3080ns
        CodecUtils.convert(inputObj, UserModel.class); // 4727ns
    }

}
