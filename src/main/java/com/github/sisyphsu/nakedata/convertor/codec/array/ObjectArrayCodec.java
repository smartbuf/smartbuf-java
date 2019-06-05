package com.github.sisyphsu.nakedata.convertor.codec.array;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.Arrays;
import java.util.Collection;

/**
 * Codec for Object[]
 *
 * @author sulin
 * @since 2019-05-13 18:53:04
 */
public class ObjectArrayCodec extends Codec<Object[]> {

    public Collection<Object> toCollection(Object[] objects) {
        return Arrays.asList(objects);
    }

}
