package com.github.sisyphsu.nakedata.test;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sulin
 * @since 2019-03-21 20:20:40
 */
public class JSONTest {

    private static ObjectMapper mapper = new ObjectMapper();
    private static Gson gson = new Gson();

    @Test
    public void testMap() throws Exception {
        User user = new User(1, "test");

        Map<User, String> map = new HashMap<>();
        map.put(user, "world");

        String json = JSON.toJSONString(map);

        System.out.println(json);

        Map<User, Long> tmp = new HashMap<>();
        tmp.put(user, System.currentTimeMillis());

        System.out.println(JSON.toJSONString(tmp));
        System.out.println(mapper.writeValueAsString(tmp));
        System.out.println(gson.toJson(tmp));

//        JSON.parseArray()
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
