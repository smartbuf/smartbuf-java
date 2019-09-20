package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.reflect.XType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

        assert methodMap.get("valid1").getTgtClass() == Object.class;
        assert methodMap.get("valid1").getSrcClass() == String.class;
        assert !methodMap.get("valid1").isHasTypeArg();

        assert methodMap.get("valid2").getTgtClass() == Object.class;
        assert methodMap.get("valid2").getSrcClass() == String.class;
        assert methodMap.get("valid2").isHasTypeArg();
    }

    private static class TestCodec extends Codec {

        @Converter
        public Object valid1(String s) {
            return null;
        }

        @Converter
        public Object valid2(String s, XType type) {
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
    }

}