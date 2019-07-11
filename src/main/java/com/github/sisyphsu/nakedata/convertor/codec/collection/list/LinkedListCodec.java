package com.github.sisyphsu.nakedata.convertor.codec.collection.list;

import com.github.sisyphsu.nakedata.convertor.codec.collection.CollectionCodec;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;

/**
 * LinkedList's codec
 *
 * @author sulin
 * @since 2019-05-13 18:41:18
 */
public class LinkedListCodec extends CollectionCodec {

    /**
     * Convert Collection to LinkedList
     *
     * @param data Collection
     * @param type Type
     * @return LinkedList
     */
    public LinkedList toLinkedList(Collection data, Type type) {
        if (checkCompatible(data, type)) {
            return (LinkedList) data;
        }
        return execCopy(data, new LinkedList(), type);
    }

}
