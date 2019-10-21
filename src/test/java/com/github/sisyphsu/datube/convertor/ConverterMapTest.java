package com.github.sisyphsu.datube.convertor;

import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-08-02 16:31:07
 */
public class ConverterMapTest {

    @Test
    public void printChart() {
        ConverterMap map = CodecFactory.Instance.getConverterMap();
        System.out.println(map.printDot());
    }

}
