package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import com.github.sisyphsu.nakedata.reflect.TypeRef;
import com.github.sisyphsu.nakedata.reflect.XTypeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * @author sulin
 * @since 2019-08-04 18:58:30
 */
@SuppressWarnings("ALL")
public class ReferenceCodecTest {

    private ReferenceCodec codec = new ReferenceCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void testToObject() {
        SoftReference<Long> ref = new SoftReference<>(10000L);
        assert codec.toObject(ref, XTypeUtils.toXType(Integer.class)) instanceof Integer;
        assert codec.toObject(ref, XTypeUtils.toXType(Long.class)) == ref.get();
    }

    @Test
    public void testToReference() {
        int i = 1000;
        WeakReference<Long> ref = (WeakReference<Long>) codec.toReference(i, XTypeUtils.toXType(new TypeRef<WeakReference<Long>>() {
        }.getType()));

        // shouldn't gc
        assert ref != null;
        assert i == ref.get().intValue();

        SoftReference<Long> softRef = (SoftReference<Long>) codec.toReference(i, XTypeUtils.toXType(new TypeRef<SoftReference<Long>>() {
        }.getType()));

        // shouldn't gc
        assert softRef != null;
        assert i == softRef.get().intValue();

        try {
            codec.toReference(i, XTypeUtils.toXType(PhantomReference.class));
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

}