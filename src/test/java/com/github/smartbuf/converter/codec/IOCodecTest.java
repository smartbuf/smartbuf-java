package com.github.smartbuf.converter.codec;

import com.github.smartbuf.converter.CodecFactory;
import com.github.smartbuf.reflect.XTypeUtils;
import com.github.smartbuf.utils.CodecUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-08-04 17:02:04
 */
public class IOCodecTest {

    private IOCodec codec = new IOCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void test() throws IOException {
        Charset charset = StandardCharsets.UTF_8;
        assert Objects.equals(charset, codec.toCharset(codec.toString(charset)));

        File file = File.createTempFile("100", "test");
        String fileName = codec.toString(file);
        assert fileName != null;
    }

    @Test
    public void testInputStream() throws IOException {
        byte[] bytes = new byte[]{1, 2, 3, 4, 5};
        assert Arrays.equals(bytes, codec.toByteArray(codec.toInputStream(bytes, XTypeUtils.toXType(ByteArrayInputStream.class))));

        assert codec.toInputStream(bytes, XTypeUtils.toXType(ByteArrayInputStream.class)).getClass() == ByteArrayInputStream.class;
        assert codec.toInputStream(bytes, XTypeUtils.toXType(BufferedInputStream.class)).getClass() == BufferedInputStream.class;
        assert codec.toInputStream(bytes, XTypeUtils.toXType(DataInputStream.class)).getClass() == DataInputStream.class;

        try {
            codec.toInputStream(bytes, CodecUtils.toXType(FileInputStream.class));
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }
    }

    @Test
    public void testReadable() throws IOException {
        String str = "hello world";
        Readable readable = codec.toReadable(str, XTypeUtils.toXType(CharArrayReader.class));
        assert readable instanceof CharArrayReader;
        assert str.equals(codec.toString(readable));

        assert codec.toReadable(str, XTypeUtils.toXType(StringReader.class)) instanceof StringReader;
        assert codec.toReadable(str, XTypeUtils.toXType(BufferedReader.class)) instanceof BufferedReader;

        try {
            codec.toReadable(str, XTypeUtils.toXType(FileReader.class));
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }
    }

}
