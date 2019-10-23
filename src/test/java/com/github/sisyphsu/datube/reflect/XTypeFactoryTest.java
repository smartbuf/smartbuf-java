package com.github.sisyphsu.datube.reflect;

import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;

/**
 * @author sulin
 * @since 2019-09-20 15:34:39
 */
public class XTypeFactoryTest<T> {

    private static final XTypeFactory factory = new XTypeFactory(Collections.emptyList());

    @Test
    public void stopClass() {
        XType type = factory.toXType(BitSet.class);
        assert type.getField("words") != null;

        XTypeFactory fac = new XTypeFactory(Arrays.asList(Number.class, BitSet.class));
        type = fac.toXType(BitSet.class);
        assert type.getField("words") == null;
    }

    @Test
    public void errorCase() {
        // Unsupported Type
        try {
            factory.toXType(new Type() {
            });
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }

        // Unresolved TypeVariable
        try {
            factory.toXType(new Object() {
                private T[] ts;
            }.getClass());
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        // Invalid ParameterizedType
        try {
            factory.toXType(new ParameterizedType() {
                @Override
                public Type[] getActualTypeArguments() {
                    return new Type[0];
                }

                @Override
                public Type getRawType() {
                    return null;
                }

                @Override
                public Type getOwnerType() {
                    return null;
                }
            });
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        // Invalid ParameterizedType
        try {
            factory.toXType(new ParameterizedType() {
                @Override
                public Type[] getActualTypeArguments() {
                    return new Type[1];
                }

                @Override
                public Type getRawType() {
                    return Object.class;
                }

                @Override
                public Type getOwnerType() {
                    return null;
                }
            });
        } catch (Exception e) {
            assert e instanceof IllegalStateException;
        }
    }

}
