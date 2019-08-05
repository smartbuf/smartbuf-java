package com.github.sisyphsu.nakedata.convertor.codec;


import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-08-04 17:02:14
 */
public class DateCodecTest {

    private CollectionCodec codec = new CollectionCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void test() {
        // TODO need test
    }

}