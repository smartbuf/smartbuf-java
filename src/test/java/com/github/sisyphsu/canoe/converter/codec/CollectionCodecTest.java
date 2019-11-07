package com.github.sisyphsu.canoe.converter.codec;

import com.github.sisyphsu.canoe.converter.CodecFactory;
import com.github.sisyphsu.canoe.reflect.TypeRef;
import com.github.sisyphsu.canoe.reflect.XTypeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author sulin
 * @since 2019-08-04 15:52:41
 */
@SuppressWarnings("ALL")
public class CollectionCodecTest {

    private CollectionCodec codec = new CollectionCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
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
    public void testCollectionCompatible() {
        Set emptySet = Collections.emptySet();
        assert emptySet == codec.toCollection(emptySet, XTypeUtils.toXType(Set.class));
        assert emptySet == codec.toCollection(emptySet, XTypeUtils.toXType(new TypeRef<Set<String>>() {
        }.getType()));

        List list = new ArrayList();
        list.add(1);
        list.add(null);
        list.add(2L);
        assert list == codec.toCollection(list, XTypeUtils.toXType(new TypeRef<List<Number>>() {
        }.getType()));

        assert list != codec.toCollection(list, XTypeUtils.toXType(new TypeRef<List<Long>>() {
        }.getType()));

        assert list != codec.toCollection(list, XTypeUtils.toXType(new TypeRef<List<Optional<Integer>>>() {
        }.getType()));

        assert list == codec.toCollection(list, XTypeUtils.toXType(new TypeRef<List>() {
        }.getType()));
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

        Collection coll = codec.toCollection(new Object[]{1, null, new Date()});
        assert coll.size() == 3;
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

        try {
            CollectionCodec.create(Collections.singletonList(0).getClass(), Integer.class, 10);
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }

        try {
            CollectionCodec.create(Collections.singleton(0).getClass(), Integer.class, 10);
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }

        try {
            CollectionCodec.create(Collections.asLifoQueue(new LinkedList()).getClass(), Integer.class, 10);
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }
    }

}
