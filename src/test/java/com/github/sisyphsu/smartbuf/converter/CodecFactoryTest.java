package com.github.sisyphsu.smartbuf.converter;

import com.github.sisyphsu.smartbuf.converter.codec.LangCodec;
import com.github.sisyphsu.smartbuf.exception.NoShortestPipelineException;
import com.github.sisyphsu.smartbuf.node.basic.ObjectNode;
import com.github.sisyphsu.smartbuf.reflect.TypeRef;
import com.github.sisyphsu.smartbuf.utils.CodecUtils;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author sulin
 * @since 2019-08-02 14:10:30
 */
@SuppressWarnings("ALL")
public class CodecFactoryTest {

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
        ConverterPipeline pipeline1 = CodecUtils.getPipeline(BitSet.class, Byte[].class);
        assert pipeline1.getMethods().size() == 2;
    }

    @Test
    public void testNullable() {
        Date date = new Date();
        Optional<Long> opt = (Optional<Long>) CodecUtils.convert(new Date(), new TypeRef<Optional<Long>>() {
        });

        assert opt.get() == date.getTime();

        opt = (Optional<Long>) CodecUtils.convert(null, new TypeRef<Optional<Long>>() {
        });

        assert !opt.isPresent();
    }

    @Test
    public void testPrimary() {
        int i = 100;
        long l = CodecUtils.convert(i, long.class);
        assert l == 100L;
    }

    @Test
    public void testError() {
        CodecFactory factory = CodecFactory.Instance;
        try {
            factory.installCodec(IllegalCodec.class);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

    @Test
    public void testPojo() {
        Pojo pojo = new Pojo();

        Map map = CodecUtils.convert(pojo, Map.class);

        Pojo newPojo = CodecUtils.convert(map, Pojo.class);
        assert pojo.equals(newPojo);

        newPojo = CodecUtils.convert(new HashMap<>(), Pojo.class);
        assert pojo.equals(newPojo);

        map.remove("bool1");
        newPojo = CodecUtils.convert(map, Pojo.class);
        assert pojo.equals(newPojo);

        ObjectNode node = CodecUtils.convert(map, ObjectNode.class);
        newPojo = CodecUtils.convert(node, Pojo.class);
        assert pojo.equals(newPojo);

        map.put("bool3", true);
        newPojo = CodecUtils.convert(map, Pojo.class);
        assert pojo.equals(newPojo);

        node = CodecUtils.convert(map, ObjectNode.class);
        newPojo = CodecUtils.convert(node, Pojo.class);
        assert pojo.equals(newPojo);

        map.put("zzzzz", "lalala");
        newPojo = CodecUtils.convert(map, Pojo.class);
        assert pojo.equals(newPojo);

        node = new ObjectNode(true, new String[]{"id", "name"}, new Object[]{1, "hello"});
        newPojo = CodecUtils.convert(node, Pojo.class);
        assert pojo.equals(newPojo);
    }

    static class IllegalCodec extends Codec {
        public IllegalCodec(int i) {
        }
    }

    @Data
    public static class Pojo {
        private boolean   bool1   = true;
        private Boolean   bool2   = false;
        private byte      byte1   = 1;
        private Byte      byte2   = Byte.MIN_VALUE;
        private short     short1  = 2;
        private Short     short2  = Short.MAX_VALUE;
        private int       int1    = 3;
        private Integer   int2    = Integer.MAX_VALUE;
        private long      long1   = 4;
        private Long      long2   = Long.MAX_VALUE;
        private float     float1  = 1.0f;
        private Float     float2  = Float.MAX_VALUE;
        private double    double1 = 1.1;
        private Double    double2 = Double.MAX_VALUE;
        private char      char1   = 'a';
        private Character char2   = 'z';
    }

    @Test
    public void testConflict() {
        CodecFactory factory = new CodecFactory();
        factory.installCodec(ConflictCodec.class);

        try {
            factory.convert(1L, BitSet.class);
            assert false;
        } catch (Exception e) {
            assert e instanceof NoShortestPipelineException;
        }
    }

    public static class ConflictCodec extends Codec {

        @Converter
        public BitSet toBitSet(Short s) {
            return null;
        }

        @Converter
        public BitSet toBitSet(Integer i) {
            return null;
        }

    }

}
