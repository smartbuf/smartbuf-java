package com.github.smartbuf.benchmark.small;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.smartbuf.SmartPacket;
import com.github.smartbuf.SmartStream;
import org.msgpack.MessagePack;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark                       Mode  Cnt     Score    Error  Units
 * SmallSerialBenchmark.json       avgt    6   767.941 ± 20.675  ns/op
 * SmallSerialBenchmark.kryo       avgt    6   303.953 ±  5.110  ns/op
 * SmallSerialBenchmark.msgpack    avgt    6  1458.512 ± 47.205  ns/op
 * SmallSerialBenchmark.protobuf   avgt    6   210.945 ± 20.209  ns/op
 * SmallSerialBenchmark.sb_packet  avgt    6   635.780 ±  8.427  ns/op
 * SmallSerialBenchmark.sb_stream  avgt    6   397.773 ± 12.083  ns/op
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

    static final UserModel user = UserModel.random();

    static final ObjectMapper MAPPER  = new ObjectMapper();
    static final SmartStream  STREAM  = new SmartStream();
    static final MessagePack  MSGPACK = new MessagePack();
    static final Kryo         KRYO    = new Kryo();

    static final ByteBufferOutput kryoOutput = new ByteBufferOutput(1024);

    static {
        KRYO.register(UserModel.class);
    }

    @Benchmark
    public void json() throws JsonProcessingException {
        MAPPER.writeValueAsString(user);
    }

    @Benchmark
    public void msgpack() throws Exception {
        MSGPACK.write(user);
    }

    @Benchmark
    public void protobuf() {
        user.toPB().toByteArray();
    }

    @Benchmark
    public void kryo() {
        KRYO.writeObject(kryoOutput, user);
        kryoOutput.toBytes();
        kryoOutput.clear();
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
