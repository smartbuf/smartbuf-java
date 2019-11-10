package com.github.sisyphsu.smartbuf.converter.codec;

import com.github.sisyphsu.smartbuf.converter.Codec;
import com.github.sisyphsu.smartbuf.converter.Converter;
import com.github.sisyphsu.smartbuf.reflect.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Codec for java.lang package, excludes Number and others.
 *
 * @author sulin
 * @since 2019-07-26 10:55:47
 */
@SuppressWarnings("unchecked")
public final class LangCodec extends Codec {

    /**
     * Convert Map to Object
     */
    @Converter(extensible = true, distance = 1 << 24)
    public Object toObject(Map<?, ?> map, XType<?> type) {
        if (type.getRawType() == Object.class) {
            return map; // dont need convert
        }
        Object result;
        try {
            result = type.getRawType().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't newInstance of " + type.getRawType() + ": ", e);
        }
        BeanWriter writer = BeanWriterBuilder.build(type.getRawType());
        // build values
        Object[] values = new Object[writer.getFields().length];
        for (int i = 0, len = writer.getFields().length; i < len; i++) {
            BeanField field = writer.getFields()[i];
            Object value = map.get(field.getName());
            if (value != null) {
                values[i] = convert(value, type.getField(field.getName()).getType());
                continue;
            }
            switch (field.getType()) {
                case Z:
                    values[i] = false;
                    break;
                case B:
                    values[i] = (byte) 0;
                    break;
                case S:
                    values[i] = (short) 0;
                    break;
                case I:
                    values[i] = 0;
                    break;
                case J:
                    values[i] = (long) 0;
                    break;
                case F:
                    values[i] = (float) 0;
                    break;
                case D:
                    values[i] = (double) 0;
                    break;
                case C:
                    values[i] = (char) 0;
                    break;
                default:
                    values[i] = null;
            }
        }
        writer.setValues(result, values);
        return result;
    }

    /**
     * Convert Object to Map, use cglib directly
     */
    @Converter(distance = 1 << 24)
    public Map toMap(Object obj) {
        BeanReader reader = BeanReaderBuilder.build(obj.getClass());
        Map<String, Object> result = new HashMap<>();
        String[] names = reader.getFieldNames();
        Object[] values = reader.getValues(obj);
        for (int i = 0; i < names.length; i++) {
            result.put(names[i], values[i]);
        }
        return result;
    }

    /**
     * Convert String to Enum
     */
    @Converter(extensible = true)
    public Enum toEnum(String name, XType type) {
        return Enum.valueOf(type.getRawType(), name);
    }

    /**
     * Convert Enum to String
     */
    @Converter
    public String toString(Enum e) {
        return e.name();
    }

    /**
     * Convert String to Class
     */
    @Converter
    public Class toClass(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }

    /**
     * Convert Class to String
     */
    @Converter
    public String toString(Class cls) {
        return cls.getName();
    }

    /**
     * Convert String to SimpleDateFormat
     */
    @Converter
    public SimpleDateFormat toSimpleDateFormat(String s) {
        return new SimpleDateFormat(s);
    }

    /**
     * Convert SimpleDateFormat to String
     */
    @Converter
    public String toString(SimpleDateFormat format) {
        return format.toPattern();
    }

}
