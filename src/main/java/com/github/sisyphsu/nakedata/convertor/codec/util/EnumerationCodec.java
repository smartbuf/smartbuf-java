package com.github.sisyphsu.nakedata.convertor.codec.util;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Enumeration's codec
 *
 * @author sulin
 * @since 2019-05-13 18:48:23
 */
public class EnumerationCodec extends Codec {

    /**
     * Convert List to Enumeration, wrap an Inner Enumeration.
     *
     * @param l List
     * @param t Type
     * @return Enumeration
     */
    public Enumeration toEnumeration(List l, Type t) {
        Iterator it = l.iterator();
        Type genericType = getGenericType(t);
        return new Enumeration() {
            @Override
            public boolean hasMoreElements() {
                return it.hasNext();
            }

            @Override
            public Object nextElement() {
                Object obj = it.next();
                return genericType == null ? obj : convert(obj, genericType);
            }
        };
    }

    /**
     * Convert Enumeration to List
     *
     * @param e Enumeration
     * @return List
     */
    public List toList(Enumeration e) {
        if (e == null)
            return null;

        List<Object> l = new ArrayList<>();
        while (e.hasMoreElements()) {
            l.add(e.nextElement());
        }
        return l;
    }

}
