package com.github.sisyphsu.datube.reflect;

import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author sulin
 * @since 2019-09-20 15:34:39
 */
public class XTypeFactoryTest<T> {

    @Test
    public void toXType() {
    }

    @Test
    public void errorCase() {
        // Unsupported Type
        try {
            XTypeUtils.toXType(new Type() {
            });
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }

        // Unresolved TypeVariable
        try {
            XTypeUtils.toXType(new Object() {
                private T[] ts;
            }.getClass());
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        // Invalid ParameterizedType
        try {
            XTypeUtils.toXType(new ParameterizedType() {
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
            XTypeUtils.toXType(new ParameterizedType() {
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
