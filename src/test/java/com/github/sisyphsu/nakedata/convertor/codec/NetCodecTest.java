package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-08-04 18:58:14
 */
public class NetCodecTest {

    private static NetCodec codec = new NetCodec();

    static {
        codec.setFactory(new CodecFactory(null));
    }

    @Test
    public void test() {

    }

}