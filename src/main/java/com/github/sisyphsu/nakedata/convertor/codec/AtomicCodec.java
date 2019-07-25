package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.util.concurrent.atomic.*;

/**
 * @author sulin
 * @since 2019-07-25 12:00:54
 */
public class AtomicCodec extends Codec {

    /**
     * Convert Boolean to AtomicBoolean
     */
    @Converter
    public AtomicBoolean toAtomicBoolean(Boolean b) {
        return b == null ? null : new AtomicBoolean(b);
    }

    /**
     * Convert AtomicBoolean to Boolean
     */
    @Converter
    public Boolean toBoolean(AtomicBoolean ab) {
        return ab == null ? null : ab.get();
    }

    /**
     * Convert int[] to AtomicIntegerArray
     */
    @Converter
    public AtomicIntegerArray toAtomicIntegerArray(int[] arr) {
        return arr == null ? null : new AtomicIntegerArray(arr);
    }

    /**
     * Convert AtomicIntegerArray to int[]
     */
    @Converter
    public int[] toIntegerArray(AtomicIntegerArray aia) {
        if (aia == null)
            return null;
        int[] result = new int[aia.length()];
        for (int i = 0; i < result.length; i++) {
            result[i] = aia.get(i);
        }
        return result;
    }

    /**
     * Convert Integer to AtomicInteger
     */
    @Converter
    public AtomicInteger toAtomicInteger(Integer i) {
        return i == null ? null : new AtomicInteger(i);
    }

    /**
     * Convert AtomicInteger to Integer
     */
    @Converter
    public Integer toInteger(AtomicInteger ai) {
        return ai == null ? null : ai.get();
    }

    /**
     * Convert long[] to AtomicLongArray
     */
    @Converter
    public AtomicLongArray toAtomicLongArray(long[] arr) {
        return arr == null ? null : new AtomicLongArray(arr);
    }

    /**
     * Convert AtomicLongArray to long[]
     */
    @Converter
    public long[] toLongArray(AtomicLongArray ala) {
        if (ala == null)
            return null;
        long[] result = new long[ala.length()];
        for (int i = 0; i < result.length; i++) {
            result[i] = ala.get(i);
        }
        return result;
    }

    /**
     * Convert Long to AtomicLong
     */
    @Converter
    public AtomicLong toAtomicLong(Long l) {
        return l == null ? null : new AtomicLong(l);
    }

    /**
     * Convert AtomicLong to Long
     */
    @Converter
    public Long toLong(AtomicLong al) {
        return al == null ? null : al.get();
    }

    /**
     * Convert everything to AtomicReference
     */
    @Converter
    public AtomicReference<?> toAtomicReference(Object obj, XType type) {
        if (obj == null) {
            return null;
        }
        XType<?> refType = type.getParameterizedType();
        if (refType == null) {
            return new AtomicReference<>(obj); // no generic, use Object directly
        }
        if (refType.isPure() && refType.getRawType().isAssignableFrom(obj.getClass())) {
            return new AtomicReference<>(obj); // compatible
        }
        return new AtomicReference<>(convert(obj, refType));
    }

    /**
     * Convert AtomicReference to Object
     */
    @Converter
    public Object toObject(AtomicReference ref) {
        return ref == null ? null : ref.get();
    }

    /**
     * Convert Double into DoubleAdder
     */
    @Converter
    public DoubleAdder toDoubleAdder(Double d) {
        if (d == null) {
            return null;
        }
        DoubleAdder addr = new DoubleAdder();
        addr.add(d);
        return addr;
    }

    /**
     * Convert DoubleAdder to Double
     */
    @Converter
    public Double toDouble(DoubleAdder adder) {
        return adder == null ? null : adder.doubleValue();
    }

    /**
     * Convert Long to LongAdder
     */
    @Converter
    public LongAdder toLongAdder(Long l) {
        if (l == null) {
            return null;
        }
        LongAdder adder = new LongAdder();
        adder.add(l);
        return adder;
    }

    /**
     * Convert LongAdder to Long
     */
    @Converter
    public Long toLong(LongAdder adder) {
        return adder == null ? null : adder.longValue();
    }

}
