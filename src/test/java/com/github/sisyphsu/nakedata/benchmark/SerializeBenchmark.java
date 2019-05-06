package com.github.sisyphsu.nakedata.benchmark;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.nakedata.proto.MultiMsg;
import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * 测试ProtoBuf、Jackson、Gson、Fastjson的序列化性能。
 * <p>
 * protobuf的特点在于build速度慢，toByteArray快，所以测试性能时不能复用MultiMsg
 * <p>
 * 第一轮测试：
 * Benchmark                    Mode  Cnt  Score   Error  Units
 * SerializeTest.testFastjson   avgt    6  0.508 ± 0.055  us/op
 * SerializeTest.testGson       avgt    6  0.337 ± 0.048  us/op
 * SerializeTest.testJackson    avgt    6  0.264 ± 0.091  us/op
 * SerializeTest.testProtobuf   avgt    6  0.354 ± 0.125  us/op
 * <p>
 * 第二轮测试：
 * Benchmark                   Mode  Cnt  Score   Error  Units
 * SerializeTest.testFastjson  avgt    9  0.490 ± 0.005  us/op
 * SerializeTest.testGson      avgt    9  0.316 ± 0.002  us/op
 * SerializeTest.testJackson   avgt    9  0.253 ± 0.021  us/op
 * SerializeTest.testProtobuf  avgt    9  0.302 ± 0.005  us/op
 * <p>
 * 对于普通的对象而言，200~400毫秒的耗时就足够了，因此在这种算法里面中，应该尽量少地使用hash表这种数据结构。
 *
 * @author sulin
 * @since 2019-05-02 19:11:28
 */
@Warmup(iterations = 2, time = 2)
@BenchmarkMode(Mode.AverageTime)
@Fork(3)
@Measurement(iterations = 3, time = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class SerializeBenchmark {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Gson gson = new Gson();

    @Benchmark
    public void testProtobuf() {
        MultiMsg msg = MultiMsg.newBuilder()
                .setMsg1("hello1")
                .setMsg2("hello2")
                .setMsg3("hello3")
                .setMsg4("hello4")
                .setMsg5("hello5")
                .build();
        msg.toByteArray();
    }

    @Benchmark
    public void testJackson() {
        MultiMsg2 msg = MultiMsg2.builder()
                .msg1("hello1")
                .msg2("hello2")
                .msg3("hello3")
                .msg4("hello4")
                .msg5("hello5")
                .build();
        mapper.valueToTree(msg);
    }

    @Benchmark
    public void testGson() {
        MultiMsg2 msg = MultiMsg2.builder()
                .msg1("hello1")
                .msg2("hello2")
                .msg3("hello3")
                .msg4("hello4")
                .msg5("hello5")
                .build();
        gson.toJsonTree(msg);
    }

    @Benchmark
    public void testFastjson() {
        MultiMsg2 msg = MultiMsg2.builder()
                .msg1("hello1")
                .msg2("hello2")
                .msg3("hello3")
                .msg4("hello4")
                .msg5("hello5")
                .build();
        JSON.toJSON(msg);
    }

    @Data
    @Builder
    public static class MultiMsg2 {
        private String msg1;
        private String msg2;
        private String msg3;
        private String msg4;
        private String msg5;
    }

}
