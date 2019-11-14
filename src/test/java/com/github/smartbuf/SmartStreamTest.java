package com.github.smartbuf;

import com.github.smartbuf.exception.SmartBufClosedException;
import com.github.smartbuf.reflect.TypeRef;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.*;

/**
 * @author sulin
 * @since 2019-10-28 17:16:46
 */
public class SmartStreamTest {

    byte[]      data;
    SmartStream stream = new SmartStream();

    @Test
    public void testSimple() throws IOException {

        data = stream.serialize(null);
        assert stream.deserialize(data, Object.class) == null;

        data = stream.serialize(false);
        assert !stream.deserialize(data, Boolean.class);

        data = stream.serialize(Byte.MIN_VALUE);
        assert stream.deserialize(data, Byte.class) == Byte.MIN_VALUE;

        data = stream.serialize(Short.MIN_VALUE);
        assert stream.deserialize(data, Short.class) == Short.MIN_VALUE;

        data = stream.serialize(Integer.MIN_VALUE);
        assert stream.deserialize(data, Integer.class) == Integer.MIN_VALUE;

        data = stream.serialize(Long.MIN_VALUE);
        assert stream.deserialize(data, Long.class) == Long.MIN_VALUE;

        data = stream.serialize(Float.MIN_VALUE);
        assert stream.deserialize(data, Float.class) == Float.MIN_VALUE;

        data = stream.serialize(Double.MIN_VALUE);
        assert stream.deserialize(data, Double.class) == Double.MIN_VALUE;

        data = stream.serialize("hello world");
        assert Objects.equals(stream.deserialize(data, String.class), "hello world");

        data = stream.serialize(Thread.State.BLOCKED);
        assert Objects.equals(stream.deserialize(data, Thread.State.class), Thread.State.BLOCKED);
    }

    @Test
    public void testArray() throws IOException {
        boolean[] booleans = new boolean[]{true, false, false, true, true, true, false, false, false, true, true};
        data = stream.serialize(booleans);
        assert Objects.deepEquals(booleans, stream.deserialize(data, boolean[].class));

        byte[] bytes = RandomUtils.nextBytes(1025);
        data = stream.serialize(bytes);
        assert Objects.deepEquals(bytes, stream.deserialize(data, byte[].class));

        short[] shorts = new short[]{0, 1, 10, 100, 1000, Short.MIN_VALUE, Short.MAX_VALUE};
        data = stream.serialize(shorts);
        assert Objects.deepEquals(shorts, stream.deserialize(data, short[].class));

        int[] ints = new int[]{0, 1, 10, 100, 1000, 10000, 100000, Integer.MIN_VALUE, Integer.MAX_VALUE};
        data = stream.serialize(ints);
        assert Objects.deepEquals(ints, stream.deserialize(data, int[].class));

        long[] longs = new long[]{0, 1, 10, 100, 10000, 10000000, Long.MIN_VALUE, Long.MAX_VALUE};
        data = stream.serialize(longs);
        assert Objects.deepEquals(longs, stream.deserialize(data, long[].class));

        float[] floats = new float[]{0, 0.1f, Float.MIN_VALUE, Float.MAX_VALUE};
        data = stream.serialize(floats);
        assert Objects.deepEquals(floats, stream.deserialize(data, float[].class));

        double[] doubles = new double[]{0, 0.1, Double.MIN_VALUE, Double.MAX_VALUE};
        data = stream.serialize(doubles);
        assert Objects.deepEquals(doubles, stream.deserialize(data, double[].class));

        Object[] objects = new Object[]{null, booleans, bytes, shorts, ints, longs, floats, doubles};
        data = stream.serialize(objects);
        assert Objects.deepEquals(objects, stream.deserialize(data, Object[].class));
    }

    @Test
    public void testObject() throws IOException {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        data = stream.serialize(atomicBoolean);
        assert atomicBoolean.get() == stream.deserialize(data, AtomicBoolean.class).get();

        AtomicInteger atomicInteger = new AtomicInteger(RandomUtils.nextInt());
        data = stream.serialize(atomicInteger);
        assert atomicInteger.get() == stream.deserialize(data, AtomicInteger.class).get();

        AtomicIntegerArray intArray = new AtomicIntegerArray(new int[]{0, 100, -100, Integer.MIN_VALUE, Integer.MAX_VALUE});
        data = stream.serialize(intArray);
        AtomicIntegerArray intArray2 = stream.deserialize(data, AtomicIntegerArray.class);
        for (int i = 0; i < intArray.length(); i++) {
            assert intArray.get(i) == intArray2.get(i);
        }

        AtomicLongArray longArray = new AtomicLongArray(new long[]{0, 1000, -1000, Long.MIN_VALUE, Long.MAX_VALUE});
        data = stream.serialize(longArray);
        AtomicLongArray longArray2 = stream.deserialize(data, AtomicLongArray.class);
        for (int i = 0; i < longArray.length(); i++) {
            assert longArray.get(i) == longArray2.get(i);
        }

        AtomicReference<String> stringRef = new AtomicReference<>("hello world");
        data = stream.serialize(stringRef);
        AtomicReference<String> stringRef2 = stream.deserialize(data, new TypeRef<AtomicReference<String>>() {
        });
        assert Objects.equals(stringRef.get(), stringRef2.get());

        DoubleAdder doubleAdder = new DoubleAdder();
        doubleAdder.add(RandomUtils.nextDouble());
        data = stream.serialize(doubleAdder);
        assert doubleAdder.doubleValue() == stream.deserialize(data, DoubleAdder.class).doubleValue();

        LongAdder longAdder = new LongAdder();
        longAdder.add(RandomUtils.nextLong());
        data = stream.serialize(longAdder);
        assert longAdder.longValue() == stream.deserialize(data, LongAdder.class).longValue();
    }

    @Test
    public void testWorld() throws IOException {
        World oldWorld = new World();
        World newWorld = null;

        try {
            data = stream.serialize(oldWorld);
            newWorld = stream.deserialize(data, World.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert Objects.equals(oldWorld, newWorld);
    }

    @Test
    public void testClose() throws IOException {
        SmartStream stream = new SmartStream();

        byte[] bytes = stream.serialize(null);
        assert stream.deserialize(bytes, Object.class) == null;

        stream.close();
        try {
            stream.serialize(null);
            assert false;
        } catch (Exception e) {
            assert e instanceof SmartBufClosedException;
        }

        try {
            stream.deserialize(bytes, Object.class);
            assert false;
        } catch (Exception e) {
            assert e instanceof SmartBufClosedException;
        }

        try {
            stream.deserialize(bytes, new TypeRef<List<Long>>() {
            });
            assert false;
        } catch (Exception e) {
            assert e instanceof SmartBufClosedException;
        }

        stream.close();
    }

}
