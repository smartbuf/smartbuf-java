package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;

/**
 * @author sulin
 * @since 2019-08-04 18:58:39
 */
public class SqlCodecTest {

    private static SqlCodec codec = new SqlCodec();

    static {
        codec.setFactory(new CodecFactory(null));
    }
}