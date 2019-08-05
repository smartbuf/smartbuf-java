package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.jar.Attributes;

/**
 * @author sulin
 * @since 2019-08-04 18:17:25
 */
public class MapCodecTest {

    private MapCodec codec = new MapCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void testMap() {
        // TODO need complete
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

}