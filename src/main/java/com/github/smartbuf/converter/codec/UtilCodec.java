package com.github.smartbuf.converter.codec;

import com.github.smartbuf.converter.Codec;
import com.github.smartbuf.converter.Converter;
import com.github.smartbuf.reflect.XType;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Codec for java.util package, exclude Date and others
 *
 * @author sulin
 * @since 2019-05-13 18:15:18
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class UtilCodec extends Codec {

    @Converter
    public BitSet toBitSet(byte[] bytes) {
        return BitSet.valueOf(bytes);
    }

    @Converter
    public byte[] toByteArray(BitSet bitSet) {
        return bitSet.toByteArray();
    }

    @Converter
    public Currency toCurrency(String s) {
        return Currency.getInstance(s);
    }

    @Converter
    public String toString(Currency c) {
        return c.getCurrencyCode();
    }

    @Converter
    public Locale toLocale(String s) {
        return Locale.forLanguageTag(s);
    }

    @Converter
    public String toString(Locale locale) {
        return locale.toLanguageTag();
    }

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
                } else {
                    throw new IllegalArgumentException("Invalid UUID: " + s);
                }
            } else if (s.length() == 32) {
                components[0] = Long.valueOf(s.substring(0, 8), 16);
                components[1] = Long.valueOf(s.substring(8, 12), 16);
                components[2] = Long.valueOf(s.substring(12, 16), 16);
                components[3] = Long.valueOf(s.substring(16, 20), 16);
                components[4] = Long.valueOf(s.substring(20, 32), 16);
            } else {
                throw new IllegalArgumentException("Invalid UUID: " + s);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid UUID: " + s, e);
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

    @Converter
    public String toString(UUID uuid) {
        return uuid.toString();
    }

    @Converter
    public TimeZone toTimeZone(String s) {
        return TimeZone.getTimeZone(s);
    }

    @Converter
    public String toString(TimeZone tz) {
        return tz.getID();
    }

    @Converter
    public Pattern toPattern(String s) {
        return Pattern.compile(s);
    }

    @Converter
    public String toString(Pattern p) {
        return p.pattern();
    }

    @Converter(nullable = true)
    public Optional toOptional(Object o, XType type) {
        if (o == null)
            return Optional.empty();
        XType<?> paramType = type.getParameterizedType();
        if (paramType.isPure() && paramType.getRawType().isInstance(o)) {
            return Optional.of(o); // compatible
        }
        return Optional.of(convert(o, paramType));
    }

    @Converter(extensible = true)
    public Object toObject(Optional optional, XType type) {
        if (!optional.isPresent()) {
            return null;
        }
        Object obj = optional.get();
        if (type.isPure() && type.getRawType().isInstance(obj)) {
            return obj; // compatible
        }
        return convert(obj, type);
    }

    @Converter(nullable = true)
    public OptionalDouble toOptionalDouble(Double d) {
        return d == null ? OptionalDouble.empty() : OptionalDouble.of(d);
    }

    @Converter
    public Double toDouble(OptionalDouble od) {
        return od.isPresent() ? od.getAsDouble() : null;
    }

    @Converter(nullable = true)
    public OptionalInt toOptionalInt(Integer i) {
        return i == null ? OptionalInt.empty() : OptionalInt.of(i);
    }

    @Converter
    public Integer toInteger(OptionalInt oi) {
        return oi.isPresent() ? oi.getAsInt() : null;
    }

    @Converter(nullable = true)
    public OptionalLong toOptionalLong(Long l) {
        return l == null ? OptionalLong.empty() : OptionalLong.of(l);
    }

    @Converter
    public Long toLong(OptionalLong ol) {
        return ol.isPresent() ? ol.getAsLong() : null;
    }

}
