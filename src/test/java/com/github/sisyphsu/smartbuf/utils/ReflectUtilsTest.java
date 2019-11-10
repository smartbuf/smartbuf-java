package com.github.sisyphsu.smartbuf.utils;

import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-11-09 19:55:16
 */
public class ReflectUtilsTest {

    @Test
    public void test() {
        assert ReflectUtils.findAllValidFields(null).isEmpty();
        assert ReflectUtils.findAllValidFields(boolean.class).isEmpty();
        assert !ReflectUtils.findAllValidFields(Boolean.class).isEmpty();
    }

}
