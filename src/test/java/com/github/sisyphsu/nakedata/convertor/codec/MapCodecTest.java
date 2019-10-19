package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import com.github.sisyphsu.nakedata.reflect.TypeRef;
import com.github.sisyphsu.nakedata.reflect.XTypeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.jar.Attributes;

/**
 * @author sulin
 * @since 2019-08-04 18:17:25
 */
@SuppressWarnings("ALL")
public class MapCodecTest {

    private MapCodec codec = new MapCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void testCompatible() {
        Map emptyMap = new HashMap();
        assert codec.toMap(emptyMap, XTypeUtils.toXType(Map.class)) == emptyMap;

        Map<Long, String> map = new HashMap<>();
        map.put(1L, "1234567.999");
        map.put(2L, "0.1234567999");

        assert codec.toMap(map, XTypeUtils.toXType(Map.class)) == map;
        assert codec.toMap(map, XTypeUtils.toXType(new TypeRef<Map<Long, String>>() {
        }.getType())) == map;
        assert codec.toMap(map, XTypeUtils.toXType(new TypeRef<Map<Number, String>>() {
        }.getType())) == map;
        assert codec.toMap(map, XTypeUtils.toXType(new TypeRef<Map<Number, CharSequence>>() {
        }.getType())) == map;

        Map map1 = new HashMap();
        map1.put(1L, "hello");
        map1.put(2, "world");
        Map map2 = codec.toMap(map1, XTypeUtils.toXType(new TypeRef<Map<Long, CharSequence>>() {
        }.getType()));
        assert map1 != map2;
        assert map2.get(2L) == map1.get(2);

        codec.toMap(map1, XTypeUtils.toXType(new TypeRef<Map<Optional<Long>, Optional<String>>>() {
        }.getType()));
        codec.toMap(map1, XTypeUtils.toXType(new TypeRef<Map<Long, Optional<String>>>() {
        }.getType()));
        codec.toMap(map1, XTypeUtils.toXType(new TypeRef<Map<Optional<Long>, String>>() {
        }.getType()));

        codec.toMap(map1, XTypeUtils.toXType(new TypeRef<Map<Long, ?>>() {
        }.getType()));
        codec.toMap(map1, XTypeUtils.toXType(new TypeRef<Map<?, String>>() {
        }.getType()));

        Map map3 = new HashMap();
        map3.put(Byte.valueOf((byte) 1), "hello");
        codec.toMap(map3, XTypeUtils.toXType(new TypeRef<Map<Byte, byte[]>>() {
        }.getType()));
    }

    @Test
    public void testCopy() {
        Map emptyMap = new HashMap();
        assert codec.toMap(emptyMap, XTypeUtils.toXType(TreeMap.class)) instanceof TreeMap;

        Map<Long, String> map = new HashMap<>();
        map.put(1L, "1234567.999");
        map.put(2L, "0.1234567999");

        Map newMap = codec.toMap(map, XTypeUtils.toXType(TreeMap.class));
        assert newMap.get(1L) == map.get(1L);
        assert newMap.get(2L) == map.get(2L);
    }

    @Test
    public void testMapEntry() {
        Object key = 100L;
        Object value = "hello world";
        Map map = new HashMap();
        map.put(key, value);

        Map.Entry entry = codec.toMapEntry(map);
        assert entry != null && entry.getKey().equals(key) && entry.getValue().equals(value);

        assert codec.toMapEntry(new HashMap<>()) == null;

        Map map2 = new HashMap();
        map2.put(1L, "");
        map2.put(2L, "");
        try {
            codec.toMapEntry(map2);
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        assert codec.toMapEntry(entry, XTypeUtils.toXType(AbstractMap.SimpleEntry.class)) instanceof AbstractMap.SimpleEntry;
        assert codec.toMapEntry(entry, XTypeUtils.toXType(AbstractMap.SimpleImmutableEntry.class)) instanceof AbstractMap.SimpleImmutableEntry;
        try {
            codec.toMapEntry(entry, XTypeUtils.toXType(InvalidEntry.class));
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        Map.Entry newEntry = codec.toMapEntry(entry, XTypeUtils.toXType(new TypeRef<Map.Entry<Integer, byte[]>>() {
        }.getType()));
        assert newEntry.getKey() instanceof Integer;
        assert newEntry.getValue() instanceof byte[];
        Map newMap = codec.toMap(newEntry);
        assert newMap.containsKey(100);
        assert newMap.get(100) instanceof byte[];
    }

    @Test
    public void testCreate() {
        assert MapCodec.create(HashMap.class, Long.class, 10) instanceof HashMap;
        assert MapCodec.create(TreeMap.class, Long.class, 10) instanceof TreeMap;
        assert MapCodec.create(Hashtable.class, Long.class, 10) instanceof Hashtable;
        assert MapCodec.create(WeakHashMap.class, Long.class, 10) instanceof WeakHashMap;
        assert MapCodec.create(LinkedHashMap.class, Long.class, 10) instanceof LinkedHashMap;
        assert MapCodec.create(IdentityHashMap.class, Long.class, 10) instanceof IdentityHashMap;
        assert MapCodec.create(ConcurrentHashMap.class, Long.class, 10) instanceof ConcurrentHashMap;
        assert MapCodec.create(ConcurrentSkipListMap.class, Long.class, 10) instanceof ConcurrentSkipListMap;
        assert MapCodec.create(Properties.class, String.class, 10) instanceof Properties;
        assert MapCodec.create(Attributes.class, String.class, 10) instanceof Attributes;
        assert MapCodec.create(EnumMap.class, TimeUnit.class, 10) instanceof EnumMap;

        assert MapCodec.create(Map.class, Long.class, 10) instanceof HashMap;
        assert MapCodec.create(AbstractMap.class, Long.class, 10) instanceof HashMap;
        assert MapCodec.create(SortedMap.class, Long.class, 10) instanceof TreeMap;
        assert MapCodec.create(NavigableMap.class, Long.class, 10) instanceof TreeMap;
        assert MapCodec.create(Dictionary.class, Long.class, 10) instanceof Hashtable;
        assert MapCodec.create(ConcurrentMap.class, Long.class, 10) instanceof ConcurrentHashMap;
        assert MapCodec.create(ConcurrentNavigableMap.class, Long.class, 10) instanceof ConcurrentSkipListMap;
        assert MapCodec.create(EnumMap.class, TimeUnit.class, 10) instanceof EnumMap;

        try {
            MapCodec.create(MyMap.class, null, 10);
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }
    }

    public static class MyMap extends HashMap {
    }

    public static class InvalidEntry<K, E> extends AbstractMap.SimpleEntry<K, E> {
        public InvalidEntry(K key, E value) {
            super(key, value);
        }
    }

}
