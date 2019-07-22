package com.github.sisyphsu.nakedata.convertor.codec.util;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.util.ArrayList;
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
     * Convert List to Iterator
     *
     * @param list List
     * @param type Type
     * @return Iterator
     */
    public Iterator toIterator(List list, XType type) {
        if (list == null)
            return null;
        final Iterator it = list.iterator();
        XType<?> genericType = type.getParameterizedType();
        return new Iterator() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Object next() {
                Object obj = it.next();
                return genericType == null ? obj : convert(obj, genericType);
            }
        };
    }

    /**
     * Convert Iterator to List
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
