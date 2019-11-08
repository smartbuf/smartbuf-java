package com.github.sisyphsu.canoe.reflect;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-11-08 19:42:15
 */
public class BeanReaderUtilsTest {

    @Test
    public void test() {
        BeanReader reader = BeanReaderUtils.build(Child.class);

        Child child = new Child();
        child._name = "_name";
        child.enable = true;
        child.name = "name";
        child.time = System.currentTimeMillis();

        String[] fieldNames = reader.getFieldNames();
        assert fieldNames.length == 5;
        assert Arrays.equals(fieldNames, new String[]{"_name", "enable", "ider", "name", "time"});

        Object[] arr = reader.getValues(child);
        assert Objects.equals(child._name, arr[0]);
        assert Objects.equals(child.enable, arr[1]);
        assert Objects.equals(child.getIder(), arr[2]);
        assert Objects.equals(child.getName(), arr[3]);
        assert Objects.equals(child.getTime(), arr[4]);
    }

    public static abstract class Parent {

        public String _name;
        @Deprecated
        public String $name;

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
    }

    @Data
    public static class Child extends Parent {
        private boolean enable;
        private String  name;
        private long    time;
    }

}
