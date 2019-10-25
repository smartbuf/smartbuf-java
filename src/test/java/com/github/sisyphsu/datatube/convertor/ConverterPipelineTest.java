package com.github.sisyphsu.datatube.convertor;

import com.github.sisyphsu.datatube.reflect.XTypeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

/**
 * @author sulin
 * @since 2019-08-03 11:09:37
 */
public class ConverterPipelineTest {

    private CodecFactory factory;

    @BeforeEach
    void setUp() {
        factory = CodecFactory.Instance;
    }

    @Test
    public void convert() {
        ConverterPipeline pipeline = factory.getPipeline(BitSet.class, Byte[].class);
        assert pipeline.getSrcClass() == BitSet.class;
        assert pipeline.getTgtClass() == Byte[].class;

        Object tgt = pipeline.convert(BitSet.valueOf(new byte[]{1, 2, 3, 4}), XTypeUtils.toXType(Byte[].class));
        assert tgt != null;
        assert tgt instanceof Byte[];

        Byte[] bs = (Byte[]) tgt;
        assert bs.length == 4;
        assert bs[0] == 1;
        assert bs[1] == 2;
        assert bs[2] == 3;
        assert bs[3] == 4;
    }

}
