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

    private static final UserModel user = UserModel.random();

    private static final SmartStream STREAM = new SmartStream();

    @Test
    public void json() throws Exception {
        byte[] json = OBJECT_MAPPER.writeValueAsBytes(user);
        byte[] pb = user.toPB().toByteArray();
        byte[] packet = SmartPacket.serialize(user);
        byte[] stream = STREAM.serialize(user);

        System.out.println("json: " + json.length);
        System.out.println("protobuf: " + pb.length);
        System.out.println("packet: " + packet.length);
        System.out.println("stream: " + stream.length);

        UserModel newUser = STREAM.deserialize(stream, UserModel.class);
        assert newUser.equals(user);
        newUser = SmartPacket.deserialize(packet, UserModel.class);
        assert newUser.equals(user);

        System.out.println();

        json = OBJECT_MAPPER.writeValueAsBytes(user);
        pb = user.toPB().toByteArray();
        packet = SmartPacket.serialize(user);
        stream = STREAM.serialize(user);

        System.out.println("json: " + json.length);
        System.out.println("protobuf: " + pb.length);
        System.out.println("packet: " + packet.length);
        System.out.println("stream: " + stream.length);

        newUser = STREAM.deserialize(stream, UserModel.class);
        assert newUser.equals(user);
        newUser = SmartPacket.deserialize(packet, UserModel.class);
        assert newUser.equals(user);
    }

}
