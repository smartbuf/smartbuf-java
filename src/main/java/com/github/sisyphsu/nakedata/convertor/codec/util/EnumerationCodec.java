package com.github.sisyphsu.nakedata.convertor.codec.util;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.*;

/**
 * Enumeration's codec
 * This codec don't handle generic type, which should be processed by CollectionCodec
 *
 * @author sulin
 * @since 2019-05-13 18:48:23
 */
public class EnumerationCodec extends Codec {

    /**
     * Convert Collection to Enumeration
     *
     * @param coll List
     * @return Enumeration
     */
    @Converter
    public Enumeration toEnumeration(Collection<?> coll) {
        if (coll == null)
            return null;

        return Collections.enumeration(coll);
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

        List<Object> result = new ArrayList<>();
        while (e.hasMoreElements()) {
            result.add(e.nextElement());
        }
        return result;
    }

}
