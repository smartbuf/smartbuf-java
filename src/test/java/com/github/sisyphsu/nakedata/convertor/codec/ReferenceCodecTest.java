package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;

/**
 * @author sulin
 * @since 2019-08-04 18:58:30
 */
public class ReferenceCodecTest {

    private static ReferenceCodec codec = new ReferenceCodec();

    static {
        codec.setFactory(new CodecFactory(null));
    }

}