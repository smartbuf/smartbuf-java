package com.github.sisyphsu.nakedata.convertor.codec.collection;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Collection's codec
 *
 * @author sulin
 * @since 2019-05-13 18:40:23
 */
public class CollectionCodec extends Codec {

    /**
     * User List as Collection's default implementation.
     *
     * @param list List
     * @return Collection
     */
    public Collection toCollection(List list) {
        return list;
    }

    /**
     * Convert Collection to List
     *
     * @param c Collection
     * @return List
     */
    public List toList(Collection c) {
        if (c == null)
            return null;
        if (c instanceof List)
            return (List) c;
        return new ArrayList<Object>(c);
    }

    /**
     * Copy Src to Tgt collection, and execute Item convert.
     */
    @SuppressWarnings("unchecked")
    protected <T extends Collection> T execCopy(Collection src, T tgt, Type tgtType) {
        Type genericType = getGenericType(tgtType);
        for (Object o : src) {
            tgt.add(convert(o, genericType));
        }
        return tgt;
    }

    /**
     * Check whether src is compatible with tgtType
     */
    protected boolean checkCompatible(Collection src, Type tgtType) {
        if (src == null)
            return true; // null compatible with everything
        // check class, empty special
        // check generic type, and object type compare
        return false;
    }

}
