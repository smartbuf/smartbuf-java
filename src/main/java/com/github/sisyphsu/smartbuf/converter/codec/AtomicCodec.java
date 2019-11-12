package com.github.sisyphsu.smartbuf.converter.codec;

import com.github.sisyphsu.smartbuf.converter.Codec;
import com.github.sisyphsu.smartbuf.converter.Converter;
import com.github.sisyphsu.smartbuf.reflect.XType;

import java.util.concurrent.atomic.*;

/**
 * Codec for java.util.concurrent.atomic package.
 *
 * @author sulin
 * @since 2019-07-25 12:00:54
 */
public final class AtomicCodec extends Codec {

    @Converter
    public AtomicBoolean toAtomicBoolean(Boolean b) {
        return new AtomicBoolean(b);
    }

    @Converter
    public Boolean toBoolean(AtomicBoolean ab) {
        return ab.get();
    }

    @Converter
    public AtomicIntegerArray toAtomicIntegerArray(int[] arr) {
        return new AtomicIntegerArray(arr);
    }

    @Converter
    public int[] toIntegerArray(AtomicIntegerArray aia) {
        int[] result = new int[aia.length()];
        for (int i = 0; i < result.length; i++) {
            result[i] = aia.get(i);
        }
        return result;
    }

    @Converter
    public AtomicInteger toAtomicInteger(Integer i) {
        return new AtomicInteger(i);
    }

    @Converter
    public Integer toInteger(AtomicInteger ai) {
        return ai.get();
    }

    @Converter
    public AtomicLongArray toAtomicLongArray(long[] arr) {
        return new AtomicLongArray(arr);
    }

    @Converter
    public long[] toLongArray(AtomicLongArray ala) {
        long[] result = new long[ala.length()];
        for (int i = 0; i < result.length; i++) {
            result[i] = ala.get(i);
        }
        return result;
    }

    @Converter
    public AtomicLong toAtomicLong(Long l) {
        return new AtomicLong(l);
    }

    @Converter
    public Long toLong(AtomicLong al) {
        return al.get();
    }

    @Converter
    public AtomicReference<?> toAtomicReference(Object obj, XType type) {
        XType<?> refType = type.getParameterizedType();
        if (refType.isPure() && refType.getRawType().isAssignableFrom(obj.getClass())) {
            return new AtomicReference<>(obj); // compatible
        }
        return new AtomicReference<>(convert(obj, refType));
    }

    @Converter(extensible = true)
    public Object toObject(AtomicReference ref, XType type) {
        Object data = ref.get();
        return convert(data, type);
    }

    @Converter
    public DoubleAdder toDoubleAdder(Double d) {
        DoubleAdder addr = new DoubleAdder();
        addr.add(d);
        return addr;
    }

    @Converter
    public Double toDouble(DoubleAdder adder) {
        return adder.doubleValue();
    }

    @Converter
    public LongAdder toLongAdder(Long l) {
        LongAdder adder = new LongAdder();
        adder.add(l);
        return adder;
    }

    @Converter
    public Long toLong(LongAdder adder) {
        return adder.longValue();
    }

}
