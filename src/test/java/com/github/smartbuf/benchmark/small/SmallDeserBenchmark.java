package com.github.smartbuf.benchmark.small;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.smartbuf.SmartPacket;
import com.github.smartbuf.SmartStream;
import org.msgpack.MessagePack;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark                      Mode  Cnt     Score     Error  Units
 * SmallDeserBenchmark.json       avgt    6   831.411 ± 133.241  ns/op
 * SmallDeserBenchmark.kryo       avgt    6   958.711 ± 901.156  ns/op
 * SmallDeserBenchmark.msgpack    avgt    6  1304.432 ±  21.346  ns/op
 * SmallDeserBenchmark.protobuf   avgt    6   142.961 ±   1.740  ns/op
 * SmallDeserBenchmark.sb_packet  avgt    6   624.565 ±   4.355  ns/op
 * SmallDeserBenchmark.sb_stream  avgt    6   363.003 ±   8.824  ns/op
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

    private static final ObjectMapper mapper  = new ObjectMapper();
    private static final SmartStream  stream  = new SmartStream();
    static final         MessagePack  MSGPACK = new MessagePack();
    static final         Kryo         KRYO    = new Kryo();

    private static final UserModel user = UserModel.random();

    static byte[] jsonBytes;
    static byte[] msgpackBytes;
    static byte[] kryoBytes;
    static byte[] pbBytes;
    static byte[] packetBytes;
    static byte[] streamBytes;

    static {
        try {
            ByteBufferOutput kryoOutput = new ByteBufferOutput(1024);

            KRYO.writeObject(kryoOutput, user);

            jsonBytes = mapper.writeValueAsBytes(user);
            msgpackBytes = MSGPACK.write(user);
            pbBytes = user.toPB().toByteArray();
            kryoBytes = kryoOutput.toBytes();
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
    public void kryo() {
        KRYO.readObject(new ByteBufferInput(kryoBytes), UserModel.class);
    }

    @Benchmark
    public void msgpack() throws Exception {
        MSGPACK.read(msgpackBytes, UserModel.class);
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
