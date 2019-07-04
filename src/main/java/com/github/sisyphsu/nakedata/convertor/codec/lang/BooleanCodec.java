package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

/**
 * Boolean's codec
 *
 * @author sulin
 * @since 2019-05-13 18:12:00
 */
public class BooleanCodec extends Codec {

    public Boolean[] convert(boolean[] arr) {
        if (arr == null) {
            return null;
        }
        Boolean[] result = new Boolean[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    public boolean[] convert(Boolean[] arr) {
        if (arr == null) {
            return null;
        }
        boolean[] result = new boolean[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i]; // could NPE
        }
        return result;
    }

}
