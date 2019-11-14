package com.github.smartbuf.converter.codec;

import com.github.smartbuf.converter.CodecFactory;
import com.github.smartbuf.reflect.TypeRef;
import com.github.smartbuf.reflect.XTypeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.Optional;

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

        Reference result = codec.toReference("", XTypeUtils.toXType(new TypeRef<SoftReference<Optional<String>>>() {
        }.getType()));
        assert result.get() instanceof Optional;
        assert Objects.equals("", ((Optional) result.get()).get());

        Reference ref2 = codec.toReference(0L, XTypeUtils.toXType(new TypeRef<Reference<Long>>() {
        }.getType()));
        assert Objects.equals(0L, ref2.get());
    }

    @Test
    public void testToObject() {
        SoftReference<Long> ref = new SoftReference<>(10000L);
        assert codec.toObject(ref, XTypeUtils.toXType(Integer.class)) instanceof Integer;
        assert codec.toObject(ref, XTypeUtils.toXType(Long.class)) == ref.get();

        SoftReference<String> ref2 = new SoftReference<>("");
        Object result = codec.toObject(ref2, XTypeUtils.toXType(new TypeRef<Optional<String>>() {
        }.getType()));
        assert result instanceof Optional;
        assert Objects.equals(((Optional) result).get(), "");
    }

}
