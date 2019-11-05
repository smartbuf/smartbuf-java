package com.github.sisyphsu.canoe.utils;

public final class UTF8Encoder {

    private static final byte repl = 63;

    public static byte[] encode(CharSequence cs) {
        byte[] buf = new byte[cs.length() * 3];
        int size = encode(cs, buf, 0);
        byte[] result = new byte[size];
        System.arraycopy(buf, 0, result, 0, size);
        return result;
    }

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
                    buf[bufOffset++] = repl;
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
        } else if (Character.isLowSurrogate(c)) {
            return -1;
        } else {
            character = c;
            return character;
        }
    }
}
