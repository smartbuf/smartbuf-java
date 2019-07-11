package com.github.sisyphsu.nakedata.convertor.codec.collection.list;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.convertor.codec.collection.CollectionCodec;

import java.lang.reflect.Type;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;

/**
 * AbstractList's codec
 *
 * @author sulin
 * @since 2019-07-10 21:27:09
 */
public class AbstractListCodec extends CollectionCodec {

    /**
     * Convert Collection to AbstractList, use ArrayList as default
     *
     * @param data Collection
     * @param type Type
     * @return AbstractList
     */
    @Converter
    public AbstractList toAbstractList(Collection data, Type type) {
        if (checkCompatible(data, type)) {
            return (AbstractList) data;
        }
        return execCopy(data, new ArrayList(data.size()), type);
    }

}
