package com.github.sisyphsu.nakedata.convertor.codec.collection.list;

import com.github.sisyphsu.nakedata.convertor.codec.collection.CollectionCodec;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWriteArrayList's codec
 *
 * @author sulin
 * @since 2019-07-10 21:30:38
 */
public class CopyOnWriteArrayListCodec extends CollectionCodec {

    /**
     * Convert Collection to CopyOnWriteArrayList
     *
     * @param data Collection
     * @param type Type
     * @return CopyOnWriteArrayList
     */
    public CopyOnWriteArrayList toCopyOnWriteArrayList(Collection data, Type type) {
        if (checkCompatible(data, type)) {
            return (CopyOnWriteArrayList) data;
        }
        return execCopy(data, new CopyOnWriteArrayList(), type);
    }

}
