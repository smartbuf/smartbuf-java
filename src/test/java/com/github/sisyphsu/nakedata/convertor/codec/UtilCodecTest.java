package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import com.github.sisyphsu.nakedata.convertor.reflect.TypeRef;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;
import com.github.sisyphsu.nakedata.convertor.reflect.XTypeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author sulin
 * @since 2019-08-04 18:59:09
 */
public class UtilCodecTest {

    private UtilCodec codec = new UtilCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void test() {
        byte[] bytes = new byte[]{1, 4, 8, 100};
        assert Arrays.equals(bytes, codec.toByteArray(codec.toBitSet(bytes)));

        Currency currency = Currency.getInstance(Locale.CHINA);
        assert currency.equals(codec.toCurrency(codec.toString(currency)));

        Locale locale = Locale.KOREAN;
        assert locale.equals(codec.toLocale(codec.toString(locale)));

        UUID uuid = UUID.randomUUID();
        assert uuid.equals(codec.toUUID(codec.toString(uuid)));

        String uuidStr = codec.toString(UUID.randomUUID()).replaceAll("-", "");
        assert uuidStr.equals(codec.toString(codec.toUUID(uuidStr)).replaceAll("-", ""));

        TimeZone timeZone = TimeZone.getDefault();
        assert timeZone.equals(codec.toTimeZone(codec.toString(timeZone)));

        Pattern pattern = Pattern.compile(".*");
        Pattern pattern1 = codec.toPattern(codec.toString(pattern));
        assert pattern.pattern().equals(pattern1.pattern());

        Integer i = 100;
        assert i.equals(codec.toInteger(codec.toOptionalInt(i)));
        assert codec.toInteger(codec.toOptionalInt(null)) == null;

        Long l = 100000L;
        assert l.equals(codec.toLong(codec.toOptionalLong(l)));
        assert codec.toLong(codec.toOptionalLong(null)) == null;

        Double d = 1.0;
        assert d.equals(codec.toDouble(codec.toOptionalDouble(d)));
        assert codec.toDouble(codec.toOptionalDouble(null)) == null;

        Object o = new Object();
        XType oType = XTypeUtils.toXType(o.getClass());
        XType optionalType = XTypeUtils.toXType(new TypeRef<Optional<Object>>() {
        }.getType());
        assert o == codec.toObject(codec.toOptional(o, optionalType), oType);
        assert codec.toObject(codec.toOptional(null, optionalType), oType) == null;
    }

    @Test
    public void testUUID() {
        System.out.println(UUID.randomUUID().toString());
    }

}