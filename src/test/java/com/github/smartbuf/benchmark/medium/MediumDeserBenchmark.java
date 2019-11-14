package com.github.smartbuf.benchmark.medium;

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
 * Benchmark                       Mode  Cnt      Score     Error  Units
 * MediumDeserBenchmark.json       avgt    6   7040.950 ± 459.725  ns/op
 * MediumDeserBenchmark.kryo       avgt    6   6432.450 ± 731.190  ns/op
 * MediumDeserBenchmark.msgpack    avgt    6  15989.744 ± 208.045  ns/op
 * MediumDeserBenchmark.protobuf   avgt    6   2598.041 ± 135.756  ns/op
 * MediumDeserBenchmark.sb_packet  avgt    6   6819.044 ± 546.147  ns/op
 * MediumDeserBenchmark.sb_stream  avgt    6   5466.728 ± 141.585  ns/op
 *
 * @author sulin
 * @since 2019-11-11 10:53:43
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MediumDeserBenchmark {

    private static final UserModel user = UserModel.random();

    static final ObjectMapper MAPPER  = new ObjectMapper();
    static final SmartStream  STREAM  = new SmartStream();
    static final MessagePack  MSGPACK = new MessagePack();
    static final Kryo         KRYO    = new Kryo();

    static byte[] jsonBytes;
    static byte[] kryoBytes;
    static byte[] msgpackBytes;
    static byte[] pbBytes;
    static byte[] packetBytes;
    static byte[] streamBytes;

    static {
        KRYO.register(UserModel.class);
        KRYO.register(UserModel.Message.class);
        KRYO.register(UserModel.Tag.class);

        ByteBufferOutput kryoOutput = new ByteBufferOutput(1024);
        try {
            KRYO.writeObject(kryoOutput, user);

            jsonBytes = MAPPER.writeValueAsBytes(user);
            msgpackBytes = MSGPACK.write(user);
            kryoBytes = kryoOutput.toBytes();
            pbBytes = user.toUser().toByteArray();
            packetBytes = SmartPacket.serialize(user);
            streamBytes = STREAM.serialize(user);
            // smartbuf's stream-mode need warm up to avoid sequence error
            STREAM.deserialize(streamBytes, UserModel.class);

            jsonBytes = MAPPER.writeValueAsBytes(user);
            pbBytes = user.toUser().toByteArray();
            packetBytes = SmartPacket.serialize(user);
            streamBytes = STREAM.serialize(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public void json() throws Exception {
        MAPPER.readValue(jsonBytes, UserModel.class);
    }

    @Benchmark
    public void msgpack() throws IOException {
        MSGPACK.read(msgpackBytes, UserModel.class);
    }

    @Benchmark
    public void kryo() {
        KRYO.readObject(new ByteBufferInput(kryoBytes), UserModel.class);
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
        STREAM.deserialize(streamBytes, UserModel.class);
    }

}
