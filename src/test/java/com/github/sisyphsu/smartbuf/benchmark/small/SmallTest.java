package com.github.sisyphsu.smartbuf.benchmark.small;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.smartbuf.SmartPacket;
import com.github.sisyphsu.smartbuf.SmartStream;
import org.junit.jupiter.api.Test;

/**
 * First time:
 * json: 171
 * protobuf: 67
 * packet: 138
 * stream: 139
 * <p>
 * Following:
 * json: 171
 * protobuf: 67
 * packet: 138
 * stream: 71
 *
 * @author sulin
 * @since 2019-10-28 18:27:56
 */
public class SmallTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final UserModel USER = UserModel.random();

    private static final SmartStream STREAM = new SmartStream();

    @Test
    public void json() throws Exception {
        byte[] json = OBJECT_MAPPER.writeValueAsBytes(USER);
        byte[] packet = SmartPacket.serialize(USER);
        byte[] stream = STREAM.serialize(USER);
        byte[] pb = USER.toPB().toByteArray();

        System.out.println("json: " + json.length);
        System.out.println("packet: " + packet.length);
        System.out.println("stream: " + stream.length);
        System.out.println("protobuf: " + pb.length);

        System.out.println();

        json = OBJECT_MAPPER.writeValueAsBytes(USER);
        packet = SmartPacket.serialize(USER);
        stream = STREAM.serialize(USER);
        pb = USER.toPB().toByteArray();

        System.out.println("json: " + json.length);
        System.out.println("protobuf: " + pb.length);
        System.out.println("packet: " + packet.length);
        System.out.println("stream: " + stream.length);

        System.out.println();

        json = OBJECT_MAPPER.writeValueAsBytes(USER);
        packet = SmartPacket.serialize(USER);
        stream = STREAM.serialize(USER);
        pb = USER.toPB().toByteArray();

        System.out.println("json: " + json.length);
        System.out.println("protobuf: " + pb.length);
        System.out.println("packet: " + packet.length);
        System.out.println("stream: " + stream.length);
    }

}
