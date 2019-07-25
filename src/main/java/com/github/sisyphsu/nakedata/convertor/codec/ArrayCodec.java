package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;
import com.github.sisyphsu.nakedata.convertor.reflect.XTypeUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

/**
 * Convert everything to Object[] if could
 *
 * @author sulin
 * @since 2019-05-13 18:53:04
 */
public class ArrayCodec extends Codec {

    /**
     * Convert Object[] to T[] based on specified T Type.
     * For beter performance, some array shouldn't use this way, like byte[]...
     *
     * @param arr  Source Array
     * @param type Target Type
     * @return Target Array
     */
    @Converter
    public Object[] toArray(Object[] arr, XType<?> type) {
        if (type == null) {
            return arr; // dont convert
        }
        if (type.isPure()) {
            Class<?> srcArrClass = arr.getClass();
            Class<?> tgtArrClass = type.getRawType();
            if (tgtArrClass.isAssignableFrom(srcArrClass)) {
                return arr; // can assign arr to be type.
            }
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
    @Converter
    public Object[] toArray(Collection list, XType<?> type) {
        if (list == null)
            return null;
        if (type == null) {
            return list.toArray();
        }
        XType<?> itemType = type.getComponentType();
        if (itemType == null) {
            itemType = XTypeUtils.toXType(type.getRawType().getComponentType());
        }
        Object[] result = (Object[]) Array.newInstance(itemType.getRawType(), list.size());
        int i = 0;
        for (Object item : list) {
            if (item == null || (itemType.isPure() && item.getClass() == itemType.getRawType())) {
                result[i] = item; // copy directly
            } else {
                result[i] = convert(item, itemType); // need convert
            }
            i++;
        }
        return result;
    }

}
