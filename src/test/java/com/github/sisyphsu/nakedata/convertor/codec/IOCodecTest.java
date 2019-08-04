package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import com.github.sisyphsu.nakedata.convertor.reflect.XTypeUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-08-04 17:02:04
 */
public class IOCodecTest {

    private static IOCodec codec = new IOCodec();

    static {
        codec.setFactory(new CodecFactory(null));
    }

    @Test
    public void test() throws IOException {
        Charset charset = Charset.forName("UTF-8");
        assert Objects.equals(charset, codec.toCharset(codec.toString(charset)));

        File file = File.createTempFile("100", "test");
        String fileName = codec.toString(file);
        assert fileName != null;

        byte[] bytes = new byte[]{1, 2, 3, 4, 5};
        assert Arrays.equals(bytes, codec.toByteArray(codec.toInputStream(bytes, XTypeUtils.toXType(ByteArrayInputStream.class))));

        String str = "hello world";
        Readable readable = codec.toReadable(str, XTypeUtils.toXType(CharArrayReader.class));
        assert readable instanceof CharArrayReader;
        assert str.equals(codec.toString(readable));
    }

}