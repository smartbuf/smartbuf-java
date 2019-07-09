package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Object's codec
 *
 * @author sulin
 * @since 2019-05-14 21:41:32
 */
public class ObjectCodec extends Codec {

    public Object toObject(Map map, Type type) {
        if (map == null)
            return null;
        if (type == Object.class)
            return map;
        Class clz = (Class) type;
        try {
            Object result = clz.newInstance();

            return result;
        } catch (Exception e) {

        }
        return null;
    }

}
