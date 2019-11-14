package com.github.smartbuf.converter.codec;

import com.github.smartbuf.converter.Codec;
import com.github.smartbuf.converter.Converter;
import com.github.smartbuf.reflect.XType;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Codec for `java.io` and `java.nio` packages.
 *
 * @author sulin
 * @since 2019-07-25 14:40:12
 */
public final class IOCodec extends Codec {

    @Converter
    public Charset toCharset(String s) {
        return Charset.forName(s);
    }

    @Converter
    public String toString(Charset c) {
        return c.name();
    }

    @Converter
    public String toString(File file) {
        return file.toString();
    }

    @Converter
    public byte[] toByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int n; (n = is.read(buffer)) >= 0; ) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    @Converter
    public InputStream toInputStream(byte[] bytes, XType type) {
        Class<?> clz = type.getRawType();
        if (clz.isAssignableFrom(ByteArrayInputStream.class)) {
            return new ByteArrayInputStream(bytes);
        }
        if (clz.isAssignableFrom(BufferedInputStream.class)) {
            return new BufferedInputStream(new ByteArrayInputStream(bytes));
        }
        if (clz.isAssignableFrom(DataInputStream.class)) {
            return new DataInputStream(new ByteArrayInputStream(bytes));
        }
        throw new UnsupportedOperationException("unsupported type: " + clz);
    }

    @Converter
    public String toString(Readable reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        CharBuffer buf = CharBuffer.allocate(1024);
        while (reader.read(buf) >= 0) {
            buf.flip();
            while (buf.hasRemaining()) {
                sb.append(buf.get());
            }
            buf.clear();
        }
        return sb.toString();
    }

    @Converter
    public Readable toReadable(String s, XType type) {
        Class<?> clz = type.getRawType();
        if (clz.isAssignableFrom(StringReader.class)) {
            return new StringReader(s);
        }
        if (clz.isAssignableFrom(CharArrayReader.class)) {
            return new CharArrayReader(s.toCharArray());
        }
        if (clz.isAssignableFrom(BufferedReader.class)) {
            return new BufferedReader(new StringReader(s));
        }

        throw new UnsupportedOperationException("Unsupported Readable: " + clz);
    }

}
