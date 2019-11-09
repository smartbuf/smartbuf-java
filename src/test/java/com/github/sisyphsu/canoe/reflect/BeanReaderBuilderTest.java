package com.github.sisyphsu.canoe.reflect;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-11-08 19:42:15
 */
public class BeanReaderBuilderTest {

    @Test
    public void test() {
        BeanReader reader = BeanReaderBuilder.build(Child.class);

        Child child = new Child();
        child._name = "_name";
        child.enable = true;
        child.name = "name";
        child.time = System.currentTimeMillis();

        String[] fieldNames = reader.getFieldNames();
        assert fieldNames.length == 4;
        assert Arrays.equals(fieldNames, new String[]{"_name", "enable", "name", "time"});

        Object[] arr = reader.getValues(child);
        assert Objects.equals(child._name, arr[0]);
        assert Objects.equals(child.enable, arr[1]);
        assert Objects.equals(child.getName(), arr[2]);
        assert Objects.equals(child.getTime(), arr[3]);
    }

    @Test
    public void testFull() {
        Full full = new Full();
        full.setV(null);

        BeanReader reader = BeanReaderBuilder.build(Full.class);
        assert reader.getFields().length == 24;
    }

    @Test
    public void testError() {
        String oldName = BeanReader.API_NAME;

        BeanReader.API_NAME = "...";
        try {
            BeanReaderBuilder.build(Full.class);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        BeanReader.API_NAME = oldName;
    }

    public static abstract class Parent {

        public String _name;
        @Deprecated
        public String $name;

        private long timestamp;

        public abstract long getTime();

        public static int getId() {
            return 0;
        }

        public int getIder() {
            return 1;
        }

        @Deprecated
        public String getDesc() {
            return null;
        }

        public String getTimestamp() {
            return "";
        }

    }

    @Data
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

        public String isValid() {
            return "true";
        }

        public String get_name() {
            return null;
        }

        public String get$name() {
            return null;
        }

    }

}
