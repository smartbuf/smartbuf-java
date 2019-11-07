package com.github.sisyphsu.canoe.converter;

import com.github.sisyphsu.canoe.converter.codec.LangCodec;
import com.github.sisyphsu.canoe.reflect.TypeRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author sulin
 * @since 2019-08-02 14:10:30
 */
@SuppressWarnings("ALL")
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
    public void getPipeline() {
        ConverterPipeline pipeline1 = factory.getPipeline(BitSet.class, Byte[].class);
        assert pipeline1.getMethods().size() == 2;
    }

    @Test
    public void testNullable() {
        Date date = new Date();
        Optional<Long> opt = (Optional<Long>) factory.convert(new Date(), new TypeRef<Optional<Long>>() {
        }.getType());

        assert opt.get() == date.getTime();

        opt = (Optional<Long>) factory.convert(null, new TypeRef<Optional<Long>>() {
        }.getType());

        assert !opt.isPresent();
    }

    @Test
    public void testPrimary() {
        int i = 100;
        long l = factory.convert(i, long.class);
        assert l == 100L;
    }

    @Test
    public void testError() {
        try {
            factory.installCodec(IllegalCodec.class);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

    static class IllegalCodec extends Codec {
        public IllegalCodec(int i) {
        }
    }

}
