package com.github.sisyphsu.smartbuf.benchmark.large;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.smartbuf.SmartPacket;
import com.github.sisyphsu.smartbuf.SmartStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * json: 64861
 * kryo: 24628
 * msgpack: 23267
 * protobuf: 23755
 * sb-packet: 20960
 * sb-stream: 20972
 * <p>
 * json: 64861
 * kryo: 49256
 * msgpack: 23267
 * protobuf: 23755
 * sb-packet: 20960
 * sb-stream: 19857
 *
 * @author sulin
 * @since 2019-11-10 15:45:57
 */
public class LargeTest {

    static final ObjectMapper MAPPER  = new ObjectMapper();
    static final SmartStream  STREAM  = new SmartStream();
    static final MessagePack  MSGPACK = new MessagePack();
    static final Kryo         KRYO    = new Kryo();

    static final TrendsModel trendsModel;

    static {
        KRYO.register(TrendsModel.class);
        KRYO.register(TrendModel.class);
        KRYO.register(TrendModel.Entities.class);
        KRYO.register(TrendModel.Description.class);
        KRYO.register(TrendModel.Urls.class);
        KRYO.register(TrendModel.Url.class);

        try {
            InputStream is = LargeTest.class.getClassLoader().getResourceAsStream("large.json");

            String json = StringUtils.join(IOUtils.readLines(is, StandardCharsets.UTF_8), "");

            List<TrendModel> trends = MAPPER.readValue(json, new TypeReference<List<TrendModel>>() {
            });

            trendsModel = new TrendsModel(trends);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test() throws Exception {
        ByteBufferOutput kryoOutput = new ByteBufferOutput(1 << 20);

        byte[] jsonBytes = MAPPER.writeValueAsBytes(trendsModel);
        byte[] msgpackBytes = MSGPACK.write(trendsModel);
        KRYO.writeObject(kryoOutput, trendsModel);
        byte[] kryoBytes = kryoOutput.toBytes();
        byte[] pbBytes = trendsModel.toPB().toByteArray();
        byte[] packetBytes = SmartPacket.serialize(trendsModel);
        byte[] streamBytes = STREAM.serialize(trendsModel);

        System.out.println("json: " + jsonBytes.length);
        System.out.println("kryo: " + kryoBytes.length);
        System.out.println("msgpack: " + msgpackBytes.length);
        System.out.println("protobuf: " + pbBytes.length);
        System.out.println("sb-packet: " + packetBytes.length);
        System.out.println("sb-stream: " + streamBytes.length);

        TrendsModel newTrends = SmartPacket.deserialize(packetBytes, TrendsModel.class);
        assert newTrends.equals(trendsModel);
        newTrends = STREAM.deserialize(streamBytes, TrendsModel.class);
        assert newTrends.equals(trendsModel);
        newTrends = MSGPACK.read(msgpackBytes, TrendsModel.class);
        assert newTrends.equals(trendsModel);
        newTrends = KRYO.readObject(new ByteBufferInput(kryoBytes), TrendsModel.class);
        assert newTrends.equals(trendsModel);

        System.out.println();

        jsonBytes = MAPPER.writeValueAsBytes(trendsModel);
        msgpackBytes = MSGPACK.write(trendsModel);
        KRYO.writeObject(kryoOutput, trendsModel);
        kryoBytes = kryoOutput.toBytes();
        pbBytes = trendsModel.toPB().toByteArray();
        packetBytes = SmartPacket.serialize(trendsModel);
        streamBytes = STREAM.serialize(trendsModel);

        System.out.println("json: " + jsonBytes.length);
        System.out.println("kryo: " + kryoBytes.length);
        System.out.println("msgpack: " + msgpackBytes.length);
        System.out.println("protobuf: " + pbBytes.length);
        System.out.println("sb-packet: " + packetBytes.length);
        System.out.println("sb-stream: " + streamBytes.length);

        newTrends = SmartPacket.deserialize(packetBytes, TrendsModel.class);
        assert newTrends.equals(trendsModel);
        newTrends = STREAM.deserialize(streamBytes, TrendsModel.class);
        assert newTrends.equals(trendsModel);
        newTrends = MSGPACK.read(msgpackBytes, TrendsModel.class);
        assert newTrends.equals(trendsModel);
        newTrends = KRYO.readObject(new ByteBufferInput(kryoBytes), TrendsModel.class);
        assert newTrends.equals(trendsModel);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Message
    public static class TrendsModel {
        private List<TrendModel> trends;

        public Large.Trends toPB() {
            return Large.Trends.newBuilder().addAllTrends(trends.stream().map(TrendModel::toPB).collect(Collectors.toList())).build();
        }
    }
}
