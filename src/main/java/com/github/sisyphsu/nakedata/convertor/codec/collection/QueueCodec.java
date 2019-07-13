package com.github.sisyphsu.nakedata.convertor.codec.collection;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.*;

/**
 * Queue's codec
 *
 * @author sulin
 * @since 2019-07-11 20:11:33
 */
public class QueueCodec extends CollectionCodec {

    /**
     * Convert Collection to Queue, support all standard Queue
     *
     * @param data Collection
     * @param type Type
     * @param <T>  Generic
     * @return Queue
     */
    public <T extends Queue> T toQueue(Collection data, Type type) {
        if (data == null)
            return null;
        if (checkCompatible(data, type)) {
            return (T) data;
        }
        Class clz = (Class) type;
        Queue queue;
        if (ArrayBlockingQueue.class.isAssignableFrom(clz)) {
            queue = new ArrayBlockingQueue(data.size());
        } else if (ArrayDeque.class.isAssignableFrom(clz)) {
            queue = new ArrayDeque(data.size());
        } else if (DelayQueue.class.isAssignableFrom(clz)) {
            queue = new DelayQueue();
        } else if (LinkedList.class.isAssignableFrom(clz)) {
            queue = new LinkedList();
        } else if (LinkedBlockingDeque.class.isAssignableFrom(clz)) {
            queue = new LinkedBlockingDeque(data.size());
        } else if (LinkedBlockingQueue.class.isAssignableFrom(clz)) {
            queue = new LinkedBlockingQueue(data.size());
        } else if (LinkedTransferQueue.class.isAssignableFrom(clz)) {
            queue = new LinkedTransferQueue();
        } else if (PriorityBlockingQueue.class.isAssignableFrom(clz)) {
            queue = new PriorityBlockingQueue(data.size());
        } else if (PriorityQueue.class.isAssignableFrom(clz)) {
            queue = new PriorityQueue(data.size());
        } else if (SynchronousQueue.class.isAssignableFrom(clz)) {
            queue = new SynchronousQueue();
        } else if (ConcurrentLinkedDeque.class.isAssignableFrom(clz)) {
            queue = new ConcurrentLinkedDeque();
        } else if (ConcurrentLinkedQueue.class.isAssignableFrom(clz)) {
            queue = new ConcurrentLinkedQueue();
        } else {
            throw new IllegalArgumentException("");
        }
        Type genericType = getGenericType(type);
        for (Object o : data) {
            queue.offer(convert(o, genericType));
        }
        return (T) queue;
    }

}
