package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;

/**
 * @author sulin
 * @since 2019-08-04 18:59:00
 */
public class ThrowableCodecTest {

    private static ThrowableCodec codec = new ThrowableCodec();

    static {
        codec.setFactory(new CodecFactory(null));
    }

}