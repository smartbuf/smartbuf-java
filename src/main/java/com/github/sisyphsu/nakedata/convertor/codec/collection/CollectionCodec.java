package com.github.sisyphsu.nakedata.convertor.codec.collection;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.util.*;
import java.util.concurrent.*;

/**
 * Collection's codec
 * Use Map to
 *
 * @author sulin
 * @since 2019-05-13 18:40:23
 */
public class CollectionCodec extends Codec {

    /**
     * Convert Any Collection to Collection, support GenericType convert.
     *
     * @param src  Collection
     * @param type Type
     * @return Collection
     */
    public Collection toCollection(Collection src, XType<?> type) {
        if (src == null || checkCompatible(src, type))
            return src;
        // Initialize target collection
        Collection result;
        int size = src.size();
        Class<?> clz = type.getRawType();
        XType genericType = type.getParameterizedType();
        if (clz.isAssignableFrom(List.class)) {
            if (clz.isAssignableFrom(ArrayList.class)) {
                result = new ArrayList();
            } else if (clz.isAssignableFrom(LinkedList.class)) {
                result = new LinkedList();
            } else if (clz.isAssignableFrom(Stack.class)) {
                result = new Stack();
            } else if (clz.isAssignableFrom(Vector.class)) {
                result = new Vector();
            } else if (clz.isAssignableFrom(CopyOnWriteArrayList.class)) {
                result = new CopyOnWriteArrayList();
            }
        } else if (clz.isAssignableFrom(Set.class)) {
            if (clz.isAssignableFrom(HashSet.class)) {
                result = new HashSet();
            } else if (clz.isAssignableFrom(TreeSet.class)) {
                result = new TreeSet();
            } else if (clz.isAssignableFrom(LinkedHashSet.class)) {
                result = new LinkedHashSet();
            } else if (clz.isAssignableFrom(EnumSet.class)) {
                result = EnumSet.noneOf(genericType.getRawType());
            } else if (clz.isAssignableFrom(CopyOnWriteArraySet.class)) {
                result = new CopyOnWriteArraySet();
            } else if (clz.isAssignableFrom(ConcurrentSkipListSet.class)) {
                result = new ConcurrentSkipListSet();
            }
        } else if (clz.isAssignableFrom(Queue.class)) {
            if (clz.isAssignableFrom(ArrayBlockingQueue.class)) {
                result = new ArrayBlockingQueue(size);
            } else if (clz.isAssignableFrom(ArrayDeque.class)) {
                result = new ArrayDeque(size);
            } else if (clz.isAssignableFrom(DelayQueue.class)) {
                result = new DelayQueue();
            } else if (clz.isAssignableFrom(LinkedList.class)) {
                result = new LinkedList();
            } else if (clz.isAssignableFrom(LinkedBlockingDeque.class)) {
                result = new LinkedBlockingDeque(size);
            } else if (clz.isAssignableFrom(LinkedBlockingQueue.class)) {
                result = new LinkedBlockingQueue(size);
            } else if (clz.isAssignableFrom(LinkedTransferQueue.class)) {
                result = new LinkedTransferQueue();
            } else if (clz.isAssignableFrom(PriorityBlockingQueue.class)) {
                result = new PriorityBlockingQueue(size);
            } else if (clz.isAssignableFrom(PriorityQueue.class)) {
                result = new PriorityQueue(size);
            } else if (clz.isAssignableFrom(SynchronousQueue.class)) {
                result = new SynchronousQueue();
            } else if (clz.isAssignableFrom(ConcurrentLinkedDeque.class)) {
                result = new ConcurrentLinkedDeque();
            } else if (clz.isAssignableFrom(ConcurrentLinkedQueue.class)) {
                result = new ConcurrentLinkedQueue();
            }
        }
        // use ArrayList as default Collection
        List list = new ArrayList();
        for (Object o : src) {
            list.add(convert(o, genericType));
        }
        return list;
    }

    /**
     * Check whether src is compatible with tgtType
     */
    protected boolean checkCompatible(Collection src, XType tgtType) {
        if (src == null)
            return true; // null compatible with everything
        // TODO check class, empty special
        // TODO check generic type, and object type compare
        return false;
    }

}
