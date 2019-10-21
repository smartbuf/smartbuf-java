package com.github.sisyphsu.datube.node;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sulin
 * @since 2019-09-21 14:10:57
 */
public class NodeTest {

    @Test
    public void testError() {
        Node node = new Node() {
            @Override
            public NodeType type() {
                return null;
            }
        };

        try {
            node.booleanValue();
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }
        try {
            node.floatValue();
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }
        try {
            node.doubleValue();
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }
        try {
            node.longValue();
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }
        try {
            node.stringValue();
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }
    }

    //    @Test
    void test() {
        List<Bean> beans = new ArrayList<>();
        beans.add(new Bean(1, "hello"));
        beans.add(new Bean(2, "world"));
    }

    @Data
    public static class Bean {

        private int    id;
        private String name;
        private Date   timestamp;

        public Bean(int id, String name) {
            this.id = id;
            this.name = name;
            this.timestamp = new Date();
        }
    }

}
