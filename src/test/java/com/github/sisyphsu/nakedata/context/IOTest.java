package com.github.sisyphsu.nakedata.context;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-10-12 11:06:07
 */
public class IOTest {

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
