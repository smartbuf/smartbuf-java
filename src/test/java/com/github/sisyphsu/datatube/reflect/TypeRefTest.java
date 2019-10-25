package com.github.sisyphsu.datatube.reflect;

import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    public void test2() {
        TypeRef ref1 = new TypeRef<List<Long>>() {
        };
        TypeRef ref2 = new TypeRef<List<Long>>() {
        };
        TypeRef ref3 = new TypeRef<List<Number>>() {
        };

        assert ref1.getType() != ref2.getType();
        assert ref1.getType() != ref3.getType();
        assert ref3.getType() != ref2.getType();
    }

}
