package com.github.sisyphsu.nakedata.convertor.codec.collection.list;

import com.github.sisyphsu.nakedata.convertor.codec.collection.CollectionCodec;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Vector;

/**
 * Vector's codec
 *
 * @author sulin
 * @since 2019-07-10 21:29:48
 */
public class VectorCodec extends CollectionCodec {

    /**
     * Convert Collection to Vector
     *
     * @param data Collection
     * @param type Type
     * @return Vector
     */
    public Vector toVector(Collection data, Type type) {
        if (checkCompatible(data, type)) {
            return (Vector) data;
        }
        return execCopy(data, new Vector(data.size()), type);
    }

}
