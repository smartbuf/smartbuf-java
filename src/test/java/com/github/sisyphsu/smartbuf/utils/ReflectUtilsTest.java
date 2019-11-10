package com.github.sisyphsu.smartbuf.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

/**
 * @author sulin
 * @since 2019-11-09 19:55:16
 */
public class ReflectUtilsTest {

    @Test
    public void test() throws NoSuchFieldException {
        assert ReflectUtils.findAllValidFields(null).isEmpty();
        assert ReflectUtils.findAllValidFields(boolean.class).isEmpty();
        assert !ReflectUtils.findAllValidFields(Boolean.class).isEmpty();

        Field field = Bean.class.getDeclaredField("blocked");
        assert ReflectUtils.findGetter(Bean.class, field) != null;
        assert ReflectUtils.findSetter(Bean.class, field) != null;

        field = Bean.class.getDeclaredField("is_valid");
        assert ReflectUtils.findGetter(Bean.class, field) != null;
        assert ReflectUtils.findSetter(Bean.class, field) != null;

        field = Bean.class.getDeclaredField("is_visible");
        assert ReflectUtils.findGetter(Bean.class, field) != null;
        assert ReflectUtils.findSetter(Bean.class, field) != null;

        field = Bean.class.getDeclaredField("is_ok");
        assert ReflectUtils.findGetter(Bean.class, field) == null;
        assert ReflectUtils.findSetter(Bean.class, field) == null;
    }

    public static class Bean {
        private boolean blocked;
        private boolean is_valid;
        private boolean is_visible;
        private boolean is_ok;

        public boolean isBlocked() {
            return blocked;
        }

        public void setBlocked(boolean blocked) {
            this.blocked = blocked;
        }

        public boolean isIs_valid() {
            return is_valid;
        }

        public void setIs_valid(boolean is_valid) {
            this.is_valid = is_valid;
        }

        public boolean isIs_visible() {
            return is_visible;
        }

        public void setIs_visible(boolean is_visible) {
            this.is_visible = is_visible;
        }

        @Deprecated
        public boolean isIs_ok() {
            return is_ok;
        }

        public static void setIs_ok(boolean is_ok) {
        }
    }

}
