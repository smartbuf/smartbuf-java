package com.github.sisyphsu.datatube.convertor;

import com.github.sisyphsu.datatube.convertor.codec.LangCodec;
import com.github.sisyphsu.datatube.node.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author sulin
 * @since 2019-08-02 14:10:30
 */
public class CodecFactoryTest {

    private CodecFactory factory;

    @BeforeEach
    void setUp() {
        factory = CodecFactory.Instance;
    }

    @Test
    public void testInstallCodec() {
        CodecFactory factory = new CodecFactory();

        factory.installCodec((Set<Codec>) null);

        List<Codec> codecs = new ArrayList<>();
        codecs.add(null);
        factory.installCodec(codecs);

        codecs.clear();
        LangCodec codec = new LangCodec();
        codecs.add(codec);
        codecs.add(codec);
        factory.installCodec(codecs);

        Byte b = factory.convert(0L, Byte.class);
        assert b.intValue() == 0;

        factory.getConverterMap().clear();
        try {
            factory.convert(new byte[]{1, 2, 3, 4}, BitSet.class);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalStateException;
        }
    }

    @Test
    public void installCodec() {
        Set<Class<? extends Codec>> codecs = CodecScanner.scanCodecs(Node.class.getPackage().getName());
        for (Class<? extends Codec> codec : codecs) {
            factory.installCodec(codec);
        }
    }

    @Test
    public void getPipeline() {
        ConverterPipeline pipeline1 = factory.getPipeline(BitSet.class, Byte[].class);
        assert pipeline1.getMethods().size() == 2;
    }

    @Test
    public void testKey() {
        CodecFactory.PKey pKey = new CodecFactory.PKey(Long.class, Number.class);
        assert pKey.equals(pKey);
        assert !pKey.equals(null);
        assert !pKey.equals(new Object());

        CodecFactory.PKey pKey2 = new CodecFactory.PKey(Long.class, Object.class);
        assert !pKey.equals(pKey2);

        CodecFactory.PKey pKey3 = new CodecFactory.PKey(Integer.class, Number.class);
        assert !pKey.equals(pKey3);
    }

}
