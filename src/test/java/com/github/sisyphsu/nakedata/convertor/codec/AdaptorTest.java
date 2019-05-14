package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.codec.time.java8.DurationCodec;
import org.junit.Test;

/**
 * @author sulin
 * @since 2019-05-12 16:26:20
 */
public class AdaptorTest {

    @Test
    public void testType() {
        Codec adaptor = new DurationCodec();
        System.out.println(adaptor.getType());
    }

}