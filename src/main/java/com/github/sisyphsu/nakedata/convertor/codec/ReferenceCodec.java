package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Reference's codec
 *
 * @author sulin
 * @since 2019-05-13 18:39:05
 */
public class ReferenceCodec extends Codec {

    /**
     * Convert Any Object to Reference
     */
    @Converter
    public Reference toReference(Object obj, XType type) {
        XType<?> paramType = type.getParameterizedType();
        Object value;
        if (paramType.isPure() && paramType.getRawType().isAssignableFrom(obj.getClass())) {
            value = obj; // compatible
        } else {
            value = convert(obj, paramType); // convert
        }
        // build reference
        Class<?> refClass = type.getRawType();
        if (refClass == SoftReference.class || refClass == Reference.class) {
            return new SoftReference<>(value);
        }
        if (refClass == WeakReference.class) {
            return new WeakReference<>(value);
        }
        throw new IllegalArgumentException("Unsupport Reference: " + refClass);
    }

    /**
     * Convert Reference to Object
     *
     * @param ref Reference
     * @return Object
     */
    public Object toObject(Reference ref) {
        return ref.get();
    }

}
