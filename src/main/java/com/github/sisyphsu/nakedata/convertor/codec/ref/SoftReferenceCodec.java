package com.github.sisyphsu.nakedata.convertor.codec.ref;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.lang.ref.SoftReference;

/**
 * SoftReference's codec
 *
 * @author sulin
 * @since 2019-05-13 18:39:34
 */
public class SoftReferenceCodec extends Codec {

    /**
     * Convert Any Object to SoftReference
     *
     * @param o Object
     * @param t Type
     * @return SoftReference
     */
    public SoftReference toSoftReference(Object o, XType t) {
        if (o == null)
            return null;

        XType genericType = t.getParameterizedType();
        if (genericType == null || genericType.getRawType() == Object.class) {
            return new SoftReference<>(o);
        }
        return new SoftReference<>(convert(o, genericType));
    }

    /**
     * Convert SoftReference to Object
     *
     * @param ref SoftReference
     * @return Object
     */
    public Object toObject(SoftReference ref) {
        if (ref == null)
            return null;

        return ref.get();
    }

}
