package com.github.sisyphsu.smartbuf.benchmark.tiny;

import com.github.sisyphsu.smartbuf.SmartStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * ProtoBuf: [8, -23, 7, 26, 5, 104, 101, 108, 108, 111, 56, -112, 78]
 *
 * @author sulin
 * @since 2019-11-12 20:16:49
 */
public class TinyTest {

    @Test
    public void testPB() {
        Tiny.User user = Tiny.User.newBuilder()
            .setId(1001)
            .setName("hello")
            .setTime(10000L)
            .build();

        System.out.println(Integer.toHexString(0b1101001));
        System.out.println(Integer.toHexString(0b0010000));
        System.out.println(Integer.toBinaryString(1001));
        System.out.println(Integer.toBinaryString(10000));
        System.out.println(bytesToHex(user.getName().getBytes()));

        System.out.println("ProtoBuf: " + bytesToHex(user.toByteArray()));
    }

    @Test
    public void testSB() throws IOException {
        UserModel user = new UserModel(1001, "hello", 10000L);
        SmartStream stream = new SmartStream();

        byte[] streamData = stream.serialize(user);
        System.out.println(bytesToHex(streamData));

        streamData = stream.serialize(user);
        System.out.println(bytesToHex(streamData));

        System.out.println(bytesToHex("hello".getBytes()));
        System.out.println(bytesToHex("id".getBytes()));
        System.out.println(bytesToHex("name".getBytes()));
        System.out.println(bytesToHex("time".getBytes()));

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserModel {
        private int    id;
        private String name;
        private long   time;
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            int v = aByte & 0xFF;
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("0x");
            sb.append(HEX_ARRAY[v >>> 4]);
            sb.append(HEX_ARRAY[v & 0x0F]);
        }
        return "[" + sb + "]";
    }

}
