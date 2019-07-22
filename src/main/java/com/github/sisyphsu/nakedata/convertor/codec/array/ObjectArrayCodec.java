package com.github.sisyphsu.nakedata.convertor.codec.array;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

/**
 * Convert everything to Object[] if could
 *
 * @author sulin
 * @since 2019-05-13 18:53:04
 */
public class ObjectArrayCodec extends Codec {

    /**
     * Convert Object[] to T[] based on specified T Type.
     * For beter performance, some array shouldn't use this way, like byte[]...
     *
     * @param arr  Source Array
     * @param type Target Type
     * @return Target Array
     */
    public Object[] toArray(Object[] arr, XType<Object[]> type) {
        if (type == null) {
            return arr; // dont convert
        }
        Class itemType = type.getRawType().getComponentType();
        if (type.getComponentType() != null) {
            itemType = type.getComponentType().getRawType();
        }
        if (itemType == arr.getClass().getComponentType()) {
            return arr; // compatible array
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
    public Object[] toArray(Collection list, XType<Object[]> type) {
        if (list == null)
            return null;
        if (type == null) {
            return list.toArray();
        }
        XType<?> itemType = type.getComponentType();
        Object[] result = (Object[]) Array.newInstance(itemType.getRawType(), list.size());
        int i = 0;
        for (Object item : list) {
            if (item == null || (item.getClass() == itemType.getRawType())) {
                result[i] = item; // copy directly
            } else {
                result[i] = convert(item, itemType); // need convert
            }
            i++;
        }
        return result;
    }

}
