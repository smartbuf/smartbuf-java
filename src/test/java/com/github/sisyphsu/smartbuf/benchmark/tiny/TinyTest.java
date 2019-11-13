package com.github.sisyphsu.smartbuf.benchmark.tiny;

import org.junit.jupiter.api.Test;

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
