package com.github.sisyphsu.nakedata.test;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.nakedata.proto.MultiMsg;
import com.github.sisyphsu.nakedata.proto.SingleMsg;
import com.github.sisyphsu.nakedata.proto.UserDetail;
import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Test ProtoBuf
 * <p>
 * 压缩率相对非常高的ProtoBuf，仍然需要添加一些描述性字段，尤其是针对成员较多的Object，每个field都需要额外2-byte的元数据，这一块可以优化掉。
 *
 * @author sulin
 * @since 2019-04-23 20:32:43
 */
@Slf4j
public class ProtoTest {

    @Test
    public void testNormal() {
        UserDetail normal = UserDetail.newBuilder()
                .setId(1000000)
                .setAge(30)
                .setNickname("helloworld")
                .setAvatarUrl("http://bing.com")
                .setCreateTime(System.currentTimeMillis())
                .setUpdateTime(System.currentTimeMillis())
                .build();
        byte[] normalData = normal.toByteArray();
        log.info("protobuf normal: {}", normalData.length);

        UserDetail large = UserDetail.newBuilder()
                .setId(Long.MAX_VALUE)
                .setAge(Integer.MAX_VALUE)
                .setNickname("helloworld")
                .setAvatarUrl("http://bing.com")
                .setCreateTime(Long.MAX_VALUE)
                .setUpdateTime(Long.MAX_VALUE)
                .build();
        byte[] largeData = large.toByteArray();
        log.info("protobuf large: {}", largeData.length);
        /*
            protobuf normal: 49
            protobuf large: 65
            pure data: 53
         */
    }

    @Test
    public void testPure() {
        SingleMsg msg = SingleMsg.newBuilder().setMsg("hello").build();
        log.info("protobuf: {}", msg.toByteArray().length);
        // protobuf: 7
        // pure data: 5
    }

    @Test
    public void testMulti() {
        MultiMsg msg = MultiMsg.newBuilder()
                .setMsg1("hello1")
                .setMsg2("hello2")
                .setMsg3("hello3")
                .setMsg4("hello4")
                .setMsg5("hello5")
                .build();
        log.info("protobuf: {}", msg.toByteArray().length);
        // protobuf: 40
        // pure data: 30
    }

    @Test
    public void testBenchmark() {
        long times = 1000000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            MultiMsg msg = MultiMsg.newBuilder()
                    .setMsg1("hello1")
                    .setMsg2("hello2")
                    .setMsg3("hello3")
                    .setMsg4("hello4")
                    .setMsg5("hello5")
                    .build();
            msg.toByteArray();
        }
        long cost = System.currentTimeMillis() - startTime;
        log.info("protobuf toByteArray {} times, cost: {}ms, {}us", times, cost, cost * 1000.0 / times);

        // protobuf toByteArray 1000000 times, cost: 624ms, 0.624us
        // protobuf的特点在于build速度慢，toByteArray快，所以测试性能时不能复用MultiMsg
    }

    @Test
    public void testJackson() {
        ObjectMapper mapper = new ObjectMapper();
        long times = 1000000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            MultiMsg2 msg = MultiMsg2.builder()
                    .msg1("hello1")
                    .msg2("hello2")
                    .msg3("hello3")
                    .msg4("hello4")
                    .msg5("hello5")
                    .build();
            mapper.valueToTree(msg);
        }
        long cost = System.currentTimeMillis() - startTime;
        log.info("JsonNode toTree {} times, cost: {}ms, {}us", times, cost, cost * 1000.0 / times);

        // JsonNode toTree 1000000 times, cost: 511ms, 0.511us
    }

    @Test
    public void testGson() {
        Gson gson = new Gson();
        long times = 1000000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            MultiMsg2 msg = MultiMsg2.builder()
                    .msg1("hello1")
                    .msg2("hello2")
                    .msg3("hello3")
                    .msg4("hello4")
                    .msg5("hello5")
                    .build();
            gson.toJsonTree(msg);
        }
        long cost = System.currentTimeMillis() - startTime;
        log.info("Gson toJsonTree {} times, cost: {}ms, {}us", times, cost, cost * 1000.0 / times);

        // Gson toJsonTree 1000000 times, cost: 529ms, 0.529us
    }

    @Test
    public void testFastjson() {
        long times = 1000000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            MultiMsg2 msg = MultiMsg2.builder()
                    .msg1("hello1")
                    .msg2("hello2")
                    .msg3("hello3")
                    .msg4("hello4")
                    .msg5("hello5")
                    .build();
            JSON.toJSON(msg);
        }
        long cost = System.currentTimeMillis() - startTime;
        log.info("Fastjson toJSON {} times, cost: {}ms, {}us", times, cost, cost * 1000.0 / times);

        // Fastjson toJSON 1000000 times, cost: 792ms, 0.792us
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
