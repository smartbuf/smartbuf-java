package com.github.smartbuf.benchmark.large;

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
 * Benchmark                      Mode  Cnt       Score       Error  Units
 * LargeDeserBenchmark.json       avgt    9  215988.530 ± 14369.676  ns/op
 * LargeDeserBenchmark.kryo       avgt    9  144229.596 ±  8665.693  ns/op
 * LargeDeserBenchmark.msgpack    avgt    9   91418.850 ±  2470.538  ns/op
 * LargeDeserBenchmark.protobuf   avgt    9   49573.240 ±   483.989  ns/op
 * LargeDeserBenchmark.sb_packet  avgt    9  105982.178 ±  6484.683  ns/op
 * LargeDeserBenchmark.sb_stream  avgt    9   98243.964 ±  2018.606  ns/op
 *
 * @author sulin
 * @since 2019-11-11 10:58:07
 */
@Warmup(iterations = 2, time = 2)
@Fork(3)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LargeDeserBenchmark {

    static final LargeTest.TrendsModel trends = LargeTest.trendsModel;

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
        ByteBufferOutput kryoOutput = new ByteBufferOutput(1 << 20);

        try {
            KRYO.writeObject(kryoOutput, trends);
            kryoBytes = kryoOutput.toBytes();
            jsonBytes = MAPPER.writeValueAsBytes(trends);
            msgpackBytes = MSGPACK.write(trends);
            pbBytes = trends.toPB().toByteArray();
            packetBytes = SmartPacket.serialize(trends);
            streamBytes = STREAM.serialize(trends);

            // smartbuf's stream-mode need warm up to avoid sequence error
            STREAM.deserialize(streamBytes, LargeTest.TrendsModel.class);

            jsonBytes = MAPPER.writeValueAsBytes(trends);
            pbBytes = trends.toPB().toByteArray();
            packetBytes = SmartPacket.serialize(trends);
            streamBytes = STREAM.serialize(trends);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public void json() throws Exception {
        MAPPER.readValue(jsonBytes, LargeTest.TrendsModel.class);
    }

    @Benchmark
    public void kryo() {
        KRYO.readObject(new ByteBufferInput(kryoBytes), LargeTest.TrendsModel.class);
    }

    @Benchmark
    public void msgpack() throws IOException {
        MSGPACK.read(msgpackBytes, LargeTest.TrendsModel.class);
    }

    @Benchmark
    public void protobuf() throws Exception {
        Large.Trends.parseFrom(pbBytes);
    }

    @Benchmark
    public void sb_packet() throws Exception {
        SmartPacket.deserialize(packetBytes, LargeTest.TrendsModel.class);
    }

    @Benchmark
    public void sb_stream() throws Exception {
        STREAM.deserialize(streamBytes, LargeTest.TrendsModel.class);
    }

}
