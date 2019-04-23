package com.github.sisyphsu.nakedata.test;

import com.github.sisyphsu.nakedata.proto.MultiMsg;
import com.github.sisyphsu.nakedata.proto.SingleMsg;
import com.github.sisyphsu.nakedata.proto.UserDetail;
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

}
