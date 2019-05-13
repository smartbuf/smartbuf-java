package com.github.sisyphsu.nakedata.convertor.adaptor;

import com.github.sisyphsu.nakedata.convertor.adaptor.time.java8.DurationAdaptor;
import org.junit.Test;

/**
 * @author sulin
 * @since 2019-05-12 16:26:20
 */
public class AdaptorTest {

    @Test
    public void testType() {
        Adaptor adaptor = new DurationAdaptor();
        System.out.println(adaptor.getSrcType());
        System.out.println(adaptor.getTgtType());
    }

}