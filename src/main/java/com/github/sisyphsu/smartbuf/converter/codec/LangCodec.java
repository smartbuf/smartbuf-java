package com.github.sisyphsu.smartbuf.converter.codec;

import com.github.sisyphsu.smartbuf.converter.BeanInfo;
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

    /*
     * Convert Map to BeanValues, BeanValues will be used to build Bean
     */
    @Converter(extensible = true)
    public BeanInfo toObject(Map<?, ?> map, XType<?> type) {
        BeanWriter writer = BeanWriterBuilder.build(type.getRawType());
        BeanField[] fields = writer.getFields();
        Object[] values = new Object[fields.length];
        for (int i = 0, len = fields.length; i < len; i++) {
            BeanField field = fields[i];
            values[i] = map.get(field.getName());
        }
        return new BeanInfo(writer, values);
    }

    /*
     * Build Object by specified BeanValues info
     */
    @Converter(extensible = true, distance = 1 << 20)
    public Object toObject(BeanInfo bv, XType<?> type) {
        Object result;
        try {
            result = type.getRawType().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't newInstance of " + type.getRawType() + ": ", e);
        }
        BeanWriter writer = bv.getWriter();
        BeanField[] fields = writer.getFields();
        XField[] xFields = type.getFields();
        Object[] values = bv.getValues();
        if (xFields.length != fields.length) {
            throw new IllegalArgumentException("unmatched xtype for " + type.getRawType());
        }
        if (values.length != fields.length) {
            throw new IllegalArgumentException("invalid bean values for " + type.getRawType());
        }
        // check and convert all values
        for (int i = 0, len = fields.length; i < len; i++) {
            BeanField field = fields[i];
            Object value = values[i];
            XType fxType = xFields[i].getType();
            // value compatible
            if (value == null || fxType.getRawType().isAssignableFrom(value.getClass()) && fxType.isPure()) {
                continue;
            }
            // quick handler for basic data convert
            switch (field.getType()) {
                case Z:
                case BOOLEAN:
                    if (value instanceof Boolean) {
                        continue;
                    }
                    break;
                case B:
                case BYTE:
                    if (value instanceof Number) {
                        values[i] = ((Number) value).byteValue();
                        continue;
                    }
                    break;
                case S:
                case SHORT:
                    if (value instanceof Number) {
                        values[i] = ((Number) value).shortValue();
                        continue;
                    }
                    break;
                case I:
                case INTEGER:
                    if (value instanceof Number) {
                        values[i] = ((Number) value).intValue();
                        continue;
                    }
                    break;
                case J:
                case LONG:
                    if (value instanceof Number) {
                        values[i] = ((Number) value).longValue();
                        continue;
                    }
                    break;
                case F:
                case FLOAT:
                    if (value instanceof Number) {
                        values[i] = ((Number) value).floatValue();
                        continue;
                    }
                    break;
                case D:
                case DOUBLE:
                    if (value instanceof Number) {
                        values[i] = ((Number) value).doubleValue();
                        continue;
                    }
                    break;
            }
            // call codec converter
            values[i] = convert(values[i], xFields[i].getType());
        }
        writer.setValues(result, values);
        return result;
    }

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

    @Converter(extensible = true)
    public Enum toEnum(String name, XType type) {
        return Enum.valueOf(type.getRawType(), name);
    }

    @Converter
    public String toString(Enum e) {
        return e.name();
    }

    @Converter
    public Class toClass(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }

    @Converter
    public String toString(Class cls) {
        return cls.getName();
    }

    @Converter
    public SimpleDateFormat toSimpleDateFormat(String s) {
        return new SimpleDateFormat(s);
    }

    @Converter
    public String toString(SimpleDateFormat format) {
        return format.toPattern();
    }

}
