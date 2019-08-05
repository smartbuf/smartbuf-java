package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author sulin
 * @since 2019-08-04 18:59:09
 */
public class UtilCodecTest {

    private UtilCodec codec = new UtilCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

}