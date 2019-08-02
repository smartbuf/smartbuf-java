package com.github.sisyphsu.nakedata.convertor;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author sulin
 * @since 2019-08-02 16:31:07
 */
public class ConverterMapTest {

    @Test
    public void printChart() throws Exception {
        ConverterMap map = CodecFactory.Instance.getConverterMap();
        map.printChart();
    }

}