package com.github.sisyphsu.smartbuf.benchmark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.smartbuf.utils.CodecUtils;
import com.github.sisyphsu.smartbuf.SmartPacket;
import com.github.sisyphsu.smartbuf.node.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark            Mode  Cnt     Score     Error  Units
 * NormalTest.json      avgt    6  1313.661 ± 207.883  ns/op
 * NormalTest.smartbuf  avgt    6  2369.977 ± 163.346  ns/op
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

    private static ObjectMapper MAPPER = new ObjectMapper();
    private static List<Tag>    tags   = new ArrayList<>();

    static Node node;

    static {
        for (int i = 0; i < 16; i++) {
            tags.add(new Tag(i, "Tag" + i));
        }
        node = CodecUtils.convert(tags, Node.class);
    }

    @Benchmark
    public void json() throws JsonProcessingException {
        MAPPER.writeValueAsBytes(tags);
    }

    @Benchmark
    public void smartbuf() throws IOException {
        SmartPacket.serialize(tags);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tag {
        private int    code;
        private String msg;
    }

}
