package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;

/**
 * @author sulin
 * @since 2019-08-04 18:58:48
 */
public class StringCodecTest {

    private static StringCodec codec = new StringCodec();

    static {
        codec.setFactory(new CodecFactory(null));
    }

}