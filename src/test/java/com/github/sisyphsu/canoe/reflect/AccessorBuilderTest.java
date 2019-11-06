package com.github.sisyphsu.canoe.reflect;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * @author sulin
 * @since 2019-10-29 18:04:22
 */
public class AccessorBuilderTest {

    @Test
    public void test() throws Exception {
        Class<User> cls = User.class;
        Accessor accessor = AccessorBuilder.buildAccessor(cls,
            new BeanField(cls.getDeclaredField("id"), cls.getDeclaredMethod("getId"), cls.getDeclaredMethod("setId", int.class)),
            new BeanField(cls.getDeclaredField("name"), cls.getDeclaredMethod("getName"), cls.getDeclaredMethod("setName", String.class)),
            new BeanField(cls.getDeclaredField("time"), cls.getDeclaredMethod("getTime"), cls.getDeclaredMethod("setTime", Long.class))
        );

        User user = new User();
        user.id = 10;
        user.name = "hello";
        user.time = 10000000L;

        Object[] arr = new Object[3];
        accessor.getAll(user, arr);
        assert Objects.equals(arr[0], user.id);
        assert Objects.equals(arr[1], user.name);
        assert Objects.equals(arr[2], user.time);

        arr[0] = 100;
        arr[1] = "hello world";
        arr[2] = System.currentTimeMillis();
        accessor.setAll(user, arr);
        assert Objects.equals(arr[0], user.id);
        assert Objects.equals(arr[1], user.name);
        assert Objects.equals(arr[2], user.time);
    }

    @Test
    public void test2() throws Exception {
        Class<User> cls = User.class;
        BeanField[] fields = {
            new BeanField(cls.getDeclaredField("name"), cls.getDeclaredMethod("getName"), cls.getDeclaredMethod("setName", String.class)),
            new BeanField(cls.getDeclaredField("time"), cls.getDeclaredMethod("getTime"), cls.getDeclaredMethod("setTime", Long.class))
        };
        Accessor accessor1 = AccessorBuilder.buildAccessor(cls, fields);
        Accessor accessor2 = AccessorBuilder.buildAccessor(cls, fields);
        assert accessor1 != accessor2;
    }

    @Test
    public void testError() {
        AccessorBuilder.ACCESSOR_CLS_MAP.put(Void.class, InvalidAccessor.class);
        try {
            AccessorBuilder.buildAccessor(Void.class);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

    @Data
    public static class User {
        private int    id;
        private String name;
        private Long   time;
    }

    static class InvalidAccessor implements Accessor {
        public InvalidAccessor(int i) {
        }

        @Override
        public void getAll(Object o, Object[] values) {
        }

        @Override
        public void setAll(Object o, Object[] values) {
        }
    }
}
