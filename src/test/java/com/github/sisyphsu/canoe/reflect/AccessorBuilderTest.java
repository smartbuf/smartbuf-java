package com.github.sisyphsu.canoe.reflect;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-10-29 18:04:22
 */
public class AccessorBuilderTest {

    @Test
    public void createGetter() throws Exception {
        Class<User> cls = User.class;
        Class<? extends Accessor> accessorCls = AccessorBuilder.buildAccessor(cls,
            new BeanProperty(cls.getDeclaredField("id"), cls.getDeclaredMethod("getId"), cls.getDeclaredMethod("setId", int.class)),
            new BeanProperty(cls.getDeclaredField("name"), cls.getDeclaredMethod("getName"), cls.getDeclaredMethod("setName", String.class)),
            new BeanProperty(cls.getDeclaredField("time"), cls.getDeclaredMethod("getTime"), cls.getDeclaredMethod("setTime", Long.class))
        );

        User user = new User();

        Constructor<? extends Accessor> constructor = accessorCls.getDeclaredConstructor(cls);
        Accessor accessor = constructor.newInstance(user);

        user.id = 10;
        user.name = "hello";
        user.time = 10000000L;

        Object[] arr = new Object[3];
        accessor.getAll(arr);
        assert Objects.equals(arr[0], user.id);
        assert Objects.equals(arr[1], user.name);
        assert Objects.equals(arr[2], user.time);

        arr[0] = 100;
        arr[1] = "hello world";
        arr[2] = System.currentTimeMillis();
        accessor.setAll(arr);
        assert Objects.equals(arr[0], user.id);
        assert Objects.equals(arr[1], user.name);
        assert Objects.equals(arr[2], user.time);
    }

    @Data
    public static class User {
        private int    id;
        private String name;
        private Long   time;
    }

}
