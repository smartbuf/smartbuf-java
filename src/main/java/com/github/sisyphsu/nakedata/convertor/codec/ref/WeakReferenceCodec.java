package com.github.sisyphsu.nakedata.convertor.codec.ref;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

/**
 * WeakReference's codec
 *
 * @author sulin
 * @since 2019-05-13 18:39:45
 */
public class WeakReferenceCodec extends Codec {

    /**
     * Convert Any Object to WeakReference
     *
     * @param o Object
     * @param t Type
     * @return WeakReference
     */
    public WeakReference toWeakReference(Object o, Type t) {
        if (o == null)
            return null;
        Type genericType = getGenericType(t);
        if (genericType == null) {
            return new WeakReference<>(o);
        }
        return new WeakReference<>(convert(o, genericType));
    }

    /**
     * Convert WeakReference to Object
     *
     * @param ref WeakReference
     * @return Object
     */
    public Object toObject(WeakReference ref) {
        if (ref == null)
            return null;
        return ref.get();
    }

}
