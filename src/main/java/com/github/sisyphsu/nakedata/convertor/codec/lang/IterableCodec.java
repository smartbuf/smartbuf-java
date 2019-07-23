package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.util.Collection;
import java.util.Iterator;

/**
 * Iterable's codec
 *
 * @author sulin
 * @since 2019-05-13 20:22:12
 */
public class IterableCodec extends Codec {

    /**
     * Convert List to Iterable
     * <p>
     * CollectionCodec should handle generic-type previously, so this converter don't need care.
     *
     * @param coll Compatible Collection
     * @return Iterable
     */
    @Converter
    public Iterable toIterable(Collection coll) {
        return coll;
    }

    /**
     * Convert Iterable to Iterator
     *
     * @param it Iterable
     * @return Iterator
     */
    @Converter
    public Iterator toIterator(Iterable it, XType<?> type) {
        if (it == null)
            return null;
        Iterator iterator = it.iterator();
        XType paramType = type.getParameterizedType();
        return new Iterator() {
            public boolean hasNext() {
                return iterator.hasNext();
            }

            public Object next() {
                Object obj = iterator.next();
                return paramType == null ? obj : convert(obj, paramType);
            }
        };
    }

}
