package com.github.sisyphsu.nakedata.convertor.codec.collection.list;

import com.github.sisyphsu.nakedata.convertor.codec.collection.CollectionCodec;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Stack;

/**
 * Stack's codec
 *
 * @author sulin
 * @since 2019-07-10 21:29:21
 */
public class StackCodec extends CollectionCodec {

    /**
     * Convert Collection to Type
     *
     * @param data Collection
     * @param type Type
     * @return Stack
     */
    public Stack toStack(Collection data, Type type) {
        if (checkCompatible(data, type)) {
            return (Stack) data;
        }
        return execCopy(data, new Stack(), type);
    }

}
