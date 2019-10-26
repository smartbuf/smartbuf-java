package com.github.sisyphsu.canoe;

import com.github.sisyphsu.canoe.reflect.TypeRef;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.*;

/**
 * @author sulin
 * @since 2019-10-26 14:01:01
 */
public class PacketTest {

    @Test
    public void testWriter() throws IOException {
        final int size = 1 << 24;
        CanoePacket.ByteArrayWriter writer = new CanoePacket.ByteArrayWriter();
        for (int i = 0; i < size; i++) {
            writer.write((byte) i);
        }
        byte[] bs = writer.toByteArray();
        for (int i = 0; i < size; i++) {
            assert bs[i] == (byte) i;
        }
        assert bs.length == size;
    }

    @Test
    public void testSimple() throws IOException {
        byte[] data;
        data = CanoePacket.serialize(null);
        assert CanoePacket.deserialize(data, Object.class) == null;

        data = CanoePacket.serialize(false);
        assert !CanoePacket.deserialize(data, Boolean.class);

        data = CanoePacket.serialize(Byte.MIN_VALUE);
        assert CanoePacket.deserialize(data, Byte.class) == Byte.MIN_VALUE;

        data = CanoePacket.serialize(Short.MIN_VALUE);
        assert CanoePacket.deserialize(data, Short.class) == Short.MIN_VALUE;

        data = CanoePacket.serialize(Integer.MIN_VALUE);
        assert CanoePacket.deserialize(data, Integer.class) == Integer.MIN_VALUE;

        data = CanoePacket.serialize(Long.MIN_VALUE);
        assert CanoePacket.deserialize(data, Long.class) == Long.MIN_VALUE;

        data = CanoePacket.serialize(Float.MIN_VALUE);
        assert CanoePacket.deserialize(data, Float.class) == Float.MIN_VALUE;

        data = CanoePacket.serialize(Double.MIN_VALUE);
        assert CanoePacket.deserialize(data, Double.class) == Double.MIN_VALUE;

        data = CanoePacket.serialize("hello world");
        assert Objects.equals(CanoePacket.deserialize(data, String.class), "hello world");

        data = CanoePacket.serialize(Thread.State.BLOCKED);
        assert Objects.equals(CanoePacket.deserialize(data, Thread.State.class), Thread.State.BLOCKED);
    }

    @Test
    public void testArray() throws IOException {
        byte[] data;

        boolean[] booleans = new boolean[]{true, false, false, true, true, true, false, false, false, true, true};
        data = CanoePacket.serialize(booleans);
        assert Objects.deepEquals(booleans, CanoePacket.deserialize(data, boolean[].class));

        byte[] bytes = RandomUtils.nextBytes(1025);
        data = CanoePacket.serialize(bytes);
        assert Objects.deepEquals(bytes, CanoePacket.deserialize(data, byte[].class));

        short[] shorts = new short[]{0, 1, 10, 100, 1000, Short.MIN_VALUE, Short.MAX_VALUE};
        data = CanoePacket.serialize(shorts);
        assert Objects.deepEquals(shorts, CanoePacket.deserialize(data, short[].class));

        int[] ints = new int[]{0, 1, 10, 100, 1000, 10000, 100000, Integer.MIN_VALUE, Integer.MAX_VALUE};
        data = CanoePacket.serialize(ints);
        assert Objects.deepEquals(ints, CanoePacket.deserialize(data, int[].class));

        long[] longs = new long[]{0, 1, 10, 100, 10000, 10000000, Long.MIN_VALUE, Long.MAX_VALUE};
        data = CanoePacket.serialize(longs);
        assert Objects.deepEquals(longs, CanoePacket.deserialize(data, long[].class));

        float[] floats = new float[]{0, 0.1f, Float.MIN_VALUE, Float.MAX_VALUE};
        data = CanoePacket.serialize(floats);
        assert Objects.deepEquals(floats, CanoePacket.deserialize(data, float[].class));

        double[] doubles = new double[]{0, 0.1, Double.MIN_VALUE, Double.MAX_VALUE};
        data = CanoePacket.serialize(doubles);
        assert Objects.deepEquals(doubles, CanoePacket.deserialize(data, double[].class));

        Object[] objects = new Object[]{null, booleans, bytes, shorts, ints, longs, floats, doubles};
        data = CanoePacket.serialize(objects);
        assert Objects.deepEquals(objects, CanoePacket.deserialize(data, Object[].class));
    }

    @Test
    public void testObject() throws IOException {
        byte[] data;

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        data = CanoePacket.serialize(atomicBoolean);
        assert atomicBoolean.get() == CanoePacket.deserialize(data, AtomicBoolean.class).get();

        AtomicInteger atomicInteger = new AtomicInteger(RandomUtils.nextInt());
        data = CanoePacket.serialize(atomicInteger);
        assert atomicInteger.get() == CanoePacket.deserialize(data, AtomicInteger.class).get();

        AtomicIntegerArray intArray = new AtomicIntegerArray(new int[]{0, 100, -100, Integer.MIN_VALUE, Integer.MAX_VALUE});
        data = CanoePacket.serialize(intArray);
        AtomicIntegerArray intArray2 = CanoePacket.deserialize(data, AtomicIntegerArray.class);
        for (int i = 0; i < intArray.length(); i++) {
            assert intArray.get(i) == intArray2.get(i);
        }

        AtomicLongArray longArray = new AtomicLongArray(new long[]{0, 1000, -1000, Long.MIN_VALUE, Long.MAX_VALUE});
        data = CanoePacket.serialize(longArray);
        AtomicLongArray longArray2 = CanoePacket.deserialize(data, AtomicLongArray.class);
        for (int i = 0; i < longArray.length(); i++) {
            assert longArray.get(i) == longArray2.get(i);
        }

        AtomicReference<String> stringRef = new AtomicReference<>("hello world");
        data = CanoePacket.serialize(stringRef);
        AtomicReference<String> stringRef2 = CanoePacket.deserialize(data, new TypeRef<AtomicReference<String>>() {
        });
        assert Objects.equals(stringRef.get(), stringRef2.get());

        DoubleAdder doubleAdder = new DoubleAdder();
        doubleAdder.add(RandomUtils.nextDouble());
        data = CanoePacket.serialize(doubleAdder);
        assert doubleAdder.doubleValue() == CanoePacket.deserialize(data, DoubleAdder.class).doubleValue();

        LongAdder longAdder = new LongAdder();
        longAdder.add(RandomUtils.nextLong());
        data = CanoePacket.serialize(longAdder);
        assert longAdder.longValue() == CanoePacket.deserialize(data, LongAdder.class).longValue();
    }

    @Test
    public void testWorld() throws IOException {
        byte[] data;
        World oldWorld = new World();
        World newWorld = null;

        try {
            data = CanoePacket.serialize(oldWorld);
            newWorld = CanoePacket.deserialize(data, World.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert Objects.equals(oldWorld, newWorld);
    }

    @Test
    public void test2() {
    }

}
