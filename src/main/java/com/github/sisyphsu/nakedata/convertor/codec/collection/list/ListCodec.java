package com.github.sisyphsu.nakedata.convertor.codec.collection.list;

import com.github.sisyphsu.nakedata.convertor.codec.collection.CollectionCodec;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * List's codec
 *
 * @author sulin
 * @since 2019-05-13 18:40:44
 */
public class ListCodec extends CollectionCodec {

    /**
     * Convert Collection to List, use ArrayList as default implementation.
     *
     * @param data Collection
     * @param type Type
     * @return List
     */
    public List toList(Collection data, Type type) {
        if (checkCompatible(data, type)) {
            return (List) data;
        }
        return execCopy(data, new ArrayList(data.size()), type);
    }

}
