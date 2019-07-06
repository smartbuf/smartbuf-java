package com.github.sisyphsu.nakedata.convertor.codec.ref;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

/**
 * Reference's codec
 *
 * @author sulin
 * @since 2019-05-13 18:39:05
 */
public class ReferenceCodec extends Codec {

    /**
     * Use SoftReference as default
     *
     * @param ref SoftReference
     * @return Reference
     */
    public Reference toReference(SoftReference ref) {
        return ref;
    }

    /**
     * Convert Reference to Object
     *
     * @param ref Reference
     * @return Object
     */
    public Object toObject(Reference ref) {
        if (ref == null)
            return null;
        return ref.get();
    }

}
