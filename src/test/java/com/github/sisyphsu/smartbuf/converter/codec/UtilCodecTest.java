package com.github.sisyphsu.smartbuf.converter.codec;

import com.github.sisyphsu.smartbuf.converter.CodecFactory;
import com.github.sisyphsu.smartbuf.reflect.TypeRef;
import com.github.sisyphsu.smartbuf.reflect.XType;
import com.github.sisyphsu.smartbuf.reflect.XTypeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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

        assert Objects.equals(codec.toObject(optional, oType), l);

        Object obj = codec.toObject(Optional.of(1), XTypeUtils.toXType(new TypeRef<AtomicReference<Long>>() {
        }.getType()));
        assert obj instanceof AtomicReference;
        assert Objects.equals(((AtomicReference) obj).get(), 1L);

        Optional opt2 = codec.toOptional(1, XTypeUtils.toXType(new TypeRef<Optional<AtomicReference<Long>>>() {
        }.getType()));
        assert opt2.isPresent();
        assert opt2.get() instanceof AtomicReference;
        assert Objects.equals(1L, ((AtomicReference) opt2.get()).get());
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
            codec.toUUID("");
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        try {
            codec.toUUID("b8f7f030-e07d-44d3-bded-b6dce4cff-a3");
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

}
