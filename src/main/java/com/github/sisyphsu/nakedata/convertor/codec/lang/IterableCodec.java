package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

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
     * NOTICE: User List as Default, List's codec should support GenericType
     *
     * @param l List
     * @return Iterable
     */
    public Iterable toIterable(List l) {
        return l;
    }

    /**
     * Convert Iterable to Iterator
     *
     * @param it Iterable
     * @return Iterator
     */
    public Iterator toIterator(Iterable it, Type type) {
        if (it == null)
            return null;
        Iterator iterator = it.iterator();
        Type genericType = getGenericType(type);
        return new Iterator() {
            public boolean hasNext() {
                return iterator.hasNext();
            }

            public Object next() {
                Object obj = iterator.next();
                return genericType == null ? obj : convert(obj, genericType);
            }
        };
    }

}
