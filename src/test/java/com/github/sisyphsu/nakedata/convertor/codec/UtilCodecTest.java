package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;

/**
 * @author sulin
 * @since 2019-08-04 18:59:09
 */
public class UtilCodecTest {

    private static UtilCodec codec = new UtilCodec();

    static {
        codec.setFactory(new CodecFactory(null));
    }

}