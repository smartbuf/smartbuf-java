package com.github.smartbuf.benchmark.medium;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.smartbuf.SmartPacket;
import com.github.smartbuf.SmartStream;
import org.msgpack.MessagePack;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * Benchmark                        Mode  Cnt      Score     Error  Units
 * MediumSerialBenchmark.json       avgt    9   4329.829 ±  69.129  ns/op
 * MediumSerialBenchmark.kryo       avgt    9   5325.744 ± 142.879  ns/op
 * MediumSerialBenchmark.msgpack    avgt    9  21493.727 ± 207.717  ns/op
 * MediumSerialBenchmark.protobuf   avgt    9    796.052 ±  11.317  ns/op
 * MediumSerialBenchmark.sb_packet  avgt    9   4486.963 ±  43.700  ns/op
 * MediumSerialBenchmark.sb_stream  avgt    9   3927.292 ±  57.563  ns/op
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

    static final ObjectMapper MAPPER  = new ObjectMapper();
    static final SmartStream  STREAM  = new SmartStream();
    static final MessagePack  MSGPACK = new MessagePack();
    static final Kryo         KRYO    = new Kryo();

    static final UserModel model = UserModel.random();

    static final ByteBufferOutput kryoOutput = new ByteBufferOutput(1024);

    static {
        KRYO.register(UserModel.class);
        KRYO.register(UserModel.Message.class);
        KRYO.register(UserModel.Tag.class);
    }

    @Benchmark
    public void json() throws Exception {
        MAPPER.writeValueAsBytes(model);
    }

    @Benchmark
    public void msgpack() throws Exception {
        MSGPACK.write(model);
    }

    @Benchmark
    public void kryo() {
        KRYO.writeObject(kryoOutput, model);
        kryoOutput.toBytes();
        kryoOutput.clear();
    }

    @Benchmark
    public void protobuf() {
        model.toUser().toBuilder();
    }

    @Benchmark
    public void sb_packet() throws Exception {
        SmartPacket.serialize(model);
    }

    @Benchmark
    public void sb_stream() throws Exception {
        STREAM.serialize(model);
    }

}
