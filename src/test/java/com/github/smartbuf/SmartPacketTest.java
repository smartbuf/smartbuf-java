package com.github.smartbuf;

import com.github.smartbuf.reflect.TypeRef;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.*;

/**
 * @author sulin
 * @since 2019-10-26 14:01:01
 */
public class SmartPacketTest {

    @Test
    public void testSimple() throws IOException {
        byte[] data;
        data = SmartPacket.serialize(null);
        assert SmartPacket.deserialize(data, Object.class) == null;

        data = SmartPacket.serialize(false);
        assert !SmartPacket.deserialize(data, Boolean.class);

        data = SmartPacket.serialize(Byte.MIN_VALUE);
        assert SmartPacket.deserialize(data, Byte.class) == Byte.MIN_VALUE;

        data = SmartPacket.serialize(Short.MIN_VALUE);
        assert SmartPacket.deserialize(data, Short.class) == Short.MIN_VALUE;

        data = SmartPacket.serialize(Integer.MIN_VALUE);
        assert SmartPacket.deserialize(data, Integer.class) == Integer.MIN_VALUE;

        data = SmartPacket.serialize(Long.MIN_VALUE);
        assert SmartPacket.deserialize(data, Long.class) == Long.MIN_VALUE;

        data = SmartPacket.serialize(Float.MIN_VALUE);
        assert SmartPacket.deserialize(data, Float.class) == Float.MIN_VALUE;

        data = SmartPacket.serialize(Double.MIN_VALUE);
        assert SmartPacket.deserialize(data, Double.class) == Double.MIN_VALUE;

        data = SmartPacket.serialize("hello world");
        assert Objects.equals(SmartPacket.deserialize(data, String.class), "hello world");

        data = SmartPacket.serialize(Thread.State.BLOCKED);
        assert Objects.equals(SmartPacket.deserialize(data, Thread.State.class), Thread.State.BLOCKED);
    }

    @Test
    public void testArray() throws IOException {
        byte[] data;

        boolean[] booleans = new boolean[]{true, false, false, true, true, true, false, false, false, true, true};
        data = SmartPacket.serialize(booleans);
        assert Objects.deepEquals(booleans, SmartPacket.deserialize(data, boolean[].class));

        byte[] bytes = RandomUtils.nextBytes(1025);
        data = SmartPacket.serialize(bytes);
        assert Objects.deepEquals(bytes, SmartPacket.deserialize(data, byte[].class));

        short[] shorts = new short[]{0, 1, 10, 100, 1000, Short.MIN_VALUE, Short.MAX_VALUE};
        data = SmartPacket.serialize(shorts);
        assert Objects.deepEquals(shorts, SmartPacket.deserialize(data, short[].class));

        int[] ints = new int[]{0, 1, 10, 100, 1000, 10000, 100000, Integer.MIN_VALUE, Integer.MAX_VALUE};
        data = SmartPacket.serialize(ints);
        assert Objects.deepEquals(ints, SmartPacket.deserialize(data, int[].class));

        long[] longs = new long[]{0, 1, 10, 100, 10000, 10000000, Long.MIN_VALUE, Long.MAX_VALUE};
        data = SmartPacket.serialize(longs);
        assert Objects.deepEquals(longs, SmartPacket.deserialize(data, long[].class));

        float[] floats = new float[]{0, 0.1f, Float.MIN_VALUE, Float.MAX_VALUE};
        data = SmartPacket.serialize(floats);
        assert Objects.deepEquals(floats, SmartPacket.deserialize(data, float[].class));

        double[] doubles = new double[]{0, 0.1, Double.MIN_VALUE, Double.MAX_VALUE};
        data = SmartPacket.serialize(doubles);
        assert Objects.deepEquals(doubles, SmartPacket.deserialize(data, double[].class));

        Object[] objects = new Object[]{null, booleans, bytes, shorts, ints, longs, floats, doubles};
        data = SmartPacket.serialize(objects);
        assert Objects.deepEquals(objects, SmartPacket.deserialize(data, Object[].class));
    }

    @Test
    public void testObject() throws IOException {
        byte[] data;

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        data = SmartPacket.serialize(atomicBoolean);
        assert atomicBoolean.get() == SmartPacket.deserialize(data, AtomicBoolean.class).get();

        AtomicInteger atomicInteger = new AtomicInteger(RandomUtils.nextInt());
        data = SmartPacket.serialize(atomicInteger);
        assert atomicInteger.get() == SmartPacket.deserialize(data, AtomicInteger.class).get();

        AtomicIntegerArray intArray = new AtomicIntegerArray(new int[]{0, 100, -100, Integer.MIN_VALUE, Integer.MAX_VALUE});
        data = SmartPacket.serialize(intArray);
        AtomicIntegerArray intArray2 = SmartPacket.deserialize(data, AtomicIntegerArray.class);
        for (int i = 0; i < intArray.length(); i++) {
            assert intArray.get(i) == intArray2.get(i);
        }

        AtomicLongArray longArray = new AtomicLongArray(new long[]{0, 1000, -1000, Long.MIN_VALUE, Long.MAX_VALUE});
        data = SmartPacket.serialize(longArray);
        AtomicLongArray longArray2 = SmartPacket.deserialize(data, AtomicLongArray.class);
        for (int i = 0; i < longArray.length(); i++) {
            assert longArray.get(i) == longArray2.get(i);
        }

        AtomicReference<String> stringRef = new AtomicReference<>("hello world");
        data = SmartPacket.serialize(stringRef);
        AtomicReference<String> stringRef2 = SmartPacket.deserialize(data, new TypeRef<AtomicReference<String>>() {
        }.getType());
        assert Objects.equals(stringRef.get(), stringRef2.get());
        stringRef2 = SmartPacket.deserialize(data, new TypeRef<AtomicReference<String>>() {
        });
        assert Objects.equals(stringRef.get(), stringRef2.get());

        DoubleAdder doubleAdder = new DoubleAdder();
        doubleAdder.add(RandomUtils.nextDouble());
        data = SmartPacket.serialize(doubleAdder);
        assert doubleAdder.doubleValue() == SmartPacket.deserialize(data, DoubleAdder.class).doubleValue();

        LongAdder longAdder = new LongAdder();
        longAdder.add(RandomUtils.nextLong());
        data = SmartPacket.serialize(longAdder);
        assert longAdder.longValue() == SmartPacket.deserialize(data, LongAdder.class).longValue();
    }

    @Test
    public void testStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SmartPacket.serialize("hello", outputStream);
        SmartPacket.serialize("world", outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String str1 = SmartPacket.deserialize(inputStream, String.class);
        Optional<String> str2 = SmartPacket.deserialize(inputStream, new TypeRef<Optional<String>>() {
        });

        assert str1.equals("hello");
        assert str2.get().equals("world");
    }

    @Test
    public void testError() {
        try {
            SmartPacket.deserialize(new byte[0], Object.class);
            assert false;
        } catch (IOException e) {
            assert e instanceof EOFException;
        }
    }

    @Test
    public void testWorld() throws IOException {
        byte[] data;
        World oldWorld = new World();
        World newWorld = null;

        try {
            data = SmartPacket.serialize(oldWorld);
            newWorld = SmartPacket.deserialize(data, World.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert Objects.equals(oldWorld, newWorld);
    }

}
