package com.github.sisyphsu.datube.reflect;

import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-09-20 14:24:25
 */
public class TypeRefTest {

    @Test
    public void testRef() {
        TypeRef ref = new TypeRef<byte[][]>() {
        };

        assert ref.getType() != null;

        try {
            new TypeRef() {
            };
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

    @Test
    public void testCompare() {
        TypeRef ref = new TypeRef<byte[][]>() {
        };
    }

}
