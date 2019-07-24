package com.github.sisyphsu.nakedata.convertor.codec.collection;

import java.util.*;
import java.util.concurrent.*;

/**
 * Maintain optimized global Creator for collection
 *
 * @author sulin
 * @since 2019-07-24 11:50:08
 */
public final class CollectionUtils {

    /**
     * Create an collection instance by the specified Type.
     *
     * @param clz      Collection Type
     * @param itemType Element Type, for EnumSet
     * @param size     Initialize siz
     * @return Collection
     */
    @SuppressWarnings("unchecked")
    public static <T extends Collection> T create(Class<T> clz, Class itemType, int size) {
        Collection result = null;

        if (List.class.isAssignableFrom(clz)) {
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
        } else if (Set.class.isAssignableFrom(clz)) {
            if (clz.isAssignableFrom(HashSet.class)) {
                result = new HashSet();
            } else if (clz.isAssignableFrom(TreeSet.class)) {
                result = new TreeSet();
            } else if (clz.isAssignableFrom(LinkedHashSet.class)) {
                result = new LinkedHashSet();
            } else if (clz.isAssignableFrom(EnumSet.class)) {
                result = EnumSet.noneOf(itemType);
            } else if (clz.isAssignableFrom(CopyOnWriteArraySet.class)) {
                result = new CopyOnWriteArraySet();
            } else if (clz.isAssignableFrom(ConcurrentSkipListSet.class)) {
                result = new ConcurrentSkipListSet();
            }
        } else if (Queue.class.isAssignableFrom(clz)) {
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
        if (result == null) {
            throw new UnsupportedOperationException("Invalid Collection Type: " + clz);
        }
        return (T) result;
    }

}
