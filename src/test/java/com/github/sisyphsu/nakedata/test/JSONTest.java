package com.github.sisyphsu.nakedata.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sulin
 * @since 2019-03-21 20:20:40
 */
public class JSONTest {

    @Test
    public void testMap() {
        User user = new User(1, "test");

        Map<User, String> map = new HashMap<>();
        map.put(user, "world");

        String json = JSON.toJSONString(map);

        System.out.println(json);
    }

    public static class User {

        private int age;
        private String name;

        public User(int age, String name) {
            this.age = age;
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
