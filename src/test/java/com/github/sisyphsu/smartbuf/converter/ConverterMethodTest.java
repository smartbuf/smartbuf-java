package com.github.sisyphsu.smartbuf.converter;

import com.github.sisyphsu.smartbuf.reflect.XType;
import com.github.sisyphsu.smartbuf.reflect.XTypeUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author sulin
 * @since 2019-08-03 12:07:12
 */
public class ConverterMethodTest {

    @Test
    public void testTranConverterMethod() {
        ConverterMethod method = new TranConverterMethod(ArrayList.class, Collection.class);
        Object tgt = method.convert(new ArrayList(), null);
        assert tgt != null;
        assert tgt instanceof Collection;
    }

    @Test
    public void testRealConverterMethod() {
        TestCodec codec = new TestCodec();
        Map<String, RealConverterMethod> methodMap = new HashMap<>();
        for (Method method : codec.getClass().getDeclaredMethods()) {
            methodMap.put(method.getName(), RealConverterMethod.valueOf(codec, method));
        }

        assert methodMap.get("invalid1") == null;
        assert methodMap.get("invalid2") == null;
        assert methodMap.get("invalid3") == null;
        assert methodMap.get("invalid4") == null;
        assert methodMap.get("invalid5") == null;
        assert methodMap.get("invalid6") == null;

        assert methodMap.get("valid1") != null;
        assert methodMap.get("valid2") != null;

        assert methodMap.get("valid1").getTgtClass() == BitSet.class;
        assert methodMap.get("valid1").getSrcClass() == String.class;
        assert !methodMap.get("valid1").isHasTypeArg();

        assert methodMap.get("valid2").getTgtClass() == Timestamp.class;
        assert methodMap.get("valid2").getSrcClass() == String.class;
        assert methodMap.get("valid2").isHasTypeArg();

        RealConverterMethod method = methodMap.get("valid1");
        try {
            method.convert(new Date(), null);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
        try {
            method.convert(null, XTypeUtils.toXType(BitSet.class));
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalStateException;
        }
    }

    private static class TestCodec extends Codec implements IC {

        @Converter(nullable = true)
        public BitSet valid1(String s) {
            throw new UnsupportedOperationException();
        }

        @Converter
        public Timestamp valid2(String s, XType type) {
            return null;
        }

        public Object invalid1(String s) {
            return null;
        }

        public Object invalid2(String s, XType type) {
            return null;
        }

        @Converter
        public Object invalid3() {
            return null;
        }

        @Converter
        public Object invalid4(String s, Type type) {
            return null;
        }

        @Converter
        public void invalid5(String s) {
        }

        @Converter
        public Object invalid6(String... s) {
            return null;
        }

        @Converter
        public Void invalid7(String s) {
            return null;
        }

        @Override
        public void setFactory(CodecFactory factory) {
            super.setFactory(factory);
        }
    }

    interface IC {
        default void test() {
        }
    }
}
