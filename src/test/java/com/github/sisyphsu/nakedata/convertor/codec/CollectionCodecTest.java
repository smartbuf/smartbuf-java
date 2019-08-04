package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import com.github.sisyphsu.nakedata.convertor.reflect.TypeRef;
import com.github.sisyphsu.nakedata.convertor.reflect.XTypeUtils;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author sulin
 * @since 2019-08-04 15:52:41
 */
public class CollectionCodecTest {

    private static CollectionCodec codec = new CollectionCodec();

    static {
        codec.setFactory(new CodecFactory(null));
    }

    @Test
    public void testIterable() {
        List<Integer> list = Arrays.asList(1, 3, 5, 9);
        Iterable iterable = codec.toIterable(list);

        int i = 0;
        for (Object o : iterable) {
            assert Objects.equals(o, list.get(i));
            i++;
        }

        Collection collection = codec.toCollection(iterable);

        i = 0;
        for (Object o : collection) {
            assert Objects.equals(o, list.get(i));
            i++;
        }
    }

    @Test
    public void testIterator() {
        List<Integer> list = Arrays.asList(1, 3, 5, 9);
        Collection collection = codec.toCollection(list.iterator());
        int i = 0;
        for (Object o : collection) {
            assert Objects.equals(o, list.get(i));
            i++;
        }

        Iterator iterator = codec.toIterator(collection);
        i = 0;
        while (iterator.hasNext()) {
            assert Objects.equals(iterator.next(), list.get(i));
            i++;
        }
    }

    @Test
    public void testEnumeration() {
        List<Integer> list = Arrays.asList(1, 3, 5, 9);
        Enumeration enumeration = codec.toEnumeration(list);
        assert enumeration != null;
        for (Integer i : list) {
            assert Objects.equals(enumeration.nextElement(), i);
        }

        Collection collection = codec.toCollection(codec.toEnumeration(list));
        int i = 0;
        for (Object o : collection) {
            assert Objects.equals(o, list.get(i));
            i++;
        }
    }

    @Test
    public void testCollection() {
        Set set = (Set) codec.toCollection(Arrays.asList(1, 3, 5, 9), XTypeUtils.toXType(Set.class));
        assert set != null && set.size() == 4;
        assert Objects.equals(set.toArray()[3], 9);

        Queue queue = (Queue) codec.toCollection(Arrays.asList(1, 2, 3, 4), XTypeUtils.toXType(new TypeRef<Queue<Integer>>() {
        }.getType()));
        assert queue != null && queue.size() == 4;
        assert Objects.equals(queue.element(), 1);

        Vector vector = (Vector) codec.toCollection(Arrays.asList(100, 200, 300), XTypeUtils.toXType(new TypeRef<Vector<Integer>>() {
        }.getType()));
        assert vector != null && vector.size() == 3;
        assert Objects.equals(vector.get(1), 200);
    }

    @Test
    public void testCreate() {
        assert CollectionCodec.create(ArrayList.class, Integer.class, 10) != null;
        assert CollectionCodec.create(LinkedList.class, Integer.class, 10) != null;
        assert CollectionCodec.create(Stack.class, Integer.class, 10) != null;
        assert CollectionCodec.create(Vector.class, Integer.class, 10) != null;
        assert CollectionCodec.create(CopyOnWriteArrayList.class, Integer.class, 10) != null;

        assert CollectionCodec.create(HashSet.class, Integer.class, 10) != null;
        assert CollectionCodec.create(TreeSet.class, Integer.class, 10) != null;
        assert CollectionCodec.create(LinkedHashSet.class, Integer.class, 10) != null;
        assert CollectionCodec.create(CopyOnWriteArraySet.class, Integer.class, 10) != null;
        assert CollectionCodec.create(ConcurrentSkipListSet.class, Integer.class, 10) != null;
        assert CollectionCodec.create(EnumSet.class, TimeUnit.class, 10) != null;

        assert CollectionCodec.create(ArrayBlockingQueue.class, Long.class, 10) != null;
        assert CollectionCodec.create(ArrayDeque.class, Long.class, 10) != null;
        assert CollectionCodec.create(DelayQueue.class, Long.class, 10) != null;
        assert CollectionCodec.create(LinkedList.class, Long.class, 10) != null;
        assert CollectionCodec.create(LinkedBlockingDeque.class, Long.class, 10) != null;
        assert CollectionCodec.create(LinkedBlockingQueue.class, Long.class, 10) != null;
        assert CollectionCodec.create(LinkedTransferQueue.class, Long.class, 10) != null;
        assert CollectionCodec.create(PriorityBlockingQueue.class, Long.class, 10) != null;
        assert CollectionCodec.create(PriorityQueue.class, Long.class, 10) != null;
        assert CollectionCodec.create(SynchronousQueue.class, Long.class, 10) != null;
        assert CollectionCodec.create(ConcurrentLinkedDeque.class, Long.class, 10) != null;
        assert CollectionCodec.create(ConcurrentLinkedQueue.class, Long.class, 10) != null;

        assert CollectionCodec.create(Collection.class, Integer.class, 10) instanceof ArrayList;
        assert CollectionCodec.create(AbstractCollection.class, Integer.class, 10) instanceof ArrayList;
        assert CollectionCodec.create(List.class, Integer.class, 10) instanceof ArrayList;
        assert CollectionCodec.create(AbstractList.class, Integer.class, 10) instanceof ArrayList;
        assert CollectionCodec.create(AbstractSequentialList.class, Integer.class, 10) instanceof LinkedList;

        assert CollectionCodec.create(Set.class, Integer.class, 10) instanceof HashSet;
        assert CollectionCodec.create(AbstractSet.class, Integer.class, 10) instanceof HashSet;
        assert CollectionCodec.create(SortedSet.class, Integer.class, 10) instanceof TreeSet;
        assert CollectionCodec.create(NavigableSet.class, Integer.class, 10) instanceof TreeSet;

        assert CollectionCodec.create(Queue.class, Integer.class, 10) instanceof LinkedList;
        assert CollectionCodec.create(Deque.class, Integer.class, 10) instanceof LinkedList;
        assert CollectionCodec.create(BlockingQueue.class, Integer.class, 10) instanceof DelayQueue;
        assert CollectionCodec.create(BlockingDeque.class, Integer.class, 10) instanceof LinkedBlockingDeque;
    }

}