package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.node.Node;
import com.github.sisyphsu.canoe.node.std.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static com.github.sisyphsu.canoe.transport.IOTest.enableCxt;
import static com.github.sisyphsu.canoe.transport.IOTest.transIO;

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

        enableCxt = false;
        Object result = transIO(objectNode);
        assert result instanceof Map;
        Map tgtMap = (Map) result;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object tgtItem = tgtMap.get(entry.getKey());
            assert Objects.deepEquals(entry.getValue(), tgtItem);
        }

        enableCxt = true;
        result = transIO(objectNode);
        assert result instanceof Map;
        tgtMap = (Map) result;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object tgtItem = tgtMap.get(entry.getKey());
            assert Objects.deepEquals(entry.getValue(), tgtItem);
        }

        Object obj = transIO(ObjectNode.EMPTY);
        assert obj instanceof Map;
        assert ((Map) obj).isEmpty();
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

        String[] fieldNames = map.keySet().toArray(new String[0]);
        ObjectNode objectNode = buildObjectNode(true, fieldNames, map);

        Object result;
        Map tgtMap;

        enableCxt = false;
        result = transIO(objectNode);
        assert result instanceof Map;
        tgtMap = (Map) result;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object tgtItem = tgtMap.get(entry.getKey());
            assert Objects.deepEquals(entry.getValue(), tgtItem);
        }

        enableCxt = true;
        result = transIO(objectNode);
        assert result instanceof Map;
        tgtMap = (Map) result;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object tgtItem = tgtMap.get(entry.getKey());
            assert Objects.deepEquals(entry.getValue(), tgtItem);
        }

        // test for array
        ArrayNode arrayNode = new ArrayNode();
        arrayNode.addObjectSlice(Arrays.asList(objectNode, objectNode, objectNode));

        // for temporary
        enableCxt = false;
        result = transIO(arrayNode);
        assert result instanceof Object[];
        tgtMap = (Map) ((Object[]) result)[0];
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object tgtItem = tgtMap.get(entry.getKey());
            assert Objects.deepEquals(entry.getValue(), tgtItem);
        }

        // for context
        enableCxt = true;
        result = transIO(arrayNode);
        assert result instanceof Object[];
        tgtMap = (Map) ((Object[]) result)[0];
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object tgtItem = tgtMap.get(entry.getKey());
            assert Objects.deepEquals(entry.getValue(), tgtItem);
        }
    }

    @Test
    public void testMixArray() throws IOException {
        ArrayNode node = new ArrayNode();

        Map<String, Object> map = new HashMap<>();
        map.put("id", RandomUtils.nextLong());
        map.put("name", RandomStringUtils.randomAlphanumeric(16));
        map.put("score", RandomUtils.nextDouble());
        String[] fieldNames = map.keySet().toArray(new String[0]);
        node.addObjectSlice(Collections.singletonList(buildObjectNode(false, fieldNames, map)));

        int[] ints = new int[]{0, Integer.MIN_VALUE, Integer.MAX_VALUE};
        float[] floats = new float[]{0.0F, Float.MIN_VALUE, Float.MAX_VALUE};
        node.addArraySlice(Arrays.asList(ArrayNode.valueOf(ints), ArrayNode.valueOf(floats)));

        Object[] objects = new Object[]{map, ints, floats};

        enableCxt = true;
        Object result = transIO(node);
        assert Objects.deepEquals(objects, result);

        enableCxt = false;
        result = transIO(node);
        assert Objects.deepEquals(objects, result);
    }

    ObjectNode buildObjectNode(boolean stable, String[] names, Map<String, Object> map) {
        Map<String, Node> nodeMap = new HashMap<>();
        for (String fieldName : names) {
            Object data = map.get(fieldName);
            if (data == null) {
                nodeMap.put(fieldName, null);
            } else if (data instanceof Boolean) {
                nodeMap.put(fieldName, BooleanNode.valueOf((Boolean) data));
            } else if (data instanceof Long) {
                nodeMap.put(fieldName, VarintNode.valueOf((Long) data));
            } else if (data instanceof String) {
                nodeMap.put(fieldName, StringNode.valueOf((String) data));
            } else if (data instanceof Float) {
                nodeMap.put(fieldName, FloatNode.valueOf((Float) data));
            } else if (data instanceof Double) {
                nodeMap.put(fieldName, DoubleNode.valueOf((Double) data));
            } else if (data instanceof byte[]) {
                nodeMap.put(fieldName, ArrayNode.valueOf((byte[]) data));
            } else {
                throw new RuntimeException();
            }
        }
        return new ObjectNode(stable, names, nodeMap);
    }

}
