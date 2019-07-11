package com.github.sisyphsu.nakedata.convertor.codec.collection.list;

import com.github.sisyphsu.nakedata.convertor.codec.collection.CollectionCodec;

import java.lang.reflect.Type;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * AbstractSequentialList's codec
 *
 * @author sulin
 * @since 2019-07-10 21:28:34
 */
public class AbstractSequentialListCodec extends CollectionCodec {

    /**
     * Convert Collection to AbstractSequentialList
     *
     * @param data Collection
     * @param type Type
     * @return AbstractSequentialList
     */
    public AbstractSequentialList toAbstractSequentialList(Collection data, Type type) {
        if (checkCompatible(data, type)) {
            return (AbstractSequentialList) data;
        }
        return execCopy(data, new LinkedList(), type);
    }

}
