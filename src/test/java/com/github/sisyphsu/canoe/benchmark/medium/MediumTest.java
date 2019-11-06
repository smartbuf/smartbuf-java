package com.github.sisyphsu.canoe.benchmark.medium;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.canoe.CanoePacket;
import com.github.sisyphsu.canoe.CanoeStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * First time:
 * json: 1874
 * protobuf: 975
 * packet: 1018
 * stream: 1022
 * <p>
 * Following:
 * json: 1874
 * protobuf: 975
 * packet: 1018
 * stream: 850
 *
 * @author sulin
 * @since 2019-10-31 20:32:32
 */
public class MediumTest {

    static ObjectMapper MAPPER = new ObjectMapper();
    static CanoeStream  stream = new CanoeStream();
    static UserModel    model  = UserModel.random();

    @Test
    public void test() throws Exception {


        byte[] jsonBytes = MAPPER.writeValueAsBytes(model);
        byte[] pbBytes = model.toUser().toByteArray();
        byte[] packetBytes = CanoePacket.serialize(model);
        byte[] streamBytes = stream.serialize(model);

        System.out.println("json: " + jsonBytes.length);
        System.out.println("protobuf: " + pbBytes.length);
        System.out.println("packet: " + packetBytes.length);
        System.out.println("stream: " + streamBytes.length);

        System.out.println();

        jsonBytes = MAPPER.writeValueAsBytes(model);
        pbBytes = model.toUser().toByteArray();
        packetBytes = CanoePacket.serialize(model);
        streamBytes = stream.serialize(model);

        System.out.println("json: " + jsonBytes.length);
        System.out.println("protobuf: " + pbBytes.length);
        System.out.println("packet: " + packetBytes.length);
        System.out.println("stream: " + streamBytes.length);
    }

    @Test
    public void test2() throws IOException {
        stream.serialize(model);
    }

}
