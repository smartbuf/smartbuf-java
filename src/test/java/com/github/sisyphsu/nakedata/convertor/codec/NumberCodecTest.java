package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;

/**
 * @author sulin
 * @since 2019-08-04 18:58:23
 */
public class NumberCodecTest {

    private static NumberCodec codec = new NumberCodec();

    static {
        codec.setFactory(new CodecFactory(null));
    }

}