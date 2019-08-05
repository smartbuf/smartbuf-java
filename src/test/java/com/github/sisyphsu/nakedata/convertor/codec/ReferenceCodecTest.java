package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import com.github.sisyphsu.nakedata.convertor.reflect.TypeRef;
import com.github.sisyphsu.nakedata.convertor.reflect.XTypeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * @author sulin
 * @since 2019-08-04 18:58:30
 */
public class ReferenceCodecTest {

    private ReferenceCodec codec = new ReferenceCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void test() {
        SoftReference<Long> ref = new SoftReference<>(10000L);
        Object i = codec.toObject(ref, XTypeUtils.toXType(Integer.class));
        assert i instanceof Integer;

        WeakReference<Long> newRef = (WeakReference<Long>) codec.toReference(i, XTypeUtils.toXType(new TypeRef<WeakReference<Long>>() {
        }.getType()));

        // shouldn't gc

        assert newRef != null;
        assert newRef.get().equals(ref.get());
    }

}