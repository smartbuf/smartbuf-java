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
     * Convert List to Iterable, User List as Default
     *
     * @param l List
     * @return Iterable
     */
    public Iterable toIterable(List l, Type type) {
        // List<R> to List<T>
        return l;
    }

    /**
     * Convert Iterable to Iterator
     *
     * @param it Iterable
     * @return Iterator
     */
    public Iterator toIterator(Iterable it) {
        if (it == null)
            return null;
        return it.iterator();
    }

}
