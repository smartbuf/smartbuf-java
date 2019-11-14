package com.github.smartbuf.utils;

import java.nio.charset.Charset;

/**
 * UTF8Utils wraps encode and decode functions for utf-8 charset,
 * it provides higher performance to convert String as byte[]
 *
 * @author sulin
 * @since 2019-11-05 19:51:53
 */
public final class UTF8Utils {

    public static final byte REPL = 63;

    private UTF8Utils() {
    }

    /**
     * Encode the speicifed CharSequence into byte[], it works like {@link String#getBytes(Charset)}
     *
     * @param cs The original string to encode
     * @return Encoding result
     */
    public static byte[] encode(CharSequence cs) {
        byte[] buf = new byte[cs.length() * 3];
        int size = encode(cs, buf, 0);
        byte[] result = new byte[size];
        System.arraycopy(buf, 0, result, 0, size);
        return result;
    }

    /**
     * Use UTF-8 encode the specified {@link CharSequence} to bytes, and write them into the specified buf
     *
     * @param str       The original string to encode
     * @param buf       The buffer to accept encoding result
     * @param bufOffset buf's write offset
     * @return Final position of writing
     */
    public static int encode(CharSequence str, byte[] buf, int bufOffset) {
        int charOff = 0;
        int charNum = str.length();

        // encode the beginning ascii chars [0, 128)
        for (char c; charOff < charNum; charOff++) {
            if ((c = str.charAt(charOff)) >= 128) {
                break;
            }
            buf[bufOffset++] = (byte) c;
        }

        while (charOff < charNum) {
            char c = str.charAt(charOff++);
            if (c < 128) {
                buf[bufOffset++] = (byte) c;
            } else if (c < 2048) {
                buf[bufOffset++] = (byte) (192 | c >> 6);
                buf[bufOffset++] = (byte) (128 | c & 63);
            } else if (Character.isSurrogate(c)) {

                int code = parse(str, c, charOff - 1, charNum);
                if (code < 0) {
                    buf[bufOffset++] = REPL;
                } else {
                    buf[bufOffset++] = (byte) (240 | code >> 18);
                    buf[bufOffset++] = (byte) (128 | code >> 12 & 63);
                    buf[bufOffset++] = (byte) (128 | code >> 6 & 63);
                    buf[bufOffset++] = (byte) (128 | code & 63);
                    ++charOff;
                }
            } else {
                buf[bufOffset++] = (byte) (224 | c >> 12);
                buf[bufOffset++] = (byte) (128 | c >> 6 & 63);
                buf[bufOffset++] = (byte) (128 | c & 63);
            }
        }

        return bufOffset;
    }

    /**
     * surrogate char parser, mainly copied from {@link sun.nio.cs.Surrogate.Parser#parse}
     */
    private static int parse(CharSequence cs, char c, int pos, int limit) {
        int character;
        if (Character.isHighSurrogate(c)) {
            if (limit - pos < 2) {
                return -1;
            } else {
                char var5 = cs.charAt(pos + 1);
                if (Character.isLowSurrogate(var5)) {
                    character = Character.toCodePoint(c, var5);
                    return character;
                } else {
                    return -1;
                }
            }
        } else {
            return -1;
        }
    }
}
