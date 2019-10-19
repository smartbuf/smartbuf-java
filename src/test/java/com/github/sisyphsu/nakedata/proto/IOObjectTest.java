package com.github.sisyphsu.nakedata.proto;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.std.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.github.sisyphsu.nakedata.proto.IOTest.enableCxt;
import static com.github.sisyphsu.nakedata.proto.IOTest.transIO;

/**
 * @author sulin
 * @since 2019-10-19 11:44:37
 */
public class IOObjectTest {

    @Test
    public void testSimpleObject() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", RandomUtils.nextLong());
        map.put("name", RandomStringUtils.randomAlphanumeric(64));
        map.put("nil", null);
        map.put("real", true);
        map.put("score1", RandomUtils.nextFloat());
        map.put("score2", RandomUtils.nextDouble());
        map.put("arr", RandomUtils.nextBytes(1024));

        String[] fieldNames = map.keySet().toArray(new String[0]);
        Map<String, Node> nodeMap = new HashMap<>();
        for (String fieldName : fieldNames) {
            Object data = map.get(fieldName);
            if (data == null) {
                nodeMap.put(fieldName, BooleanNode.valueOf(null));
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
        Object result;
        Map tgtMap;

        enableCxt = false;
        result = transIO(ObjectNode.valueOf(true, fieldNames, nodeMap));
        assert result instanceof Map;
        tgtMap = (Map) result;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object tgtItem = tgtMap.get(entry.getKey());
            assert Objects.deepEquals(entry.getValue(), tgtItem);
        }

        enableCxt = true;
        result = transIO(ObjectNode.valueOf(true, fieldNames, nodeMap));
        assert result instanceof Map;
        tgtMap = (Map) result;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object tgtItem = tgtMap.get(entry.getKey());
            assert Objects.deepEquals(entry.getValue(), tgtItem);
        }
    }

}
