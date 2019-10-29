package com.github.sisyphsu.canoe;

import lombok.Data;

/**
 * @author sulin
 * @since 2019-10-28 22:06:04
 */
public class Test {

    public interface Accessor {
        void getAll(Object[] vals);

        void setAll(Object[] vals);
    }

    public static class BeanGetter implements Accessor {

        private Bean bean;

        public BeanGetter(Bean bean) {
            this.bean = bean;
        }

        @Override
        public void getAll(Object[] result) {
            result[0] = bean.getId();
            result[1] = bean.getName();
            result[2] = bean.time;
        }

        @Override
        public void setAll(Object[] vals) {
            bean.setId((Integer) vals[0]);
            bean.setName((String) vals[1]);
            bean.time = ((Long) vals[2]);
        }
    }

    @Data
    public static class Bean {
        private int    id;
        private String name;
        public  Long   time;
    }
}
