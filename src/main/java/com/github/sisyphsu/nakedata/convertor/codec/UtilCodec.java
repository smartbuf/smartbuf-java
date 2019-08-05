package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Codec for java.util package, exclude Date and others
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
        return BitSet.valueOf(bytes);
    }

    /**
     * Convert BitSet to byte[]
     */
    @Converter
    public byte[] toByteArray(BitSet bitSet) {
        return bitSet.toByteArray();
    }

    /**
     * Convert String to Currency
     */
    @Converter
    public Currency toCurrency(String s) {
        return Currency.getInstance(s);
    }

    /**
     * Convert Currency to String
     */
    @Converter
    public String toString(Currency c) {
        return c.getCurrencyCode();
    }

    /**
     * Convert String to Locale
     */
    @Converter
    public Locale toLocale(String s) {
        return Locale.forLanguageTag(s);
    }

    /**
     * Convert Locale to String
     */
    @Converter
    public String toString(Locale locale) {
        return locale.toLanguageTag();
    }

    /**
     * Convert String to UUID
     */
    @Converter
    public UUID toUUID(String s) {
        Long[] components = new Long[5];
        try {
            if (s.length() == 36) {
                String[] parts = s.split("-");
                if (parts.length == 5) {
                    for (int i = 0; i < 5; i++) {
                        components[i] = Long.valueOf(parts[i], 16);
                    }
                }
            } else if (s.length() == 32) {
                components[0] = Long.valueOf(s.substring(0, 8), 16);
                components[1] = Long.valueOf(s.substring(8, 12), 16);
                components[2] = Long.valueOf(s.substring(12, 16), 16);
                components[3] = Long.valueOf(s.substring(16, 20), 16);
                components[4] = Long.valueOf(s.substring(20, 32), 16);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid UUID string: " + s);
        }
        if (components[0] == null) {
            throw new IllegalArgumentException("Invalid UUID string: " + s);
        }
        long mostSigBits = components[0];
        mostSigBits <<= 16;
        mostSigBits |= components[1];
        mostSigBits <<= 16;
        mostSigBits |= components[2];

        long leastSigBits = components[3];
        leastSigBits <<= 48;
        leastSigBits |= components[4];
        return new UUID(mostSigBits, leastSigBits);
    }

    /**
     * Convert UUID to String
     */
    @Converter
    public String toString(UUID uuid) {
        return uuid.toString();
    }

    /**
     * Convert String to TimeZone
     */
    @Converter
    public TimeZone toTimeZone(String s) {
        return TimeZone.getTimeZone(s);
    }

    /**
     * Convert TimeZone to String
     */
    @Converter
    public String toString(TimeZone tz) {
        return tz.getID();
    }

    /**
     * Convert String to Pattern
     */
    @Converter
    public Pattern toPattern(String s) {
        return Pattern.compile(s);
    }

    /**
     * Convert Pattern to String
     */
    @Converter
    public String toString(Pattern p) {
        return p.pattern();
    }

    /**
     * Convert Object to Optional, support generic type
     */
    @Converter(nullable = true)
    public Optional toOptional(Object o, XType type) {
        if (o == null)
            return Optional.empty();
        XType<?> paramType = type.getParameterizedType();
        if (!(paramType.isPure() && paramType.getRawType().isInstance(o))) {
            o = convert(o, paramType); // not compatible
        }
        return Optional.of(o);
    }

    /**
     * Convert Optional to Object
     */
    @Converter
    public Object toObject(Optional optional, XType type) {
        if (!optional.isPresent()) {
            return null;
        }
        Object obj = optional.get();
        if (!(type.isPure() && type.getRawType().isInstance(obj))) {
            obj = convert(obj, type); // not compatible
        }
        return obj;
    }

    /**
     * Convert Double to OptionalDouble
     */
    @Converter(nullable = true)
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
    @Converter(nullable = true)
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
    @Converter(nullable = true)
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
