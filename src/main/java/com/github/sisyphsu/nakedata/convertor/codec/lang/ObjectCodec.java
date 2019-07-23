package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Object's codec
 *
 * @author sulin
 * @since 2019-05-14 21:41:32
 */
public class ObjectCodec extends Codec {

    /**
     * TODO implementation
     *
     * @param map
     * @param type
     * @return
     */
    public Object toObject(Map map, XType type) {
        if (map == null) {
            return null;
        }
        if (type.getRawType() == Object.class) {
            return map;
        }
        try {
            Object result = type.getRawType().newInstance();

            return result;
        } catch (Exception e) {

        }
        return null;
    }

}
