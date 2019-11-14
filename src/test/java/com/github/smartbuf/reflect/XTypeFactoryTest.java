package com.github.smartbuf.reflect;

import com.github.smartbuf.exception.CircleReferenceException;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.ref.Reference;
import java.lang.reflect.*;
import java.util.BitSet;

/**
 * @author sulin
 * @since 2019-09-20 15:34:39
 */
public class XTypeFactoryTest<T> {

    @Test
    public void stopClass() {
        XType type = XTypeUtils.toXType(BitSet.class);
        assert type.getField("words") != null;

        XTypeFactory fac = new XTypeFactory().addStopClass(Number.class, BitSet.class);
        type = fac.toXType(BitSet.class);
        assert type.getField("words") == null;

        try {
            fac.toXType(Reference.class);
        } catch (Exception e) {
            assert e instanceof CircleReferenceException;
        }
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

        try {
            XTypeUtils.toXType(new TypeVariableImpl2());
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

        try {
            XTypeUtils.toXType(new WildcardType() {
                @Override
                public Type[] getUpperBounds() {
                    return new Type[2];
                }

                @Override
                public Type[] getLowerBounds() {
                    return new Type[2];
                }
            });
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

    static class TypeVariableImpl2 implements TypeVariable<Class> {
        @Override
        public Type[] getBounds() {
            return new Type[2];
        }

        @Override
        public Class getGenericDeclaration() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public AnnotatedType[] getAnnotatedBounds() {
            return new AnnotatedType[0];
        }

        @Override
        public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            return null;
        }

        @Override
        public Annotation[] getAnnotations() {
            return new Annotation[0];
        }

        @Override
        public Annotation[] getDeclaredAnnotations() {
            return new Annotation[0];
        }
    }
}
