package com.github.sisyphsu.canoe.reflect;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.Date;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-10-29 21:18:15
 */
public class BeanHelperTest {

    @Test
    public void testPojo() {
        BeanHelper<Pojo> helper = BeanHelper.valueOf(Pojo.class);

        String[] names = helper.getNames();
        assert names.length == 5;
        assert Objects.equals(names[0], "code");
        assert Objects.equals(names[1], "date");
        assert Objects.equals(names[2], "id");
        assert Objects.equals(names[3], "name");
        assert Objects.equals(names[4], "time");

        Pojo pojo = new Pojo();
        pojo.id = 10;
        pojo.name = "hello";
        pojo.code = 1000;
        pojo.time = System.currentTimeMillis();
        pojo.date = new Date();

        Object[] objects = helper.getValues(pojo);
        assert Objects.equals(objects[0], pojo.code);
        assert Objects.equals(objects[1], pojo.date);
        assert Objects.equals(objects[2], pojo.id);
        assert Objects.equals(objects[3], pojo.name);
        assert Objects.equals(objects[4], pojo.time);

        objects[0] = 100000;
        objects[1] = new Date();
        objects[2] = 100;
        objects[3] = "hello world";
        objects[4] = System.currentTimeMillis() + 1;

        helper.setValues(pojo, objects);
        assert Objects.equals(objects[0], pojo.code);
        assert Objects.equals(objects[1], pojo.date);
        assert Objects.equals(objects[2], pojo.id);
        assert Objects.equals(objects[3], pojo.name);
        assert Objects.equals(objects[4], pojo.time);
    }

    @Test
    public void testStruct() {
        BeanHelper<Struct> helper = BeanHelper.valueOf(Struct.class);

        String[] props = helper.getNames();
        assert props.length == 6;
        assert Objects.equals(props[0], "code");
        assert Objects.equals(props[1], "date");
        assert Objects.equals(props[2], "flags");
        assert Objects.equals(props[3], "id");
        assert Objects.equals(props[4], "name");
        assert Objects.equals(props[5], "time");

        Struct struct = new Struct();
        struct.id = 10;
        struct.name = "hello";
        struct.setCode(1000);
        struct.setTime(System.currentTimeMillis());
        struct.setDate(new Date());
        struct.flags = BitSet.valueOf(new byte[]{1});

        Object[] objects = helper.getValues(struct);
        assert Objects.equals(objects[0], struct.getCode());
        assert Objects.equals(objects[1], struct.getDate());
        assert Objects.equals(objects[2], struct.flags);
        assert Objects.equals(objects[3], struct.getId());
        assert Objects.equals(objects[4], struct.getName());
        assert Objects.equals(objects[5], struct.getTime());

        objects[0] = 100000;
        objects[1] = new Date();
        objects[2] = BitSet.valueOf(new byte[]{1, 2});
        objects[3] = 100;
        objects[4] = "hello world";
        objects[5] = System.currentTimeMillis() + 1;

        helper.setValues(struct, objects);
        assert Objects.equals(objects[0], struct.getCode());
        assert Objects.equals(objects[1], struct.getDate());
        assert Objects.equals(objects[2], struct.flags);
        assert Objects.equals(objects[3], struct.getId());
        assert Objects.equals(objects[4], struct.getName());
        assert Objects.equals(objects[5], struct.getTime());
    }

    @Data
    public static class Pojo {
        private int     id;
        private String  name;
        private Integer code;
        private Long    time;
        private Date    date;
    }

    public static class Struct extends Pojo {
        public int    id;
        public String name;

        public transient Boolean invalid;

        public BitSet flags;

        private Boolean flag;
    }

}
