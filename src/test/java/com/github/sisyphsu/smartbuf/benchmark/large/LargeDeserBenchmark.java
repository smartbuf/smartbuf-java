package com.github.sisyphsu.smartbuf.benchmark.large;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.smartbuf.SmartPacket;
import com.github.sisyphsu.smartbuf.SmartStream;
import com.github.sisyphsu.smartbuf.reflect.TypeRef;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Benchmark                      Mode  Cnt       Score       Error  Units
 * LargeDeserBenchmark.json       avgt    6  204493.099 ± 14516.472  ns/op
 * LargeDeserBenchmark.protobuf   avgt    6   48283.187 ±  1613.566  ns/op
 * LargeDeserBenchmark.sb_packet  avgt    6  101545.126 ±  4309.135  ns/op
 * LargeDeserBenchmark.sb_stream  avgt    6   97387.743 ±  1440.772  ns/op
 *
 * @author sulin
 * @since 2019-11-11 10:58:07
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LargeDeserBenchmark {

    static final ObjectMapper     mapper = new ObjectMapper();
    static final List<TrendModel> trends = LargeTest.trends;
    static final SmartStream      stream = new SmartStream();

    static byte[] jsonBytes;
    static byte[] pbBytes;
    static byte[] packetBytes;
    static byte[] streamBytes;

    static {
        try {
            jsonBytes = mapper.writeValueAsBytes(trends);
            pbBytes = Large.Trends.newBuilder().addAllTrends(trends.stream().map(TrendModel::toPB).collect(Collectors.toList())).build().toByteArray();
            packetBytes = SmartPacket.serialize(trends);
            streamBytes = stream.serialize(trends);

            // smartbuf's stream-mode need warm up to avoid sequence error
            stream.deserialize(streamBytes, new TypeRef<List<TrendModel>>() {
            });

            jsonBytes = mapper.writeValueAsBytes(trends);
            pbBytes = Large.Trends.newBuilder().addAllTrends(trends.stream().map(TrendModel::toPB).collect(Collectors.toList())).build().toByteArray();
            packetBytes = SmartPacket.serialize(trends);
            streamBytes = stream.serialize(trends);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public void json() throws Exception {
        mapper.readValue(jsonBytes, new TypeReference<List<TrendModel>>() {
        });
    }

    @Benchmark
    public void protobuf() throws Exception {
        Large.Trends.parseFrom(pbBytes);
    }

    @Benchmark
    public void sb_packet() throws Exception {
        SmartPacket.deserialize(packetBytes, new TypeRef<List<TrendModel>>() {
        });
    }

    @Benchmark
    public void sb_stream() throws Exception {
        stream.deserialize(streamBytes, new TypeRef<List<TrendModel>>() {
        });
    }

}
