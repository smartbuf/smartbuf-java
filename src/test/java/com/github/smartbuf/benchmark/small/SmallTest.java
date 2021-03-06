package com.github.smartbuf.benchmark.small;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.smartbuf.SmartBuf;
import com.github.smartbuf.SmartPacket;
import com.github.smartbuf.SmartStream;
import com.github.smartbuf.exception.SmartBufClosedException;
import com.github.smartbuf.reflect.TypeRef;
import org.junit.jupiter.api.Test;
import org.msgpack.MessagePack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

/**
 * json: 172
 * msgpack: 66
 * protobuf: 65
 * kryo: 62
 * sb-packet: 138
 * sb-stream: 142
 * <p>
 * json: 172
 * msgpack: 66
 * protobuf: 65
 * kryo: 62
 * sb-packet: 138
 * sb-stream: 74
 *
 * @author sulin
 * @since 2019-10-28 18:27:56
 */
public class SmallTest {

    static final ObjectMapper MAPPER  = new ObjectMapper();
    static final SmartStream  STREAM  = new SmartStream();
    static final MessagePack  MSGPACK = new MessagePack();
    static final Kryo         KRYO    = new Kryo();

    static final UserModel model = UserModel.random();

    static {
        KRYO.register(UserModel.class);
    }

    @Test
    public void test() throws Exception {
        ByteBufferOutput kryoOutput = new ByteBufferOutput(1024);

        byte[] json = MAPPER.writeValueAsBytes(model);
        byte[] msgpack = MSGPACK.write(model);
        byte[] pb = model.toPB().toByteArray();
        KRYO.writeObject(kryoOutput, model);
        byte[] kryo = kryoOutput.toBytes();
        byte[] packet = SmartPacket.serialize(model);
        byte[] stream = SmallTest.STREAM.serialize(model);

        System.out.println("json: " + json.length);
        System.out.println("kryo: " + kryo.length);
        System.out.println("msgpack: " + msgpack.length);
        System.out.println("protobuf: " + pb.length);
        System.out.println("sb-packet: " + packet.length);
        System.out.println("sb-stream: " + stream.length);

        UserModel newUser = SmallTest.STREAM.deserialize(stream, UserModel.class);
        assert newUser.equals(model);
        newUser = SmartPacket.deserialize(packet, UserModel.class);
        assert newUser.equals(model);
        newUser = MSGPACK.read(msgpack, UserModel.class);
        assert newUser.equals(model);
        newUser = KRYO.readObject(new ByteBufferInput(kryo), UserModel.class);
        assert newUser.equals(model);

        System.out.println();
        kryoOutput.clear();

        json = MAPPER.writeValueAsBytes(model);
        msgpack = MSGPACK.write(model);
        pb = model.toPB().toByteArray();
        KRYO.writeObject(kryoOutput, model);
        kryo = kryoOutput.toBytes();
        packet = SmartPacket.serialize(model);
        stream = SmallTest.STREAM.serialize(model);

        System.out.println("json: " + json.length);
        System.out.println("msgpack: " + msgpack.length);
        System.out.println("protobuf: " + pb.length);
        System.out.println("kryo: " + kryo.length);
        System.out.println("sb-packet: " + packet.length);
        System.out.println("sb-stream: " + stream.length);

        newUser = SmallTest.STREAM.deserialize(stream, UserModel.class);
        assert newUser.equals(model);
        newUser = SmartPacket.deserialize(packet, UserModel.class);
        assert newUser.equals(model);
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

        newUser = buf.read(bis, new TypeRef<UserModel>() {
        });
        assert newUser.equals(model);

        try {
            buf.readObject(bis);
            assert false;
        } catch (Exception e) {
            assert e instanceof EOFException;
        }

        buf.close();

        try {
            buf.read(bis, new TypeRef<UserModel>() {
            });
            assert false;
        } catch (Exception e) {
            assert e instanceof SmartBufClosedException;
        }

        try {
            buf.write(model, bos);
            assert false;
        } catch (Exception e) {
            assert e instanceof SmartBufClosedException;
        }
    }

}
