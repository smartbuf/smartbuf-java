package com.github.sisyphsu.nakedata.convertor.codec.collection;

import org.junit.Test;
import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * Test and Benchmark for CollectionUtils
 * <p>
 * Benchmark(ArrayList)                Mode  Cnt   Score   Error  Units
 * CollectionUtilsTest.createByIfElse  avgt    9   0.256 ± 0.001  ns/op
 * CollectionUtilsTest.createBySwitch  avgt    9  18.626 ± 0.161  ns/op
 * <p>
 * Benchmark(ConcurrentSkipListSet)    Mode  Cnt   Score   Error  Units
 * CollectionUtilsTest.createByIfElse  avgt    9  13.706 ± 0.112  ns/op
 * CollectionUtilsTest.createBySwitch  avgt    9  36.589 ± 0.236  ns/op
 * <p>
 * Benchmark(ConcurrentLinkedQueue)    Mode  Cnt   Score   Error  Units
 * CollectionUtilsTest.createByIfElse  avgt    9  11.473 ± 0.116  ns/op
 * CollectionUtilsTest.createBySwitch  avgt    9  30.070 ± 0.732  ns/op
 *
 * @author sulin
 * @since 2019-07-24 12:30:41
 */
@SuppressWarnings("all")
@Warmup(iterations = 2, time = 2)
@Fork(3)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class CollectionUtilsTest {

    private static final Class<? extends Collection> collType = ConcurrentLinkedQueue.class;
    private static final Class<?> itemType = Long.class;
    private static final int size = 0;

    @Test
    public void create() {
        Collection set1 = createBySwitch(collType, itemType, size);
        System.out.println(set1);
        createBySwitch(collType, itemType, size);

        Collection set2 = createByIfElse(collType, itemType, size);
        System.out.println(set2);
    }

    @Benchmark
    public void createBySwitch() {
        createBySwitch(collType, itemType, size);
    }

    @Benchmark
    public void createByIfElse() {
        createByIfElse(collType, itemType, size);
    }

    /**
     * Create collection for benchmark
     */
    protected static <T extends Collection> T createByIfElse(Class<T> clz, Class itemType, int size) {
        Collection result = null;
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
        } else if (clz.isAssignableFrom(HashSet.class)) {
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
        } else if (clz.isAssignableFrom(ArrayBlockingQueue.class)) {
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
        return (T) result;
    }

    private final static Creator NULL = (t, s) -> null;
    private final static Map<Class, Creator> CREATORS = new HashMap<>();
    private final static Map<Class, Creator> CREATOR_MAP = new ConcurrentHashMap<>();

    static {
        // List
        CREATORS.put(ArrayList.class, (t, size) -> new ArrayList<>(size));
        CREATORS.put(LinkedList.class, (t, size) -> new LinkedList<>());
        CREATORS.put(Vector.class, (t, size) -> new Vector<>(size));
        CREATORS.put(CopyOnWriteArrayList.class, (t, size) -> new CopyOnWriteArrayList<>());
        // Set
        CREATORS.put(HashSet.class, (t, size) -> new HashSet<>(size));
        CREATORS.put(TreeSet.class, (t, size) -> new TreeSet<>());
        CREATORS.put(LinkedHashSet.class, (t, size) -> new LinkedHashSet<>(size));
        CREATORS.put(EnumSet.class, (t, size) -> EnumSet.noneOf(t));
        CREATORS.put(CopyOnWriteArraySet.class, (t, size) -> new CopyOnWriteArraySet<>());
        CREATORS.put(ConcurrentSkipListSet.class, (t, size) -> new ConcurrentSkipListSet<>());
        // Queue
        CREATORS.put(ArrayBlockingQueue.class, (t, size) -> new ArrayBlockingQueue<>(size));
        CREATORS.put(ArrayDeque.class, (t, size) -> new ArrayDeque<>(size));
        CREATORS.put(DelayQueue.class, (t, size) -> new DelayQueue<>());
        CREATORS.put(LinkedBlockingDeque.class, (t, size) -> new LinkedBlockingDeque<>(size));
        CREATORS.put(LinkedBlockingQueue.class, (t, size) -> new LinkedBlockingQueue<>(size));
        CREATORS.put(LinkedTransferQueue.class, (t, size) -> new LinkedTransferQueue<>());
        CREATORS.put(PriorityBlockingQueue.class, (t, size) -> new PriorityBlockingQueue<>(size));
        CREATORS.put(PriorityQueue.class, (t, size) -> new PriorityQueue<>(size));
        CREATORS.put(SynchronousQueue.class, (t, size) -> new SynchronousQueue<>());
        CREATORS.put(ConcurrentLinkedDeque.class, (t, size) -> new ConcurrentLinkedDeque<>());
        CREATORS.put(ConcurrentLinkedQueue.class, (t, size) -> new ConcurrentLinkedQueue<>());
    }

    /**
     * Create an collection instance by the specified Type.
     */
    protected static <T extends Collection> T createBySwitch(Class<T> clz, Class<?> itemType, int size) {
        Creator creator = CREATOR_MAP.computeIfAbsent(clz, t -> {
            for (Map.Entry<Class, Creator> entry : CREATORS.entrySet()) {
                if (clz.isAssignableFrom(entry.getKey())) {
                    return entry.getValue();
                }
            }
            return NULL;
        });
        if (creator == NULL) {
            throw new IllegalArgumentException("Unsupported Type: " + clz);
        }
        return (T) creator.create(itemType, size);
    }

    @FunctionalInterface
    private interface Creator {
        Collection create(Class itemType, int size);
    }

}