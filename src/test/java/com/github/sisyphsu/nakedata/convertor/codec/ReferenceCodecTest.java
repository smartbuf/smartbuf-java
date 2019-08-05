package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-08-04 18:58:30
 */
public class ReferenceCodecTest {

    private ReferenceCodec codec = new ReferenceCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void test() {

    }

}