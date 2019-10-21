package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import com.github.sisyphsu.nakedata.node.std.ArrayNode;
import com.github.sisyphsu.nakedata.node.std.ObjectNode;
import lombok.Data;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author sulin
 * @since 2019-10-21 11:12:53
 */
public class BeanNodeCodecTest {

    private BeanNodeCodec codec = new BeanNodeCodec();

    @BeforeEach
    void setUp() {
        CodecFactory.Instance.installCodec(BasicNodeCodec.class);
        CodecFactory.Instance.installCodec(ArrayNodeCodec.class);
        CodecFactory.Instance.installCodec(BeanNodeCodec.class);

        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void testMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", RandomUtils.nextLong());
        map.put("name", RandomStringUtils.randomAlphanumeric(16));
        map.put("score", RandomUtils.nextDouble());
        Node node = codec.toNode(map);
        assert node instanceof ObjectNode;

        Map<String, Node> map2 = codec.toMap((ObjectNode) node);
        assert map.size() == map2.size();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object val = entry.getValue();
            Node val2 = map2.get(key);
            if (val instanceof Long) {
                assert Objects.equals(val, val2.longValue());
            } else if (val instanceof String) {
                assert Objects.equals(val, val2.stringValue());
            } else {
                assert Objects.equals(val, val2.doubleValue());
            }
        }
    }

    @Test
    public void testBeanMap() {
        Persion persion = new Persion();
        Map map = codec.convert(persion, Map.class);
        assert map instanceof BeanMap;

        ObjectNode node = (ObjectNode) codec.toNode(map);
        Persion persion1 = codec.convert(node, Persion.class);

        assert persion.equals(persion1);
    }

    @Test
    public void testArray() {
        Group group = new Group();
        group.persions.add(new Persion());
        group.persions.add(new Persion());
        group.persions.add(new Persion());
        group.persions.add(new Persion());
        group.persions.add(new Persion());

        Node node = codec.convert(group, Node.class);
        assert node instanceof ObjectNode;

        Group group1 = codec.convert(node, Group.class);
        assert group.equals(group1);
    }

    @Test
    public void testObjectNodeArray() {
        List<Object> list = new ArrayList<>();
        list.add(new Group());
        list.add(new Persion());
        Node node = codec.convert(list, Node.class);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 2;
    }

    @Data
    public static class Persion {
        private int     id    = RandomUtils.nextInt();
        private float   score = RandomUtils.nextFloat();
        private boolean old   = RandomUtils.nextBoolean();
        private String  name  = RandomStringUtils.randomAlphanumeric(16);
        private Object  obj;
    }

    @Data
    public static class Group {
        private Date          date     = new Date();
        private Thread.State  state    = Thread.State.BLOCKED;
        private List<Persion> persions = new ArrayList<>();
    }

}