package com.github.sisyphsu.nakedata.convertor.codec.util;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Iterator's codec
 *
 * @author sulin
 * @since 2019-05-13 20:23:01
 */
public class IteratorCodec extends Codec {

    /**
     * Convert Collection to Iterator
     * Collection is compatible with Iterator, CollectionCodec will handle generic type.
     *
     * @param coll Collection
     * @param type Type
     * @return Iterator
     */
    @Converter
    public Iterator toIterator(Collection coll, XType type) {
        return coll == null ? null : coll.iterator();
    }

    /**
     * Convert Iterator to List as default collection, CollectionCodec will handle generic type.
     *
     * @param it Iterator
     * @return List
     */
    public List toList(Iterator it) {
        if (it == null)
            return null;

        List<Object> list = new ArrayList<>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }

}
