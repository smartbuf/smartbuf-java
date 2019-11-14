package com.github.smartbuf.transport;

import com.github.smartbuf.node.basic.ObjectNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

/**
 * @author sulin
 * @since 2019-10-19 11:44:37
 */
public class IOObjectTest {

    @Test
    public void testUnstable() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", RandomUtils.nextLong());
        map.put("name", RandomStringUtils.randomAlphanumeric(16));
        map.put("score", RandomUtils.nextDouble());
        String[] fieldNames = map.keySet().toArray(new String[0]);
        ObjectNode objectNode = buildObjectNode(false, fieldNames, map);

        IOTest.enableCxt = false;
        Object result = IOTest.transIO(objectNode);
        assert result instanceof HashMap;

        IOTest.enableCxt = true;
        result = IOTest.transIO(objectNode);
        assert result instanceof HashMap;

        Object obj = IOTest.transIO(new HashMap<>());
        assert obj instanceof ObjectNode;
    }

    @Test
    public void testObject() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", RandomUtils.nextLong());
        map.put("name", RandomStringUtils.randomAlphanumeric(64));
        map.put("nil", null);
        map.put("real", true);
        map.put("score1", RandomUtils.nextFloat());
        map.put("score2", RandomUtils.nextDouble());
        map.put("arr", RandomUtils.nextBytes(1024));
        map.put("state", Thread.State.RUNNABLE);

        String[] fieldNames = map.keySet().toArray(new String[0]);
        ObjectNode objectNode = buildObjectNode(true, fieldNames, map);

        Object result;
        Map tgtMap;

        IOTest.enableCxt = false;
        result = IOTest.transIO(objectNode);
        assert result instanceof Map;
        tgtMap = (Map) result;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object tgtItem = tgtMap.get(entry.getKey());
            if (entry.getValue() instanceof Enum) {
                assert ((Enum) entry.getValue()).name().equals(tgtItem);
            } else {
                assert Objects.deepEquals(entry.getValue(), tgtItem);
            }
        }

        IOTest.enableCxt = true;
        result = IOTest.transIO(objectNode);
        assert result instanceof ObjectNode;

        // test for array
        List<Object> array = new ArrayList<>(Arrays.asList(objectNode, objectNode, objectNode));

        // for temporary
        IOTest.enableCxt = false;
        result = IOTest.transIO(array);
        assert result instanceof Object[];
        tgtMap = (Map) ((Object[]) result)[0];
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object tgtItem = tgtMap.get(entry.getKey());
            if (entry.getValue() instanceof Enum) {
                assert ((Enum) entry.getValue()).name().equals(tgtItem);
            } else {
                assert Objects.deepEquals(entry.getValue(), tgtItem);
            }
        }

        // for context
        IOTest.enableCxt = true;
        result = IOTest.transIO(array);
        assert result instanceof Object[];
    }

    @Test
    public void testMixArray() throws IOException {
        List node = new ArrayList();

        Map<String, Object> map = new HashMap<>();
        map.put("id", RandomUtils.nextLong());
        map.put("name", RandomStringUtils.randomAlphanumeric(16));
        map.put("score", RandomUtils.nextDouble());
        String[] fieldNames = map.keySet().toArray(new String[0]);
        node.addAll(Collections.singletonList(buildObjectNode(false, fieldNames, map)));

        int[] ints = new int[]{0, Integer.MIN_VALUE, Integer.MAX_VALUE};
        float[] floats = new float[]{0.0F, Float.MIN_VALUE, Float.MAX_VALUE};
        node.addAll(Arrays.asList(ints, floats));

        IOTest.enableCxt = true;
        Object result = IOTest.transIO(node);
        assert result instanceof Object[];
        assert ((Object[]) result).length == node.size();

        IOTest.enableCxt = false;
        result = IOTest.transIO(node);

        assert result instanceof Object[];
        assert ((Object[]) result).length == node.size();
    }

    ObjectNode buildObjectNode(boolean stable, String[] names, Map<String, Object> map) {
        Object[] nodes = new Object[map.size()];
        int off = 0;
        for (String fieldName : names) {
            Object data = map.get(fieldName);
            nodes[off++] = data;
        }
        return new ObjectNode(stable, names, nodes);
    }

}
