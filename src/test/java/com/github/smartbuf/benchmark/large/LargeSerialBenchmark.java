package com.github.smartbuf.benchmark.large;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.smartbuf.SmartPacket;
import com.github.smartbuf.SmartStream;
import org.msgpack.MessagePack;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark                       Mode  Cnt       Score      Error  Units
 * LargeSerialBenchmark.json       avgt    9  126749.482 ± 3173.811  ns/op
 * LargeSerialBenchmark.kryo       avgt    9  134176.479 ± 1208.534  ns/op
 * LargeSerialBenchmark.msgpack    avgt    9   70548.730 ± 3072.337  ns/op
 * LargeSerialBenchmark.protobuf   avgt    9   97395.157 ± 1056.336  ns/op
 * LargeSerialBenchmark.sb_packet  avgt    9   81460.734 ± 1396.504  ns/op
 * LargeSerialBenchmark.sb_stream  avgt    9   77611.622 ±  430.881  ns/op
 *
 * @author sulin
 * @since 2019-11-10 16:12:28
 */
@Warmup(iterations = 2, time = 2)
@Fork(3)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LargeSerialBenchmark {

    static final LargeTest.TrendsModel trends = LargeTest.trendsModel;

    static final ObjectMapper MAPPER  = new ObjectMapper();
    static final SmartStream  STREAM  = new SmartStream();
    static final MessagePack  MSGPACK = new MessagePack();
    static final Kryo         KRYO    = new Kryo();

    static ByteBufferOutput kryoOutput = new ByteBufferOutput(1 << 20);

    @Benchmark
    public void json() throws Exception {
        MAPPER.writeValueAsBytes(trends);
    }

    @Benchmark
    public void kryo() {
        KRYO.writeObject(kryoOutput, trends);
        kryoOutput.toBytes();
        kryoOutput.clear();
    }

    @Benchmark
    public void msgpack() throws IOException {
        MSGPACK.write(trends);
    }

    @Benchmark
    public void protobuf() {
        trends.toPB().toByteArray();
    }

    @Benchmark
    public void sb_packet() throws Exception {
        SmartPacket.serialize(trends);
    }

    @Benchmark
    public void sb_stream() throws Exception {
        STREAM.serialize(trends);
    }

}
