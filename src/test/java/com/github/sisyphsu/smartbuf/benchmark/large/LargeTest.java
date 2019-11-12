package com.github.sisyphsu.smartbuf.benchmark.large;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.smartbuf.SmartPacket;
import com.github.sisyphsu.smartbuf.SmartStream;
import com.github.sisyphsu.smartbuf.reflect.TypeRef;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * json: 64850
 * protobuf: 23755
 * packet: 20950
 * stream: 20960
 * <p>
 * json: 64850
 * protobuf: 23755
 * packet: 20950
 * stream: 19854
 *
 * @author sulin
 * @since 2019-11-10 15:45:57
 */
public class LargeTest {

    static final ObjectMapper     mapper = new ObjectMapper();
    static final String           json;
    static final List<TrendModel> trends;
    static final SmartStream      stream = new SmartStream();

    static {
        try {
            InputStream is = LargeTest.class.getClassLoader().getResourceAsStream("large.json");

            json = StringUtils.join(IOUtils.readLines(is, StandardCharsets.UTF_8), "");

            trends = mapper.readValue(json, new TypeReference<List<TrendModel>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test() throws Exception {
        byte[] jsonBytes = mapper.writeValueAsBytes(trends);
        byte[] pbBytes = Large.Trends.newBuilder().addAllTrends(trends.stream().map(TrendModel::toPB).collect(Collectors.toList())).build().toByteArray();
        byte[] packetBytes = SmartPacket.serialize(trends);
        byte[] streamBytes = stream.serialize(trends);

        System.out.println("json: " + jsonBytes.length);
        System.out.println("protobuf: " + pbBytes.length);
        System.out.println("packet: " + packetBytes.length);
        System.out.println("stream: " + streamBytes.length);

        List<TrendModel> newTrends = SmartPacket.deserialize(packetBytes, new TypeRef<List<TrendModel>>() {
        });
        assert newTrends.equals(trends);
        newTrends = stream.deserialize(streamBytes, new TypeRef<List<TrendModel>>() {
        });
        assert newTrends.equals(trends);

        System.out.println();

        jsonBytes = mapper.writeValueAsBytes(trends);
        pbBytes = Large.Trends.newBuilder().addAllTrends(trends.stream().map(TrendModel::toPB).collect(Collectors.toList())).build().toByteArray();
        packetBytes = SmartPacket.serialize(trends);
        streamBytes = stream.serialize(trends);

        System.out.println("json: " + jsonBytes.length);
        System.out.println("protobuf: " + pbBytes.length);
        System.out.println("packet: " + packetBytes.length);
        System.out.println("stream: " + streamBytes.length);

        newTrends = SmartPacket.deserialize(packetBytes, new TypeRef<List<TrendModel>>() {
        });
        assert newTrends.equals(trends);
        newTrends = stream.deserialize(streamBytes, new TypeRef<List<TrendModel>>() {
        });
        assert newTrends.equals(trends);
    }

}
