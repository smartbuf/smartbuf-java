package com.github.sisyphsu.canoe;

import lombok.Data;

/**
 * @author sulin
 * @since 2019-10-28 22:06:04
 */
public class Test {

    public interface Getter {
        Object[] getAll();
    }

    public static class BeanGetter implements Getter {

        private Bean bean;

        public BeanGetter(Bean bean) {
            this.bean = bean;
        }

        @Override
        public Object[] getAll() {
            Object[] result = new Object[3];
            result[0] = bean.getId();
            result[1] = bean.getName();
            result[2] = bean.getTime();
            return result;
        }
    }

    @Data
    public static class Bean {
        private int    id;
        private String name;
        private Long   time;

    }
}
