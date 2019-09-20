package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import com.github.sisyphsu.nakedata.reflect.TypeRef;
import com.github.sisyphsu.nakedata.reflect.XType;
import com.github.sisyphsu.nakedata.reflect.XTypeUtils;
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

        TimeZone timeZone = TimeZone.getDefault();
        assert timeZone.equals(codec.toTimeZone(codec.toString(timeZone)));

        Pattern pattern = Pattern.compile(".*");
        Pattern pattern1 = codec.toPattern(codec.toString(pattern));
        assert pattern.pattern().equals(pattern1.pattern());
    }

    @Test
    public void testOptional() {
        Integer i = 100;
        assert i.equals(codec.toInteger(codec.toOptionalInt(i)));
        assert codec.toInteger(codec.toOptionalInt(null)) == null;

        Long l = 100000L;
        assert l.equals(codec.toLong(codec.toOptionalLong(l)));
        assert codec.toLong(codec.toOptionalLong(null)) == null;

        Double d = 1.0;
        assert d.equals(codec.toDouble(codec.toOptionalDouble(d)));
        assert codec.toDouble(codec.toOptionalDouble(null)) == null;

        XType oType = XTypeUtils.toXType(l.getClass());
        XType optionalType = XTypeUtils.toXType(new TypeRef<Optional<Long>>() {
        }.getType());
        assert l == codec.toObject(codec.toOptional(l, optionalType), oType);
        assert codec.toObject(codec.toOptional(null, optionalType), oType) == null;

        Optional optional = codec.toOptional(l, XTypeUtils.toXType(new TypeRef<Optional<Integer>>() {
        }.getType()));
        assert optional.isPresent() && optional.get().equals(l.intValue());

        assert codec.toObject(optional, oType).equals(l);
    }

    @Test
    public void testUUID() {
        UUID uuid = UUID.randomUUID();
        assert uuid.equals(codec.toUUID(codec.toString(uuid)));

        String uuidStr = codec.toString(UUID.randomUUID()).replaceAll("-", "");
        assert uuidStr.equals(codec.toString(codec.toUUID(uuidStr)).replaceAll("-", ""));

        try {
            codec.toUUID("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException && e.getCause() instanceof NumberFormatException;
        }

        try {
            codec.toUUID("1234");
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException && e.getCause() == null;
        }
    }

}