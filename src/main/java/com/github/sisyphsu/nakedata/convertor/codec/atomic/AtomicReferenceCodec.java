package com.github.sisyphsu.nakedata.convertor.codec.atomic;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.lang.reflect.Type;
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
     * @param o Source Object
     * @param t Reference Type
     * @return AtomicReference
     */
    public AtomicReference<?> toAtomicReference(Object o, Type t) {
        if (o == null)
            return null;
        Type genericType = getGenericType(t);
        if (genericType == null) {
            return new AtomicReference<>(o); // no generic, use Object directly
        }
        return new AtomicReference<>(convert(o, genericType));
    }

}
