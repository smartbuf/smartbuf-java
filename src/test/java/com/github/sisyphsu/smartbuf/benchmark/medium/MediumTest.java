package com.github.sisyphsu.smartbuf.benchmark.medium;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.smartbuf.SmartPacket;
import com.github.sisyphsu.smartbuf.SmartStream;
import com.github.sisyphsu.smartbuf.transport.Input;
import com.github.sisyphsu.smartbuf.utils.CodecUtils;
import org.junit.jupiter.api.Test;

/**
 * json: 1876
 * protobuf: 973
 * packet: 1012
 * stream: 1020
 *
 * json: 1876
 * protobuf: 973
 * packet: 1012
 * stream: 851
 *
 * @author sulin
 * @since 2019-10-31 20:32:32
 */
public class MediumTest {

    static ObjectMapper mapper = new ObjectMapper();
    static SmartStream  stream = new SmartStream();
    static UserModel    model  = UserModel.random();

    @Test
    public void test() throws Exception {
        byte[] jsonBytes = mapper.writeValueAsBytes(model);
        byte[] pbBytes = model.toUser().toByteArray();
        byte[] packetBytes = SmartPacket.serialize(model);
        byte[] streamBytes = stream.serialize(model);

        System.out.println("json: " + jsonBytes.length);
        System.out.println("protobuf: " + pbBytes.length);
        System.out.println("packet: " + packetBytes.length);
        System.out.println("stream: " + streamBytes.length);

        UserModel newUser = stream.deserialize(streamBytes, UserModel.class);
        assert newUser.equals(model);
        newUser = SmartPacket.deserialize(packetBytes, UserModel.class);
        assert newUser.equals(model);

        System.out.println();

        jsonBytes = mapper.writeValueAsBytes(model);
        pbBytes = model.toUser().toByteArray();
        packetBytes = SmartPacket.serialize(model);
        streamBytes = stream.serialize(model);

        System.out.println("json: " + jsonBytes.length);
        System.out.println("protobuf: " + pbBytes.length);
        System.out.println("packet: " + packetBytes.length);
        System.out.println("stream: " + streamBytes.length);

        newUser = stream.deserialize(streamBytes, UserModel.class);
        assert newUser.equals(model);
        newUser = SmartPacket.deserialize(packetBytes, UserModel.class);
        assert newUser.equals(model);

        Input input = new Input(false);
        CodecUtils.convert(input.read(packetBytes), UserModel.class);
    }

}
