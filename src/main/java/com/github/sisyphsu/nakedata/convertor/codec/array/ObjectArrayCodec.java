package com.github.sisyphsu.nakedata.convertor.codec.array;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

/**
 * 1. Convert everything to Object[] if can
 * 2. Convert Object[] to String
 *
 * @author sulin
 * @since 2019-05-13 18:53:04
 */
public class ObjectArrayCodec extends Codec<Object[]> {

    /**
     * Convert Object[] to T[] based on specified T Type.
     * For beter performance, some array shouldn't use this way, like byte[]...
     *
     * @param arr  Source Array
     * @param type Target Type
     * @return Target Array
     */
    public Object[] toArray(Object[] arr, Type type) {
        if (type == null) {
            return arr; // dont convert
        }
        if (!(type instanceof Class)) {
            throw new IllegalArgumentException("need T[].class to convert array: " + type);
        }
        Class clz = (Class) type;
        if (!clz.isArray()) {
            throw new IllegalArgumentException("need T[].class to convert array: " + type);
        }
        if (arr.getClass() == clz) {
            return arr; // same array type
        }
        return this.toArray(Arrays.asList(arr), type);
    }

    /**
     * Convert Collection to T[] based on specified T Type.
     *
     * @param list Source List
     * @param type Target Type
     * @return Target Array
     */
    public Object[] toArray(Collection list, Type type) {
        if (type == null) {
            return list.toArray();
        }
        if (!(type instanceof Class)) {
            throw new IllegalArgumentException("need T[].class to convert: " + type);
        }
        Class clz = (Class) type;
        if (!clz.isArray()) {
            throw new IllegalArgumentException("need T[].class to convert: " + type);
        }
        Class<?> itemType = clz.getComponentType();
        Object[] result = (Object[]) Array.newInstance(itemType, list.size());
        int i = 0;
        for (Object item : list) {
            if (item == null || (item.getClass() == itemType)) {
                result[i] = item; // copy directly
            } else {
                result[i] = convert(item, itemType); // need convert
            }
            i++;
        }
        return result;
    }

    public String toString(Object[] arr) {
        // TODO, toString, fromString?
        return "";
    }

}
