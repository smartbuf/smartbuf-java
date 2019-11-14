package com.github.smartbuf.node;

import com.github.smartbuf.converter.BeanInfo;
import com.github.smartbuf.converter.CodecFactory;
import com.github.smartbuf.reflect.BeanWriterBuilder;
import com.github.smartbuf.reflect.XTypeUtils;
import com.github.smartbuf.node.basic.ObjectNode;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author sulin
 * @since 2019-10-21 11:12:53
 */
public class NodeCodecTest {

    private NodeCodec    codec   = new NodeCodec();
    private CodecFactory factory = CodecFactory.Instance;

    @BeforeEach
    void setUp() {
        factory.installCodec(NodeCodec.class);
        codec.setFactory(factory);
    }

    @Test
    public void testNormalCodec() {
        Boolean b = true;
        assert codec.toValue(codec.toNode(b)) == b;
        assert b.equals(factory.convert(factory.convert(b, Node.class), Boolean.class));

        Byte b1 = 1;
        assert b1.equals(codec.toValue(codec.toNode(b1)).byteValue());
        assert b1.equals(factory.convert(factory.convert(b, Node.class), Byte.class));

        Short s = 1;
        assert s.equals(codec.toValue(codec.toNode(s)).shortValue());
        assert s.equals(factory.convert(factory.convert(s, Node.class), Short.class));

        Integer i = 1;
        assert i.equals(codec.toValue(codec.toNode(i)).intValue());
        assert i.equals(factory.convert(factory.convert(i, Node.class), Integer.class));

        Long l = 1L;
        assert l.equals(codec.toValue(codec.toNode(l)));
        assert l.equals(factory.convert(factory.convert(l, Node.class), Long.class));

        Float f = 0f;
        assert f.equals(codec.toValue(codec.toNode(f)));
        assert f.equals(factory.convert(factory.convert(f, Node.class), Float.class));

        Double d = 1.1;
        assert d.equals(codec.toValue(codec.toNode(d)));
        assert d.equals(factory.convert(factory.convert(d, Node.class), Double.class));

        String str = "hello world";
        assert str.equals(codec.toValue(codec.toNode(str)));
        assert str.equals(factory.convert(factory.convert(str, Node.class), String.class));

        Enum symbol = Thread.State.BLOCKED;
        assert symbol.name().equals(codec.toValue(codec.toSymbolNode(symbol)));
        assert symbol.name().equals(codec.toValue(codec.toSymbolNode(symbol.name())));
        assert symbol.equals(factory.convert(factory.convert(symbol, Node.class), Thread.State.class));
    }

    @Test
    public void testArrayCodec() {
        boolean[] booleans = new boolean[1];
        assert Arrays.equals(booleans, codec.toValue(codec.toNode(booleans)));
        assert Arrays.equals(booleans, factory.convert(factory.convert(booleans, Node.class), boolean[].class));

        byte[] bytes = new byte[1];
        assert Arrays.equals(bytes, codec.toValue(codec.toNode(bytes)));
        assert Arrays.equals(bytes, factory.convert(factory.convert(bytes, Node.class), byte[].class));

        short[] shorts = new short[1];
        assert Arrays.equals(shorts, codec.toValue(codec.toNode(shorts)));
        assert Arrays.equals(shorts, factory.convert(factory.convert(shorts, Node.class), short[].class));

        int[] ints = new int[1];
        assert Arrays.equals(ints, codec.toValue(codec.toNode(ints)));
        assert Arrays.equals(ints, factory.convert(factory.convert(ints, Node.class), int[].class));

        long[] longs = new long[1];
        assert Arrays.equals(longs, codec.toValue(codec.toNode(longs)));
        assert Arrays.equals(longs, factory.convert(factory.convert(longs, Node.class), long[].class));

        float[] floats = new float[1];
        assert Arrays.equals(floats, codec.toValue(codec.toNode(floats)));
        assert Arrays.equals(floats, factory.convert(factory.convert(floats, Node.class), float[].class));

        double[] doubles = new double[1];
        assert Arrays.equals(doubles, codec.toValue(codec.toNode(doubles)));
        assert Arrays.equals(doubles, factory.convert(factory.convert(doubles, Node.class), double[].class));

        Object[] arr = new Object[1];
        assert Arrays.equals(arr, codec.toValue(codec.toNode(arr)).toArray());
        assert Arrays.equals(arr, factory.convert(factory.convert(arr, Node.class), Object[].class));

        List<?> list = Arrays.asList(1, 2);
        assert list.equals(codec.toValue(codec.toNode(list)));
        assert list.equals(factory.convert(factory.convert(list, Node.class), Collection.class));
    }

    @Test
    public void testMap() {
        Map<Object, Object> map = new HashMap<>();

        assert codec.toNode(map) == ObjectNode.EMPTY;
        assert codec.toValue(ObjectNode.EMPTY).isEmpty();

        map.put("id", RandomUtils.nextLong());
        map.put("name", RandomStringUtils.randomAlphanumeric(16));
        map.put("score", RandomUtils.nextDouble());
        map.put(System.currentTimeMillis(), RandomUtils.nextDouble());
        ObjectNode node = codec.toNode(map);

        Map<String, Object> map2 = codec.toValue(node);
        assert map.size() == map2.size();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object val = entry.getValue();
            Object val2 = map2.get(String.valueOf(key));
            assert Objects.equals(val, val2);
        }
    }

    @Test
    public void testBean() {
        Person person = new Person();
        ObjectNode node = codec.toNode(person);

        BeanInfo beanInfo = codec.toObject(node, XTypeUtils.toXType(Person.class));
        assert beanInfo.getWriter() == BeanWriterBuilder.build(Person.class);

        Person p2 = CodecFactory.Instance.convert(node, Person.class);
        assert person.equals(p2);
    }

    @Test
    public void testArray() {
        Group group = new Group();
        group.persons.add(new Person());
        group.persons.add(new Person());
        group.persons.add(new Person());
        group.persons.add(new Person());
        group.persons.add(new Person());

        Node node = CodecFactory.Instance.convert(group, Node.class);
        assert node instanceof ObjectNode;

        Group group1 = CodecFactory.Instance.convert(node, Group.class);
        assert group.equals(group1);
    }

    @Data
    public static class Person {
        private int     id    = RandomUtils.nextInt();
        private float   score = RandomUtils.nextFloat();
        private boolean old   = RandomUtils.nextBoolean();
        private String  name  = RandomStringUtils.randomAlphanumeric(16);
        private Object  obj;
    }

    @Data
    public static class Group {
        private Date         date    = new Date();
        private Thread.State state   = Thread.State.BLOCKED;
        private List<Person> persons = new ArrayList<>();
    }

}
