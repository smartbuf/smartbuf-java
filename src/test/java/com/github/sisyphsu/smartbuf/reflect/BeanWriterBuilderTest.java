package com.github.sisyphsu.smartbuf.reflect;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-11-09 15:00:28
 */
public class BeanWriterBuilderTest {

    @Test
    public void test() {
        BeanWriter writer = BeanWriterBuilder.build(Child.class);

        assert writer.getFields().length == 4;
        assert writer.getFields()[0].name.equals("_name");
        assert writer.getFields()[1].name.equals("enable");
        assert writer.getFields()[2].name.equals("name");
        assert writer.getFields()[3].name.equals("time");

        Object[] values = new Object[]{"_name", true, "name", System.currentTimeMillis()};
        Child child = new Child();
        writer.setValues(child, values);

        assert Objects.equals(child._name, values[0]);
        assert Objects.equals(child.enable, values[1]);
        assert Objects.equals(child.name, values[2]);
        assert Objects.equals(child.time, values[3]);
    }

    @Test
    public void testFull() {
        BeanWriter writer = BeanWriterBuilder.build(Full.class);

        assert writer.getFields().length == 24;

        Full full = new Full();

        try {
            writer.setValues(full, null);
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        try {
            writer.setValues(full, new Object[1]);
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        Object[] obj = new Object[]{
            false, true, new boolean[]{true},
            Byte.MIN_VALUE, Byte.MAX_VALUE, new byte[]{1},
            'a', 'Z', new char[]{1},
            1.1, 1.2, new double[]{1},
            1.0f, 2.0f, new float[]{1},
            Integer.MIN_VALUE, Integer.MAX_VALUE, new int[]{1},
            Long.MIN_VALUE, Long.MAX_VALUE, new long[]{1},
            Short.MIN_VALUE, Short.MAX_VALUE, new short[]{1}
        };

        writer.setValues(full, obj);

        assert Objects.equals(obj[0], full.bool1);
        assert Objects.equals(obj[1], full.bool2);
        assert Objects.equals(obj[2], full.booleans);

        assert Objects.equals(obj[3], full.byte1);
        assert Objects.equals(obj[4], full.byte2);
        assert Objects.equals(obj[5], full.bytes);

        assert Objects.equals(obj[6], full.char1);
        assert Objects.equals(obj[7], full.char2);
        assert Objects.equals(obj[8], full.chars);

        assert Objects.equals(obj[9], full.double1);
        assert Objects.equals(obj[10], full.double2);
        assert Objects.equals(obj[11], full.doubles);

        assert Objects.equals(obj[12], full.float1);
        assert Objects.equals(obj[13], full.float2);
        assert Objects.equals(obj[14], full.floats);

        assert Objects.equals(obj[15], full.int1);
        assert Objects.equals(obj[16], full.int2);
        assert Objects.equals(obj[17], full.ints);

        assert Objects.equals(obj[18], full.long1);
        assert Objects.equals(obj[19], full.long2);
        assert Objects.equals(obj[20], full.longs);

        assert Objects.equals(obj[21], full.short1);
        assert Objects.equals(obj[22], full.short2);
        assert Objects.equals(obj[23], full.shorts);
    }

    public static abstract class Parent {
        @Deprecated
        public  String $name;
        public  String _name;
        private long   timestamp;

        public void setIder(int id) {
        }

        @Deprecated
        public void setTimestamp(long time) {
            this.timestamp = time;
        }

        public static void setTimestamp(Date date) {
        }

        public void setTimestamp(String time) {
            this.timestamp = Long.parseLong(time);
        }
    }

    @Getter
    @Setter
    public static class Child extends Parent {
        private boolean enable;
        private String  name;
        private long    time;
    }

    @Data
    public static class Full {
        private boolean   bool1;
        private Boolean   bool2;
        private float     float1;
        private Float     float2;
        private double    double1;
        private Double    double2;
        private byte      byte1;
        private Byte      byte2;
        private short     short1;
        private Short     short2;
        private int       int1;
        private Integer   int2;
        private long      long1;
        private Long      long2;
        private char      char1;
        private Character char2;
        private boolean[] booleans;
        private byte[]    bytes;
        private short[]   shorts;
        private int[]     ints;
        private long[]    longs;
        private float[]   floats;
        private double[]  doubles;
        private char[]    chars;
        private Void      v;

        public void isValid(String String) {
        }

        public void set_name(String String) {
        }

        public void set$name() {
        }
    }

}
