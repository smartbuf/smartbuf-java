package com.github.sisyphsu.nakedata.convertor.codec.atomic;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Convert everything to AtomicReference
 *
 * @author sulin
 * @since 2019-05-13 18:38:45
 */
public class AtomicReferenceCodec extends Codec {

    /**
     * Convert everything to AtomicReference
     *
     * @param obj  Source Object
     * @param type Reference Type
     * @return AtomicReference
     */
    @Converter
    public AtomicReference<?> toAtomicReference(Object obj, XType<AtomicReference> type) {
        if (obj == null) {
            return null;
        }
        XType<?> refType = type.getParameterizedType();
        if (refType == null) {
            return new AtomicReference<>(obj); // no generic, use Object directly
        }
        if (refType.isPure() && refType.getRawType().isAssignableFrom(obj.getClass())) {
            return new AtomicReference<>(obj); // compatible
        }
        return new AtomicReference<>(convert(obj, refType));
    }

}
