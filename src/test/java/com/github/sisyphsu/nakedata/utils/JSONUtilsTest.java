package com.github.sisyphsu.nakedata.utils;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author sulin
 * @since 2019-04-25 11:52:09
 */
@Slf4j
public class JSONUtilsTest {

    @Test
    public void toJsonNode() {
        this.testObject(1);
        this.testObject(1L);
        this.testObject(true);
        this.testObject(1.0f);
        this.testObject(new byte[10]);
        this.testObject(new User("hello", 20, new HashMap<>()));
    }

    private void testObject(Object obj) {
        JsonNode node = JSONUtils.toJsonNode(obj);
        log.info("object [{}]: {}", obj.getClass(), obj);
        log.info("json [{} - {}]: {}", node.getNodeType(), node.getClass(), node);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private String name;
        private int age;
        private Map<String, String> props;
    }
}