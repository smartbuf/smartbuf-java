package com.github.smartbuf.converter;

import com.github.smartbuf.reflect.TypeRef;
import com.github.smartbuf.reflect.XTypeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author sulin
 * @since 2019-08-03 11:09:37
 */
public class ConverterPipelineTest {

    private CodecFactory factory = new CodecFactory();

    @BeforeEach
    void setUp() {
        factory = CodecFactory.Instance;
    }

    @Test
    public void convert() {
        ConverterPipeline pipeline = factory.getPipeline(BitSet.class, Byte[].class);

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

    @Test
    public void test() throws Exception {
        ConverterPipeline pipeline = factory.getPipeline(Long.class, Optional.class);

        List<RealConverterMethod> methods = new ArrayList<>();
        for (ConverterMethod method : pipeline.getMethods()) {
            if (method instanceof RealConverterMethod) {
                methods.add((RealConverterMethod) method);
            }
        }

        ConverterPipeline.Pipeline pip = ConverterPipeline.build(methods);
        Object result = pip.convert(1L, XTypeUtils.toXType(new TypeRef<Optional<Long>>() {
        }.getType()));
        System.out.println(result);
    }

    @Test
    public void testError() {
        new ConverterPipeline(Collections.singletonList(new RealConverterMethod(Long.class, BitSet.class)));
    }

    @Test
    public void disableAsm() {
        ConverterPipeline.ENABLE_ASM = false;

        Collection collection = factory.convert(new int[]{1, 2, 3}, Collection.class);
        assert collection.size() == 3;

        assert factory.convert(OptionalInt.empty(), BitSet.class) == null;

        ConverterPipeline.ENABLE_ASM = true;
    }

    @Test
    public void testPrimaryArg() {
        CodecFactory factory = new CodecFactory();
        factory.installCodec(PrimaryArgCodec.class);

        OptionalInt opt = factory.convert(100, OptionalInt.class);
        assert opt.getAsInt() == 101;
    }

    public static class PrimaryArgCodec extends Codec {
        @Converter(distance = -100)
        public OptionalInt toBitSet(int i) {
            return OptionalInt.of(i + 1);
        }
    }

}
