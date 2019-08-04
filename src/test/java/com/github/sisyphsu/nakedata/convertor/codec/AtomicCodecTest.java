package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import com.github.sisyphsu.nakedata.convertor.reflect.TypeRef;
import com.github.sisyphsu.nakedata.convertor.reflect.XTypeUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.BitSet;
import java.util.concurrent.atomic.*;

/**
 * @author sulin
 * @since 2019-08-03 16:26:30
 */
public class AtomicCodecTest {

    private static AtomicCodec codec = new AtomicCodec();

    static {
        codec.setFactory(new CodecFactory(null));
    }

    @Test
    public void testAtomic() {
        assert codec.toBoolean(new AtomicBoolean(true));
        assert codec.toAtomicBoolean(true).get();

        assert codec.toInteger(new AtomicInteger(100)) == 100;
        assert codec.toAtomicInteger(100).get() == 100;

        assert codec.toLong(new AtomicLong(2000)) == 2000;
        assert codec.toAtomicLong(2000L).get() == 2000;

        DoubleAdder doubleAdder = new DoubleAdder();
        doubleAdder.add(0.02);
        assert codec.toDoubleAdder(0.02).doubleValue() == 0.02;
        assert codec.toDouble(doubleAdder) == 0.02;

        LongAdder longAdder = new LongAdder();
        longAdder.add(999);
        assert codec.toLongAdder(999L).longValue() == 999;
        assert codec.toLong(longAdder) == 999;

        int[] ints = new int[]{1, 4, 9};
        assert Arrays.equals(codec.toIntegerArray(codec.toAtomicIntegerArray(ints)), ints);

        long[] longs = new long[]{100, 700, 999};
        assert Arrays.equals(codec.toLongArray(codec.toAtomicLongArray(longs)), longs);

        Byte[] bytes = new Byte[]{1, 9, 100};
        AtomicReference<BitSet> ref = (AtomicReference<BitSet>) codec.toAtomicReference(bytes, XTypeUtils.toXType(new TypeRef<AtomicReference<BitSet>>() {
        }.getType()));
        assert ref.get().toByteArray().length == bytes.length;
        assert ref.get().toByteArray()[2] == bytes[2];

        Object obj = codec.toObject(ref);
        assert obj instanceof BitSet;
    }

}