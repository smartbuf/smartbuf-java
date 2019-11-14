package com.github.smartbuf.converter;

import com.github.smartbuf.reflect.XType;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @author sulin
 * @since 2019-08-02 16:31:07
 */
public class ConverterMapTest {

    @Test
    public void printChart() {
        ConverterMap map = CodecFactory.Instance.getConverterMap();
        map.printDot();
    }

    @Test
    public void test() throws NoSuchMethodException {
        ConverterMap map = new ConverterMap();
        assert map.get(Date.class).isEmpty();
        assert map.get(Date.class, Long.class) == null;

        map.put(new TranConverterMethod(Long.class, Number.class));
        map.put(new TranConverterMethod(Long.class, Number.class)); // should do nothing
        try {
            map.put(new InvalidConverterMethod(Long.class, Number.class));
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        map.put(RealConverterMethod.valueOf(new TmpCodec(), TmpCodec.class.getDeclaredMethod("toNumber", Long.class)));
        map.put(new TranConverterMethod(Long.class, Number.class)); // should do nothing
        try {
            map.put(new InvalidConverterMethod(Long.class, Number.class));
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        map.put(new InvalidConverterMethod(Date.class, Long.class));
        try {
            map.put(new InvalidConverterMethod(Date.class, Long.class));
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

    static class TmpCodec extends Codec {
        @Converter
        public Number toNumber(Long l) {
            return 0;
        }
    }

    static class InvalidConverterMethod extends ConverterMethod {

        public InvalidConverterMethod(Class<?> srcClass, Class<?> tgtClass) {
            super(srcClass, tgtClass);
        }

        @Override
        public Object convert(Object data, XType tgtType) {
            return null;
        }

        @Override
        public int getDistance() {
            return 0;
        }

        @Override
        public boolean isExtensible() {
            return false;
        }
    }
}
