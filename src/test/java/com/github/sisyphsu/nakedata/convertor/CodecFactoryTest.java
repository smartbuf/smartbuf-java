package com.github.sisyphsu.nakedata.convertor;

import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

/**
 * @author sulin
 * @since 2019-08-02 14:10:30
 */
public class CodecFactoryTest {

    @Test
    public void test() {
        CodecFactory factory = CodecFactory.Instance;
        ConverterPipeline pipeline = factory.getPipeline(BitSet.class, Byte[].class);
    }

    @Test
    public void installCodec() {
    }

    @Test
    public void doConvert() {
    }

}