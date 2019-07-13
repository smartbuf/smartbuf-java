package com.github.sisyphsu.nakedata.convertor.codec.collection;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Set's codec
 *
 * @author sulin
 * @since 2019-07-13 11:26:50
 */
public class SetCodec extends CollectionCodec {

    /**
     * Convert Collection to Set
     *
     * @param src  Collection
     * @param type Set Type
     * @param <T>  Generic
     * @return Set
     */
    public <T extends Set> T toSet(Collection src, Type type) {
        if (src == null)
            return null;
        if (checkCompatible(src, type))
            return (T) src;
        // init Set
        Set set;
        Class clz = (Class) type;
        Type genericType = getGenericType(type);
        if (HashSet.class.isAssignableFrom(clz)) {
            set = new HashSet();
        } else if (TreeSet.class.isAssignableFrom(clz)) {
            set = new TreeSet();
        } else if (LinkedHashSet.class.isAssignableFrom(clz)) {
            set = new LinkedHashSet();
        } else if (EnumSet.class.isAssignableFrom(clz)) {
            set = EnumSet.noneOf((Class) genericType);
        } else if (CopyOnWriteArraySet.class.isAssignableFrom(clz)) {
            set = new CopyOnWriteArraySet();
        } else if (ConcurrentSkipListSet.class.isAssignableFrom(clz)) {
            set = new ConcurrentSkipListSet();
        } else {
            throw new IllegalArgumentException("");
        }
        // copy collection
        for (Object o : src) {
            set.add(convert(o, genericType));
        }
        return (T) set;
    }

}
