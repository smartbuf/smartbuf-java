package com.github.sisyphsu.canoe.benchmark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.canoe.Canoe;
import com.github.sisyphsu.canoe.CanoePacket;
import com.github.sisyphsu.canoe.node.Node;
import com.github.sisyphsu.canoe.transport.Output;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TODO Find out why List is so slow...
 * <p>
 * Benchmark         Mode  Cnt     Score    Error  Units
 * NormalTest.canoe  avgt    6  4043.888 ± 55.323  ns/op
 * NormalTest.json   avgt    6  1059.555 ± 72.362  ns/op
 *
 * @author sulin
 * @since 2019-10-31 21:21:49
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class NormalTest {

    private static ObjectMapper  MAPPER        = new ObjectMapper();
    private static List<Tag>     tags          = new ArrayList<>();

    static Output output = new Output(false);

    static Node node;

    static {
        for (int i = 0; i < 16; i++) {
            tags.add(new Tag(i, "Tag" + i));
        }
        node = Canoe.CODEC.convert(tags, Node.class);
    }

    @Benchmark
    public void json() throws JsonProcessingException {
        MAPPER.writeValueAsBytes(tags);
    }

    @Benchmark
    public void canoe() throws IOException {
        CanoePacket.serialize(tags);
    }

    @Benchmark
    public void other() {
//        Canoe.CODEC.convert(tags, Node.class); // 1367ns

//        output.testScan(node); // 1027ns

//        arrayNodeCodec.toNode(tags); // 1347ns

//        tags.forEach(t -> Canoe.CODEC.convert(t, Node.class)); // 960ns

//        tags.forEach(t -> beanNodeCodec.toNode(t)); // 750ns
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tag {
        private int    code;
        private String msg;
    }

}
