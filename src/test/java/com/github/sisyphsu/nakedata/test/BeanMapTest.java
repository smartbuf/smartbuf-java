package com.github.sisyphsu.nakedata.test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author sulin
 * @since 2019-05-10 14:14:42
 */
@Slf4j
public class BeanMapTest {

    @Test
    public void test() throws NoSuchFieldException {
        Bean bean = new Bean();
        BeanMap map = BeanMap.create(bean);

        for (Object key : map.keySet()) {
            Class clz = map.getPropertyType(key.toString());
            Object value = map.get(key);
            log.info("key[{} : {}]: {}", key, clz, value);
        }

        // 测试获取泛型的方式
        Field field = Bean.class.getDeclaredField("subs");
        log.info(field.getGenericType().getTypeName());
        ParameterizedType listType = (ParameterizedType) field.getGenericType();
        Class<?> clz = (Class<?>) listType.getActualTypeArguments()[0];
        log.info("" + listType.getActualTypeArguments()[0].getTypeName());
        log.info("" + clz);
    }

    @Data
    public static class Bean {
        private int id = RandomUtils.nextInt();
        private Long timestamp = System.currentTimeMillis();
        private byte[] data = new byte[]{1, 2, 3, 4, 5};
        private Double[] scores = new Double[]{null, 1.0, 2.0, 3.0};
        private Collection<Object> coll = new ArrayList<>(1);
        private List<Bean> subs;
    }

}
