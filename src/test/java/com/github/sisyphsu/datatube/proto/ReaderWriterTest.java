package com.github.sisyphsu.datatube.proto;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.*;

/**
 * @author sulin
 * @since 2019-10-12 11:06:07
 */
public class ReaderWriterTest {

    @RepeatedTest(100)
    public void repeatBasic() throws IOException {
        this.basic();
    }

    @Test
    public void basic() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1 << 16);

        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add((byte) RandomUtils.nextInt());
            list.add(RandomUtils.nextLong());
            list.add(new ULong(RandomUtils.nextLong()));
            list.add(RandomUtils.nextFloat());
            list.add(RandomUtils.nextDouble());
            list.add(RandomStringUtils.random(RandomUtils.nextInt(10, 64)));
        }
        list.add(Byte.MIN_VALUE);
        list.add(Byte.MAX_VALUE);
        list.add((byte) 0);
        list.add(new ULong(0));
        list.add(new ULong(Long.MIN_VALUE));
        list.add(new ULong(Long.MAX_VALUE));
        list.add(0L);
        list.add(Long.MIN_VALUE);
        list.add(Long.MAX_VALUE);
        list.add(0f);
        list.add(Float.MIN_VALUE);
        list.add(Float.MAX_VALUE);
        list.add(0.0);
        list.add(Double.MIN_VALUE);
        list.add(Double.MAX_VALUE);
        list.add("");

        Collections.shuffle(list);

        OutputWriter output = new OutputWriter(outputStream);
        for (Object o : list) {
            if (o instanceof Byte) {
                output.writeByte((Byte) o);
            } else if (o instanceof Long) {
                output.writeVarInt((Long) o);
            } else if (o instanceof ULong) {
                output.writeVarUint(((ULong) o).val);
            } else if (o instanceof Float) {
                output.writeFloat((Float) o);
            } else if (o instanceof Double) {
                output.writeDouble((Double) o);
            } else if (o instanceof String) {
                output.writeString((String) o);
            } else {
                assert false;
            }
        }

        InputReader input = new InputReader(new ByteArrayInputStream(outputStream.toByteArray()));
        for (Object o : list) {
            if (o instanceof Byte) {
                assert input.readByte() == (Byte) o;
            } else if (o instanceof Long) {
                assert input.readVarInt() == (Long) o;
            } else if (o instanceof ULong) {
                assert input.readVarUint() == ((ULong) o).val;
            } else if (o instanceof Float) {
                assert input.readFloat() == (Float) o;
            } else if (o instanceof Double) {
                assert input.readDouble() == (Double) o;
            } else {
                assert input.readString().equals(o);
            }
        }
    }

    @Test
    public void testArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1 << 16);
        List<Object> list = new ArrayList<>();
        list.add(RandomUtils.nextBytes(RandomUtils.nextInt(10, 100)));
        list.add(new boolean[]{true, false, false, true, true, false, false, false, false, true, true, false, true});
        list.add(new short[]{0, Short.MAX_VALUE, Short.MIN_VALUE, (short) RandomUtils.nextInt()});
        list.add(new int[]{0, Integer.MAX_VALUE, Integer.MIN_VALUE, RandomUtils.nextInt()});
        list.add(new long[]{0, Long.MIN_VALUE, Long.MAX_VALUE, RandomUtils.nextLong()});
        list.add(new float[]{0, Float.MIN_VALUE, Float.MAX_VALUE, RandomUtils.nextFloat()});
        list.add(new double[]{0, Double.MIN_VALUE, Double.MAX_VALUE, RandomUtils.nextDouble()});
        Collections.shuffle(list);

        OutputWriter output = new OutputWriter(outputStream);
        for (Object o : list) {
            if (o instanceof boolean[]) {
                output.writeBooleanArray((boolean[]) o);
            } else if (o instanceof byte[]) {
                output.writeByteArray((byte[]) o);
            } else if (o instanceof short[]) {
                output.writeShortArray((short[]) o);
            } else if (o instanceof int[]) {
                output.writeIntArray((int[]) o);
            } else if (o instanceof long[]) {
                output.writeLongArray((long[]) o);
            } else if (o instanceof float[]) {
                output.writeFloatArray((float[]) o);
            } else if (o instanceof double[]) {
                output.writeDoubleArray((double[]) o);
            } else {
                assert false;
            }
        }

        InputReader input = new InputReader(new ByteArrayInputStream(outputStream.toByteArray()));
        for (Object o : list) {
            if (o instanceof boolean[]) {
                assert Objects.deepEquals(o, input.readBooleanArray(((boolean[]) o).length));
            } else if (o instanceof byte[]) {
                assert Objects.deepEquals(o, input.readByteArray(((byte[]) o).length));
            } else if (o instanceof short[]) {
                assert Objects.deepEquals(o, input.readShortArray(((short[]) o).length));
            } else if (o instanceof int[]) {
                assert Objects.deepEquals(o, input.readIntArray(((int[]) o).length));
            } else if (o instanceof long[]) {
                assert Objects.deepEquals(o, input.readLongArray(((long[]) o).length));
            } else if (o instanceof float[]) {
                assert Objects.deepEquals(o, input.readFloatArray(((float[]) o).length));
            } else if (o instanceof double[]) {
                assert Objects.deepEquals(o, input.readDoubleArray(((double[]) o).length));
            } else {
                assert false;
            }
        }
    }

    @Test
    public void testSlice() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1 << 16);

        List<Boolean> booleans = Arrays.asList(true, false, false, true, true, false, false, false, false, true, true, false, true);
        List<Byte> bytes = Arrays.asList((byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) RandomUtils.nextInt());
        List<Short> shorts = Arrays.asList((short) 0, Short.MIN_VALUE, Short.MAX_VALUE, (short) RandomUtils.nextInt());
        List<Integer> ints = Arrays.asList(0, Integer.MIN_VALUE, Integer.MAX_VALUE, RandomUtils.nextInt());
        List<Long> longs = Arrays.asList(0L, Long.MIN_VALUE, Long.MAX_VALUE, RandomUtils.nextLong());
        List<Float> floats = Arrays.asList(0.0f, Float.MIN_VALUE, Float.MAX_VALUE, RandomUtils.nextFloat());
        List<Double> doubles = Arrays.asList(0.0, Double.MIN_VALUE, Double.MAX_VALUE, RandomUtils.nextDouble());

        OutputWriter output = new OutputWriter(outputStream);
        output.writeBooleanSlice(booleans);
        output.writeByteSlice(bytes);
        output.writeShortSlice(shorts);
        output.writeIntSlice(ints);
        output.writeLongSlice(longs);
        output.writeFloatSlice(floats);
        output.writeDoubleSlice(doubles);

        InputReader input = new InputReader(new ByteArrayInputStream(outputStream.toByteArray()));
        assert Objects.deepEquals(booleans.toArray(), input.readBooleanSlice(booleans.size()));
        assert Objects.deepEquals(bytes.toArray(), input.readByteSlice(bytes.size()));
        assert Objects.deepEquals(shorts.toArray(), input.readShortSlice(shorts.size()));
        assert Objects.deepEquals(ints.toArray(), input.readIntSlice(ints.size()));
        assert Objects.deepEquals(longs.toArray(), input.readLongSlice(longs.size()));
        assert Objects.deepEquals(floats.toArray(), input.readFloatSlice(floats.size()));
        assert Objects.deepEquals(doubles.toArray(), input.readDoubleSlice(doubles.size()));
    }

    @Test
    public void testError() {
        InputReader input = new InputReader(new ByteArrayInputStream(new byte[2]));
        try {
            input.readDouble();
            assert false;
        } catch (Exception e) {
            assert e instanceof EOFException;
        }

        byte[] bytes = new byte[20];
        Arrays.fill(bytes, (byte) 0xFF);
        input = new InputReader(new ByteArrayInputStream(bytes));
        try {
            input.readVarInt();
            assert false;
        } catch (Exception e) {
            assert e instanceof IOException;
        }
    }

    @Test
    public void test() throws IOException {
        String val = "HKJWipzmpSwv4PberSjUkSHfZodbVvhQmPAB1nwHoExYHmbD4bZj9";
        System.out.println(val);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1 << 16);
        OutputWriter output = new OutputWriter(outputStream);
        output.writeString(val);

        InputReader input = new InputReader(new ByteArrayInputStream(outputStream.toByteArray()));
        String tmp = input.readString();
        System.out.println(tmp);

        assert Objects.equals(tmp, val);
    }

    @Test
    public void testVarint() {
        long l = Integer.MAX_VALUE;
        long rev = (l << 1) ^ (l >> 63);
        System.out.println(l);
        System.out.println(rev);
        System.out.println((rev >>> 1) ^ -(rev & 1));
    }

    static class ULong {
        long val;

        public ULong(long val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return String.valueOf(val);
        }
    }

}
