package com.github.smartbuf.benchmark.medium;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.smartbuf.SmartBuf;
import com.github.smartbuf.SmartPacket;
import com.github.smartbuf.SmartStream;
import com.github.smartbuf.transport.Input;
import com.github.smartbuf.utils.CodecUtils;
import org.junit.jupiter.api.Test;
import org.msgpack.MessagePack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

/**
 * json: 1875
 * kryo: 930
 * msgpack: 918
 * protobuf: 975
 * packet: 1011
 * stream: 1019
 * <p>
 * json: 1875
 * kryo: 930
 * msgpack: 918
 * protobuf: 975
 * packet: 1011
 * stream: 850
 *
 * @author sulin
 * @since 2019-10-31 20:32:32
 */
public class MediumTest {

    static final ObjectMapper MAPPER  = new ObjectMapper();
    static final SmartStream  STREAM  = new SmartStream();
    static final MessagePack  MSGPACK = new MessagePack();
    static final Kryo         KRYO    = new Kryo();

    static UserModel model = UserModel.random();

    static {
        KRYO.register(UserModel.class);
        KRYO.register(UserModel.Message.class);
        KRYO.register(UserModel.Tag.class);
    }

    @Test
    public void test() throws Exception {
        ByteBufferOutput kryoOutput = new ByteBufferOutput(1024);

        byte[] jsonBytes = MAPPER.writeValueAsBytes(model);
        byte[] msgpack = MSGPACK.write(model);
        KRYO.writeObject(kryoOutput, model);
        byte[] kryo = kryoOutput.toBytes();
        byte[] pbBytes = model.toUser().toByteArray();
        byte[] packetBytes = SmartPacket.serialize(model);
        byte[] streamBytes = STREAM.serialize(model);

        System.out.println("json: " + jsonBytes.length);
        System.out.println("kryo: " + kryo.length);
        System.out.println("msgpack: " + msgpack.length);
        System.out.println("protobuf: " + pbBytes.length);
        System.out.println("packet: " + packetBytes.length);
        System.out.println("stream: " + streamBytes.length);

        UserModel newUser = STREAM.deserialize(streamBytes, UserModel.class);
        assert newUser.equals(model);
        newUser = SmartPacket.deserialize(packetBytes, UserModel.class);
        assert newUser.equals(model);
        newUser = MSGPACK.read(msgpack, UserModel.class);
        assert newUser.equals(model);
        newUser = KRYO.readObject(new ByteBufferInput(kryo), UserModel.class);
        assert newUser.equals(model);

        System.out.println();
        kryoOutput.clear();

        jsonBytes = MAPPER.writeValueAsBytes(model);
        msgpack = MSGPACK.write(model);
        KRYO.writeObject(kryoOutput, model);
        kryo = kryoOutput.toBytes();
        pbBytes = model.toUser().toByteArray();
        packetBytes = SmartPacket.serialize(model);
        streamBytes = STREAM.serialize(model);

        System.out.println("json: " + jsonBytes.length);
        System.out.println("kryo: " + kryo.length);
        System.out.println("msgpack: " + msgpack.length);
        System.out.println("protobuf: " + pbBytes.length);
        System.out.println("packet: " + packetBytes.length);
        System.out.println("stream: " + streamBytes.length);

        newUser = STREAM.deserialize(streamBytes, UserModel.class);
        assert newUser.equals(model);
        newUser = SmartPacket.deserialize(packetBytes, UserModel.class);
        assert newUser.equals(model);
        newUser = MSGPACK.read(msgpack, UserModel.class);
        assert newUser.equals(model);
        newUser = KRYO.readObject(new ByteBufferInput(kryo), UserModel.class);
        assert newUser.equals(model);

        Input input = new Input(false);
        CodecUtils.convert(input.read(packetBytes), UserModel.class);
    }

    @Test
    public void testIOStream() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        SmartBuf buf = new SmartBuf(true);
        buf.write(model, bos);
        buf.write(model, bos);
        buf.write(model, bos);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

        UserModel newUser = buf.read(bis, UserModel.class);
        assert newUser.equals(model);

        newUser = buf.read(bis, UserModel.class);
        assert newUser.equals(model);

        newUser = buf.read(bis, UserModel.class);
        assert newUser.equals(model);

        try {
            buf.readObject(bis);
            assert false;
        } catch (Exception e) {
            assert e instanceof EOFException;
        }
    }

}
