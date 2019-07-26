package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Optional's codec
 *
 * @author sulin
 * @since 2019-05-13 18:15:18
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class UtilCodec extends Codec {

    /**
     * Convert byte[] to BitSet
     */
    @Converter
    public BitSet toBitSet(byte[] bytes) {
        return bytes == null ? null : BitSet.valueOf(bytes);
    }

    /**
     * Convert BitSet to byte[]
     */
    @Converter
    public byte[] toByteArray(BitSet bitSet) {
        return bitSet == null ? null : bitSet.toByteArray();
    }

    /**
     * Convert String to Currency
     */
    @Converter
    public Currency toCurrency(String s) {
        return s == null ? null : Currency.getInstance(s);
    }

    /**
     * Convert Currency to String
     */
    @Converter
    public String toString(Currency c) {
        return c == null ? null : c.getCurrencyCode();
    }

    /**
     * Convert String to Locale
     */
    @Converter
    public Locale toLocale(String s) {
        return s == null ? null : Locale.forLanguageTag(s);
    }

    /**
     * Convert Locale to String
     */
    @Converter
    public String toString(Locale locale) {
        return locale == null ? null : locale.toLanguageTag();
    }

    /**
     * Convert String to UUID
     */
    @Converter
    public UUID toUUID(String s) {
        return s == null ? null : UUID.fromString(s);
    }

    /**
     * Convert UUID to String
     */
    @Converter
    public String toString(UUID uuid) {
        return uuid == null ? null : uuid.toString();
    }

    /**
     * Convert String to TimeZone
     */
    @Converter
    public TimeZone toTimeZone(String s) {
        return s == null ? null : TimeZone.getTimeZone(s);
    }

    /**
     * Convert TimeZone to String
     */
    @Converter
    public String toString(TimeZone tz) {
        return tz == null ? null : tz.getID();
    }

    /**
     * Convert String to Pattern
     */
    @Converter
    public Pattern toPattern(String s) {
        return s == null ? null : Pattern.compile(s);
    }

    /**
     * Convert Pattern to String
     */
    @Converter
    public String toString(Pattern p) {
        return p == null ? null : p.pattern();
    }

    /**
     * Convert Object to Optional
     */
    @Converter
    public Optional toOptional(Object o, XType type) {
        if (o == null)
            return Optional.empty();
        XType<?> genericType = type.getParameterizedType();
        if (genericType.isPure() && genericType.getRawType().isAssignableFrom(o.getClass())) {
            return Optional.of(o);
        } else {
            return Optional.of(convert(o, genericType));
        }
    }

    /**
     * Convert Optional to Object
     */
    @Converter
    public Object toObject(Optional optional) {
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * Convert Double to OptionalDouble
     */
    @Converter
    public OptionalDouble toOptionalDouble(Double d) {
        return d == null ? OptionalDouble.empty() : OptionalDouble.of(d);
    }

    /**
     * Convert OptionalDouble to Double
     */
    @Converter
    public Double toDouble(OptionalDouble od) {
        return od.isPresent() ? od.getAsDouble() : null;
    }

    /**
     * Convert Integer to OptionalInt
     */
    @Converter
    public OptionalInt toOptionalInt(Integer i) {
        return i == null ? OptionalInt.empty() : OptionalInt.of(i);
    }

    /**
     * Convert OptionalInt to Integer
     */
    @Converter
    public Integer toInteger(OptionalInt oi) {
        return oi.isPresent() ? oi.getAsInt() : null;
    }

    /**
     * Convert Long to OptionalLong
     */
    @Converter
    public OptionalLong toOptionalLong(Long l) {
        return l == null ? OptionalLong.empty() : OptionalLong.of(l);
    }

    /**
     * Convert OptionalLong to Long
     */
    @Converter
    public Long toLong(OptionalLong ol) {
        return ol.isPresent() ? ol.getAsLong() : null;
    }

}
