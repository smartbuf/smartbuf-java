package com.github.sisyphsu.canoe.benchmark.medium;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.canoe.CanoePacket;
import com.github.sisyphsu.canoe.CanoeStream;
import org.junit.jupiter.api.Test;

/**
 * json: 1874
 * protobuf: 976
 * packet: 1012
 * stream: 844
 *
 * @author sulin
 * @since 2019-10-31 20:32:32
 */
public class MediumTest {

    static ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void test() throws Exception {
        UserModel model = UserModel.random();

        CanoeStream stream = new CanoeStream();
        stream.serialize(model); // sync context previously

        byte[] jsonBytes = MAPPER.writeValueAsBytes(model);
        byte[] pbBytes = model.toUser().toByteArray();
        byte[] packetBytes = CanoePacket.serialize(model);
        byte[] streamBytes = stream.serialize(model);

        System.out.println("json: " + jsonBytes.length);
        System.out.println("protobuf: " + pbBytes.length);
        System.out.println("packet: " + packetBytes.length);
        System.out.println("stream: " + streamBytes.length);
    }

}
