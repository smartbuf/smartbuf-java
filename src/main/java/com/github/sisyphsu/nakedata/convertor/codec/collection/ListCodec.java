package com.github.sisyphsu.nakedata.convertor.codec.collection;

import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * List's codec
 *
 * @author sulin
 * @since 2019-07-13 11:38:16
 */
public class ListCodec extends CollectionCodec {

    /**
     * Convert Collection to List
     *
     * @param src  Collection
     * @param type List's Type
     * @param <T>  Generic Type
     * @return List
     */
    public <T extends List> T toList(Collection src, XType type) {
        if (src == null)
            return null;
        if (checkCompatible(src, type)) {
            return (T) src;
        }
        // init List
        List result;
        Class clz = type.getRawType();
        if (ArrayList.class.isAssignableFrom(clz)) {
            result = new ArrayList();
        } else if (LinkedList.class.isAssignableFrom(clz)) {
            result = new LinkedList();
        } else if (Stack.class.isAssignableFrom(clz)) {
            result = new Stack();
        } else if (Vector.class.isAssignableFrom(clz)) {
            result = new Vector();
        } else if (CopyOnWriteArrayList.class.isAssignableFrom(clz)) {
            result = new CopyOnWriteArrayList();
        } else {
            throw new RuntimeException("");
        }
        // copy item
        XType genericType = type.getParameterizedType();
        for (Object o : src) {
            result.add(convert(o, genericType));
        }
        return (T) result;
    }

}
