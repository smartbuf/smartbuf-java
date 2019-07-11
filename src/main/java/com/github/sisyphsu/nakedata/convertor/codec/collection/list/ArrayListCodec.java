package com.github.sisyphsu.nakedata.convertor.codec.collection.list;

import com.github.sisyphsu.nakedata.convertor.codec.collection.CollectionCodec;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * ArrayList's codec.
 * <p>
 * Only provide decode(toArrayList) function, encode function in ListCodec
 *
 * @author sulin
 * @since 2019-05-13 18:40:54
 */
public class ArrayListCodec extends CollectionCodec {

    /**
     * Convert List to ArrayList
     *
     * @param arr  Source List
     * @param type Target Type
     * @return ArrayList
     */
    @Deprecated
    public ArrayList toArrayList(ArrayList arr, Type type) {
        if (arr == null)
            return null;
        Type genericType = getGenericType(type);
        if (genericType == null || genericType == Object.class) {
            return arr; // don't need convert
        }
        boolean compatible = true;
        for (Object o : arr) {
            if (o.getClass() != genericType && !o.getClass().isAssignableFrom((Class<?>) genericType)) {
                compatible = false;
                break;
            }
        }
        if (compatible) {
            return arr;
        }
        // route to Collection and execute copy
        return this.execCopy(arr, new ArrayList(arr.size()), type);
    }

    /**
     * Convert Collection to ArrayList
     *
     * @param data Collection
     * @param type Type with generic
     * @return ArrayList
     */
    public ArrayList toArrayList(Collection data, Type type) {
        if (checkCompatible(data, type)) {
            return (ArrayList) data;
        }
        return this.execCopy(data, new ArrayList(data.size()), type);
    }

}
