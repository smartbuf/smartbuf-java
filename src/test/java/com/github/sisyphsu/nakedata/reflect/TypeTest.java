package com.github.sisyphsu.nakedata.reflect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.ref.Reference;
import java.lang.reflect.*;
import java.util.*;

/**
 * Test type
 *
 * @author sulin
 * @since 2019-07-13 17:20:50
 */
public class TypeTest {

    @Test
    public void testRawType() {
        TypeReference ref = new TypeReference<Collection<Queue<Long>>>() {
        };
        Type type = ref.getType();
        System.out.println(type);

        ParameterizedType pt = (ParameterizedType) type;
        System.out.println(pt.getRawType() instanceof Class);
    }

    @Test
    public void testNormalType() throws NoSuchFieldException {
        for (Field field : Model.class.getDeclaredFields()) {
            System.out.println(field.getGenericType());
        }
        GenericArrayType tsType = (GenericArrayType) Model.class.getDeclaredField("ts").getGenericType();
        System.out.println(tsType);
    }

    @Test
    public void testGenericType() throws NoSuchFieldException {
        TypeReference typeReference = new TypeReference<Model<Void, Queue<Long>>>() {
        };
        Type type = typeReference.getType();
        System.out.println(type);

        ParameterizedType pt = (ParameterizedType) type;
        Class clz = (Class) pt.getRawType();
        for (Field field : clz.getDeclaredFields()) {
            Type fType = field.getGenericType();
            System.out.println(fType);
        }

        Type tsType = clz.getDeclaredField("ts").getGenericType();
        GenericArrayType arrayType = (GenericArrayType) tsType;
        TypeVariable genArrType = (TypeVariable) arrayType.getGenericComponentType();
        System.out.println(genArrType.getGenericDeclaration());
        System.out.println(tsType);

        // 变量泛型通过声明类的TypeParameters进行坐标定位，然后结合Type进行具体值判定。
        Class decClass = (Class) genArrType.getGenericDeclaration();
        for (TypeVariable parameter : decClass.getTypeParameters()) {
            System.out.println(parameter.getName());
        }
    }

    @Test
    public void testVariableType() {
        Bean<String> bean = new Bean<>();
        bean.name = new String[]{"test"};

        String str = JSON.toJSONString(bean);
        Bean<String> b2 = JSON.parseObject(str, new TypeReference<Bean<String>>() {
        });
        System.out.println(b2);

        P<Double>.Item item = new P<Double>().new Item();
        item.t = 10000.3;
        str = JSON.toJSONString(item);
//        P.Item item2 = JSON.parseObject(str, new TypeReference<P<Double>.Item>() {
//        });
//        System.out.println(item2);
    }

    public static class Model<X, T> {
        private long time;
        private Map objectMap;
        private Map<String, ? extends Date> strDateMap;
        private Map<? extends Date, ? super String> wildMap;
        private List<Long> longs;
        private T[] ts;
        private Reference<T> ref;
    }

    @Data
    public static class Bean<T> {
        private T[] name;

        public void setName(T[] name) {
            this.name = name;
        }
    }

    public static class P<T> {
        @Data
        public class Item {
            private T t;
        }
    }

}